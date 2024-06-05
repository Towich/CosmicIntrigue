package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.application.App
import com.towich.cosmicintrigue.databinding.FragmentFinalBinding
import com.towich.cosmicintrigue.ui.viewmodel.FinalViewModel

class FinalFragment: Fragment(){

    private var _binding: FragmentFinalBinding? = null
    private val binding get() = _binding!!

    private val finalViewModel : FinalViewModel by viewModels{
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finalButton.setOnClickListener {
            findNavController().navigate(R.id.action_Final_to_menu)
        }

        val isInnocentsWinners = finalViewModel.getWinners()
        binding.victoryText.text = when(isInnocentsWinners){
            true -> {
                "Мирные победили"
            }
            false -> {
                "Предатели победили"
            }
            else -> {
                "Ошибка"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}