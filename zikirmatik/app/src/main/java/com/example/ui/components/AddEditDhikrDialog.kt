package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.db.DhikrEntity

@Composable
fun AddEditDhikrDialog(
    initialDhikr: DhikrEntity? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, arabic: String, trans: String, meaning: String, target: Int) -> Unit
) {
    var title by remember { mutableStateOf(initialDhikr?.title ?: "") }
    var arabicText by remember { mutableStateOf(initialDhikr?.arabicText ?: "") }
    var transliteration by remember { mutableStateOf(initialDhikr?.transliteration ?: "") }
    var meaning by remember { mutableStateOf(initialDhikr?.meaning ?: "") }
    var targetText by remember { mutableStateOf((initialDhikr?.targetCount ?: 33).toString()) }

    var titleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = if (initialDhikr == null) "Yeni Zikir Ekle" else "Zikri Düzenle",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text("Zikir Adı *") },
                    placeholder = { Text("Örn: Sübhanallahi ve Bihamdihi") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Zikir adı boş bırakılamaz") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dhikr_title_input")
                )

                OutlinedTextField(
                    value = arabicText,
                    onValueChange = { arabicText = it },
                    label = { Text("Arapça Metin (İsteğe Bağlı)") },
                    placeholder = { Text("سُبْحَانَ اللَّهِ...") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    label = { Text("Türkçe Okunuş") },
                    placeholder = { Text("Subhanallahi ve bihamdihi") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("Anlamı & Fazileti") },
                    placeholder = { Text("Allah'ı hamd ile tesbih ederim...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            targetText = it
                        }
                    },
                    label = { Text("Hedef Sayı (0 = Serbest)") },
                    placeholder = { Text("33, 99, 100, 1000...") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dhikr_target_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        val target = targetText.toIntOrNull() ?: 33
                        onSave(title, arabicText, transliteration, meaning, target)
                    }
                },
                modifier = Modifier.testTag("dhikr_save_button")
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
