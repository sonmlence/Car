package com.example.car.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImg(img: String){
    Glide.with(this).load(img).into(this);

}