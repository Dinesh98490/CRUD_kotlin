package com.example.crudac.model

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudac.R
import com.example.crudac.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    lateinit var  updateBinding: ActivityUpdateBinding
    var id = ""
    var firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference = firebaseDatabase.reference.child("product")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateBinding  = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)
        var product: ProductModel? = intent.getParcelableExtra("product")
        updateBinding.updatename.setText(product?.productName)
        updateBinding.updateprice.setText(product?.productPrice.toString())
        updateBinding.updatedesc.setText(product?.productDesc)



        id =product?.id.toString()

        updateBinding.updatebutton.setOnClickListener {
            var updateName : String = updateBinding.updatename.text.toString()
            var updatePrice : Int = updateBinding.updateprice.text.toString().toInt()
            var updateDisc : String = updateBinding.updatedesc.text.toString()

            var updateMap = mutableMapOf<String, Any>()
            updateMap["productName"] = updateName
            updateMap["productPrice"] = updatePrice
            updateMap["productDesc"] = updateDisc
            updateMap["id"] = id
            ref.child(id).updateChildren(updateMap).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "Data Updated", Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,
                        it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }


        }






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}