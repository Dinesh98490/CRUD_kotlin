package com.example.crudac.model

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudac.R
import com.example.crudac.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdateActivity : AppCompatActivity() {
    lateinit var  updateBinding: ActivityUpdateBinding
    var id = ""
    var imageName=""
    var firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference = firebaseDatabase.reference.child("product")
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateBinding  = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)
        var product: ProductModel? = intent.getParcelableExtra("product")
        updateBinding.updatename.setText(product?.productName)
        updateBinding.updateprice.setText(product?.productPrice.toString())
        updateBinding.updatedesc.setText(product?.productDesc)
        Picasso.get().load(product?.url).into(updateBinding.imageUpdate)




        id =product?.id.toString()
        imageName=product?.id.toString()

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






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnSensorList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}
