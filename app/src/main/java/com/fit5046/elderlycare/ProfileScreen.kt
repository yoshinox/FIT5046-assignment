package com.fit5046.elderlycare

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─── F3: User Profile Form ────────────────────────────────────────────────────
// Components used:
//   [Required]  ExposedDropdownMenuBox  — chronic condition & emergency relationship
//   [Required]  DatePicker              — date of birth
//   [Extra]     RadioButton             — gender & activity level
//   [Extra]     Switch                  — fall detection, reminders, night mode
// Design guidelines applied:
//   [G3] Every field has a visible floating label + descriptive placeholder
//   [G4] Age / height / weight validated on Save; errors shown below the field
//   [G5] Dropdowns and DatePicker replace free-text wherever applicable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // ── State: Personal information ──────────────────────────────────
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var weightKg by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var activityLevel by remember { mutableStateOf("Moderate") }
    var chronicCondition by remember { mutableStateOf("None") }

    // ── State: Emergency contact ─────────────────────────────────────
    var emergencyName by remember { mutableStateOf("") }
    var emergencyPhone by remember { mutableStateOf("") }
    var emergencyRelationship by remember { mutableStateOf("Child") }

    // ── State: App preferences ───────────────────────────────────────
    var fallDetection by remember { mutableStateOf(true) }
    var medicationReminders by remember { mutableStateOf(true) }
    var nightMode by remember { mutableStateOf(false) }

    // ── Dropdown / picker visibility ─────────────────────────────────
    var conditionExpanded by remember { mutableStateOf(false) }
    var relationshipExpanded by remember { mutableStateOf(false) }
    var showDOBPicker by remember { mutableStateOf(false) }

    // ── Validation error flags [G4] ──────────────────────────────────
    var nameError by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }

    var showSavedBanner by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7FF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "My Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(Modifier.height(4.dp))

        // ── Section 1: Personal Information ─────────────────────────
        ProfileSectionHeader("Personal Information")

        // [G3] Visible floating label + placeholder on every field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = false },
            label = { Text("Full Name *") },
            placeholder = { Text("Enter your full name") },
            isError = nameError,
            supportingText = if (nameError) {
                { Text("Name is required", color = MaterialTheme.colorScheme.error) }
            } else null,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Age — [G4] immediate error if out of range
            OutlinedTextField(
                value = age,
                onValueChange = { age = it.filter { c -> c.isDigit() }.take(3); ageError = false },
                label = { Text("Age *") },
                placeholder = { Text("e.g. 70") },
                isError = ageError,
                supportingText = if (ageError) {
                    { Text("Enter valid age (1–120)", color = MaterialTheme.colorScheme.error) }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            // Height — [G4] validated on save
            OutlinedTextField(
                value = heightCm,
                onValueChange = { heightCm = it.filter { c -> c.isDigit() }.take(3); heightError = false },
                label = { Text("Height (cm)") },
                placeholder = { Text("e.g. 165") },
                isError = heightError,
                supportingText = if (heightError) {
                    { Text("Enter valid height", color = MaterialTheme.colorScheme.error) }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        // Weight — [G4] validated on save
        OutlinedTextField(
            value = weightKg,
            onValueChange = { weightKg = it.filter { c -> c.isDigit() }.take(3); weightError = false },
            label = { Text("Weight (kg)") },
            placeholder = { Text("e.g. 65") },
            isError = weightError,
            supportingText = if (weightError) {
                { Text("Enter valid weight", color = MaterialTheme.colorScheme.error) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // [G5] DatePicker replaces free-text for date of birth
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date of Birth") },
            placeholder = { Text("Tap to select") },
            trailingIcon = {
                IconButton(onClick = { showDOBPicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date of birth")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        // ── Section 2: Health Details ────────────────────────────────
        ProfileSectionHeader("Health Details")

        // [Extra component] RadioButton — gender selection
        Text(
            "Gender",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF555555),
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Male", "Female", "Other").forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { gender = option }
                ) {
                    RadioButton(
                        selected = gender == option,
                        onClick = { gender = option },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF7E57C2))
                    )
                    Text(option, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // [Extra component] RadioButton — activity level
        Text(
            "Activity Level",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF555555),
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Low", "Moderate", "High").forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activityLevel = option }
                ) {
                    RadioButton(
                        selected = activityLevel == option,
                        onClick = { activityLevel = option },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF7E57C2))
                    )
                    Text(option, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // [Required] Expanded Dropdown Menu — chronic condition [G5]
        ExposedDropdownMenuBox(
            expanded = conditionExpanded,
            onExpandedChange = { conditionExpanded = !conditionExpanded }
        ) {
            OutlinedTextField(
                value = chronicCondition,
                onValueChange = {},
                readOnly = true,
                label = { Text("Chronic Condition") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = conditionExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = conditionExpanded,
                onDismissRequest = { conditionExpanded = false }
            ) {
                listOf("None", "Hypertension", "Diabetes", "Heart Disease", "Arthritis", "Other")
                    .forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = { chronicCondition = opt; conditionExpanded = false }
                        )
                    }
            }
        }

        Spacer(Modifier.height(4.dp))

        // ── Section 3: Emergency Contact ─────────────────────────────
        ProfileSectionHeader("Emergency Contact")

        OutlinedTextField(
            value = emergencyName,
            onValueChange = { emergencyName = it },
            label = { Text("Contact Name") },
            placeholder = { Text("e.g. Jane Smith") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = emergencyPhone,
            onValueChange = {
                emergencyPhone = it.filter { c -> c.isDigit() || c == '+' || c == ' ' }
            },
            label = { Text("Phone Number") },
            placeholder = { Text("e.g. +61 4XX XXX XXX") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // [G5] Dropdown for relationship — avoids free-text errors
        ExposedDropdownMenuBox(
            expanded = relationshipExpanded,
            onExpandedChange = { relationshipExpanded = !relationshipExpanded }
        ) {
            OutlinedTextField(
                value = emergencyRelationship,
                onValueChange = {},
                readOnly = true,
                label = { Text("Relationship") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = relationshipExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = relationshipExpanded,
                onDismissRequest = { relationshipExpanded = false }
            ) {
                listOf("Child", "Spouse", "Sibling", "Friend", "Carer", "Other").forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = { emergencyRelationship = opt; relationshipExpanded = false }
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // ── Section 4: App Preferences ────────────────────────────────
        ProfileSectionHeader("App Preferences")

        // [Extra component] Switch — fall detection
        PreferenceToggleRow(
            title = "Fall Detection",
            subtitle = "Automatically detect falls using sensors",
            checked = fallDetection,
            onCheckedChange = { fallDetection = it }
        )
        HorizontalDivider(color = Color(0xFFEEEEEE))

        // [Extra component] Switch — medication reminders
        PreferenceToggleRow(
            title = "Medication Reminders",
            subtitle = "Receive daily reminders to take medications",
            checked = medicationReminders,
            onCheckedChange = { medicationReminders = it }
        )
        HorizontalDivider(color = Color(0xFFEEEEEE))

        // [Extra component] Switch — night mode
        PreferenceToggleRow(
            title = "Night Mode",
            subtitle = "High-contrast display for nighttime use",
            checked = nightMode,
            onCheckedChange = { nightMode = it }
        )

        Spacer(Modifier.height(16.dp))

        // Save button — triggers [G4] validation
        Button(
            onClick = {
                nameError = name.isBlank()
                val ageNum = age.toIntOrNull()
                ageError = ageNum == null || ageNum <= 0 || ageNum > 120
                val hNum = heightCm.toIntOrNull()
                heightError = heightCm.isNotBlank() && (hNum == null || hNum <= 0)
                val wNum = weightKg.toIntOrNull()
                weightError = weightKg.isNotBlank() && (wNum == null || wNum <= 0)

                if (!nameError && !ageError && !heightError && !weightError) {
                    showSavedBanner = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2))
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Save Profile", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        // Success banner
        if (showSavedBanner) {
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF43A047))
                    Spacer(Modifier.width(8.dp))
                    Text("Profile saved successfully!", color = Color(0xFF2E7D32))
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }

    // [Required] DatePicker for date of birth
    if (showDOBPicker) {
        val dpState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDOBPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { dateOfBirth = dateFormatter.format(Date(it)) }
                    showDOBPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDOBPicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = dpState) }
    }
}

// ─── Reusable helper composables ─────────────────────────────────────────────

@Composable
private fun ProfileSectionHeader(title: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7E57C2)
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = Color(0xFFD1C4E9)
        )
    }
}

@Composable
private fun PreferenceToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF7E57C2),
                checkedTrackColor = Color(0xFFD1C4E9)
            )
        )
    }
}