package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.application.App
import com.towich.cosmicintrigue.databinding.FragmentDeathBinding
import com.towich.cosmicintrigue.ui.viewmodel.DeathViewModel

class DeathFragment : Fragment() {

    private var _binding: FragmentDeathBinding? = null
    private val binding get() = _binding!!

    private val deathViewModel : DeathViewModel by viewModels{
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeathBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deathViewModel.subscribeGameStateTopic { state ->
            Log.i("MapFragment", "Game Topic | Game state = ${state.gameState}")
            when(state.gameState){
                // Innocents wins
                3 -> {
                    deathViewModel.setWinners(innocentsWins = true)
                    findNavController().navigate(R.id.action_Death_to_Final)
                }

                // Imposters wins
                4 -> {
                    deathViewModel.setWinners(innocentsWins = false)
                    findNavController().navigate(R.id.action_Death_to_Final)
                }
            }
        }

        deathViewModel.sendEmptyToGameStateTopic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}