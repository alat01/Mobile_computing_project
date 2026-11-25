package com.example.mobilecomputinproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mobilecomputinproject.model.CartItem
import com.example.mobilecomputinproject.model.Order
import com.example.mobilecomputinproject.model.Product
import com.example.mobilecomputinproject.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope

class ShopViewModel : ViewModel() {
    private val repository = ShopRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("All", "Electronics", "Fashion", "Home & Living", "Books")

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val orders: StateFlow<List<Order>> = repository.orders

    val cartTotal: StateFlow<Double> = repository.cartItems
        .combine(MutableStateFlow(0.0)) { items, _ ->
            items.sumOf { it.totalPrice }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartItemCount: StateFlow<Int> = repository.cartItems
        .combine(MutableStateFlow(0)) { items, _ ->
            items.sumOf { it.quantity }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _filteredProducts = MutableStateFlow(repository.getProducts())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterProducts(query, _selectedCategory.value)
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        filterProducts(_searchQuery.value, category)
    }

    private fun filterProducts(query: String, category: String) {
        val all = repository.getProducts()
        val filtered = all.filter { product ->
            val matchesCategory = category == "All" || product.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
        _filteredProducts.value = filtered
    }

    fun getProductById(id: String): Product? {
        return repository.getProductById(id)
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
    }

    fun updateQuantity(productId: String, quantity: Int) {
        repository.updateCartQuantity(productId, quantity)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun placeOrder(address: String, paymentMethod: String): Order? {
        return repository.placeOrder(address, paymentMethod)
    }
}
