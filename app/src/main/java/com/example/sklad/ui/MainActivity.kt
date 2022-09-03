package com.example.sklad.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sklad.data.ViewPagerAdapter
import com.example.sklad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val list = listOf(
            JustList(),
            ListFragment()
        )
        val myAdapter = ViewPagerAdapter(list, supportFragmentManager, lifecycle)

        val viewPager = vb.viewPager
        viewPager.adapter = myAdapter
    }
}