package com.towich.cosmicintrigue.application.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.towich.cosmicintrigue.R

class GeoTrackingService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "geo_tracking_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("CosmicIntrique запущен")
            .setContentText("Ваше местоположение отслеживается!")
            .build()

        startForeground(1, notification)

//        object : CountDownTimer(Constants.VOTE_TIMER_MILIS, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                Log.d("GeoTrackingService", "Timer: $millisUntilFinished ms remains")
//            }
//            override fun onFinish() {
//
//            }
//        }.start()
    }

    enum class Actions {
        START, STOP
    }
}