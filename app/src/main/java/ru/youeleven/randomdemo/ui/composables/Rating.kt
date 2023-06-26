package ru.youeleven.randomdemo.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.youeleven.randomdemo.R

@Composable
fun Rating(modifier: Modifier = Modifier, rating: Double, numberOfRatings: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(id = R.drawable.ic_star), contentDescription = null)
        Text(text = "$rating | $numberOfRatings")
        Icon(painterResource(id = R.drawable.ic_person), contentDescription = null)
    }
}