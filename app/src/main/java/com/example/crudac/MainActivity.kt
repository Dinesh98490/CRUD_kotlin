package com.example.crudac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudac.adapter.ProductAdapter
import com.example.crudac.databinding.ActivityMainBinding
import com.example.crudac.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    var firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference = firebaseDatabase.reference.child("product")

    var productList = ArrayList<ProductModel>()
    lateinit var  productAdapter: ProductAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        productAdapter = ProductAdapter(this@MainActivity, productList)

        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for(eachData in snapshot.children){
                    var product = eachData.getValue(ProductModel::class.java)
                    if(product != null){
                        Log.d("my data", product.productName)
                        Log.d("my data", product.productPrice.toString())
                        Log.d("my data", product.productDesc)

                        productList.add(product)

                    }
                    var adapter = ProductAdapter(this@MainActivity, productList)
                    mainBinding.recylerview.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recylerview.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = productAdapter.getProductID(viewHolder.adapterPosition)
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Delete Product")
                builder.setMessage("Are you sure to want to delete this product? ")
                builder.setPositiveButton("Yes") { dialog, _ ->

                ref.child(id).removeValue().addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()

                    }else{

                        Toast.makeText(applicationContext, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
                    dialog.dismiss()
            }
                builder.setNegativeButton("No") { dialog, _ ->
                    // Notify the adapter about the change so it can reset the swipe state
                    productAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    dialog.dismiss()
                }
                builder.create().show()
            }

        }).attachToRecyclerView(mainBinding.recylerview)


        mainBinding.floatingActionButton.setOnClickListener{
            var intent = Intent(this@MainActivity, AddProductActivity::class.java)
            startActivity(intent)
        }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnSensorList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}