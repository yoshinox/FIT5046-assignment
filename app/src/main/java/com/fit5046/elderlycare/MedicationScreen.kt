package com.fit5046.elderlycare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit5046.elderlycare.data.Medication
import com.fit5046.elderlycare.data.SampleMedicationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─── F4: Medication Management (CRUD) ────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen() {
    val medications = remember {
        mutableStateListOf<Medication>().apply { addAll(SampleMedicationData.medications) }
    }
    var showDialog by remember { mutableStateOf(false) }
    var editingMedication by remember { mutableStateOf<Medication?>(null) }
    var nextId by remember { mutableStateOf(SampleMedicationData.medications.size + 1) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var medicationToDelete by remember { mutableStateOf<Medication?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7FF))
            .padding(16.dp)
    ) {
        // Header row with total badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Medications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFD1C4E9)
            ) {
                Text(
                    text = "${medications.size} total",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF4527A0),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Add Medication button
        Button(
            onClick = { editingMedication = null; showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2))
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Add Medication", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(16.dp))

        if (medications.isEmpty()) {
            // Empty state
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.LocalPharmacy,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFB39DDB)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No medications added yet.\nTap 'Add Medication' to get started.",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF888888)
                    )
                }
            }
        } else {
            // [Required] LazyColumn — scrollable medication list
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(medications, key = { it.id }) { med ->
                    MedicationCard(
                        medication = med,
                        onEdit = { editingMedication = it; showDialog = true },
                        onDelete = { medicationToDelete = it; showDeleteConfirm = true }
                    )
                }
            }
        }
    }

    // Add / Edit dialog
    if (showDialog) {
        AddEditMedicationDialog(
            medication = editingMedication,
            onDismiss = { showDialog = false },
            onSave = { med ->
                if (editingMedication == null) {
                    medications.add(0, med.copy(id = nextId++))
                } else {
                    val idx = medications.indexOfFirst { it.id == med.id }
                    if (idx >= 0) medications[idx] = med
                }
                showDialog = false
            }
        )
    }

    // Delete confirmation dialog
    if (showDeleteConfirm && medicationToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Medication") },
            text = { Text("Remove \"${medicationToDelete!!.name}\" from your medication list?") },
            confirmButton = {
                TextButton(onClick = {
                    medications.removeIf { it.id == medicationToDelete!!.id }
                    showDeleteConfirm = false
                    medicationToDelete = null
                }) { Text("Delete", color = Color(0xFFE53935)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

// Returns a colour per medication category for the card accent strip
private fun categoryAccentColor(category: String): Color = when (category) {
    "Blood Pressure" -> Color(0xFFE53935)
    "Diabetes"       -> Color(0xFF1E88E5)
    "Vitamins"       -> Color(0xFF43A047)
    "Pain Relief"    -> Color(0xFFFB8C00)
    "Heart"          -> Color(0xFFE91E63)
    else             -> Color(0xFF757575)
}

@Composable
private fun MedicationCard(
    medication: Medication,
    onEdit: (Medication) -> Unit,
    onDelete: (Medication) -> Unit
) {
    val accent = categoryAccentColor(medication.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // Coloured left-border accent using intrinsic height
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {
                // Title row: drug name + category badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = accent.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = medication.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = accent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Dosage row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalPharmacy,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color(0xFF888888)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Dosage: ${medication.dosage}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444)
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Reminder time row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Alarm,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color(0xFF888888)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Reminder: ${medication.reminderTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444)
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Date range row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color(0xFF888888)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "${medication.startDate} – ${medication.endDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }

                if (medication.notes.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Note: ${medication.notes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Edit / Delete action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { onDelete(medication) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Delete", fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onEdit(medication) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Edit", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ─── Add / Edit Medication Dialog ─────────────────────────────────────────────
// Demonstrates: [Required] ExposedDropdownMenuBox (category), DatePicker (start/end),
//               [Extra] TimePicker (reminder); [G3] floating labels; [G4] validation.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditMedicationDialog(
    medication: Medication?,
    onDismiss: () -> Unit,
    onSave: (Medication) -> Unit
) {
    val isEdit = medication != null
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    var name by remember { mutableStateOf(medication?.name ?: "") }
    var category by remember { mutableStateOf(medication?.category ?: "Blood Pressure") }
    var dosage by remember { mutableStateOf(medication?.dosage ?: "") }
    var startDate by remember { mutableStateOf(medication?.startDate ?: "") }
    var endDate by remember { mutableStateOf(medication?.endDate ?: "") }
    var reminderTime by remember { mutableStateOf(medication?.reminderTime ?: "") }
    var notes by remember { mutableStateOf(medication?.notes ?: "") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var dosageError by remember { mutableStateOf(false) }

    val categories = listOf("Blood Pressure", "Diabetes", "Vitamins", "Pain Relief", "Heart", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Medication" else "Add Medication") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // [G3] Floating label + descriptive placeholder
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("Medicine Name *") },
                    placeholder = { Text("e.g. Aspirin 100mg") },
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("Medicine name is required", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // [Required] Expanded Dropdown Menu — medication category [G5]
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = { category = opt; categoryExpanded = false }
                            )
                        }
                    }
                }

                // [G3] Dosage with placeholder; [G4] error shown on save attempt
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it; dosageError = false },
                    label = { Text("Dosage *") },
                    placeholder = { Text("e.g. 100mg") },
                    isError = dosageError,
                    supportingText = if (dosageError) {
                        { Text("Please enter dosage (e.g. 100mg)", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // [Required] DatePicker — start date [G5]
                OutlinedTextField(
                    value = startDate.ifBlank { "" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Start Date") },
                    placeholder = { Text("Tap to select") },
                    trailingIcon = {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select start date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // [Required] DatePicker — end date [G5]
                OutlinedTextField(
                    value = endDate.ifBlank { "" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("End Date") },
                    placeholder = { Text("Tap to select") },
                    trailingIcon = {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select end date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // [Extra component] TimePicker — daily reminder time [G5]
                OutlinedTextField(
                    value = reminderTime.ifBlank { "" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Daily Reminder Time") },
                    placeholder = { Text("Tap to select") },
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Default.Alarm, contentDescription = "Select reminder time")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("e.g. Take with food") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // [G4] Validate required fields on save
                nameError = name.isBlank()
                dosageError = dosage.isBlank()
                if (!nameError && !dosageError) {
                    onSave(
                        Medication(
                            id = medication?.id ?: 0,
                            name = name.trim(),
                            category = category,
                            dosage = dosage.trim(),
                            reminderTime = reminderTime.ifBlank { "Not set" },
                            startDate = startDate.ifBlank { "Not set" },
                            endDate = endDate.ifBlank { "Not set" },
                            notes = notes.trim()
                        )
                    )
                }
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    // Start Date picker overlay
    if (showStartDatePicker) {
        val dpState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { startDate = dateFormatter.format(Date(it)) }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = dpState) }
    }

    // End Date picker overlay
    if (showEndDatePicker) {
        val dpState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { endDate = dateFormatter.format(Date(it)) }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = dpState) }
    }

    // [Extra component] TimePicker overlay
    if (showTimePicker) {
        val timeState = rememberTimePickerState(initialHour = 8, initialMinute = 0, is24Hour = false)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Reminder Time") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timeState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val h = timeState.hour
                    val m = timeState.minute
                    val amPm = if (h < 12) "AM" else "PM"
                    val h12 = if (h == 0) 12 else if (h > 12) h - 12 else h
                    reminderTime = "%02d:%02d %s".format(h12, m, amPm)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            }
        )
    }
}