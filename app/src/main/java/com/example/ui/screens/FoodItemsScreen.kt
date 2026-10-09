package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MenuItemEntity
import com.example.ui.components.FoodCard
import com.example.ui.components.SectionTitle
import com.example.ui.components.VegNonVegDot
import com.example.ui.theme.PureVegGreen
import com.example.ui.theme.WarmGoldSecondary
import com.example.ui.viewmodel.EnquiryDialogTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemsScreen(
    items: List<MenuItemEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    vegOnly: Boolean,
    onVegOnlyToggle: (Boolean) -> Unit,
    onOpenEnquiry: (EnquiryDialogTarget) -> Unit
) {
    val categories = listOf(
        "All",
        "Breakfast",
        "Lunch",
        "Dinner",
        "Snacks",
        "Fast Food",
        "Indian Cuisine",
        "Beverages"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("food_items_screen")
    ) {
        // Search & Filter Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SectionTitle(
                    title = "Available Food Items",
                    subtitle = "Fresh, delicious & hygienic dishes prepared on order"
                )

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search dishes (e.g. Paneer, Dosa, Thali)...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .testTag("food_search_input")
                )

                // Category Chips Scroll Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelect(cat) },
                            label = {
                                Text(
                                    text = cat,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("category_chip_$cat")
                        )
                    }
                }

                // Pure Veg Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VegNonVegDot(isVeg = true)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Show Pure Veg Only",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PureVegGreen
                        )
                    }

                    Switch(
                        checked = vegOnly,
                        onCheckedChange = onVegOnlyToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PureVegGreen,
                            checkedTrackColor = PureVegGreen.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        // Food Items List
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No dishes found matching your search.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = {
                        onSearchQueryChange("")
                        onCategorySelect("All")
                        onVegOnlyToggle(false)
                    }) {
                        Text("Reset Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items, key = { it.id }) { item ->
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
                        }
                    )
                }
            }
        }
    }
}
