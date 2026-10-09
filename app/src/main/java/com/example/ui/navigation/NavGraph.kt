package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BamBamBottomBar
import com.example.ui.components.BamBamTopBar
import com.example.ui.components.EnquiryDialog
import com.example.ui.components.launchPhoneCall
import com.example.ui.components.launchWhatsApp
import com.example.ui.screens.*
import com.example.ui.viewmodel.HotelViewModel

@Composable
fun MainAppNavigation(
    viewModel: HotelViewModel
) {
    var currentRoute by remember { mutableStateOf("home") }

    val hotelInfo by viewModel.hotelInfo.collectAsStateWithLifecycle()
    val popularItems by viewModel.popularMenuItems.collectAsStateWithLifecycle()
    val filteredMenuItems by viewModel.filteredMenuItems.collectAsStateWithLifecycle()
    val allMenuItems by viewModel.filteredMenuItems.collectAsStateWithLifecycle()
    val rooms by viewModel.hotelRooms.collectAsStateWithLifecycle()
    val enquiries by viewModel.enquiries.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val vegOnly by viewModel.vegOnlyFilter.collectAsStateWithLifecycle()

    val activeDialog by viewModel.activeEnquiryDialog.collectAsStateWithLifecycle()
    val submissionResult by viewModel.enquirySubmissionResult.collectAsStateWithLifecycle()

    val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()
    val adminError by viewModel.adminError.collectAsStateWithLifecycle()

    // Handle back button for sub-screens
    if (currentRoute != "home") {
        BackHandler {
            currentRoute = "home"
        }
    }

    Scaffold(
        topBar = {
            BamBamTopBar(
                title = hotelInfo.name,
                tagline = hotelInfo.tagline,
                onAdminClick = { currentRoute = "admin" },
                onCallClick = { launchPhoneCall(viewModel.getApplication(), hotelInfo.phone) },
                onWhatsAppClick = { launchWhatsApp(viewModel.getApplication(), hotelInfo.whatsapp) },
                currentRoute = currentRoute,
                onNavigate = { currentRoute = it }
            )
        },
        bottomBar = {
            BamBamBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route -> currentRoute = route }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Crossfade(targetState = currentRoute, label = "screen_transition") { route ->
                when (route) {
                    "home" -> HomeScreen(
                        hotelInfo = hotelInfo,
                        popularItems = popularItems,
                        rooms = rooms,
                        onNavigate = { currentRoute = it },
                        onOpenEnquiry = { target -> viewModel.openEnquiryDialog(target) }
                    )
                    "food" -> FoodItemsScreen(
                        items = filteredMenuItems,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.searchQuery.value = it },
                        selectedCategory = selectedCategory,
                        onCategorySelect = { viewModel.selectedCategory.value = it },
                        vegOnly = vegOnly,
                        onVegOnlyToggle = { viewModel.vegOnlyFilter.value = it },
                        onOpenEnquiry = { target -> viewModel.openEnquiryDialog(target) }
                    )
                    "menu" -> MenuScreen(
                        menuItems = allMenuItems,
                        whatsappNumber = hotelInfo.whatsapp,
                        onOpenEnquiry = { target -> viewModel.openEnquiryDialog(target) }
                    )
                    "rooms" -> RoomsScreen(
                        rooms = rooms,
                        phone = hotelInfo.phone,
                        onOpenEnquiry = { target -> viewModel.openEnquiryDialog(target) }
                    )
                    "facilities" -> FacilitiesScreen()
                    "about" -> AboutScreen(hotelInfo = hotelInfo, onNavigate = { currentRoute = it })
                    "contact" -> ContactScreen(
                        hotelInfo = hotelInfo,
                        onSubmitEnquiry = { name, phone, email, serviceType, date, guestCount, itemOrRoom, message ->
                            viewModel.submitEnquiry(name, phone, email, serviceType, date, guestCount, itemOrRoom, message)
                        },
                        submissionResult = submissionResult
                    )
                    "admin" -> AdminScreen(
                        isAdminMode = isAdminMode,
                        adminError = adminError,
                        hotelInfo = hotelInfo,
                        menuItems = allMenuItems,
                        rooms = rooms,
                        enquiries = enquiries,
                        onLoginAdmin = { pin -> viewModel.loginAdmin(pin) },
                        onLogoutAdmin = { viewModel.logoutAdmin() },
                        onSaveMenuItem = { item -> viewModel.saveMenuItem(item) },
                        onDeleteMenuItem = { item -> viewModel.deleteMenuItem(item) },
                        onSaveHotelRoom = { room -> viewModel.saveHotelRoom(room) },
                        onDeleteHotelRoom = { room -> viewModel.deleteHotelRoom(room) },
                        onUpdateHotelConfig = { name, tagline, phone, whatsapp, email, address, openingHours, adminPin ->
                            viewModel.updateHotelConfig(name, tagline, phone, whatsapp, email, address, openingHours, adminPin)
                        },
                        onUpdateEnquiryStatus = { id, status -> viewModel.updateEnquiryStatus(id, status) }
                    )
                }
            }
        }

        // Enquiry Modal Dialog
        activeDialog?.let { target ->
            EnquiryDialog(
                target = target,
                onDismiss = { viewModel.closeEnquiryDialog() },
                onSubmit = { name, phone, email, serviceType, date, guestCount, itemOrRoom, message ->
                    viewModel.submitEnquiry(
                        name, phone, email, serviceType, date, guestCount, itemOrRoom, message
                    )
                },
                submissionResult = submissionResult,
                whatsappNumber = hotelInfo.whatsapp
            )
        }
    }
}
