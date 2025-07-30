package com.example.iavtest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.iavtest.viewmodel.GenerateStringViewModel


@Composable
fun RandomStringScreen(viewModel: GenerateStringViewModel, navController: NavHostController) {

    val randomStrings by viewModel.randomStrings
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val maxLength = remember { mutableStateOf("5") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Left,
            text = "Random String Challenge",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = maxLength.value,
            onValueChange = { maxLength.value = it.filter { it.isDigit() } },
            label = { Text("Length of String") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Button(
                onClick = {
                    viewModel.fetchRandomString(maxLength.value.toIntOrNull() ?: 5)
                },
                modifier = Modifier
                    .height(50.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                if (isLoading) {
                    Text("", color = Color.White)
                } else {
                    Text("Fetch Data", color = Color.White)
                }
            }

            // Loader while fetching
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        /**
         *   Display list of generated random strings
         */

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val listToShow = randomStrings
            items(listToShow) { item ->
                RandomStringItem(
                    item,
                    viewModel,
                    onDelete = { viewModel.deleteString(item) },
                    navController
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.clearAll() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Delete All", color = Color.White)
        }
    }
}