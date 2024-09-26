package com.example.newsappcompose.presentation.mainactivity

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappcompose.domain.usecase.app_entry.AppEntryUseCases
import com.example.newsappcompose.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
):ViewModel(){

    var splashCondition by mutableStateOf(true)
        private set

    var startDestionation by mutableStateOf(Route.AppStartNavigation.route)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        appEntryUseCases.readAppEntry().onEach {shouldStartFromHomeScreen ->
            Log.d("MainViewModel1233", "startDestionation: ${shouldStartFromHomeScreen}")
            if(shouldStartFromHomeScreen){
                startDestionation = Route.NewsNavigation.route
            } else {
                startDestionation = Route.AppStartNavigation.route

            }
            delay(300)
            splashCondition = false
            Log.d("MainViewModel1233", "startDestionation: $startDestionation")

        }.launchIn(viewModelScope)
        loadStuff()
    }

    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000L)
            _isLoading.value = false
        }
    }
}