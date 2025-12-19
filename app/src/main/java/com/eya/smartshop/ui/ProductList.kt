package com.eya.smartshop.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eya.smartshop.cart.CartViewModel
import com.eya.smartshop.product.Product
import com.eya.smartshop.product.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    vm: ProductViewModel,
    cartVm: CartViewModel,
    onNavigateToCart: () -> Unit
) {

    val list by vm.products.collectAsState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showForm by remember { mutableStateOf(false) }
    val qtyMap = remember { mutableStateMapOf<String, Int>() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products", color = MaterialTheme.colorScheme.onPrimary) },
                actions = {
                    IconButton(onClick = { onNavigateToCart() }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "View Cart",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    selectedProduct = null
                    showForm = true
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            // LISTE DES PRODUITS
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(list) { p ->

                    val orderedQty = qtyMap[p.id] ?: 1

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // =======================
                            // LEFT : IMAGE + INFOS
                            // =======================
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = p.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp)
                                )

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = p.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text("Stock: ${p.quantity}")
                                    Text("Price: ${p.price}")
                                }
                            }

                            // =======================
                            // RIGHT : ACTIONS + QTY
                            // =======================
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                // ---- ICON BUTTONS
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    IconButton(onClick = {
                                        selectedProduct = p
                                        showForm = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }

                                    IconButton(onClick = { vm.delete(p) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }

                                    IconButton(
                                        onClick = {
                                            cartVm.addProduct(p, orderedQty)
                                            qtyMap[p.id] = 1
                                        },
                                        enabled = p.quantity > 0
                                    ) {
                                        Icon(Icons.Default.ShoppingCart, contentDescription = "Add to cart")
                                    }
                                }

                                // ---- QTY CONTROLLER
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    IconButton(
                                        onClick = { qtyMap[p.id] = orderedQty - 1 },
                                        enabled = orderedQty > 1
                                    ) {
                                        Text("−", style = MaterialTheme.typography.titleLarge)
                                    }

                                    Surface(
                                        shape = MaterialTheme.shapes.small,
                                        tonalElevation = 2.dp
                                    ) {
                                        Text(
                                            text = orderedQty.toString(),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    IconButton(
                                        onClick = { qtyMap[p.id] = orderedQty + 1 },
                                        enabled = orderedQty < p.quantity
                                    ) {
                                        Text("+", style = MaterialTheme.typography.titleLarge)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // FORMULAIRE
            if (showForm) {
                ProductForm(
                    product = selectedProduct,
                    onClose = {
                        showForm = false
                        selectedProduct = null
                    },
                    onSave = { id, name, qty, price, img ->
                        vm.saveProduct(id, name, qty, price, img)
                        showForm = false
                        selectedProduct = null
                    }
                )
            }
        }
    }
}
