package com.example.riddlebattleoftheteam.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import com.example.riddlebattleoftheteam.presentation.settings.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageUseCase: LanguageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun loadLanguage() {
        _state.value = _state.value.copy(
            currentLanguage = languageUseCase.getCurrentLanguage()
        )
    }

    fun setLanguage(code: String) {
        _state.value = _state.value.copy(
            currentLanguage = code,
            isLanguageChanged = true
        )
    }

    fun applyLanguage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                languageUseCase.setLanguage(state.value.currentLanguage)
                _state.value = _state.value.copy(
                    showSuccessMessage = true,
                    isLoading = false,
                    isLanguageChanged = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "${R.string.settings_not_saved}, e.message",
                    isLoading = false
                )
            }
        }
    }

    fun messageShown() {
        _state.value = _state.value.copy(showSuccessMessage = false)
    }

    fun errorShown() {
        _state.value = _state.value.copy(error = null)
    }
}
