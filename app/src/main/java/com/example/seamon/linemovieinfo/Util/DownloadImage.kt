package com.example.seamon.linemovieinfo.Util

import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ProgressBar
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.URLConnection
import java.util.*

import java.net.URI


/**
 * In order to download the image from the url and save it to the sdcard, we will use AsyncTask to do it.
 * The image is saved in the folder "/sdcard/LineMovieApp"
 * I also think that it is easier to make a progress dialog with percentage with AsyncTask compared to using libraries like Glide or Picasso
 */
class DownloadImage(internal var bmImage: ImageView, internal var progressBar: ProgressBar, internal var mContext: Context) : AsyncTask<String, Int, String>() {
    var mProgressDialog: ProgressDialog? = null
    /**
     *  The images are saved in "/storage/0/LineMovieApp" (for the emulator)
     *  Note that instead of saving the images to the sdcard, I decided to save them on the device storage folder instead.
     *  Using Environment.getExternalStorageDirectory().getAbsolutePath() guarantees that the images will be saved,
     *  even if the device has no sdcard
     *  If I, instead, save the images to /sdcard/LineMovieApp, it won't work on some devices that have no sdcard slot
     *
     */
    //val storagePath = "/sdcard"
    val storagePath = Environment.getExternalStorageDirectory().getAbsolutePath()


    override fun doInBackground(vararg urls: String): String {
        var file_name = "name"
        val urlDisplay = urls[0]
        var file_length = 0
        try {
            val url: URL = URL(urlDisplay)
            val urlConnection: URLConnection = url.openConnection()
            urlConnection.connect()
            file_length = urlConnection.contentLength
            mProgressDialog?.setMax(file_length/1000)

            val new_folder = File(storagePath, "LineMovieApp")

            if(!new_folder.exists()){
                new_folder.mkdir()
            }
            file_name = getNameFromUrl(urlDisplay) + ".jpg"
            val input_file = File(new_folder, file_name)

            //If the file is already existed
            if(input_file.exists()){
                return file_name
            }

            val inputStream = BufferedInputStream(url.openStream(), 8192 )
            val data = ByteArray(1024)
            var total = 0
            var count = 0


            val outputStream = FileOutputStream(input_file)
            while(true){
                count = inputStream.read(data)
                if(count == -1){
                    break
                }
                total += count
                outputStream.write(data, 0, count)
                var progress = total
                //publishProgress(progress *100 / file_length)

                publishProgress((total /1000))

            }
            inputStream.close()
            outputStream.close()

        } catch (e: Exception) {
            Log.d("Error", e.stackTrace.toString())

        }

        return file_name
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let {
            mProgressDialog?.progress = it
        }
    }

    override fun onPostExecute(result: String) {
        mProgressDialog?.hide()
        Toast.makeText(mContext, "Download Completed!", Toast.LENGTH_SHORT).show()
        val path = storagePath + "/LineMovieApp/" + result
        bmImage.setImageDrawable(Drawable.createFromPath(path))

    }

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog?.setTitle("Downloading")
        // Set progress dialog Message
        mProgressDialog?.setMessage("Downloading, Please Wait!")
        mProgressDialog?.setIndeterminate(false)
        mProgressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        mProgressDialog?.setProgressNumberFormat("%1d kb / %2d kb")
        // Show progress dialog
        mProgressDialog?.show()
    }

    /**
     * This function should be based on how the Url is formatted
     * In this case, the meaningful segment is the second to last segment (1448585271749MCMVs_JPEG)
     * from "http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2"
     */
    fun getNameFromUrl(url: String): String{
        val uri = URI(url)
        val segments = uri.getPath().split("/")
        val idStr = segments[segments.size - 2]
        //val id = Integer.parseInt(idStr)

        return idStr

    }
}