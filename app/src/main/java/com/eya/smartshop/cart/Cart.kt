package com.eya.smartshop.cart

import com.eya.smartshop.product.Product

data class CartItem(
    val product: Product,
    var quantity: Int
)
