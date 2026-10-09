package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.EnquiryBookingEntity
import com.example.data.model.HotelConfigEntity
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MenuItemEntity::class,
        HotelRoomEntity::class,
        EnquiryBookingEntity::class,
        HotelConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao
    abstract fun hotelRoomDao(): HotelRoomDao
    abstract fun enquiryDao(): EnquiryDao
    abstract fun hotelConfigDao(): HotelConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bam_bam_janki_db"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            val menuDao = db.menuItemDao()
            val roomDao = db.hotelRoomDao()
            val configDao = db.hotelConfigDao()

            // Pre-populate Menu Items
            val initialMenuItems = listOf(
                MenuItemEntity(
                    name = "Royal Maharaja Thali",
                    description = "Special Thali with Paneer Butter Masala, Dal Makhani, Jeera Rice, Butter Naan, Salad & Gulab Jamun",
                    price = 280.0,
                    category = "Indian Cuisine",
                    drawableResName = "img_royal_thali",
                    isVeg = true,
                    isPopular = true,
                    isChefSpecial = true
                ),
                MenuItemEntity(
                    name = "Special Paneer Butter Masala",
                    description = "Cottage cheese cubes simmered in a rich tomato, butter, and cashew gravy",
                    price = 240.0,
                    category = "Indian Cuisine",
                    drawableResName = "img_royal_thali",
                    isVeg = true,
                    isPopular = true,
                    isChefSpecial = true
                ),
                MenuItemEntity(
                    name = "Dal Makhani Special",
                    description = "Slow-cooked black lentils cooked overnight with cream, butter, and authentic Indian spices",
                    price = 190.0,
                    category = "Indian Cuisine",
                    isVeg = true,
                    isPopular = true
                ),
                MenuItemEntity(
                    name = "Butter Naan",
                    description = "Freshly baked soft tandoori naan brushed with rich Amul butter",
                    price = 45.0,
                    category = "Indian Cuisine",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Desi Ghee Chole Bhature",
                    description = "Fluffy hot bhature served with spiced Punjabi chickpeas, pickles & onion salad",
                    price = 120.0,
                    category = "Breakfast",
                    isVeg = true,
                    isPopular = true
                ),
                MenuItemEntity(
                    name = "Crispy Masala Dosa",
                    description = "Golden rice crepe stuffed with spiced potato filling, served with sambar & coconut chutney",
                    price = 110.0,
                    category = "Breakfast",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Banarasi Puri Bhaji",
                    description = "4 Crispy hot puris served with tangy potato curry and pickle",
                    price = 90.0,
                    category = "Breakfast",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Hyderabadi Veg Dum Biryani",
                    description = "Aromatic basmati rice cooked with mixed vegetables, saffron, and exotic biryani spices",
                    price = 220.0,
                    category = "Lunch",
                    isVeg = true,
                    isPopular = true
                ),
                MenuItemEntity(
                    name = "Jeera Rice & Tadka Dal",
                    description = "Fragrant cumin basmati rice served with yellow arhar dal fry",
                    price = 150.0,
                    category = "Lunch",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Crispy Paneer Tikka",
                    description = "Marinated paneer cubes grilled in tandoor with mint chutney",
                    price = 190.0,
                    category = "Snacks",
                    isVeg = true,
                    isPopular = true
                ),
                MenuItemEntity(
                    name = "Special Veg Chowmein",
                    description = "Wok-tossed noodles with fresh crunchy vegetables and Indo-Chinese sauces",
                    price = 120.0,
                    category = "Fast Food",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Crispy Veg Spring Rolls",
                    description = "Gold fried rolls filled with seasoned Asian vegetables",
                    price = 130.0,
                    category = "Fast Food",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Kulhad Masala Tea",
                    description = "Traditional clay-pot boiled cardamom & ginger tea",
                    price = 30.0,
                    category = "Beverages",
                    isVeg = true,
                    isPopular = true
                ),
                MenuItemEntity(
                    name = "Creamy Mango Lassi",
                    description = "Thick churned yogurt drink flavoured with sweet mango pulp",
                    price = 80.0,
                    category = "Beverages",
                    isVeg = true
                ),
                MenuItemEntity(
                    name = "Soft Hot Gulab Jamun (2 Pcs)",
                    description = "Deep fried milk dumplings soaked in cardamom sugar syrup",
                    price = 60.0,
                    category = "Beverages",
                    isVeg = true
                )
            )
            menuDao.insertAllMenuItems(initialMenuItems)

            // Pre-populate Hotel Rooms
            val initialRooms = listOf(
                HotelRoomEntity(
                    title = "Deluxe AC Bedroom",
                    roomType = "Deluxe AC",
                    description = "Spacious air-conditioned room with king bed, attached spotless bathroom, ambient LED lights, LED TV, and fast Wi-Fi.",
                    pricePerNight = 1800.0,
                    maxGuests = 2,
                    drawableResName = "img_luxury_room",
                    amenities = "Air Conditioner, Free High-Speed Wi-Fi, Smart TV, 24/7 Hot Water, Daily Room Service, Electric Kettle"
                ),
                HotelRoomEntity(
                    title = "Royal Executive Family Suite",
                    roomType = "Family Suite",
                    description = "Luxury suite designed for families. Features two double beds, living lounge area, balcony view, double AC, and premium toiletries.",
                    pricePerNight = 3200.0,
                    maxGuests = 4,
                    drawableResName = "img_hotel_exterior",
                    amenities = "2 ACs, Separate Living Lounge, Free Wi-Fi, Mini Refrigerator, 24/7 Room Service, Sofa Seating, Tea Maker"
                ),
                HotelRoomEntity(
                    title = "Super Comfort Non-AC Room",
                    roomType = "Economy Comfort",
                    description = "Clean & quiet budget room featuring comfortable queen bed, ceiling fan, TV, and neat clean private bathroom.",
                    pricePerNight = 1200.0,
                    maxGuests = 2,
                    drawableResName = "img_luxury_room",
                    amenities = "Ceiling Fan, Free Wi-Fi, Flat-screen TV, Attached Bathroom, Daily Housekeeping"
                )
            )
            roomDao.insertAllHotelRooms(initialRooms)

            // Pre-populate Hotel Configs
            val initialConfigs = listOf(
                HotelConfigEntity("hotel_name", "Bam Bam Janki Complex"),
                HotelConfigEntity("tagline", "स्वाद, सेवा और आतिथ्य का भरोसेमंद नाम"),
                HotelConfigEntity("phone", "+91 98765 43210"),
                HotelConfigEntity("whatsapp", "+919876543210"),
                HotelConfigEntity("email", "info@bambamjankicomplex.com"),
                HotelConfigEntity("address", "Near Main Chowk, Station Road, Bihar, India"),
                HotelConfigEntity("opening_hours", "Mon - Sun: 7:00 AM - 11:00 PM"),
                HotelConfigEntity("admin_pin", "1234")
            )
            configDao.setAllConfigs(initialConfigs)
        }
    }
}
