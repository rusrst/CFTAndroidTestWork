package com.example.cftandroidtestwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cftandroidtestwork.views.CurrencyList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, CurrencyList())
                .commit()
        }
    }
}