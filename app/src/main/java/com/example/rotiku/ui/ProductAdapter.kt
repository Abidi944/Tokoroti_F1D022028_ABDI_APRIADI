package com.example.rotiku.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rotiku.databinding.ItemProductBinding
import com.example.rotiku.model.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private val onBuyClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        this.products = newProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.tvProductPrice.text = format.format(product.price)
            binding.ivProductImage.load(product.imageUrl) {
                crossfade(true)
            }
            binding.btnBuy.setOnClickListener { onBuyClicked(product) }
        }
    }
}