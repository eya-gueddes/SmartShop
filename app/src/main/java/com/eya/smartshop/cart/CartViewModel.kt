package com.eya.smartshop.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    // Add product to cart
    fun addProduct(product: com.eya.smartshop.product.Product, qty: Int = 1) {
        if (qty <= 0) return  // constraint: quantity > 0

        viewModelScope.launch {
            val current = _items.value.toMutableList()
            val existing = current.find { it.product.id == product.id }
            if (existing != null) {
                val newQty = existing.quantity + qty
                if (newQty <= product.quantity) { // constraint: can't exceed stock
                    existing.quantity = newQty
                }
            } else {
                if (qty <= product.quantity) {
                    current.add(CartItem(product, qty))
                }
            }
            _items.value = current
        }
    }

    // Remove product completely from cart
    fun removeProduct(product: com.eya.smartshop.product.Product) {
        viewModelScope.launch {
            _items.value = _items.value.filter { it.product.id != product.id }
        }
    }

    // Update quantity
    fun updateQuantity(product: com.eya.smartshop.product.Product, qty: Int) {
        if (qty < 0) return
        viewModelScope.launch {
            val current = _items.value.toMutableList()
            val item = current.find { it.product.id == product.id }
            if (item != null) {
                if (qty == 0) current.remove(item)
                else if (qty <= product.quantity) item.quantity = qty
                _items.value = current
            }
        }
    }

    // Clear cart
    fun clear() {
        _items.value = emptyList()
    }

    // Total price
    fun total(): Double = _items.value.sumOf { it.product.price * it.quantity }

    fun confirmOrder() {
        println("Order confirmed:")
        _items.value.forEach { cartItem ->
            println("${cartItem.product.name} x${cartItem.quantity}")
        }
        _items.value = emptyList() // Clear cart
    }

}
