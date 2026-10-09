package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EnquiryBookingEntity
import com.example.data.model.HotelConfigEntity
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items ORDER BY isPopular DESC, id ASC")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE category = :category ORDER BY id ASC")
    fun getMenuItemsByCategory(category: String): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE isPopular = 1")
    fun getPopularMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Long): MenuItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMenuItems(items: List<MenuItemEntity>)

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(item: MenuItemEntity)

    @Query("DELETE FROM menu_items WHERE id = :id")
    suspend fun deleteMenuItemById(id: Long)
}

@Dao
interface HotelRoomDao {
    @Query("SELECT * FROM hotel_rooms ORDER BY pricePerNight ASC")
    fun getAllHotelRooms(): Flow<List<HotelRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotelRoom(room: HotelRoomEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHotelRooms(rooms: List<HotelRoomEntity>)

    @Update
    suspend fun updateHotelRoom(room: HotelRoomEntity)

    @Delete
    suspend fun deleteHotelRoom(room: HotelRoomEntity)
}

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
    fun getAllEnquiries(): Flow<List<EnquiryBookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryBookingEntity): Long

    @Query("UPDATE enquiries SET status = :status WHERE id = :id")
    suspend fun updateEnquiryStatus(id: Long, status: String)

    @Delete
    suspend fun deleteEnquiry(enquiry: EnquiryBookingEntity)
}

@Dao
interface HotelConfigDao {
    @Query("SELECT * FROM hotel_config")
    fun getAllConfigs(): Flow<List<HotelConfigEntity>>

    @Query("SELECT value FROM hotel_config WHERE `key` = :key")
    suspend fun getConfigValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setConfig(config: HotelConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllConfigs(configs: List<HotelConfigEntity>)
}
