package com.siddiqui.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siddiqui.myapplication.ui.theme.ManageContactTheme
import com.siddiqui.myapplication.utils.showToast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManageContactTheme {
                Greeting()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    val context  = LocalContext.current
    var showBottomSheet  by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Box(modifier = Modifier
        .windowInsetsPadding(WindowInsets.systemBars)
        .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd){
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Contact List", fontWeight = FontWeight.Medium)
            ContactsScreen()
        }
        AddContactButton {
            showBottomSheet = true
        }
        if (showBottomSheet){
            BottomSheet(onDismiss = {
                showBottomSheet = false
            }) {
                BottomSheetDialog()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Greeting()
}

@Composable
fun AddContactButton(onclick: ()-> Unit,){
    FloatingActionButton(onClick = onclick,
        modifier = Modifier.padding(24.dp),
        shape = CircleShape
        ) {
        Icon(Icons.Filled.Add, "Add New Contacts")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, content:@Composable ()-> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
       content()
    }
}

@Composable
fun BottomSheetDialog(){
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    Column ( modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
       ){
            OutlinedTextField(value = text, onValueChange = {text = it}, modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp),
                label = {Text(text = stringResource(R.string.enter_name))},
                singleLine = true)


        Button(onClick = {
                if (text.isEmpty()){
                    showToast(context, "please enter name")
                }else {
                    showToast(context = context, message = "Submit!")
                }

        }, modifier = Modifier.padding(bottom = 18.dp)) {
            Text(text = "Bottom Sheet Button")
        }


    }
}




