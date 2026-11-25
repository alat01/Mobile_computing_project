package com.example.mobilecomputinproject.repository

import com.example.mobilecomputinproject.model.CartItem
import com.example.mobilecomputinproject.model.Order
import com.example.mobilecomputinproject.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Repository class to manage mock products list, cart items state, and order history
class ShopRepository {

    private val _products = listOf(
        Product(
            id = "p1",
            name = "Wireless Noise-Canceling Headphones",
            description = "Immersive sound, active noise cancellation, 30-hour battery life, and ultra-comfortable ear cushions.",
            price = 199.99,
            category = "Electronics",
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
            rating = 4.8
        ),
        Product(
            id = "p2",
            name = "Smart Fitness Watch",
            description = "Track heart rate, sleep quality, workouts, and receive smartphone notifications on the go.",
            price = 129.50,
            category = "Electronics",
            imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500",
            rating = 4.6
        ),
        Product(
            id = "p3",
            name = "Ergonomic Mechanical Keyboard",
            description = "RGB backlit mechanical keyboard with tactile switches for peak typing productivity and gaming.",
            price = 89.99,
            category = "Electronics",
            imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500",
            rating = 4.7
        ),
        Product(
            id = "p4",
            name = "Classic Leather Jacket",
            description = "Genuine premium leather jacket with timeless styling, warm lining, and durable zippers.",
            price = 249.00,
            category = "Fashion",
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500",
            rating = 4.5
        ),
        Product(
            id = "p5",
            name = "Minimalist Canvas Backpack",
            description = "Water-resistant daily backpack with padded laptop compartment and spacious storage.",
            price = 59.99,
            category = "Fashion",
            imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500",
            rating = 4.4
        ),
        Product(
            id = "p6",
            name = "Stainless Steel Insulated Bottle",
            description = "Keeps drinks ice cold for 24 hours or piping hot for 12 hours. Eco-friendly and leakproof.",
            price = 34.99,
            category = "Home & Living",
            imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500",
            rating = 4.9
        ),
        Product(
            id = "p7",
            name = "LED Desk Lamp with USB Charger",
            description = "Adjustable color temperature, brightness levels, and convenient built-in USB charging port.",
            price = 45.00,
            category = "Home & Living",
            imageUrl = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=500",
            rating = 4.3
        ),
        Product(
            id = "p8",
            name = "Bestselling Sci-Fi Novel",
            description = "An epic interstellar adventure exploring humanity's future across distant galaxies.",
            price = 22.50,
            category = "Books",
            imageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500",
            rating = 4.9
        )
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun getProducts(): List<Product> = _products

    fun getProductById(id: String): Product? = _products.find { it.id == id }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val item = currentList[existingIndex]
            currentList[existingIndex] = item.copy(quantity = item.quantity + quantity)
        } else {
            currentList.add(CartItem(product, quantity))
        }
        _cartItems.value = currentList
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            if (quantity <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index] = currentList[index].copy(quantity = quantity)
            }
            _cartItems.value = currentList
        }
    }

    fun removeFromCart(productId: String) {
        val currentList = _cartItems.value.toMutableList()
        currentList.removeAll { it.product.id == productId }
        _cartItems.value = currentList
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(shippingAddress: String, paymentMethod: String): Order? {
        val items = _cartItems.value
        if (items.isEmpty()) return null

        val total = items.sumOf { it.totalPrice }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateStr = sdf.format(Date())
        val orderId = "ORD-${System.currentTimeMillis().toString().takeLast(6)}"

        val newOrder = Order(
            orderId = orderId,
            items = items.toList(),
            totalAmount = total,
            shippingAddress = shippingAddress,
            paymentMethod = paymentMethod,
            date = dateStr,
            status = "Processing"
        )

        val updatedOrders = _orders.value.toMutableList()
        updatedOrders.add(0, newOrder)
        _orders.value = updatedOrders
        clearCart()
        return newOrder
    }
}
