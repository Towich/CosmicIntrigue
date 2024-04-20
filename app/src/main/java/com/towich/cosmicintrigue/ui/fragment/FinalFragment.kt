package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentFinalBinding

class FinalFragment: Fragment(){

    private var _binding: FragmentFinalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finall.setOnClickListener {
            findNavController().navigate(R.id.action_FinalFragment_to_nav_graph)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}