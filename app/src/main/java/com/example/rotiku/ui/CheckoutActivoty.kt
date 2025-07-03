package com.example.rotiku.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.example.rotiku.databinding.ActivityCheckoutBinding
import com.example.rotiku.model.Order
import com.example.rotiku.viewmodel.MainViewModel

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> getCurrentLocation()
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> getCurrentLocation()
            else -> Toast.makeText(this, "Izin lokasi dibutuhkan untuk memesan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("PRODUCT_ID") ?: ""
        val productName = intent.getStringExtra("PRODUCT_NAME")
        binding.tvCheckoutProductName.text = "Anda membeli: $productName"

        checkLocationPermission()
        setupClickListener(productId)
        observeViewModel()
    }

    private fun setupClickListener(productId: String) {
        binding.btnPlaceOrder.setOnClickListener {
            val customerName = binding.etCustomerName.text.toString().trim()
            if (customerName.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (latitude == 0.0 || longitude == 0.0) {
                Toast.makeText(this, "Lokasi belum didapatkan, harap tunggu.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val order = Order(customerName, productId, latitude, longitude)
            mainViewModel.placeOrder(order)
        }
    }

    private fun observeViewModel() {
        mainViewModel.orderStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_LONG).show()
                finish()
            }.onFailure {
                Toast.makeText(this, "Gagal membuat pesanan: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        binding.tvLocation.text = "Lokasi: ${"%.4f".format(latitude)}, ${"%.4f".format(longitude)}"
                        Toast.makeText(this, "Lokasi berhasil didapatkan", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}