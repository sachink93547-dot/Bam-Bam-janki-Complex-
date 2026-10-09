package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionTitle
import com.example.ui.theme.WarmGoldSecondary

data class FacilityItem(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun FacilitiesScreen() {
    val facilitiesList = listOf(
        FacilityItem(
            title = "Clean & Comfortable Environment",
            description = "Spotless dining hall, daily sanitized AC rooms, and pleasant ambient lighting.",
            icon = Icons.Default.CleaningServices
        ),
        FacilityItem(
            title = "100% Quality Food & Purity",
            description = "Freshly sourced ingredients, pure Ghee preparations, and strict hygiene standards.",
            icon = Icons.Default.Restaurant
        ),
        FacilityItem(
            title = "Family Friendly Dining",
            description = "Spacious seating arrangements designed specifically for family gatherings and celebrations.",
            icon = Icons.Default.FamilyRestroom
        ),
        FacilityItem(
            title = "Fast & Polite Service",
            description = "Courteous waitstaff and speedy order execution so you never have to wait long.",
            icon = Icons.Default.Speed
        ),
        FacilityItem(
            title = "Spacious Safe Parking",
            description = "Dedicated CCTV monitored parking facility for cars, two-wheelers, and tourist buses.",
            icon = Icons.Default.LocalParking
        ),
        FacilityItem(
            title = "Online Order & Enquiry",
            description = "Easy in-app booking, table reservation, and direct WhatsApp order confirmation.",
            icon = Icons.Default.PhonelinkRing
        ),
        FacilityItem(
            title = "24/7 Hotel Accommodation",
            description = "Round-the-clock desk assistance, safe room lodging, hot water, and room service.",
            icon = Icons.Default.Hotel
        ),
        FacilityItem(
            title = "Free High-Speed Wi-Fi",
            description = "Seamless high-speed internet connectivity for stay guests and dining visitors.",
            icon = Icons.Default.Wifi
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("facilities_screen")
    ) {
        SectionTitle(
            title = "Hotel & Restaurant Facilities",
            subtitle = "Everything you need for a comfortable stay and memorable dining experience"
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(facilitiesList) { facility ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(WarmGoldSecondary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = facility.icon,
                                contentDescription = facility.title,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            text = facility.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                        )

                        Text(
                            text = facility.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
