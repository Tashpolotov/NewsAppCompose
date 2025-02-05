package com.example.newsappcompose.presentation.mainactivity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappcompose.domain.usecase.app_entry.AppEntryUseCases
import com.example.newsappcompose.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
):ViewModel(){

    var splashCondition by mutableStateOf(true)
        private set

    var startDestionation by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        appEntryUseCases.readAppEntry().onEach {shouldStartFromHomeScreen ->
            if(shouldStartFromHomeScreen){
                startDestionation = Route.NewsNavigation.route
            } else {
                startDestionation = Route.AppStartNavigation.route

            }
            delay(300)
            splashCondition = false

        }.launchIn(viewModelScope)
    }
}