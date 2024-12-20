package com.example.chatterapp.presentation.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatterapp.presentation.onBoarding.components.OnBoardingButton
import com.example.chatterapp.presentation.onBoarding.components.OnBoardingIndicator
import com.example.chatterapp.presentation.onBoarding.components.OnBoardingPage
import com.example.chatterapp.ui.theme.Blue
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    onEvent:(OnBoardingEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Blue,
                        Color.Black,
                        Color.Black,
                        Color.Black, Color.Black, Blue
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var pagerState = rememberPagerState(initialPage = 0) { pages.size }
        val scope = rememberCoroutineScope()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(0.8f)
        ) { index ->
            OnBoardingPage(pages[index])
        }
        Row(
            modifier = Modifier.weight(0.2f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OnBoardingButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                enable = pagerState.currentPage !== 0,
                onClick = {
                    scope.launch {
                        if(pagerState.currentPage != 0) {
                            pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                        }
                    }
                }
            )

            OnBoardingIndicator(
                size = pages.size,
                curPage = pagerState.currentPage
            )

            OnBoardingButton(
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                enable = true,
                onClick = {
                    scope.launch {
                        if(pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        } else if(pagerState.currentPage == pages.size - 1) {
                            onEvent(OnBoardingEvent.SaveOnBoarding)
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen(){}
}