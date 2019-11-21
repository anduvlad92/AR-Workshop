package com.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MyArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as MyArFragment

        arFragment
            .setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                arFragment.onAddNewAndy(hitResult,plane)
            }

    }
}
