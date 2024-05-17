package com.towich.cosmicintrigue.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.databinding.ActivityMapsBinding
import com.towich.cosmicintrigue.ui.util.App
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import javax.inject.Inject


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    @Inject
    lateinit var mainRepository: MainRepository

    private var compositeDisposable = CompositeDisposable()
    private var doubleBackToExitPressedOnce = false
    private var reconnectJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as App).appComponent.inject(this)
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