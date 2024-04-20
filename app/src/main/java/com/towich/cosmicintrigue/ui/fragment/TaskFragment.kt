package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentTaskBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.RoleViewModel
import com.towich.cosmicintrigue.ui.viewmodel.TaskViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val taskViewModel : TaskViewModel by viewModels{
        (requireContext().applicationContext as App).viewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button6.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_VoteFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}