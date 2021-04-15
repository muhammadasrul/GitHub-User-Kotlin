package com.asrul.github.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.asrul.github.R
import com.asrul.github.data.db.AppDatabase
import com.asrul.github.data.db.FavUserDAO
import com.asrul.github.data.db.FavUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    private lateinit var widgetItems: List<FavUserEntity>
    private lateinit var userDAO: FavUserDAO

    override fun onCreate() {
        userDAO = AppDatabase.getDatabase(context).favUserDAO()
        runBlocking(Dispatchers.IO) {
                widgetItems = userDAO.getAllWidget()
        }
    }

    override fun onDataSetChanged() {
        runBlocking(Dispatchers.IO) {
                widgetItems = userDAO.getAllWidget()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems.size

    private suspend fun getDrawable(image: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(image)
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    override fun getViewAt(position: Int): RemoteViews {
        val image: String? = widgetItems[position].avatarUrl
        val name: String? = widgetItems[position].name

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)

        var bitmap: Bitmap?

        runBlocking {
            bitmap = image?.let { getDrawable(it) }
        }

        remoteViews.setImageViewBitmap(R.id.imgUserWidget, bitmap)

        val bundle = Bundle()
        bundle.putString(GithubUserWidget.EXTRA_ITEM, name)
        val intent = Intent()
        intent.putExtras(bundle)

        remoteViews.setOnClickFillInIntent(R.id.imgUserWidget, intent)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = false

}