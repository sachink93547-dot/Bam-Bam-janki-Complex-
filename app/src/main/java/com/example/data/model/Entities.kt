package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String, // Breakfast, Lunch, Dinner, Snacks, Fast Food, Indian Cuisine, Beverages
    val drawableResName: String? = null, // e.g. "img_royal_thali"
    val imageUrl: String? = null,
    val isVeg: Boolean = true,
    val isAvailable: Boolean = true,
    val isPopular: Boolean = false,
    val isChefSpecial: Boolean = false
)

@Entity(tableName = "hotel_rooms")
data class HotelRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val roomType: String, // Deluxe, Family Suite, Executive, Economy
    val description: String,
    val pricePerNight: Double,
    val maxGuests: Int,
    val drawableResName: String? = null,
    val imageUrl: String? = null,
    val amenities: String, // Comma separated amenities
    val isAvailable: Boolean = true
)

@Entity(tableName = "enquiries")
data class EnquiryBookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerName: String,
    val phone: String,
    val email: String,
    val serviceType: String, // Food Order, Table Booking, Room Booking, General Enquiry
    val date: String,
    val guestCount: Int,
    val selectedItemOrRoom: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // PENDING, CONFIRMED, COMPLETED
)

@Entity(tableName = "hotel_config")
data class HotelConfigEntity(
    @PrimaryKey val key: String,
    val value: String
)
