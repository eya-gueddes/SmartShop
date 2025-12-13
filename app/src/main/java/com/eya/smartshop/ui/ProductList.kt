package com.eya.smartshop.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eya.smartshop.product.Product
import com.eya.smartshop.product.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(vm: ProductViewModel) {

    val list by vm.products.collectAsState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products", color = MaterialTheme.colorScheme.onPrimary) },
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // IMAGE + INFOS
                            Row {
                                AsyncImage(
                                    model = p.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp)
                                )

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        p.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text("Qty: ${p.quantity}", color = MaterialTheme.colorScheme.onSurface)
                                    Text("Price: ${p.price}", color = MaterialTheme.colorScheme.onSurface)
                                }
                            }

                            // ICONS
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                IconButton(
                                    onClick = {
                                        selectedProduct = p
                                        showForm = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Modifier",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = { vm.delete(p) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Supprimer",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
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
