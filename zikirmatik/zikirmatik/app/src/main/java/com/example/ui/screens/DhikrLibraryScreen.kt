package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.DhikrEntity
import com.example.data.model.DhikrCategory
import com.example.ui.components.AddEditDhikrDialog
import com.example.ui.components.ConfirmDialog
import com.example.viewmodel.ZikirmatikViewModel

@Composable
fun DhikrLibraryScreen(
    viewModel: ZikirmatikViewModel,
    contentPadding: PaddingValues
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val allDhikrs by viewModel.allDhikrs.collectAsStateWithLifecycle()
    val activeDhikr by viewModel.activeDhikr.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var dhikrToEdit by remember { mutableStateOf<DhikrEntity?>(null) }
    var dhikrToDelete by remember { mutableStateOf<DhikrEntity?>(null) }

    val customDhikrsCount = remember(allDhikrs) { allDhikrs.count { it.isCustom } }

    val filteredList = remember(allDhikrs, selectedCategory, searchQuery) {
        allDhikrs.filter { dhikr ->
            val matchesCategory = when (selectedCategory) {
                DhikrCategory.ALL -> true
                DhikrCategory.FAVORITES -> dhikr.isFavorite
                DhikrCategory.CUSTOM -> dhikr.isCustom
                else -> dhikr.category == selectedCategory.id
            }

            val query = searchQuery.trim().lowercase()
            val matchesSearch = query.isEmpty() ||
                    dhikr.title.lowercase().contains(query) ||
                    dhikr.transliteration.lowercase().contains(query) ||
                    dhikr.meaning.lowercase().contains(query) ||
                    dhikr.arabicText.contains(query)

            matchesCategory && matchesSearch
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Zikir veya dua ara...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ara"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Temizle")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dhikr_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(DhikrCategory.entries.toList()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategory(category) },
                        label = { Text(category.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("category_chip_${category.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dhikr List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📿",
                            fontSize = 44.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aradığınız kriterde zikir bulunamadı",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Yeni bir zikir eklemek için + düğmesine basabilirsiniz.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredList, key = { it.id }) { dhikr ->
                        val isActive = activeDhikr?.id == dhikr.id
                        DhikrItemCard(
                            dhikr = dhikr,
                            isActive = isActive,
                            onSelect = { viewModel.selectActiveDhikr(dhikr) },
                            onToggleFavorite = { viewModel.toggleFavorite(dhikr) },
                            onEdit = { dhikrToEdit = dhikr },
                            onDelete = { dhikrToDelete = dhikr }
                        )
                    }
                }
            }
        }

        // Floating Action Button to Add Custom Dhikr
        FloatingActionButton(
            onClick = {
                if (!settings.isVipActive && customDhikrsCount >= 3) {
                    viewModel.openVipPaywall("Ücretsiz planda en fazla 3 özel zikir ekleyebilirsiniz. Sınırsız özel vird & dua eklemek için VIP'ye geçin.")
                } else {
                    showAddDialog = true
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_custom_dhikr_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Özel Zikir Ekle"
            )
        }
    }

    // Add Dialog
    if (showAddDialog) {
        AddEditDhikrDialog(
            initialDhikr = null,
            onDismiss = { showAddDialog = false },
            onSave = { title, arabic, trans, meaning, target ->
                viewModel.addCustomDhikr(
                    title = title,
                    arabicText = arabic,
                    transliteration = trans,
                    meaning = meaning,
                    targetCount = target,
                    category = "kisisel"
                )
                showAddDialog = false
            }
        )
    }

    // Edit Dialog
    dhikrToEdit?.let { dhikr ->
        AddEditDhikrDialog(
            initialDhikr = dhikr,
            onDismiss = { dhikrToEdit = null },
            onSave = { title, arabic, trans, meaning, target ->
                viewModel.updateCustomDhikr(
                    dhikr.copy(
                        title = title,
                        arabicText = arabic,
                        transliteration = trans,
                        meaning = meaning,
                        targetCount = target
                    )
                )
                dhikrToEdit = null
            }
        )
    }

    // Delete Confirm Dialog
    dhikrToDelete?.let { dhikr ->
        ConfirmDialog(
            title = "Zikri Sil",
            message = "«${dhikr.title}» zikrini silmek istediğinize emin misiniz?",
            confirmText = "Sil",
            dismissText = "Vazgeç",
            isDestructive = true,
            onConfirm = {
                viewModel.deleteDhikr(dhikr)
                dhikrToDelete = null
            },
            onDismiss = { dhikrToDelete = null }
        )
    }
}

@Composable
private fun DhikrItemCard(
    dhikr: DhikrEntity,
    isActive: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isActive) 6.dp else 2.dp, RoundedCornerShape(18.dp))
            .clickable(onClick = onSelect)
            .testTag("dhikr_card_${dhikr.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Active Badge / Favorite / Target
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isActive) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Aktif Zikir",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Target Count Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isActive) MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (dhikr.targetCount > 0) "Hedef: ${dhikr.targetCount}" else "Serbest",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (dhikr.isCustom) {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Düzenle",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (dhikr.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favori",
                            tint = if (dhikr.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic Text
            if (dhikr.arabicText.isNotBlank()) {
                Text(
                    text = dhikr.arabicText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Title
            Text(
                text = dhikr.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )

            // Meaning
            if (dhikr.meaning.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dhikr.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer info: Current count and Start button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mevcut: ${dhikr.currentCount} / Toplam: ${dhikr.totalRecited}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable(onClick = onSelect)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isActive) "Devam Et" else "Zikre Başla",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
