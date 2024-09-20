package com.example.waux.components.sessionPageComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waux.R

/*
Final
 */

@Composable
fun ReorderableLazyList() {
    var list1 by remember { mutableStateOf(List(5) { it }) }
    val draggableItems by remember {
        derivedStateOf { list1.size }
    }
    val stateList = rememberLazyListState()

    val dragDropState =
        rememberDragDropState(
            lazyListState = stateList,
            draggableItemsNum = draggableItems,
            onMove = { fromIndex, toIndex ->
                list1 = list1.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
            })

    LazyColumn(
        modifier = Modifier.dragContainer(dragDropState),
        state = stateList,
        // contentPadding = PaddingValues(16.dp),
        // verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        draggableItems(items = list1, dragDropState = dragDropState) { modifier, item ->
            Item(
                modifier = modifier,
                index = item,
            )
        }
    }
}


@Composable
private fun Item(
    modifier: Modifier = Modifier,
    index: Int,
    source: String = "spotify",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .drawBehind {
                // Draw a top border
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.spotify),
                contentDescription = "Spotify logo",
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .width(25.dp)
            )
            Text(
                "Item $index",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text("GU", modifier = Modifier.padding(20.dp))
            Icon(imageVector = Icons.Default.DragHandle, contentDescription = "Drag Handle", modifier = Modifier.padding(20.dp))
        }
    }
}