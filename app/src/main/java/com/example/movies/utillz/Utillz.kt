package com.example.movies.utillz

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R
import java.io.IOException



const val MY_API_KEY = "2d2aedeb384294d90951456f47fbaf0f"
const val NETWORK_PAGE_SIZE = 30
const val STARTING_PAGE_INDEX =1

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 100f
        centerRadius = 50f
        start()
    }
}


fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {
    try {
        //Load the user image in the Imageview
        Glide
            .with(context)
            .load(Uri.parse("https://image.tmdb.org/t/p/original$url".toString()))// URi of the image
            .centerCrop()// scale type of the image.
            .placeholder(progressDrawable)// defailt place holder if the image fails to load
            .into(this)// the view in which the image will be loaded
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@BindingAdapter("android:imageGallery")
fun loadImage(binding: ImageView, url: String?) {
    binding.loadImage(url, getProgressDrawable(binding.context))
}

