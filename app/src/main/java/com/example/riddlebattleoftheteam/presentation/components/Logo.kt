package com.example.riddlebattleoftheteam.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.ui.theme.Dimens

@Composable
fun Logo() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = Dimens.ImageHeight),
            contentDescription = stringResource(id = R.string.description_logo),
            painter = painterResource(id = R.drawable.logo)
        )
        Text(
            text = stringResource(id = R.string.battle_of_the_team),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}