package com.example.crudac.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crudac.model.ProductModel
import com.google.firebase.database.*

class productrepository {



    class ProductRepository {
        private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val productRef: DatabaseReference = firebaseDatabase.reference.child("product")

        fun getProducts(): LiveData<List<ProductModel>> {
            val productsLiveData = MutableLiveData<List<ProductModel>>()
            productRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = ArrayList<ProductModel>()
                    for (eachData in snapshot.children) {
                        val product = eachData.getValue(ProductModel::class.java)
                        product?.let { productList.add(it) }
                    }
                    productsLiveData.postValue(productList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
            return productsLiveData
        }

        fun addProduct(product: ProductModel): LiveData<Boolean> {
            val result = MutableLiveData<Boolean>()
            product.id = productRef.push().key ?: ""
            productRef.child(product.id).setValue(product).addOnCompleteListener {
                result.value = it.isSuccessful
            }
            return result
        }

        fun updateProduct(product: ProductModel): LiveData<Boolean> {
            val result = MutableLiveData<Boolean>()
            productRef.child(product.id).setValue(product).addOnCompleteListener {
                result.value = it.isSuccessful
            }
            return result
        }

        fun deleteProduct(productId: String): LiveData<Boolean> {
            val result = MutableLiveData<Boolean>()
            productRef.child(productId).removeValue().addOnCompleteListener {
                result.value = it.isSuccessful
            }
            return result
        }
    }

}