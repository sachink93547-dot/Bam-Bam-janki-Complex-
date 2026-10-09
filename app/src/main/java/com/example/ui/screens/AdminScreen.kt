package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnquiryBookingEntity
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import com.example.ui.components.SectionTitle
import com.example.ui.components.VegNonVegDot
import com.example.ui.viewmodel.HotelInfoState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    isAdminMode: Boolean,
    adminError: String?,
    hotelInfo: HotelInfoState,
    menuItems: List<MenuItemEntity>,
    rooms: List<HotelRoomEntity>,
    enquiries: List<EnquiryBookingEntity>,
    onLoginAdmin: (String) -> Boolean,
    onLogoutAdmin: () -> Unit,
    onSaveMenuItem: (MenuItemEntity) -> Unit,
    onDeleteMenuItem: (MenuItemEntity) -> Unit,
    onSaveHotelRoom: (HotelRoomEntity) -> Unit,
    onDeleteHotelRoom: (HotelRoomEntity) -> Unit,
    onUpdateHotelConfig: (
        name: String,
        tagline: String,
        phone: String,
        whatsapp: String,
        email: String,
        address: String,
        openingHours: String,
        adminPin: String
    ) -> Unit,
    onUpdateEnquiryStatus: (Long, String) -> Unit
) {
    if (!isAdminMode) {
        // PIN Prompt
        var pinInput by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("admin_login_screen"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Owner / Admin Login",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Enter your PIN code to edit food items, prices, contact details, and view customer enquiries.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (adminError != null) {
                        Text(
                            text = adminError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        label = { Text("Enter Admin PIN (Default: 1234)") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("admin_pin_input")
                    )

                    Button(
                        onClick = { onLoginAdmin(pinInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_submit_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Unlock Owner Portal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // Authenticated Owner Panel
        var selectedTab by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_portal_screen")
        ) {
            // Header bar
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Owner Management Portal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Easy updates for ${hotelInfo.name}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }

                    IconButton(onClick = onLogoutAdmin) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Menu Items", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Hotel Rooms", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Hotel, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Contact Info", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.EditLocation, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Enquiries (${enquiries.size})", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> AdminMenuManager(menuItems, onSaveMenuItem, onDeleteMenuItem)
                    1 -> AdminRoomManager(rooms, onSaveHotelRoom, onDeleteHotelRoom)
                    2 -> AdminConfigManager(hotelInfo, onUpdateHotelConfig)
                    3 -> AdminEnquiryManager(enquiries, onUpdateEnquiryStatus)
                }
            }
        }
    }
}

@Composable
fun AdminMenuManager(
    menuItems: List<MenuItemEntity>,
    onSaveMenuItem: (MenuItemEntity) -> Unit,
    onDeleteMenuItem: (MenuItemEntity) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<MenuItemEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(
                        title = "Food Menu Items (${menuItems.size})",
                        subtitle = "Add, edit prices, or update food availability"
                    )

                    Button(
                        onClick = {
                            editingItem = null
                            showDialog = true
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Dish")
                    }
                }
            }

            items(menuItems, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                VegNonVegDot(isVeg = item.isVeg)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Text("Category: ${item.category} • Price: ₹${item.price.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row {
                            IconButton(onClick = {
                                editingItem = item
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }

                            IconButton(onClick = { onDeleteMenuItem(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            MenuItemEditDialog(
                item = editingItem,
                onDismiss = { showDialog = false },
                onSave = { saved ->
                    onSaveMenuItem(saved)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun MenuItemEditDialog(
    item: MenuItemEntity?,
    onDismiss: () -> Unit,
    onSave: (MenuItemEntity) -> Unit
) {
    var name by remember { mutableStateOf(item?.name ?: "") }
    var description by remember { mutableStateOf(item?.description ?: "") }
    var priceText by remember { mutableStateOf(item?.price?.toInt()?.toString() ?: "150") }
    var category by remember { mutableStateOf(item?.category ?: "Indian Cuisine") }
    var isVeg by remember { mutableStateOf(item?.isVeg ?: true) }
    var isPopular by remember { mutableStateOf(item?.isPopular ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Add New Food Dish" else "Edit Food Dish") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price in ₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. Breakfast, Lunch, Indian Cuisine)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isVeg, onCheckedChange = { isVeg = it })
                    Text("Pure Veg")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPopular, onCheckedChange = { isPopular = it })
                    Text("Highlight as Popular")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val price = priceText.toDoubleOrNull() ?: 100.0
                val newItem = MenuItemEntity(
                    id = item?.id ?: 0L,
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    drawableResName = item?.drawableResName ?: "img_royal_thali",
                    isVeg = isVeg,
                    isPopular = isPopular
                )
                onSave(newItem)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AdminRoomManager(
    rooms: List<HotelRoomEntity>,
    onSaveHotelRoom: (HotelRoomEntity) -> Unit,
    onDeleteHotelRoom: (HotelRoomEntity) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingRoom by remember { mutableStateOf<HotelRoomEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(
                        title = "Hotel Rooms (${rooms.size})",
                        subtitle = "Edit room rates per night and description"
                    )

                    Button(
                        onClick = {
                            editingRoom = null
                            showDialog = true
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Room")
                    }
                }
            }

            items(rooms, key = { it.id }) { room ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(room.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Rate: ₹${room.pricePerNight.toInt()}/night • Max Guests: ${room.maxGuests}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row {
                            IconButton(onClick = {
                                editingRoom = room
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }

                            IconButton(onClick = { onDeleteHotelRoom(room) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            RoomEditDialog(
                room = editingRoom,
                onDismiss = { showDialog = false },
                onSave = { saved ->
                    onSaveHotelRoom(saved)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun RoomEditDialog(
    room: HotelRoomEntity?,
    onDismiss: () -> Unit,
    onSave: (HotelRoomEntity) -> Unit
) {
    var title by remember { mutableStateOf(room?.title ?: "") }
    var description by remember { mutableStateOf(room?.description ?: "") }
    var priceText by remember { mutableStateOf(room?.pricePerNight?.toInt()?.toString() ?: "1800") }
    var maxGuestsText by remember { mutableStateOf(room?.maxGuests?.toString() ?: "2") }
    var amenities by remember { mutableStateOf(room?.amenities ?: "AC, Wi-Fi, TV, Hot Water") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (room == null) "Add Hotel Room" else "Edit Room Details") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Room Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price per Night (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = maxGuestsText,
                    onValueChange = { maxGuestsText = it },
                    label = { Text("Max Guest Capacity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Room Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amenities,
                    onValueChange = { amenities = it },
                    label = { Text("Amenities (Comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newRoom = HotelRoomEntity(
                    id = room?.id ?: 0L,
                    title = title,
                    roomType = room?.roomType ?: "Deluxe",
                    description = description,
                    pricePerNight = priceText.toDoubleOrNull() ?: 1800.0,
                    maxGuests = maxGuestsText.toIntOrNull() ?: 2,
                    drawableResName = room?.drawableResName ?: "img_luxury_room",
                    amenities = amenities
                )
                onSave(newRoom)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AdminConfigManager(
    hotelInfo: HotelInfoState,
    onUpdateHotelConfig: (
        name: String,
        tagline: String,
        phone: String,
        whatsapp: String,
        email: String,
        address: String,
        openingHours: String,
        adminPin: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(hotelInfo.name) }
    var tagline by remember { mutableStateOf(hotelInfo.tagline) }
    var phone by remember { mutableStateOf(hotelInfo.phone) }
    var whatsapp by remember { mutableStateOf(hotelInfo.whatsapp) }
    var email by remember { mutableStateOf(hotelInfo.email) }
    var address by remember { mutableStateOf(hotelInfo.address) }
    var openingHours by remember { mutableStateOf(hotelInfo.openingHours) }
    var newPin by remember { mutableStateOf(hotelInfo.adminPin) }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionTitle(
            title = "Hotel & Contact Information",
            subtitle = "Update details that appear across the app"
        )

        if (savedMessage != null) {
            Text(
                text = savedMessage!!,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Hotel / Restaurant Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = tagline,
            onValueChange = { tagline = it },
            label = { Text("Hindi Tagline") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = whatsapp,
            onValueChange = { whatsapp = it },
            label = { Text("WhatsApp Number (with country code)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Full Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = openingHours,
            onValueChange = { openingHours = it },
            label = { Text("Opening Hours") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        SectionTitle(
            title = "Change Admin Security PIN / Password",
            subtitle = "Set a secret PIN code for accessing this Owner Portal"
        )

        OutlinedTextField(
            value = newPin,
            onValueChange = { newPin = it },
            label = { Text("New Owner PIN Code (e.g. 1234 or 9876)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                onUpdateHotelConfig(name, tagline, phone, whatsapp, email, address, openingHours, newPin)
                savedMessage = "Hotel details & Admin PIN updated successfully!"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Contact Details & Security PIN", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminEnquiryManager(
    enquiries: List<EnquiryBookingEntity>,
    onUpdateEnquiryStatus: (Long, String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle(
                title = "Customer Enquiries & Orders (${enquiries.size})",
                subtitle = "Manage received requests and update their status"
            )
        }

        if (enquiries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No customer enquiries received yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(enquiries, key = { it.id }) { enquiry ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${enquiry.id} • ${enquiry.customerName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            AssistChip(
                                onClick = { },
                                label = { Text(enquiry.status, fontSize = 11.sp) }
                            )
                        }

                        Text("Phone: ${enquiry.phone} • Type: ${enquiry.serviceType}", fontSize = 12.sp)
                        if (enquiry.selectedItemOrRoom.isNotBlank()) {
                            Text("Selected: ${enquiry.selectedItemOrRoom}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        }
                        if (enquiry.message.isNotBlank()) {
                            Text("Note: ${enquiry.message}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { onUpdateEnquiryStatus(enquiry.id, "CONFIRMED") }) {
                                Text("Mark Confirmed")
                            }
                            TextButton(onClick = { onUpdateEnquiryStatus(enquiry.id, "COMPLETED") }) {
                                Text("Mark Completed")
                            }
                        }
                    }
                }
            }
        }
    }
}
