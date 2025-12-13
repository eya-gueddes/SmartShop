package com.eya.smartshop.product

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class ProductRepository(
    private val dao: ProductDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val col = firestore.collection("products")
    private var listener: ListenerRegistration? = null

    // expose local products flow
    fun observeLocal(): Flow<List<Product>> = dao.getAll()

    // push local upsert to cloud
    suspend fun upsert(product: Product) {
        dao.upsert(product)
        // push to firestore
        val map = hashMapOf(
            "id" to product.id,
            "name" to product.name,
            "quantity" to product.quantity,
            "price" to product.price,
            "imageUrl" to product.imageUrl,
            "updatedAt" to product.updatedAt
        )
        col.document(product.id).set(map)
    }

    suspend fun delete(product: Product) {
        dao.delete(product)
        col.document(product.id).delete()
    }

    // start listening remote -> apply to local
    fun startRemoteListener(scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
        listener?.remove()
        listener = col.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            // apply each doc to local db
            scope.launch {
                for (doc in snapshot.documents) {
                    val id = doc.getString("id") ?: continue
                    val name = doc.getString("name") ?: ""
                    val quantity = (doc.getLong("quantity") ?: 0L).toInt()
                    val price = doc.getDouble("price") ?: 0.0
                    val imageUrl = doc.getString("imageUrl")
                    val updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                    val product = Product(id, name, quantity, price, imageUrl, updatedAt)
                    dao.upsert(product)
                }

                // handle delete if local has id not in snapshot, remove local
                val remoteIds = snapshot.documents.mapNotNull { it.getString("id") }.toSet()
            }
        }
    }

    fun stopRemoteListener() {
        listener?.remove(); listener = null
    }
}
