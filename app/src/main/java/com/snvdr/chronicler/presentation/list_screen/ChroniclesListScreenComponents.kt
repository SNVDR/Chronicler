package com.snvdr.chronicler.presentation.list_screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.OrderType

@Composable
fun ChronicleListItem(
    chronicleDto: ChronicleDto,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = chronicleDto.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 22.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(20.dp)
                .weight(1.5f)
        )

        Text(
            text = chronicleDto.date,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(20.dp)
                .weight(2f)
        )

        CustomDropDownMenu(
            Modifier.weight(0.5f),
            menuItems = listOf(DropDownItems.Delete),
            onItemClick = {
                when(it){
                    DropDownItems.Delete -> {
                        onDeleteClick()
                    }
                    DropDownItems.Something1 -> {}
                    DropDownItems.Something2 -> {}
                }
            }
        )
    }
}
@Preview(
    showBackground = true,
    heightDp = 80
)
@Composable
fun PreviewChronicleListItem() {
    val fakeDto = ChronicleDto(
        id = 1488,
        title = "Fake title title title ",
        content = "Fake content",
        date = "08/1/2023/11:11:11"
    )
    ChronicleListItem(
        chronicleDto = fakeDto,
        onDeleteClick = {},
        onItemClick = {}
    )
}

sealed class DropDownItems{
    object Delete:DropDownItems()
    object Something1:DropDownItems()
    object Something2:DropDownItems()
}

@Composable
fun CustomDropDownMenu(modifier: Modifier = Modifier, menuItems: List<DropDownItems>, onItemClick: (DropDownItems) -> Unit){
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier.fillMaxWidth()) {
        IconButton(onClick = {
            expanded = !expanded
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach {
                when(it){
                    is DropDownItems.Delete -> {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onItemClick(it)
                            }
                        )
                    }
                    is DropDownItems.Something1 -> {
                        DropdownMenuItem(
                            text = { Text("Something1") },
                            onClick = {
                                Toast.makeText(context, "Something1", Toast.LENGTH_SHORT).show()
                                onItemClick(it)
                            }
                        )
                    }
                    is DropDownItems.Something2 -> {
                        DropdownMenuItem(
                            text = { Text("Something2") },
                            onClick = {
                                Toast.makeText(context, "Something2", Toast.LENGTH_SHORT).show()
                                onItemClick(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    chronicleOrder: ChronicleOrder = ChronicleOrder.Date(
        OrderType.Descending
    ),
    onOrderChange: (ChronicleOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = chronicleOrder is ChronicleOrder.Title,
                onSelect = { onOrderChange(ChronicleOrder.Title(chronicleOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = chronicleOrder is ChronicleOrder.Date,
                onSelect = { onOrderChange(ChronicleOrder.Date(chronicleOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = chronicleOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(chronicleOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = chronicleOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(chronicleOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}