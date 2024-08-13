package dev.yuanzix.beatify.ui.screens.auth.utils

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun LottieSection(showContent: Boolean, size: Int = 400, @RawRes resourceID: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resourceID))

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = tween(1000))
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(size.dp)
        )
    }
}