package com.example.riddlebattleoftheteam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.ui.theme.Dimens

@Composable
fun CustomTextFiled(
    modifier: Modifier = Modifier.padding(horizontal = Dimens.CustomBoxPadding),
    backgroundColor: Color = Color.White,
    name: String,
    index: Long,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(Dimens.ButtonCornerRadius)
            )
            .padding(Dimens.SmallPadding)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = name,
            onValueChange = onValueChange,
            label = {
                Text(
                    fontSize = Dimens.SmallText,
                    text = "${stringResource(R.string.team)} ${index + 1}"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.StandartPadding),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedLabelColor = Color.White.copy(alpha = 0.7f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }

}