package com.ar.my_andy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MyArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as MyArFragment

        arFragment
            .setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            }
    }
}
