package com.towich.cosmicintrigue.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.WroomViewBinding
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.ReadyPlayer

class WaitAdapter(val userId : Int) :RecyclerView.Adapter<WaitAdapter.WaitViewHolder>(){
    class WaitViewHolder(val binding: WroomViewBinding):RecyclerView.ViewHolder(binding.root)
    var players : List<ReadyPlayer> = ArrayList()
    fun setReady(player: List<ReadyPlayer>){
        players = player
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WroomViewBinding.inflate(inflater, parent, false)
        return WaitViewHolder(binding)
    }
    override fun getItemCount(): Int = players.count()

    override fun onBindViewHolder(holder: WaitViewHolder, position: Int) {
        with(holder.binding)
        {
            var p = players.get(position)
            this.loginText.text = p.login
            this.readyText.text = if(p.ready) "Готов" else "Не Готов"
            if(p.id == userId)
                this.back.setBackgroundColor(Color.RED)
            this.readyText.setTextColor(if(p.ready) Color.parseColor("#FF0000") else Color.parseColor("#00FF00"))
        }
    }
}