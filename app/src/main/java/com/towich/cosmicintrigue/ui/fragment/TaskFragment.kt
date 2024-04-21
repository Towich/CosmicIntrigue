package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentTaskBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.RoleViewModel
import com.towich.cosmicintrigue.ui.viewmodel.TaskViewModel

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel : TaskViewModel by viewModels{
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

        binding.taskSuccessButton.setOnClickListener {
            taskViewModel.onCompleteTask()
            findNavController().navigate(R.id.action_Task_to_Map)
        }
        binding.taskCancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_Task_to_Map)
        }
        taskViewModel.setOnCompleteCallback({
            binding.taskCancelButton.visibility = View.GONE
            binding.taskSuccessButton.visibility = View.VISIBLE
        }, {
                a:Long -> binding.taskText.text = a.toString()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
