package com.leewilson.knote.utils

import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

fun MaterialCardView.changeColor(newColor: Int) {
    setCardBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}