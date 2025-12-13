package com.eya.smartshop.ui

import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eya.smartshop.product.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForm(
    product: Product? = null,
    onClose: () -> Unit,
    onSave: (id: String?, name: String, qty: Int, price: Double, imageUrl: String?) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var qty by remember { mutableStateOf(product?.quantity?.toString() ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var image by remember { mutableStateOf(product?.imageUrl ?: "") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // TOP BAR
            TopAppBar(
                title = {
                    Text(
                        if (product == null) "Add Product"
                        else "Edit Product",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            // CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = qty,
                    onValueChange = { qty = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                // ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSave(
                                product?.id,
                                name,
                                qty.toIntOrNull() ?: 0,
                                price.toDoubleOrNull() ?: 0.0,
                                image.ifBlank { null }
                            )
                        }
                    ) {
                        Text("Save")
                    }

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClose
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}


