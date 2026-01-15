package com.siddiqui.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/*

Create a Jetpack Compose screen that displays a list of names using LazyColumn.
*
*
* */
data class Person(val name: String, val age: Int)


@Composable
fun ListItemShow(){
    val persons = listOf(
        Person("Farzan", 29),
        Person("Siddiqui", 29),
        Person("Hassan", 29),
        Person("Farzan", 29),
        Person("Siddiqui", 29),
        Person("Hassan", 29))

    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(persons){person ->
            ShowListItemData(person)
        }
    }
}


@Composable
fun ShowListItemData(person: Person){
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text(text = person.name,
            modifier = Modifier.fillMaxWidth())
        Text(text = person.age.toString(),
            modifier = Modifier.fillMaxWidth())
    }
}


/*Use Hilt to inject a Repository into a ViewModel.
* */




@Preview(showBackground = true)
@Composable
fun ShowTestingPreview(){
    ListItemShow()
}