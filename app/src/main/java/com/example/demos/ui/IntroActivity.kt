package com.example.demos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demos.R
import com.example.demos.models.Intro
import com.example.demos.utils.Constants

private val intros = arrayOfNulls<Intro>(Constants.MAX_STEP)

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

    }
}