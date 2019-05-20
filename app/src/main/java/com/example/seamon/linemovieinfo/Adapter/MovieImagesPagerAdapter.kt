package com.example.seamon.linemovieinfo.Adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.seamon.linemovieinfo.R
import com.example.seamon.linemovieinfo.Util.DownloadImage


/**
 * I decided to use PagerAdapter to show the movie images because
 * it looks way cooler to swipe between the images compared to just clicking on them :)
 */
class MovieImagesPagerAdapter: PagerAdapter {
    var mContext: Context
    var imageList: List<String>

    constructor(mContext: Context, imageList: List<String>) : super() {
        this.mContext = mContext
        this.imageList = imageList
    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view.equals(obj)
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.viewpager_item_movie, container, false)

        val movieImage: ImageView = view.findViewById(R.id.imageView_movie_image)
        val progressBarMovieImage: ProgressBar = view.findViewById(R.id.progressBar_movie_image)

        DownloadImage(movieImage, progressBarMovieImage, mContext).execute(imageList[position])

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(obj as View)
    }



}