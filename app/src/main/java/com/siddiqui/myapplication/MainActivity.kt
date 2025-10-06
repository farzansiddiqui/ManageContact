package com.siddiqui.myapplication

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.siddiqui.myapplication.navigation.NavigationStack
import com.siddiqui.myapplication.ui.theme.ManageContactTheme

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





@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Greeting() {



    NotificationHandler(
        onPermissionGranted = {  },
        onPermissionDenied = {  }
    )

    var showBottomSheet  by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .windowInsetsPadding(WindowInsets.systemBars)
        .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd){
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Contact List", fontWeight = FontWeight.Medium)
            ContactsScreen()
        }

        if (showBottomSheet){
           NavigationStack {
               showBottomSheet = false
           }
        }else {
            AddContactButton(onclick = {showBottomSheet = true})
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationHandler(
    onPermissionGranted:()-> Unit,
    onPermissionDenied: ()-> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        LaunchedEffect(permissionState.status) {
            if (permissionState.status.isGranted){
                onPermissionGranted()
            }else if (permissionState.status.shouldShowRationale){
                onPermissionDenied()
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
fun AddContactButton(onclick: ()-> Unit){
    FloatingActionButton(onClick = onclick,
        modifier = Modifier.padding(24.dp),
        shape = CircleShape
        ) {
        Icon(painter = painterResource(R.drawable.outline_record_voice_over_24),
            "Add New Contacts")
    }
}






