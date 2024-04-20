package com.towich.cosmicintrigue.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.towich.cosmicintrigue.databinding.VoteViewBinding

class VoteAdapter(var onVote:(Int)->Unit, var players: List<String>) :RecyclerView.Adapter<VoteAdapter.VoteViewHolder>(){
    class VoteViewHolder(val binding: VoteViewBinding):RecyclerView.ViewHolder(binding.root)
    var voted: Int? = null
    var user: Int? = null

    fun SetVotedId(id: Int){

    }
    fun SetUserdId(id: Int){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        //holder
    }
}