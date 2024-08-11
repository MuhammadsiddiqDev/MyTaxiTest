package com.datasite.mytaxitestapp.screen.compose.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.mytaxitestapp.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomSheet(showBottomSheet :MutableState<Boolean>): MutableState<Boolean> {

    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()


    ModalBottomSheet(onDismissRequest = {

        coroutineScope.launch {

            showBottomSheet.value = false
        }
    },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary)) {
                Box(modifier = Modifier

                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        onClick = {},
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                )
                {

                    Column(modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)) {

                        bottomItem(count = "6 / 8",icon = R.drawable.tariff_icon, text = R.string.tariff)

                        Spacer(modifier = Modifier
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer
                            ))

                        bottomItem(count = "0",icon = R.drawable.order_icon, text = R.string.orders)

                        Spacer(modifier = Modifier
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer
                            ))

                        bottomItem(count = "",icon = R.drawable.rocket_icon, text = R.string.went)




                    }


                }
            }
        }

    )
    return showBottomSheet
}

//@Preview(showBackground = true)
@Composable
fun bottomItem(count : String, icon : Int, text : Int){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable(
            onClick = {},
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                color = MaterialTheme.colorScheme.onPrimary
            )
        ))
    {
        Row(modifier = Modifier
            .height(36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = null,modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(modifier = Modifier.weight(1f),
                text = stringResource(id = text),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 2,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.W600,
                fontFamily = FontFamily(
                    Font(R.font.lato_regular)
                )

                )

            Row {

                Text(modifier = Modifier,
                    text = count,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W600,
                    fontFamily = FontFamily(
                        Font(R.font.lato_regular)
                    )


                    )

                Icon(
                    painter = painterResource(id = R.drawable.right_icon),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = null,modifier = Modifier.size(24.dp)

                )

            }


            Spacer(modifier = Modifier.size(16.dp))
        }
    }

}