package com.example.mobilecomputinproject.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String,
    val rating: Double,
    val stock: Int = 10
)

data class CartItem(
    val product: Product,
    var quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val shippingAddress: String,
    val paymentMethod: String,
    val date: String,
    val status: String = "Processing"
)
