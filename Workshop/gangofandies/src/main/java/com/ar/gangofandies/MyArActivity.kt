package com.ar.gangofandies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MyArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragment = supportFragmentManager?.findFragmentById(R.id.arFragment) as MyArFragment

        fragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            fragment.onAddGang(hitResult, plane)
        }
    }
}
