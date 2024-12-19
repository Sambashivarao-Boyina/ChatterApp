package com.example.chatterapp.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.domain.manager.LocalUserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val localUserManager: LocalUserManager
) : ViewModel() {
    fun onEvent(event: OnBoardingEvent) {
        when(event) {
            is OnBoardingEvent.SaveOnBoarding -> {
                viewModelScope.launch {
                    saveOnBoarding()
                }
            }
        }
    }
    suspend fun saveOnBoarding() {
        localUserManager.saveAppEntry()
    }
}


sealed class OnBoardingEvent {
    object SaveOnBoarding: OnBoardingEvent()
}
