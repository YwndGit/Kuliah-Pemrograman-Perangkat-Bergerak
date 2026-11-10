package com.example.food_ordering

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.food_ordering.data.CartItem
import com.example.food_ordering.data.Food
import com.example.food_ordering.data.Order
import com.example.food_ordering.data.sampleFoods

enum class ThemeMode { SYSTEM, LIGHT, DARK }

/**
 * Menyimpan state yang dipakai banyak halaman:
 * keranjang, favorit, pesanan, dan pilihan tema.
 * Data di ViewModel tidak hilang saat layar diputar (rotasi).
 */
class FoodViewModel : ViewModel() {

    // ---------- Data menu ----------
    val foods: List<Food> = sampleFoods

    // ---------- State ----------
    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    private val _favoriteIds = mutableStateListOf<Int>()

    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order> get() = _orders

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    var dynamicColor by mutableStateOf(false)
        private set

    // ---------- Nilai yang dihitung dari isi keranjang ----------
    val cartCount: Int get() = _cart.sumOf { it.quantity }
    val subtotal: Int get() = _cart.sumOf { it.food.price * it.quantity }
    val shippingCost: Int get() = if (_cart.isEmpty()) 0 else 5000
    val total: Int get() = subtotal + shippingCost

    // ---------- Menu ----------
    fun getFood(id: Int): Food? = foods.find { it.id == id }

    // ---------- Keranjang ----------
    fun addToCart(food: Food, quantity: Int = 1) {
        val index = _cart.indexOfFirst { it.food.id == food.id }
        if (index >= 0) {
            val item = _cart[index]
            _cart[index] = item.copy(quantity = item.quantity + quantity)
        } else {
            _cart.add(CartItem(food, quantity))
        }
    }

    fun updateQuantity(foodId: Int, quantity: Int) {
        val index = _cart.indexOfFirst { it.food.id == foodId }
        if (index == -1) return
        if (quantity <= 0) {
            _cart.removeAt(index)
        } else {
            _cart[index] = _cart[index].copy(quantity = quantity)
        }
    }

    fun removeFromCart(foodId: Int) {
        _cart.removeAll { it.food.id == foodId }
    }

    // ---------- Favorit ----------
    fun isFavorite(foodId: Int): Boolean = foodId in _favoriteIds

    fun toggleFavorite(foodId: Int) {
        if (foodId in _favoriteIds) {
            _favoriteIds.remove(foodId)
        } else {
            _favoriteIds.add(foodId)
        }
    }

    // ---------- Pesanan ----------
    fun placeOrder(address: String, paymentMethod: String) {
        val newOrder = Order(
            id = _orders.size + 1,
            items = _cart.toList(),
            total = total,
            address = address,
            paymentMethod = paymentMethod
        )
        _orders.add(0, newOrder) // pesanan terbaru di paling atas
        _cart.clear()
    }

    // ---------- Tema ----------
    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
    }

    fun updateDynamicColor(enabled: Boolean) {
        dynamicColor = enabled
    }
}
