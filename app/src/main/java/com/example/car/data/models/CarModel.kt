package com.example.car.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    val img: String,
    val carName: String,
    val carPrice: String
) : Parcelable
