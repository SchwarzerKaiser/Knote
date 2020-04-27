package com.leewilson.knote.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.bumptech.glide.RequestManager
import com.leewilson.knote.R
import com.leewilson.knote.ui.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var myHandler: Handler

    private val splashTime = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        requestManager.load(R.drawable.knote_pencil_splash).into(splash_img)

        myHandler = Handler()
        myHandler.postDelayed({
            goToMainActivity()
        }, splashTime)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}