package com.example.riddlebattleoftheteam.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.CustomButton
import com.example.riddlebattleoftheteam.presentation.navigation.Screen
import com.example.riddlebattleoftheteam.presentation.viewmodels.GameViewModel
import com.example.riddlebattleoftheteam.ui.theme.Bronze
import com.example.riddlebattleoftheteam.ui.theme.Dimens
import com.example.riddlebattleoftheteam.ui.theme.Gold
import com.example.riddlebattleoftheteam.ui.theme.Silver

@Composable
fun GameResultsScreen(
    navController: NavHostController,
    viewModel: GameViewModel = hiltViewModel(
        remember {
            navController.getBackStackEntry(Screen.GameScreen.route)
        }
    )
) {
    val results by viewModel.teamResults.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.CustomBoxPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
    ) {
        Text(
            text = "🏆 ${stringResource(R.string.results)}",
            //style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = Dimens.StandartPadding)
        )

        results.sortedByDescending { it.correctAnswers }
            .forEachIndexed { index, result ->
                val (medalEmoji, backgroundColor) = when (index) {
                    0 -> "🥇" to Gold
                    1 -> "🥈" to Silver
                    2 -> "🥉" to Bronze
                    else -> "" to MaterialTheme.colorScheme.surfaceVariant
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.StandartPadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$medalEmoji ${result.teamName}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${result.correctAnswers} ${stringResource(R.string.correct)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

        Spacer(modifier = Modifier.height(Dimens.BottomPadding))

        CustomButton(
            text = "🔁 ${stringResource(R.string.play_again)}",
            onClick = {
                viewModel.resetGame()
                navController.navigate(Screen.GamePreparationScreen.route) {
                    popUpTo(Screen.GameResultScreen.route) { inclusive = true }
                }
            }
        )
    }
}