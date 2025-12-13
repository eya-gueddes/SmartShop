package com.eya.smartshop.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String, // uuid ou id Firestore
    val name: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
