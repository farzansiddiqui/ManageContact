package com.siddiqui.myapplication



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siddiqui.myapplication.model.UserProfile
import com.siddiqui.myapplication.model.userProfileListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ShowSearchScreen(
    onQueryChange:(String)-> Unit,
    placeHolder: String = "Search...",
    modifier: Modifier= Modifier
){
    var searchQuery by remember { mutableStateOf("") }
    val userProfileList:List<UserProfile> = userProfileListItem

    val filtered = remember(searchQuery) {
        if (searchQuery.isEmpty()){
            userProfileList
        }else {
            userProfileList.filter { userProfile ->
                userProfile.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("Click to load") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
           onClick = {

              coroutineScope.launch {
                  text = loadDataFromNetwork()
              }
           }) {
            Text(text = text)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                onQueryChange(query)
            },
            placeholder = { Text(text = placeHolder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()){
                    IconButton(onClick = {
                        searchQuery = searchQuery.dropLast(1)

                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search) ,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LazyColumn {
            items(filtered) { userProfile->
                ProfileCard(userProfile)
            }
        }

    }

}

suspend fun loadDataFromNetwork(): String {
    delay(2000)
    return "data loaded"
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchScreenPreView(){
    ShowSearchScreen(onQueryChange = {})
}