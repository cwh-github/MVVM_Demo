package com.example.mvvm_jetpack.app

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

/**
 * Description:
 * Date：2019/7/23-20:37
 * Author: cwh
 */
@GlideModule
class MyGlideModule : AppGlideModule() {


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context,
                diskCacheFolderName(context),
                diskCacheSizeBytes()
            )
        )
            .setMemoryCache(LruResourceCache(memoryCacheSizeBytes()))
    }

    private fun memoryCacheSizeBytes(): Long {
        return 20 * 1024 * 1024 //20MB
    }

    private fun diskCacheSizeBytes(): Long {
        return 512 * 1024 * 1024 //512MB
    }

    private fun diskCacheFolderName(context: Context): String? {
        return "MVVM_Jetpack"
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    }
}