package com.easysales.letsplay.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

object ImageUtils {
    fun load(context: Context, imageView: ImageView, uri: Uri, @DrawableRes loaderDrawable:Int) {
        var builder = Glide.with(context).load(uri)

        val animPlaceholder = context.getDrawable(loaderDrawable) as AnimationDrawable
        animPlaceholder.callback = imageView
        animPlaceholder.setVisible(true, true)
        animPlaceholder.start()

        builder = builder
            .placeholder(animPlaceholder)

        builder.into(imageView)
    }

    fun load(context: Context, imageView: ImageView, uri: String, @DrawableRes loaderDrawable:Int) {
        load(context, imageView, Uri.parse(uri), loaderDrawable)
    }

    fun loadCropCircle(context: Context, imageView: ImageView, uri: Uri, @DrawableRes loaderDrawable:Int) {
        val animPlaceholder = context.getDrawable(loaderDrawable) as AnimationDrawable
        animPlaceholder.callback = imageView
        animPlaceholder.setVisible(true, true)
        animPlaceholder.start()

        Glide.with(context)
            .load(uri)
            .centerCrop()
            .placeholder(animPlaceholder)
            .transform(CircleTransform())
            .into(imageView)
    }

    class CircleTransform() : BitmapTransformation() {
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {

        }

        val id: String
            get() = javaClass.name

        override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
            return circleCrop(pool, toTransform)
        }

        private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null

            val size = Math.min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            // TODO this could be acquired from the pool too
            val squared = Bitmap.createBitmap(source, x, y, size, size)

            var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(result!!)
            val paint = Paint()
            paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            return result
        }
    }
}