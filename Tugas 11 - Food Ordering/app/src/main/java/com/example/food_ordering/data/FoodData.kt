package com.example.food_ordering.data

import java.text.NumberFormat
import java.util.Locale

// ---------- Model data ----------
data class Food(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val emoji: String, // dipakai sebagai "gambar" makanan
    val rating: Double,
    val description: String
)

data class CartItem(
    val food: Food,
    val quantity: Int
)

data class Order(
    val id: Int,
    val items: List<CartItem>,
    val total: Int,
    val address: String,
    val paymentMethod: String
)

// ---------- Data pilihan ----------
val categories = listOf("Semua", "Makanan", "Minuman", "Camilan")

val paymentMethods = listOf("Transfer Bank", "E-Wallet (OVO, GoPay, DANA)", "QRIS")

// ---------- Data menu (dummy) ----------
val sampleFoods = listOf(
    Food(id = 1, name = "Nasi Goreng Spesial", price = 18000, category = "Makanan", emoji = "🍛", rating = 4.8,
        description = "Nasi goreng dengan bumbu spesial, telur mata sapi, dan sayuran segar."),
    Food(id = 2, name = "Ayam Geprek", price = 20000, category = "Makanan", emoji = "🍗", rating = 4.7,
        description = "Ayam goreng tepung renyah yang digeprek dengan sambal bawang pedas."),
    Food(id = 3, name = "Mie Ayam", price = 15000, category = "Makanan", emoji = "🍜", rating = 4.6,
        description = "Mie kenyal dengan topping ayam kecap, sawi, dan pangsit goreng."),
    Food(id = 4, name = "Sate Ayam", price = 22000, category = "Makanan", emoji = "🍢", rating = 4.8,
        description = "10 tusuk sate ayam bakar dengan bumbu kacang dan lontong."),
    Food(id = 5, name = "Bakso Urat", price = 17000, category = "Makanan", emoji = "🍲", rating = 4.5,
        description = "Bakso urat kenyal dengan kuah kaldu sapi hangat dan bihun."),
    Food(id = 6, name = "Es Teh Manis", price = 5000, category = "Minuman", emoji = "🥤", rating = 4.4,
        description = "Teh manis dingin yang menyegarkan."),
    Food(id = 7, name = "Es Jeruk", price = 7000, category = "Minuman", emoji = "🍊", rating = 4.5,
        description = "Perasan jeruk segar dengan es batu."),
    Food(id = 8, name = "Kopi Susu", price = 12000, category = "Minuman", emoji = "☕", rating = 4.7,
        description = "Kopi susu gula aren yang creamy dan wangi."),
    Food(id = 9, name = "Pisang Goreng", price = 10000, category = "Camilan", emoji = "🍌", rating = 4.3,
        description = "Pisang goreng crispy dengan taburan gula halus."),
    Food(id = 10, name = "Kentang Goreng", price = 12000, category = "Camilan", emoji = "🍟", rating = 4.4,
        description = "Kentang goreng renyah dengan saus sambal.")
)

// Ubah angka jadi format Rupiah, contoh: 18000 -> "Rp18.000"
fun formatRupiah(amount: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp" + formatter.format(amount)
}
