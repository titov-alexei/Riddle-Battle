package com.example.riddlebattleoftheteam.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.ui.theme.Dimens

@Composable
fun About() {

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.program_version)
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerPadding))
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.program_developer)
                )
            }
        }
    }
}