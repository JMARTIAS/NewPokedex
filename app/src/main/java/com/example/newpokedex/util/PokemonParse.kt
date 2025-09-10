package com.example.newpokedex.util

import androidx.compose.ui.graphics.Color
import com.example.newpokedex.data.remote.responses.Stat
import com.example.newpokedex.data.remote.responses.Type
import java.util.Locale
import kotlin.text.lowercase
import kotlin.text.toLowerCase

fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> Color(0xFFA8A77A)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "electric" -> Color(0xFFF7D02C)
        "grass" -> Color(0xFF7AC74C)
        "ice" -> Color(0xFF96D9D6)
        "fighting" -> Color(0xFFC22E28)
        "poison" -> Color(0xFFA33EA1)
        "ground" -> Color(0xFFE2BF65)
        "flying" -> Color(0xFFA98FF3)
        "psychic" -> Color(0xFFF95587)
        "bug" -> Color(0xFFA6B91A)
        "rock" -> Color(0xFFB6A136)
        "ghost" -> Color(0xFF735797)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705746)
        "steel" -> Color(0xFFB7B7CE)
        "fairy" -> Color(0xFFD685AD)
        else -> Color.Black // Un color por defecto si el tipo no se encuentra
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.toLowerCase(Locale.ROOT)) {
        "hp" -> Color(0xFFF5FF00)
        "attack" -> Color(0xFFFF0000)
        "defense" -> Color(0xFF0000FF)
        "special-attack" -> Color(0xFF800080)
        "special-defense" -> Color(0xFF008000)
        "speed" -> Color(0xFFADD8E6)
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.toLowerCase(Locale.ROOT)) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}