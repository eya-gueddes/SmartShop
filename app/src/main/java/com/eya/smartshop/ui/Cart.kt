package com.eya.smartshop.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eya.smartshop.cart.CartViewModel
import com.eya.smartshop.product.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartVm: CartViewModel,
    productVm: ProductViewModel,
    onBack: () -> Unit
) {
    val items by cartVm.items.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // -------------------------
            // TOP BAR
            // -------------------------
            TopAppBar(
                title = { Text("Your Cart", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            // -------------------------
            // CONTENT
            // -------------------------
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (items.isEmpty()) {
                    Text(
                        "Your cart is empty",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items) { cartItem ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(cartItem.product.name, style = MaterialTheme.typography.titleMedium)
                                        Text("Qty: ${cartItem.quantity}")
                                    }

                                    Text(
                                        "${cartItem.product.price * cartItem.quantity}  TND",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    IconButton(onClick = { cartVm.removeProduct(cartItem.product) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // TOTAL
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total:", style = MaterialTheme.typography.titleLarge)
                        Text("${cartVm.total()}  TND", style = MaterialTheme.typography.titleLarge)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                // Mettre à jour les quantités dans ProductViewModel
                                cartVm.items.value.forEach { cartItem ->
                                    val newQty = cartItem.product.quantity - cartItem.quantity
                                    if (newQty >= 0) {
                                        productVm.updateQuantity(cartItem.product.id, newQty)
                                    }
                                }
                                cartVm.clear()
                                onBack()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirm Order")
                        }

                        OutlinedButton(
                            onClick = { cartVm.clear() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel Order")
                        }
                    }
                }
            }
        }
    }
}