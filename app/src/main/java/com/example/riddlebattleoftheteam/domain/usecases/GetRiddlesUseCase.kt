package com.example.riddlebattleoftheteam.domain.usecases

import com.example.riddlebattleoftheteam.domain.repository.RiddleRepository
import javax.inject.Inject

class GetRiddlesUseCase @Inject constructor(
    private val repository: RiddleRepository
) {
    suspend operator fun invoke() = repository.fetchRiddles()
}