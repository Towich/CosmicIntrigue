package com.towich.cosmicintrigue.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.databinding.VoteViewBinding

class VoteAdapter(var onVote:(Long?)->Unit, players: List<Player>, val userId : Long) :RecyclerView.Adapter<VoteAdapter.VoteViewHolder>(){
    class VoteViewHolder(val binding: VoteViewBinding):RecyclerView.ViewHolder(binding.root)
    class AdaptedPlayer(var id:Long?, var login:String, var isUser:Boolean, var isVoted:Boolean, var numVotes: Int)


    var votedId: Int = 0
    var Users : ArrayList<AdaptedPlayer> = ArrayList()
    init{
        Users.add(AdaptedPlayer(null,"пропустить",false,true,-1))
        players.forEach{
            Users.add(AdaptedPlayer(it.id,it.login,userId == it.id,false,-1))
        }
    }

    fun endVote(Votes: List<Pair<Long?,Int>>){
        val (listA:List<Long?>, listB:List<Int>) =Votes.unzip()
        for(i in Users.indices)
        {
            val it = Users.get(i)
            it.numVotes = listB.get(listA.indexOf(it.id))
            notifyItemChanged(i)
        }
    }
    fun SetVotedId(id: Int){
        Users.get(votedId).isVoted = false
        Users.get(id).isVoted = true
        notifyItemChanged(votedId)
        notifyItemChanged(id)
        votedId = id
        onVote.invoke(Users.get(votedId).id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VoteViewBinding.inflate(inflater, parent, false)
        return VoteViewHolder(binding)
    }
    override fun getItemCount(): Int = Users.count()

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {

        with(holder.binding)
        {
            val p:AdaptedPlayer  = Users.get(position)
            this.Progressbar.max = Users.count()
            this.Name.text = p.login
            this.background.setBackgroundColor(Color.parseColor("#00FFFFFF"))
            if(p.isUser)
                this.background.setBackgroundColor(Color.parseColor("#E8EBFD"))
            if(p.isVoted)
                this.background.setBackgroundColor(Color.parseColor("#F9EBEB"))
            if(p.numVotes < 0)
            {
                this.Progressbar.visibility = View.GONE
                this.Votes.visibility = View.GONE
                //Log.d("d",position.toString() +" " + (!p.isVoted && !p.isUser).toString())
                if(!p.isVoted && !p.isUser)
                {
                    this.VoteButton.visibility = View.VISIBLE
                    this.VoteButton.setOnClickListener{
                        SetVotedId(position)
                    }
                }
                else{
                    this.VoteButton.visibility = View.GONE
                }
            }
            else
            {
                this.Progressbar.visibility = View.VISIBLE
                this.Votes.visibility = View.VISIBLE
                this.VoteButton.visibility = View.GONE
                this.Votes.text = p.numVotes.toString()
                this.Progressbar.progress = p.numVotes
            }
        }

    }
}