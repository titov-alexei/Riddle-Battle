package com.example.riddlebattleoftheteam.presentation.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.CustomBox
import com.example.riddlebattleoftheteam.presentation.components.CustomButton
import com.example.riddlebattleoftheteam.presentation.viewmodels.SettingsViewModel
import com.example.riddlebattleoftheteam.ui.theme.Dimens
import com.example.riddlebattleoftheteam.utils.Constants
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLanguage()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
            ) {
                Text(text = stringResource(R.string.select_language))

                LanguageSelectionUI(
                    selectedLanguage = state.currentLanguage,
                    onLanguageSelected = { lang ->
                        viewModel.setLanguage(lang)
                    }
                )

                Spacer(Modifier.height(Dimens.SpacerPadding))

                CustomButton(
                    text = stringResource(R.string.save),
                    onClick = {
                        viewModel.applyLanguage()
                        (context as Activity).recreate()
                    },
                )
            }
        }

        if (state.showSuccessMessage) {
            Toast.makeText(context, R.string.settings_saved, Toast.LENGTH_SHORT).show()
            viewModel.messageShown()
        }

        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            viewModel.errorShown()
        }
    }
}

@Composable
private fun LanguageSelectionUI(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    CustomBox(
        text = stringResource(R.string.russian),
        showBorder = selectedLanguage == Constants.LANGUAGE_RU,
        onClick = { onLanguageSelected(Constants.LANGUAGE_RU) }
    )

    CustomBox(
        text = stringResource(R.string.english),
        showBorder = selectedLanguage == Constants.LANGUAGE_EN,
        onClick = { onLanguageSelected(Constants.LANGUAGE_EN) }
    )
}
