package com.siddiqui.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.model.Contact
import com.siddiqui.myapplication.viewModel.ContactsViewModel

@Composable
fun ContactsScreen(viewModel: ContactsViewModel = viewModel()) {

    val context = LocalContext.current

    val nameObserver = Observer<String>{newName->
        println(newName)
    }



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

    if (hasPermission) {
        val filteredContacts by viewModel.filteredContacts.collectAsState()
        val searchQuery = viewModel.searchQuery
        var isSearchBarFocused by remember { mutableStateOf(false) }

        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> viewModel.updateSearchQuery(query) },
                placeholder = { Text("Search Contacts") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = CircleShape,
               /* colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = if (isSearchBarFocused) Color.White else Color.Transparent,
                ),*/
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .onFocusChanged { focusState ->
                        isSearchBarFocused = focusState.isFocused
                    }
            )

            if (filteredContacts.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No contacts found...")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(filteredContacts) { contact ->
                        ContactItem(contact = contact) { contactItem ->
                            val isRegistered = viewModel.isNumberOnWhatsUp(context, contactItem.phoneNumber)
                            if (isRegistered) {
                                viewModel.openWhatsAppChat(context, contactItem.phoneNumber)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
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