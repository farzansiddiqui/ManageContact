package com.siddiqui.myapplication

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siddiqui.myapplication.model.UserContacts


@Preview(showSystemUi = true)
@Composable
fun ShowPreview() {
    val models = mutableListOf(
        UserContacts(1, "Farzan Siddiqui", "6306711605"),
        UserContacts(1, "Modassir", "8209252315"),
        UserContacts(1, "Zohair Rashidi", "9708167200")
    )
    MainScreen(userContactList = models)
}

@Composable
fun MainScreen(userContactList: MutableList<UserContacts>) {
    Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(userContactList) { contactList ->
                ListItem(contactList)
            }
        }
    }
}

@Composable
fun ListItem(userContacts: UserContacts) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = userContacts.name, color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = userContacts.mobileNumber, color = Color(0xFFFFA500),
                fontSize = 18.sp
            )
        }
        Image(
            painter = painterResource(id = R.drawable.round_delete_24),
            contentDescription = "Delete Button for list item"
        )

    }
}

