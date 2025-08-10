package com.example.riddlebattleoftheteam.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.CustomBox
import com.example.riddlebattleoftheteam.presentation.components.CustomButton
import com.example.riddlebattleoftheteam.presentation.components.CustomTextFiled
import com.example.riddlebattleoftheteam.presentation.viewmodels.GamePreparationViewModel
import com.example.riddlebattleoftheteam.ui.theme.DarkGreen
import com.example.riddlebattleoftheteam.ui.theme.Dimens


@Composable
fun GamePreparationScreen(
    navController: NavHostController,
    viewModel: GamePreparationViewModel = hiltViewModel()
) {

    val selectedCount by viewModel.selectedTeamCount.collectAsStateWithLifecycle()
    val teamNames by viewModel.teamNames.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(
                top = Dimens.CustomBoxPadding,
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
    ) {
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(R.string.select_number_teams)
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
        ) {
            CountTeamsSelectionUI(
                selectedCount = selectedCount,
                onCountTeamsSelected = { viewModel.selectTeamCount(it) }
            )
        }

        Text(
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(R.string.enter_command_name)
        )

        teamNames.forEachIndexed { index, name ->
            CustomTextFiled(
                name = name,
                index = index.toLong(),
                onValueChange = { newName -> viewModel.updateTeamName(index, newName) }
            )
        }

        CustomButton(
            text = stringResource(R.string.next),
            onClick = { viewModel.startGame(navController) }
        )
    }
}

@Composable
private fun CountTeamsSelectionUI(
    selectedCount: Int,
    onCountTeamsSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf(1, 2, 3, 4).forEach { number ->
            Box(
                modifier = Modifier.weight(1f)
            ) {
                CustomBox(
                    modifier = Modifier
                        .padding(horizontal = Dimens.StandartPadding / 2)
                        .fillMaxWidth(),
                    backgroundColor = DarkGreen,
                    showBorder = selectedCount == number,
                    onClick = { onCountTeamsSelected(number) },
                    text = number.toString()
                )
            }
        }
    }
}
