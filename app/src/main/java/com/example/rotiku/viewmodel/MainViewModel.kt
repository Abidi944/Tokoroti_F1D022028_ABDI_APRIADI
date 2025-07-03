package com.example.rotiku.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rotiku.model.Order
import com.example.rotiku.model.Product
import com.example.rotiku.repository.ProductRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _orderStatus = MutableLiveData<Result<Boolean>>()
    val orderStatus: LiveData<Result<Boolean>> = _orderStatus

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val productList = repository.getProducts()
                _products.postValue(productList)
            } catch (e: Exception) {
                // Handle error
                _products.postValue(emptyList())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.createOrder(order)
                _orderStatus.postValue(Result.success(true))
            } catch (e: Exception) {
                _orderStatus.postValue(Result.failure(e))
            }
        }
    }
}