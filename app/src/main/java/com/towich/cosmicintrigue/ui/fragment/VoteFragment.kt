package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS
import com.towich.cosmicintrigue.databinding.FragmentVoteBinding
import com.towich.cosmicintrigue.ui.adapters.VoteAdapter
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import com.towich.cosmicintrigue.ui.viewmodel.TaskViewModel
import com.towich.cosmicintrigue.ui.viewmodel.VoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class VoteFragment : Fragment() {

    private var _binding: FragmentVoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val voteViewModel : VoteViewModel by viewModels{
        (requireContext().applicationContext as App).viewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val players = voteViewModel.getPlayers()
        val adap =  VoteAdapter({a: Int? -> voteViewModel.setVote(a)},players,voteViewModel.getUserId())
        binding.progressBar.max = 100
        binding.rec.adapter = adap
        binding.rec.layoutManager = LinearLayoutManager(context)
        voteViewModel.getVotes ({
            a: List<Pair<Int?,Int>> ->
            adap.endVote(a)
            binding.VoteButton.visibility = View.VISIBLE
        },
            {
                a:Long ->
                binding.progressBar.progress = (a * 100/ VOTE_TIMER_MILIS).toInt()
            })
        binding.VoteButton.setOnClickListener{
            findNavController().navigate(R.id.action_VoteFragment_to_MapFragment2)
        }
        voteViewModel.setDeathCallback {
            findNavController().navigate(R.id.action_VoteFragment_to_DeathFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}