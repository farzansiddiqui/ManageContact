package com.siddiqui.myapplication.viewModel

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.myapplication.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.net.toUri
import kotlinx.coroutines.flow.StateFlow

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _filteredContacts = MutableStateFlow<List<Contact>>(emptyList())
    val filteredContacts: StateFlow<List<Contact>> = _filteredContacts.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set



    fun fetchContacts(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val contactList = withContext(Dispatchers.IO) {
              val rawContacts =   getPhoneContacts(contentResolver)
                val uniqueContacts = rawContacts.groupBy { it.name }.map { it.value.first() }
                uniqueContacts

            }
            _contacts.value = contactList
            _filteredContacts.value = contactList

        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        _filteredContacts.value =
            if (query.isBlank()) {
                _contacts.value
            } else {
                _contacts.value.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.phoneNumber.contains(query, ignoreCase = true)
                }
            }
    }

    fun isNumberOnWhatsUp(context: Context, phoneNumber:String): Boolean {
        val contentResolver = context.contentResolver
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup._ID)
        val cursor = contentResolver.query(uri, projection , null,null,null)
        cursor?.use {
            if (it.moveToFirst()){
                val contactId = it.getLong(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))

                val rawContactUri = ContactsContract.RawContacts.CONTENT_URI
                val selection = "${ContactsContract.RawContacts.CONTACT_ID} = ? AND ${ContactsContract.RawContacts.ACCOUNT_TYPE} = ?"
                val selectionArgs = arrayOf(contactId.toString(), "com.whatsapp")

                val rawCursor = contentResolver.query(rawContactUri, null, selection, selectionArgs, null)
                rawCursor?.use { rc ->
                    return rc.count > 0
                }
            }
        }

        return false
    }

    fun openWhatsAppChat(context: Context, rawNumber: String) {
        val phoneNumber = rawNumber.replace("\\D".toRegex(), "")
        val url = "https://wa.me/$phoneNumber"
        Log.d("TAG", "openWhatsAppChat:$url ")
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())

        val pm = context.packageManager

        // Try regular WhatsApp
        if (isAppInstalled(pm, "com.whatsapp")) {
            intent.setPackage("com.whatsapp")
            context.startActivity(intent)
            return
        }

        // Try WhatsApp Business
        if (isAppInstalled(pm, "com.whatsapp.w4b")) {
            intent.setPackage("com.whatsapp.w4b")
            context.startActivity(intent)
            return
        }

        // Neither version installed
        Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
    }

    fun isAppInstalled(pm: PackageManager, packageName:String): Boolean{
        return try {
            pm.getPackageInfo(packageName,0)
            true
        }catch (e: PackageManager.NameNotFoundException){
            false
        }
    }




    private fun getPhoneContacts(contentResolver: ContentResolver): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        Log.d("TAG", "TESTING with base URI: $uri")

        Log.d("TAG", "Starting to query contacts...: ")

        contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            Log.d("TAG", "Query successful. Cursor found ${cursor.count} contacts.")
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            if (nameIndex == -1 || numberIndex == -1) {
                Log.d("TAG", "Error: A required column was not found in the cursor: ")
                return@use
            }

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                contacts.add(Contact(name = name, phoneNumber = number))
            }
        } ?: run {
            Log.e("TAG", "Query failed, cursor is null.")
        }
        Log.e("TAG", "Query failed, cursor is null.")
        return contacts
    }
}