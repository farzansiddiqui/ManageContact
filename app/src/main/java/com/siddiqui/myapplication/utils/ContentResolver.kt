package com.siddiqui.myapplication.utils

import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast

fun fetchContacts(context: Context): List<Pair<String, String>> {
    val contactList = mutableListOf<Pair<String, String>>()

    val resolver = context.contentResolver
    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null, null
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            val type = it.getInt(typeIndex)

            // Only get mobile numbers
            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                contactList.add(Pair(name, number))
            }
        }
    }

    return contactList
}

fun showToast(context: Context,message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
