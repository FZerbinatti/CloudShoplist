package com.example.cloudshoplist

import android.R.attr.password
import android.util.Log
import java.util.regex.Pattern


object InputValidator {

    private const val TAG = "Main InputValidator"
    fun validateInput(item: String): Boolean{

        val special: Pattern = Pattern.compile("[!@#$%.&/*()_+=|<>?{}\\[\\]~-]")
        val hasSpecial = special.matcher(item).find()
        Log.d(TAG, "validateInput: "+item +" - has special:" + hasSpecial)

        //return true if item is valid
        return !(hasSpecial)
    }

}