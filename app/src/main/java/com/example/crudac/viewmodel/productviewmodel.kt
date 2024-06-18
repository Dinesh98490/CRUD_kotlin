package com.example.crudac.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudac.model.ProductModel
import com.google.firebase.database.*

class productviewmodel {

    class ProductViewModel : ViewModel() {
        private val _products = MutableLiveData<List<ProductModel>>()
        val products: LiveData<List<ProductModel>> get() = _products

        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("product")

        init {
            fetchProducts()
        }

        private fun fetchProducts() {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<ProductModel>()
                    for (eachData in snapshot.children) {
                        val product = eachData.getValue(ProductModel::class.java)
                        product?.let { productList.add(it) }
                    }
                    _products.value = productList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

        fun deleteProduct(id: String, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
            databaseReference.child(id).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message)
                }
            }
        }

        fun updateProduct(id: String, updateMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
            databaseReference.child(id).updateChildren(updateMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message)
                }
            }
        }

        fun addProduct(product: ProductModel, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
            val newProductRef = databaseReference.push()
            product.id = newProductRef.key.toString()
            newProductRef.setValue(product).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message)
                }
            }
        }
    }

}