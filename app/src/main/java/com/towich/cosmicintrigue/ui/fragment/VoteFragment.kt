package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS
import com.towich.cosmicintrigue.databinding.FragmentVoteBinding
import com.towich.cosmicintrigue.ui.adapters.VoteAdapter
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.VoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class VoteFragment : Fragment() {

    private var _binding: FragmentVoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val voteViewModel: VoteViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }

    private var adapter: VoteAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VoteAdapter(
            { a: Long? -> voteViewModel.sendPlayerModelToKick(a ?: -1) },
            voteViewModel.getUserId()
        )

        binding.progressBar.max = 100
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(context)

        voteViewModel.getVotes(
            onFinished = { a: List<Pair<Long?, Int>> ->
                adapter?.endVote(a)
                if(_binding != null)
                    binding.VoteButton.visibility = View.VISIBLE
            },
            onTick = { a: Long ->
                if(_binding != null)
                    binding.progressBar.progress = (a * 100 / VOTE_TIMER_MILIS).toInt()
            }
        )

        binding.VoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_Vote_to_Map)
        }

        /*
        voteViewModel.setDeathCallback {
            findNavController().navigate(R.id.action_Vote_to_Death)
        }
         */

        initObservers()

        voteViewModel.subscribeVoteTopic { kickedPlayer ->
            Log.i("VoteFragment", "VoteFragment player ${kickedPlayer.login}")
            val currPlayerId = voteViewModel.getUserId()

            if (currPlayerId == kickedPlayer.id && _binding != null) {
                findNavController().navigate(R.id.action_Vote_to_Death)
            } else if (_binding != null) {
                binding.rec.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.VoteButton.visibility = View.VISIBLE
                binding.kickedPlayerTextView.visibility = View.VISIBLE
                binding.kickedPlayerTextView.text =
                    if (kickedPlayer.id != -1L)
                        "Выгнали: ${kickedPlayer.login}"
                    else
                        "Никого не выгнали"
            }
        }

        voteViewModel.subscribeGameStateTopic { state ->
            Log.i("MapFragment", "Game Topic | Game state = ${state.gameState}")
            when (state.gameState) {
                // Innocents wins
                3 -> {
                    voteViewModel.setWinners(innocentsWins = true)
                    findNavController().navigate(R.id.action_VoteFragment_to_FinalFragment)
                }

                // Imposters wins
                4 -> {
                    voteViewModel.setWinners(innocentsWins = false)
                    findNavController().navigate(R.id.action_VoteFragment_to_FinalFragment)
                }
            }
        }

        voteViewModel.getAliveUsers()
    }

    private fun initObservers() {
        val playersToVoteObserver = Observer<List<Player>> { playersToVote ->
            // Update the UI
            for (player in playersToVote) {
                adapter?.users?.add(
                    VoteAdapter.AdaptedPlayer(
                        player.id,
                        player.login,
                        player.id == voteViewModel.getUserId(),
                        false,
                        -1
                    )
                )
            }
            adapter?.notifyDataSetChanged()
        }

        voteViewModel.playersToVote.observe(viewLifecycleOwner, playersToVoteObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}