package com.example.cftandroidtestwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cftandroidtestwork.databinding.ActivityMainBinding
import com.example.cftandroidtestwork.views.currencylist.CurrencyList
import com.google.android.material.tabs.TabLayoutMediator


val tabsArray = listOf("Валюты", "Конвертер")
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsArray[position]
        }.attach()
    }
}