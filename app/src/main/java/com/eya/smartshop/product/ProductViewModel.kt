package com.eya.smartshop.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    val products = repo.observeLocal()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        repo.startRemoteListener()
    }

    private fun validate(name: String, qty: Int, price: Double): String? {
        if (name.isBlank()) return "Le nom est requis"
        if (price <= 0.0) return "Le prix doit être > 0"
        if (qty < 0) return "La quantité doit être ≥ 0"
        return null
    }

    fun saveProduct(
        id: String?,
        name: String,
        qty: Int,
        price: Double,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            val err = validate(name, qty, price)
            if (err != null) {
                println("Validation error: $err")
                return@launch
            }

            val product = Product(
                id = id ?: UUID.randomUUID().toString(),
                name = name,
                quantity = qty,
                price = price,
                imageUrl = imageUrl,
                updatedAt = System.currentTimeMillis()
            )

            repo.upsert(product)
        }
    }

    fun delete(product: Product) {
        viewModelScope.launch {
            repo.delete(product)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.stopRemoteListener()
    }
}
