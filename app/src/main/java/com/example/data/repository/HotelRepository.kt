package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.EnquiryBookingEntity
import com.example.data.model.HotelConfigEntity
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import kotlinx.coroutines.flow.Flow

class HotelRepository(private val db: AppDatabase) {

    private val menuDao = db.menuItemDao()
    private val roomDao = db.hotelRoomDao()
    private val enquiryDao = db.enquiryDao()
    private val configDao = db.hotelConfigDao()

    // Menu Operations
    val allMenuItems: Flow<List<MenuItemEntity>> = menuDao.getAllMenuItems()
    val popularMenuItems: Flow<List<MenuItemEntity>> = menuDao.getPopularMenuItems()

    fun getMenuItemsByCategory(category: String): Flow<List<MenuItemEntity>> {
        return menuDao.getMenuItemsByCategory(category)
    }

    suspend fun insertMenuItem(item: MenuItemEntity): Long {
        return menuDao.insertMenuItem(item)
    }

    suspend fun updateMenuItem(item: MenuItemEntity) {
        menuDao.updateMenuItem(item)
    }

    suspend fun deleteMenuItem(item: MenuItemEntity) {
        menuDao.deleteMenuItem(item)
    }

    suspend fun deleteMenuItemById(id: Long) {
        menuDao.deleteMenuItemById(id)
    }

    // Room Operations
    val allHotelRooms: Flow<List<HotelRoomEntity>> = roomDao.getAllHotelRooms()

    suspend fun insertHotelRoom(room: HotelRoomEntity): Long {
        return roomDao.insertHotelRoom(room)
    }

    suspend fun updateHotelRoom(room: HotelRoomEntity) {
        roomDao.updateHotelRoom(room)
    }

    suspend fun deleteHotelRoom(room: HotelRoomEntity) {
        roomDao.deleteHotelRoom(room)
    }

    // Enquiry & Booking Operations
    val allEnquiries: Flow<List<EnquiryBookingEntity>> = enquiryDao.getAllEnquiries()

    suspend fun submitEnquiry(enquiry: EnquiryBookingEntity): Long {
        return enquiryDao.insertEnquiry(enquiry)
    }

    suspend fun updateEnquiryStatus(id: Long, status: String) {
        enquiryDao.updateEnquiryStatus(id, status)
    }

    suspend fun deleteEnquiry(enquiry: EnquiryBookingEntity) {
        enquiryDao.deleteEnquiry(enquiry)
    }

    // Config Operations
    val allConfigs: Flow<List<HotelConfigEntity>> = configDao.getAllConfigs()

    suspend fun getConfigValue(key: String): String? {
        return configDao.getConfigValue(key)
    }

    suspend fun saveConfig(key: String, value: String) {
        configDao.setConfig(HotelConfigEntity(key, value))
    }
}
