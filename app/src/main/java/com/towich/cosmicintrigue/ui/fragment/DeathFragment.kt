package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.towich.cosmicintrigue.databinding.FragmentDeathBinding
import com.towich.cosmicintrigue.ui.util.App
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}