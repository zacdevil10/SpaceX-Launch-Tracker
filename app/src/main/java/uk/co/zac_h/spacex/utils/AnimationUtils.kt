package uk.co.zac_h.spacex.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import uk.co.zac_h.spacex.R

fun animateEnterFromTop(context: Context?): Animation =
    AnimationUtils.loadAnimation(context, R.anim.slide_in_top)

fun animateExitToTop(context: Context?): Animation =
    AnimationUtils.loadAnimation(context, R.anim.slide_out_top)

fun animateLayoutFromBottom(context: Context?): LayoutAnimationController =
    AnimationUtils.loadLayoutAnimation(
        context,
        R.anim.layout_animation_from_bottom
    )