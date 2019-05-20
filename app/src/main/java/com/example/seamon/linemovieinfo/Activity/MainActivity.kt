package com.example.seamon.linemovieinfo.Activity


import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.example.seamon.linemovieinfo.Model.Movie
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.seamon.linemovieinfo.Adapter.MovieImagesPagerAdapter
import com.example.seamon.linemovieinfo.R


class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    private lateinit var permissionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //The button that will only appear when there is no storage access permission
        permissionButton = findViewById(R.id.button_get_permission)
        permissionButton.setOnClickListener{
            isStoragePermissionGranted()
        }

        //We need storage permission to write the file
        if(isStoragePermissionGranted()) {
            assignView()
            permissionButton.visibility = View.GONE
        }
        else{
            Toast.makeText(this, "Permission is required to see the information.", Toast.LENGTH_LONG).show()
            permissionButton.visibility = View.VISIBLE
        }



    }

    fun assignView(){
        val civilWar: Movie = Gson().fromJson(getMovieFromApi(), Movie::class.java)

        //Title
        val movieTitleTextView: TextView = findViewById(R.id.text_movie_name)
        movieTitleTextView.text = civilWar.title
        movieTitleTextView.visibility = View.VISIBLE

        //Image
        civilWar.image?.let {
            //make sure that the list is not null
            val movieImageAdapter = MovieImagesPagerAdapter(this, it)

            val movieImagesViewPager: ViewPager = findViewById(R.id.viewPager_movie_images)
            movieImagesViewPager.adapter = movieImageAdapter
            movieImagesViewPager.setPadding(120, 0, 120, 0)

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //after check permission
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            permissionButton.visibility = View.GONE
            assignView()
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                return true
            } else {

                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }

    /**
     * Hard code the response in this case
     * In the real case, we will receive the response from the RESTful call through libraries like Retrofit
     */
    fun getMovieFromApi(): JsonObject{
        val response = "{\n" +
                "    \"title\" : \"Civil War\",\n" +
                "    \"image\" : [\n" +
                "        \"http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2\",\n" +
                "        \"http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2\",\n" +
                "        \"http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2\"\n" +
                "    ]\n" +
                "}"

        return JsonParser().parse(response).asJsonObject
    }
}
