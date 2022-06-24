package com.example.homeapp1208

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.leanback.app.BrowseSupportFragment
import java.io.Serializable

data class App(
    var packName: String? = null,
    var iconDrawable: Drawable? = null,
    var title: String? = null
    ) : Serializable {

    override fun toString(): String {
        return ", title='" + packName
    }
}
