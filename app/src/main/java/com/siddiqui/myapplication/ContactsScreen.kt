package com.siddiqui.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.model.Contact
import com.siddiqui.myapplication.model.User
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
    }

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
fun ContactList(contacts: List<Contact>,viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
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
                ContactItem(contact = contact){contactItem->
               val isRegistered =  viewModel.isNumberOnWhatsUp(context, contactItem.phoneNumber)
                if (isRegistered){
                    viewModel.openWhatsAppChat(context,contactItem.phoneNumber)
                    Log.d("TAG", "Contact is on WhatsUp:")
                }else {
                    Log.d("TAG", "Not is on WhatsUp: ")
                }
                    println("Item Clicked ${contactItem.name}")
                }
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarQuery(
    textFieldState: TextFieldState,
    onSearch:(String)-> Unit,
    searchResult: List<String>,
    modifier: Modifier = Modifier
){
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier.fillMaxSize().semantics {isTraversalGroup = true}) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter).semantics {traversalIndex = 0f},
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = {expanded = it}
        ) { }

    }

}

@Composable
fun ContactItem(contact: Contact, onItemClick:(Contact)-> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick(contact) }
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