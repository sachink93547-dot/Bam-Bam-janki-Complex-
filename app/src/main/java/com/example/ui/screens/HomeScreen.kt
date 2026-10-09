package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HotelRoomEntity
import com.example.data.model.MenuItemEntity
import com.example.ui.components.FoodCard
import com.example.ui.components.RoomCard
import com.example.ui.components.SectionTitle
import com.example.ui.components.WebFooter
import com.example.ui.components.launchPhoneCall
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.DeepMaroonPrimary
import com.example.ui.theme.PureVegGreen
import com.example.ui.theme.WarmGoldSecondary
import com.example.ui.viewmodel.EnquiryDialogTarget
import com.example.ui.viewmodel.HotelInfoState

@Composable
fun HomeScreen(
    hotelInfo: HotelInfoState,
    popularItems: List<MenuItemEntity>,
    rooms: List<HotelRoomEntity>,
    onNavigate: (String) -> Unit,
    onOpenEnquiry: (EnquiryDialogTarget) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("home_screen")
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Bam Bam Janki Complex Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark Maroon Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.7f),
                                DeepMaroonPrimary
                            )
                        )
                    )
            )

            // Hero Text & Action Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Surface(
                    color = WarmGoldSecondary,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "HOTEL & RESTAURANT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = hotelInfo.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "“${hotelInfo.tagline}”",
                    style = MaterialTheme.typography.titleMedium,
                    color = WarmGoldSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onNavigate("menu") },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmGoldSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("hero_view_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Menu", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onNavigate("contact") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(WarmGoldSecondary, Color.White))),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("hero_contact_us_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneCallback,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Contact Us", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Category Action Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickActionButton(
                    icon = Icons.Default.Fastfood,
                    label = "Order Food",
                    onClick = { onNavigate("food") }
                )
                QuickActionButton(
                    icon = Icons.Default.TableBar,
                    label = "Book Table",
                    onClick = {
                        onOpenEnquiry(
                            EnquiryDialogTarget(
                                title = "Book a Table",
                                serviceType = "TABLE_BOOKING"
                            )
                        )
                    }
                )
                QuickActionButton(
                    icon = Icons.Default.Bed,
                    label = "Hotel Room",
                    onClick = { onNavigate("rooms") }
                )
                QuickActionButton(
                    icon = Icons.Default.Chat,
                    label = "WhatsApp",
                    color = Color(0xFF25D366),
                    onClick = { launchWhatsApp(context, hotelInfo.whatsapp) }
                )
            }
        }

        // About Introduction Card
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            SectionTitle(
                title = "Welcome to Bam Bam Janki Complex",
                subtitle = "Experience true Indian taste, warmth & luxury hospitality"
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hotel_exterior),
                        contentDescription = "Hotel Exterior",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "A Name You Can Trust for Quality & Hospitality",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Bam Bam Janki Complex is renowned for pure hygienic vegetarian & North Indian delicacies, comfortable air-conditioned family rooms, and prompt service.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Popular Dishes Section
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(
                    title = "Popular Food Items",
                    subtitle = "Handpicked chef specialities for you"
                )

                TextButton(onClick = { onNavigate("food") }) {
                    Text("View All", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            if (popularItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(popularItems) { item ->
                        FoodCard(
                            item = item,
                            onOrderClick = {
                                onOpenEnquiry(
                                    EnquiryDialogTarget(
                                        title = "Order ${item.name}",
                                        serviceType = "FOOD_ORDER",
                                        prefilledItemName = item.name,
                                        price = item.price
                                    )
                                )
                            },
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }

        // Hotel Accommodation Preview Section
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            SectionTitle(
                title = "Clean & Comfortable Rooms",
                subtitle = "Stay in peaceful luxury at affordable rates"
            )

            if (rooms.isNotEmpty()) {
                RoomCard(
                    room = rooms.first(),
                    onBookClick = { room ->
                        onOpenEnquiry(
                            EnquiryDialogTarget(
                                title = "Book ${room.title}",
                                serviceType = "ROOM_BOOKING",
                                prefilledItemName = room.title,
                                price = room.pricePerNight
                            )
                        )
                    }
                )
            }
        }

        // Why Choose Us / Badges Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Why Choose Bam Bam Janki Complex?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeatureBadge(icon = Icons.Default.Verified, title = "100% Hygienic")
                    FeatureBadge(icon = Icons.Default.SetMeal, title = "Pure Taste")
                    FeatureBadge(icon = Icons.Default.DirectionsCar, title = "Safe Parking")
                    FeatureBadge(icon = Icons.Default.HeadsetMic, title = "24/7 Desk")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        WebFooter(
            onNavigate = onNavigate,
            phone = hotelInfo.phone,
            email = hotelInfo.email,
            address = hotelInfo.address,
            openingHours = hotelInfo.openingHours
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color)
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FeatureBadge(icon: ImageVector, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
