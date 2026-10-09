package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.EnquiryBookingEntity
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import com.example.data.repository.HotelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HotelInfoState(
    val name: String = "Bam Bam Janki Complex",
    val tagline: String = "स्वाद, सेवा और आतिथ्य का भरोसेमंद नाम",
    val phone: String = "+91 98765 43210",
    val whatsapp: String = "+919876543210",
    val email: String = "info@bambamjankicomplex.com",
    val address: String = "Near Main Chowk, Station Road, Bihar, India",
    val openingHours: String = "Mon - Sun: 7:00 AM - 11:00 PM",
    val adminPin: String = "1234"
)

data class EnquiryDialogTarget(
    val title: String,
    val serviceType: String, // "FOOD_ORDER", "ROOM_BOOKING", "TABLE_BOOKING", "GENERAL"
    val prefilledItemName: String = "",
    val price: Double? = null
)

class HotelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HotelRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HotelRepository(db)
    }

    // Config & Info State
    val hotelInfo = MutableStateFlow(HotelInfoState())

    init {
        viewModelScope.launch {
            repository.allConfigs.collect { configs ->
                if (configs.isNotEmpty()) {
                    var current = hotelInfo.value
                    configs.forEach { config ->
                        when (config.key) {
                            "hotel_name" -> current = current.copy(name = config.value)
                            "tagline" -> current = current.copy(tagline = config.value)
                            "phone" -> current = current.copy(phone = config.value)
                            "whatsapp" -> current = current.copy(whatsapp = config.value)
                            "email" -> current = current.copy(email = config.value)
                            "address" -> current = current.copy(address = config.value)
                            "opening_hours" -> current = current.copy(openingHours = config.value)
                            "admin_pin" -> current = current.copy(adminPin = config.value)
                        }
                    }
                    hotelInfo.value = current
                }
            }
        }
    }

    // Menu Filters
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val vegOnlyFilter = MutableStateFlow(false)

    // Raw Menu List from DB
    private val rawMenuItems = repository.allMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered Menu List
    val filteredMenuItems: StateFlow<List<MenuItemEntity>> = combine(
        rawMenuItems,
        searchQuery,
        selectedCategory,
        vegOnlyFilter
    ) { items, query, category, vegOnly ->
        items.filter { item ->
            val matchesCategory = (category == "All" || item.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true) || item.description.contains(query, ignoreCase = true)
            val matchesVeg = !vegOnly || item.isVeg
            matchesCategory && matchesQuery && matchesVeg
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Popular Items
    val popularMenuItems = repository.popularMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Hotel Rooms
    val hotelRooms = repository.allHotelRooms.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Customer Enquiries
    val enquiries = repository.allEnquiries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Active Enquiry Dialog
    val activeEnquiryDialog = MutableStateFlow<EnquiryDialogTarget?>(null)
    val enquirySubmissionResult = MutableStateFlow<String?>(null)

    // Admin State
    val isAdminMode = MutableStateFlow(false)
    val adminError = MutableStateFlow<String?>(null)

    fun openEnquiryDialog(target: EnquiryDialogTarget) {
        activeEnquiryDialog.value = target
        enquirySubmissionResult.value = null
    }

    fun closeEnquiryDialog() {
        activeEnquiryDialog.value = null
    }

    fun submitEnquiry(
        name: String,
        phone: String,
        email: String,
        serviceType: String,
        date: String,
        guestCount: Int,
        itemOrRoom: String,
        message: String
    ) {
        viewModelScope.launch {
            val entity = EnquiryBookingEntity(
                customerName = name,
                phone = phone,
                email = email,
                serviceType = serviceType,
                date = date,
                guestCount = guestCount,
                selectedItemOrRoom = itemOrRoom,
                message = message
            )
            val id = repository.submitEnquiry(entity)
            if (id > 0) {
                enquirySubmissionResult.value = "Your enquiry/order #$id has been received! Our team will call you shortly."
            } else {
                enquirySubmissionResult.value = "Failed to submit enquiry. Please try again or call us directly."
            }
        }
    }

    // Owner / Admin Actions
    fun loginAdmin(pin: String): Boolean {
        return if (pin == hotelInfo.value.adminPin) {
            isAdminMode.value = true
            adminError.value = null
            true
        } else {
            adminError.value = "Incorrect PIN code"
            false
        }
    }

    fun logoutAdmin() {
        isAdminMode.value = false
    }

    fun saveMenuItem(item: MenuItemEntity) {
        viewModelScope.launch {
            if (item.id == 0L) {
                repository.insertMenuItem(item)
            } else {
                repository.updateMenuItem(item)
            }
        }
    }

    fun deleteMenuItem(item: MenuItemEntity) {
        viewModelScope.launch {
            repository.deleteMenuItem(item)
        }
    }

    fun saveHotelRoom(room: HotelRoomEntity) {
        viewModelScope.launch {
            if (room.id == 0L) {
                repository.insertHotelRoom(room)
            } else {
                repository.updateHotelRoom(room)
            }
        }
    }

    fun deleteHotelRoom(room: HotelRoomEntity) {
        viewModelScope.launch {
            repository.deleteHotelRoom(room)
        }
    }

    fun updateHotelConfig(
        name: String,
        tagline: String,
        phone: String,
        whatsapp: String,
        email: String,
        address: String,
        openingHours: String,
        adminPin: String = ""
    ) {
        viewModelScope.launch {
            repository.saveConfig("hotel_name", name)
            repository.saveConfig("tagline", tagline)
            repository.saveConfig("phone", phone)
            repository.saveConfig("whatsapp", whatsapp)
            repository.saveConfig("email", email)
            repository.saveConfig("address", address)
            repository.saveConfig("opening_hours", openingHours)
            if (adminPin.isNotBlank()) {
                repository.saveConfig("admin_pin", adminPin)
            }
        }
    }

    fun updateEnquiryStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(id, status)
        }
    }
}
