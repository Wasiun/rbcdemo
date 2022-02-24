package com.rbc.demo.myapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.rbc.demo.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            addToBackStack(null)
            setReorderingAllowed(true)
            add<AccountFragment>(R.id.container_fragment)
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) finish()
        else super.onBackPressed()
    }
}