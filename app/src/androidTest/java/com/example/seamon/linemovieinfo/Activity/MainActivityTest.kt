package com.example.seamon.linemovieinfo.Activity

import android.support.test.rule.ActivityTestRule
import android.widget.Button
import com.example.seamon.linemovieinfo.R
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @Rule @JvmField var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mActivity: MainActivity? = null

    @Before
    fun setUp() {
        mActivity = mActivityTestRule.activity
    }

    @Test
    fun testLaunch(){
        val view = mActivity?.findViewById<Button>(R.id.button_get_permission)
        assertNotNull(view)

    }

    @After
    fun tearDown() {
        mActivity = null
    }
}