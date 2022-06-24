package com.example.homeapp1208

import android.graphics.drawable.Drawable
import android.util.Log

class AppList {
    lateinit var packName : ArrayList<String>
    lateinit var iconDrawable : ArrayList<Drawable>
    lateinit var titleN : ArrayList<String>

    constructor(packName : ArrayList<String>,icon : ArrayList<Drawable>,title : ArrayList<String>) {
        this.packName = packName
        this.iconDrawable = icon
        this.titleN = title
    }
    val list: List<App> by lazy {
        setupMovies(this.packName,this.iconDrawable,this.titleN)
    }

    private fun setupMovies(packageName: ArrayList<String>,icon : ArrayList<Drawable>,title: ArrayList<String>): List<App> {
        packName = packageName
        iconDrawable = icon
        this.titleN = title

        val list = packName.indices.map {
            buildMovieInfo(
                packName[it].toString(),
                iconDrawable[it],
                titleN[it].toString()
            )
        }
        return list
    }

    private fun buildMovieInfo(
        packName: String,
        iconDrawable: Drawable,
        titleN: String
    ): App {
        val app = App()
        app.packName = packName
        app.iconDrawable = iconDrawable
        app.title = titleN
        return app
    }


}

