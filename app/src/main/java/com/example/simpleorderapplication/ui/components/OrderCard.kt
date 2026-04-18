package com.example.simpleorderapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.utils.toFormattedDate
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmUUID

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() },
        elevation = CardDefaults.cardElevation( 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(Modifier.padding(4.dp)) {
            Column(Modifier.fillMaxWidth(0.85f).padding(15.dp)) {
                Row( verticalAlignment = Alignment.Bottom) {
                    Text(stringResource(R.string.order_card_order_number))
                    Text(order.code.toString(), Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_client_name))
                    Text(order.clientName, Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_date))
                    Text(order.createdAt.toFormattedDate(), Modifier.padding(horizontal = 4.dp))
                }
            }
            Column(Modifier.fillMaxWidth(), Arrangement.Top, Alignment.CenterHorizontally) {
                Text(order.products.sumOf { it.priceCents.div(100.0) * it.quantity }.let { "%.2f".format(it) } , fontSize = 12.sp)
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOrderCard(){
    OrderCard(
        Order().apply {
            code = 1
            clientName = "Doug"
            createdAt = RealmInstant.now()
            products = realmListOf<Product>()
        }
    ) { }
}