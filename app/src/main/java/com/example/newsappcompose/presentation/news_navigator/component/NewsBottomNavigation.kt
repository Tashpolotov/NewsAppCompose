package com.example.newsappcompose.presentation.news_navigator.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.example.newsappcompose.R
import com.example.newsappcompose.presentation.Dimens.ExtraSmallPadding2
import com.example.newsappcompose.presentation.Dimens.IconSize
import com.example.newsappcompose.ui.theme.NewsAppComposeTheme

@Composable
fun NewsBottomNavigation(
    items: List<BottomNavigationItem>,
    selected:Int,
    onItemClick:(Int)->Unit,

) {

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed{index, item ->
            NavigationBarItem(
                selected = index == selected,
                onClick = { onItemClick(index) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(IconSize)
                            )
                        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
                        Text(text = item.text, style = MaterialTheme.typography.labelSmall)
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.body),
                    unselectedTextColor = colorResource(id = R.color.body),
                    indicatorColor = MaterialTheme.colorScheme.background

                )
            )

        }

    }

}

data class BottomNavigationItem(
    @DrawableRes val icon:Int,
    val text:String,
)


@Preview
@Composable
fun NewsBottomNavigationPreview(){
    NewsAppComposeTheme {
        NewsBottomNavigation(
            items = listOf(
                BottomNavigationItem(icon = R.drawable.ic_home, "Home"),
                BottomNavigationItem(icon = R.drawable.ic_search, "Search"),
                BottomNavigationItem(icon = R.drawable.ic_bookmark, "Bookmark")
            ),
            selected = 0,
            onItemClick = {}
            )
    }
}