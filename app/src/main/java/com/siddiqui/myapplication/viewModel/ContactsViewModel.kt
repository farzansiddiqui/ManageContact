package com.siddiqui.myapplication.viewModel

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.myapplication.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun fetchContacts(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val contactList = withContext(Dispatchers.IO) {
              val rawContacts =   getPhoneContacts(contentResolver)
                val uniqueContacts = rawContacts.groupBy { it.name }.map { it.value.first() }

                uniqueContacts

            }
            _contacts.value = contactList
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