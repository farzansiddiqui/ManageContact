package com.siddiqui.myapplication


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.siddiqui.myapplication.model.UserProfile
import com.siddiqui.myapplication.model.userProfieListItem


@Composable
fun MainScreen(userProfileList:List<UserProfile> = userProfieListItem) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {

        LazyColumn {
            items(userProfileList) { userProfile->
                ProfileCard(userProfile)

            }
        }
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.drawableInt, userProfile.status)
            ProfileContent(userProfile.name, userProfile.status)

        }

    }
}

@Composable
fun ProfilePicture(drawableInt: Int,onlineStatus: Boolean) {
    Card(
        shape = CircleShape,
        border = if (onlineStatus) BorderStroke(width = 2.dp, color = Color.Green) else
            BorderStroke(width = 2.dp, color = Color.Red),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ) {
        Image(
            painter = painterResource(id = drawableInt),
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Fit,
            contentDescription = "Content description"
        )
    }
}
@Composable
fun ProfileContent(userProfile: String, onlineStatus: Boolean) {
    Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(userProfile, fontSize = TextUnit(
                    18F, TextUnitType.Sp
                ))

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(text = if (onlineStatus)"Active now" else "Offline mode",
                        style = MaterialTheme.typography.bodySmall)
                }

            }

            RadioButton(onlineStatus, onClick = {

            })
        }
    }



}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreView(){
    MainScreen()

}