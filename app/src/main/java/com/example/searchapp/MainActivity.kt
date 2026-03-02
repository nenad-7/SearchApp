package com.example.searchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    var searchText by remember { mutableStateOf("") }
    var tagText by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Search Field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter Twitter search query here") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tag Field
        OutlinedTextField(
            value = tagText,
            onValueChange = { tagText = it },
            label = { Text("Tag your query") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save Button
        Button(onClick = {
            if (tagText.isNotBlank()) {
                tags = tags + tagText
                tagText = ""
            }
        }) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tagged Searches",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tag List
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

        // Clear Button
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