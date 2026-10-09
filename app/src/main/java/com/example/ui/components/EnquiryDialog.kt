package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.WarmGoldSecondary
import com.example.ui.viewmodel.EnquiryDialogTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnquiryDialog(
    target: EnquiryDialogTarget,
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        phone: String,
        email: String,
        serviceType: String,
        date: String,
        guestCount: Int,
        itemOrRoom: String,
        message: String
    ) -> Unit,
    submissionResult: String?,
    whatsappNumber: String
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf(target.serviceType) }
    var date by remember { mutableStateOf("Today") }
    var guestCount by remember { mutableIntStateOf(2) }
    var itemOrRoom by remember { mutableStateOf(target.prefilledItemName) }
    var message by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("enquiry_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = target.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                if (submissionResult != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = submissionResult,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = {
                                        val waMsg = "Hello Bam Bam Janki Complex! My name is $name, phone: $phone. I submitted an enquiry for $itemOrRoom ($serviceType) on $date."
                                        launchWhatsApp(context, whatsappNumber, waMsg)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open WhatsApp")
                                }

                                OutlinedButton(onClick = onDismiss) {
                                    Text("Close")
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Fill in your details below. Our desk team at Bam Bam Janki Complex will call you immediately to confirm.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (validationError != null) {
                        Text(
                            text = validationError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Service Type selector
                    Text(
                        text = "Type of Enquiry",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "FOOD_ORDER" to "Food Order",
                            "TABLE_BOOKING" to "Table",
                            "ROOM_BOOKING" to "Room Stay"
                        ).forEach { (typeKey, label) ->
                            FilterChip(
                                selected = serviceType == typeKey,
                                onClick = { serviceType = typeKey },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }

                    if (itemOrRoom.isNotBlank()) {
                        OutlinedTextField(
                            value = itemOrRoom,
                            onValueChange = { itemOrRoom = it },
                            label = { Text("Item / Room Selected") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            readOnly = false
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name *") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("enquiry_input_name")
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Number *") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("enquiry_input_phone")
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date/Time") },
                            leadingIcon = { Icon(Icons.Default.Event, contentDescription = null) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 10.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp)
                        ) {
                            Text("Guests: ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { if (guestCount > 1) guestCount-- }) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Decrease")
                            }
                            Text("$guestCount", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { if (guestCount < 20) guestCount++ }) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "Increase")
                            }
                        }
                    }

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Special Requests / Instructions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(bottom = 16.dp),
                        maxLines = 3
                    )

                    Button(
                        onClick = {
                            if (name.isBlank() || phone.isBlank()) {
                                validationError = "Please enter your Name and Mobile Number."
                            } else {
                                validationError = null
                                onSubmit(
                                    name,
                                    phone,
                                    email,
                                    serviceType,
                                    date,
                                    guestCount,
                                    itemOrRoom,
                                    message
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_enquiry_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Enquiry / Booking", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
