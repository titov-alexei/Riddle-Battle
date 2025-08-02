package com.example.riddlebattleoftheteam.presentation.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.CustomBox
import com.example.riddlebattleoftheteam.ui.theme.Dimens
import com.example.riddlebattleoftheteam.utils.LanguageHelper
import java.util.Locale


@Composable
fun Settings() {
    val context = LocalContext.current
    val languageHelper = remember { LanguageHelper(context) }
    val currentLanguage = remember { mutableStateOf(languageHelper.getCurrentLanguage()) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(onClick = {
                languageHelper.setLanguage("en")
                currentLanguage.value = "en"
            }) {
                Text(
                    "English",
                    color = if (currentLanguage.value == "en") Color.Green else Color.White
                )
            }

            Button(onClick = {
                languageHelper.setLanguage("ru")
                currentLanguage.value = "ru"
            }) {
                Text(
                    "Русский",
                    color = if (currentLanguage.value == "ru") Color.Green else Color.White
                )
            }
        }
    }

    /*Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
        ) {
            Text(
                text = stringResource(R.string.select_language)
            )

            CustomBox (
                text = "Russian",
                showBorder = resultLang == 1,
                onClick = {
                    resultLang = 1
                    Log.d("MYLOG", "-------${resultLang}")

                }
            )
            CustomBox (
                text = "English",
                showBorder = resultLang == 2,
                onClick = {
                    resultLang = 2
                    Log.d("MYLOG", "-------${resultLang}")
                }
            )

        }



    }*/
}