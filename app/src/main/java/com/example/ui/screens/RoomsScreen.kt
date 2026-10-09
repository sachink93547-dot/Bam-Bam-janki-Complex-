package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HotelRoomEntity
import com.example.ui.components.RoomCard
import com.example.ui.components.SectionTitle
import com.example.ui.components.launchPhoneCall
import com.example.ui.viewmodel.EnquiryDialogTarget

@Composable
fun RoomsScreen(
    rooms: List<HotelRoomEntity>,
    phone: String,
    onOpenEnquiry: (EnquiryDialogTarget) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("rooms_screen")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hotel Accommodation",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Clean, safe, quiet and affordable room stay at Bam Bam Janki Complex",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        IconButton(onClick = { launchPhoneCall(context, phone) }) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call Desk",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionTitle(
                title = "Available Rooms & Suites",
                subtitle = "Select a room below to check availability or submit an enquiry"
            )
        }

        if (rooms.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(rooms, key = { it.id }) { room ->
                RoomCard(
                    room = room,
                    onBookClick = {
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
    }
}
