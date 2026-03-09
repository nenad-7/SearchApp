package com.example.searchapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaggedSearchScreen()
            }
        }
    }
}

@Composable
fun TaggedSearchScreen() {

    val context = LocalContext.current
    val dictionary = remember { loadDictionary(context) }

    var searchText by remember { mutableStateOf("") }
    var tagText by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Search field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter word to search (MK or EN)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search button
        Button(
            onClick = {

                val search = searchText.lowercase().trim()

                val translation = dictionary[search]

                if (translation != null) {
                    result = "Translation: $translation"
                } else {

                    val reverse = dictionary.entries.find {
                        it.value.lowercase() == search
                    }

                    if (reverse != null) {
                        result = "Translation: ${reverse.key}"
                    } else {
                        result = "Word not found in dictionary"
                    }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search in Dictionary")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = result,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tag field
        OutlinedTextField(
            value = tagText,
            onValueChange = { tagText = it },
            label = { Text("Tag your query") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save tag
        Button(
            onClick = {
                if (tagText.isNotBlank()) {
                    tags = tags + tagText
                    tagText = ""
                }
            }
        ) {
            Text("Save Tag")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tagged Searches",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        tags.forEachIndexed { index, tag ->
            TagItem(
                tag = tag,
                onEditClick = {
                    tagText = tag
                    tags = tags.toMutableList().also { it.removeAt(index) }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { tags = emptyList() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Tags")
        }
    }
}

@Composable
fun TagItem(tag: String, onEditClick: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = tag,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = onEditClick) {
                Text("Edit")
            }
        }
    }
}

fun loadDictionary(context: Context): Map<String, String> {

    val dictionary = mutableMapOf<String, String>()

    val inputStream = context.assets.open("dictionary.txt")
    val lines = inputStream.bufferedReader().readLines()

    for (line in lines) {

        val parts = line.split("=")

        if (parts.size == 2) {
            val mk = parts[0].trim().lowercase()
            val en = parts[1].trim().lowercase()

            dictionary[mk] = en
        }
    }

    return dictionary
}