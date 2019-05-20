package com.example.seamon.linemovieinfo.Model

import android.net.Uri
import com.google.gson.JsonObject
import org.json.JSONObject

/**
 * Movie object
 * I created this object mainly for Gson to do the work when pulling the data
 */
class Movie(
    var title: String? = null,
    var image: List<String>? = null
)