package com.towich.cosmicintrigue.application.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.tap_one_more_time_to_exit), Toast.LENGTH_SHORT)
            .show()

        Handler(Looper.getMainLooper()).postDelayed(
            { doubleBackToExitPressedOnce = false },
            2000
        )
    }
}