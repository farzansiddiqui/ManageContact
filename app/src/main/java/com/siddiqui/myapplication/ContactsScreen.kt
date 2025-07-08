package com.siddiqui.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.model.Contact
import com.siddiqui.myapplication.viewModel.ContactsViewModel

@Composable
fun ContactsScreen(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // This launcher is used to request the permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
    }

    // Effect to run once when the composable is first displayed
    // if permission is already granted.
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.fetchContacts(context.contentResolver)
        }
    }

    // UI layout
    if (hasPermission) {

        val contacts by viewModel.contacts.collectAsState()
        Log.d("TAG", "ContactsScreen:${contacts.size} ")

        ContactList(contacts = contacts)
    } else {
        PermissionRequestScreen {
            // On button click, launch the permission request
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("We need permission to read your contacts.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Request Permission")
        }
    }
}

@Composable
fun ContactList(contacts: List<Contact>) {
    if (contacts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No contacts found or still loading...")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(contact = contact)
                Divider()
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = contact.name, fontWeight = FontWeight.Bold)
        Text(text = contact.phoneNumber)
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowList(){
    val listOfContacts = listOf(
        Contact("Farzan", "6306711605"),
        Contact("Modassir", "97281672000"),
        Contact("Shabina", "9192414910")
    )
    ContactList(listOfContacts)
}