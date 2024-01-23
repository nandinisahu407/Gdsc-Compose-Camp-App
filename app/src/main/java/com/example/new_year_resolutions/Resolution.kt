package com.example.new_year_resolutions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Resolution(
    var name: String,
    var editedName: MutableState<String> = mutableStateOf(name)
)
