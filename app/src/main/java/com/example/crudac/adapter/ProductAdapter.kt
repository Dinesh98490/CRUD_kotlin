package com.example.crudac.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crudac.R
import com.example.crudac.model.ProductModel
import com.example.crudac.model.UpdateActivity
import org.w3c.dom.Text

class ProductAdapter(var context: Context,
                     var data :ArrayList<ProductModel>):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    class ProductViewHolder(view: View):RecyclerView.ViewHolder(view){
        var productName: TextView = view.findViewById(R.id.lblName)
        var productPrice: TextView = view.findViewById(R.id.lblPrice)
        var productDirection: TextView = view.findViewById(R.id.lblDescription)
        var  btnedit : TextView = view.findViewById(R.id.btnedit)
        var  imageview : TextView = view.findViewById(R.id.imageView)
        var  progressbar : TextView = view.findViewById(R.id.progressBar)




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sample_product, parent,false)

        return  ProductViewHolder(view)

    }


    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productName.text = data[position].productName
        holder.productPrice.text = data[position].productPrice.toString()
        holder.productDirection.text = data[position].productDesc


        holder.btnedit.setOnClickListener{
            var intent = Intent(context,UpdateActivity::class.java)
            intent.putExtra("product", data[position])
            context.startActivity(intent)

        }

    }
    fun getProductID(position:Int):String{
        return  data[position].id
    }




}