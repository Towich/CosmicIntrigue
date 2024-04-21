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

        // Создаем таймер на 5 секунд
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Этот метод вызывается каждую секунду до истечения таймера
                Toast.makeText(requireContext(), "$millisUntilFinished", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {
                // Этот метод вызывается по истечении таймера (через 5 секунд)

                // Включаем кнопку после истечения таймера
                binding.taskCancelButton.visibility = View.GONE
                binding.taskSuccessButton.visibility = View.VISIBLE
            }
        }.start()

        binding.taskSuccessButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_VoteFragment2)
            //taskViewModel.SendLogin(binding.task.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
