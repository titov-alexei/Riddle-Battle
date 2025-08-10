package com.example.riddlebattleoftheteam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.ui.theme.DarkGreen
import com.example.riddlebattleoftheteam.ui.theme.Dimens
import com.example.riddlebattleoftheteam.ui.theme.LightGreen

@Composable
fun CustomViewQuestion(
    modifier: Modifier = Modifier,
    numberQuestion: Int = 1,
    question: String = "",
    answer: String = "",
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(Dimens.StandartPadding)
            .fillMaxWidth()
            .background(
                color = DarkGreen.copy(alpha = 0.3f),
                shape = RoundedCornerShape(Dimens.ButtonCornerRadius)
            ),
        contentAlignment = Alignment.TopCenter

    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.BottomPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
        ) {
            Text(
                text = "${stringResource(R.string.question)} $numberQuestion",
                modifier = Modifier.padding(bottom = Dimens.CustomViewPadding)
            )

            Text(
                modifier = Modifier.padding(bottom = Dimens.CustomBoxPadding),
                text = question,
                fontSize = Dimens.SmallText,
            )

            Text(
                text = answer,
                color = LightGreen
            )
        }
    }

}