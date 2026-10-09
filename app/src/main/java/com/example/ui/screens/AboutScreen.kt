package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SectionTitle
import com.example.ui.components.WebFooter
import com.example.ui.theme.WarmGoldSecondary
import com.example.ui.viewmodel.HotelInfoState

@Composable
fun AboutScreen(
    hotelInfo: HotelInfoState,
    onNavigate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("about_screen")
    ) {
        SectionTitle(
            title = "About Bam Bam Janki Complex",
            subtitle = "Serving authentic taste and warm hospitality"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hotel_exterior),
                        contentDescription = "Hotel Exterior",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "स्वाद, सेवा और आतिथ्य का भरोसेमंद नाम",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Bam Bam Janki Complex has been built on the foundation of genuine Indian hospitality, uncompromising food purity, and absolute customer satisfaction. Whether you are visiting us for a hearty family meal, a quick evening snack, or an overnight stay in our comfortable hotel rooms, our team is dedicated to giving you a warm, memorable experience.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Core Pillars
        SectionTitle(title = "Our Core Standards")

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PillarCard(
                icon = Icons.Default.Restaurant,
                title = "Authentic Taste & Hygiene",
                description = "Our experienced chefs prepare every dish with pure Ghee, rich hand-ground spices, and immaculate cleanliness."
            )
            PillarCard(
                icon = Icons.Default.Favorite,
                title = "Warm Hospitality",
                description = "We treat every guest like family, ensuring polite service, custom preferences, and complete comfort."
            )
            PillarCard(
                icon = Icons.Default.CheckCircle,
                title = "Affordable Quality",
                description = "Premium restaurant quality and luxury hotel lodging offered at fair, customer-friendly prices."
            )
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
fun PillarCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
