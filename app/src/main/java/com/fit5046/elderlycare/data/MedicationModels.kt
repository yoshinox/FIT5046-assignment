package com.fit5046.elderlycare.data

data class Medication(
    val id: Int,
    val name: String,
    val category: String,
    val dosage: String,
    val reminderTime: String,
    val startDate: String,
    val endDate: String,
    val notes: String = ""
)

object SampleMedicationData {
    val medications = mutableListOf(
        Medication(
            id = 1,
            name = "Aspirin",
            category = "Blood Pressure",
            dosage = "100mg",
            reminderTime = "08:00 AM",
            startDate = "01 Jan 2026",
            endDate = "31 Dec 2026",
            notes = "Take with food"
        ),
        Medication(
            id = 2,
            name = "Metformin",
            category = "Diabetes",
            dosage = "500mg",
            reminderTime = "12:00 PM",
            startDate = "01 Feb 2026",
            endDate = "31 Jul 2026",
            notes = ""
        ),
        Medication(
            id = 3,
            name = "Vitamin D",
            category = "Vitamins",
            dosage = "1000 IU",
            reminderTime = "09:00 AM",
            startDate = "01 Jan 2026",
            endDate = "30 Jun 2026",
            notes = "Take with meal"
        ),
        Medication(
            id = 4,
            name = "Paracetamol",
            category = "Pain Relief",
            dosage = "500mg",
            reminderTime = "06:00 PM",
            startDate = "10 Apr 2026",
            endDate = "20 Apr 2026",
            notes = "As needed for pain"
        )
    )
}