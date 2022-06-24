package com.example.homeapp1208

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import java.util.*

class MainFragment : BrowseSupportFragment() {
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareBackgroundManager()
        setupUIElements()
        loadRows()
        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackgroundTimer?.cancel()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(activity!!.window)
        mDefaultBackground = ContextCompat.getDrawable(activity!!, R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(activity!!, R.color.default_background)
        searchAffordanceColor = ContextCompat.getColor(activity!!, R.color.fastlane_background)

    }

    @SuppressLint("WrongConstant", "UseRequireInsteadOfGet")
    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        // 端末にインストール済のアプリケーション一覧情報を取得
        val pm = activity!!.packageManager
        val installedAppList = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        var packName = mutableListOf<String>()
        var icon = mutableListOf<Drawable>()
        var title = mutableListOf<String>()
        var i = 0

        if (installedAppList != null) {
            for (app in installedAppList) {

                packName.add(app.packageName)
                pm.let { app.loadLabel(it).toString() }.let { title.add(it) }
                pm.getApplicationIcon(app.packageName).let { icon.add(it) }
            }
        }

        val appList = AppList(packName as ArrayList<String>, icon as ArrayList<Drawable>,title as ArrayList<String>)
        var listAs = appList.list

        if (installedAppList != null) {
            for (app in installedAppList) {
                listRowAdapter.add(listAs[i])//コンテンツ数
                i++
            }
        }
        val header = HeaderItem(0.toLong(), "AppList")//カテゴリー数
        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setupEventListeners() {
        setOnSearchClickedListener {
            try {
                //設定画面呼び出し
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    activity!!,
                    "Setting Activity Error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        @SuppressLint("UseRequireInsteadOfGet")
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is App) {
                val stringBuilder = StringBuilder()
                stringBuilder.append(item.toString())

                stringBuilder.delete(0, 9)
                try {
                    var packageManager = activity!!.packageManager
                    var intent =
                        packageManager.getLeanbackLaunchIntentForPackage(stringBuilder.toString())
                    startActivity(intent)

                } catch (e: Exception) {
                    Toast.makeText(
                        activity!!,
                        stringBuilder.toString() + "を開けません。",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.default_background))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    companion object {
        private val TAG = "MainFragment"
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
    }
}