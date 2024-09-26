package com.example.newsappcompose.presentation.refresh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyColum(isLoading: Boolean,
                           onRefresh: () -> Unit,
                           content: @Composable (Modifier) -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        content(Modifier.fillMaxSize())
    }
}