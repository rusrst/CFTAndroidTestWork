package com.example.cftandroidtestwork

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.example.cftandroidtestwork.data.contract.HasCustomTitle
import com.example.cftandroidtestwork.data.contract.Navigator
import com.example.cftandroidtestwork.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.TimeUnit


val tabsArray = listOf("Валюты", "Конвертер")
class MainActivity : AppCompatActivity(), Navigator {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsArray[position]
        }.attach()
        if (!isWorkSchedule("myWork")) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val data = Data.Builder()
            data.putString("url", "https://www.cbr-xml-daily.ru/daily_json.js")
            val work = PeriodicWorkRequest
                .Builder(CurrencyWorkManager::class.java, 4, TimeUnit.HOURS)
                .addTag("myWork")
                .setConstraints(constraints)
                .setInputData(data.build())
                .apply {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        this.setInitialDelay(10, TimeUnit.SECONDS)
                }
                .build()
            WorkManager.getInstance(this.applicationContext)
                .enqueueUniquePeriodicWork("myWork", ExistingPeriodicWorkPolicy.REPLACE, work)
        }
    }
    override fun updateUi(id: Int) {
        val fragment = supportFragmentManager.findFragmentByTag("f$id")
        if (fragment is HasCustomTitle) supportActionBar?.title = fragment.getTitle()
        else supportActionBar?.title = "------"
    }

    private fun isWorkSchedule(tag: String): Boolean{
        val instance = WorkManager.getInstance(this.applicationContext)
        val statuses = instance.getWorkInfosByTag(tag)
        var running = false
        val workInfoList = statuses.get()
        for (workInfo in workInfoList){
            val state = workInfo.state
            running = (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED)
        if (running) return true
        }
        return running
    }
}