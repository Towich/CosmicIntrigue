package com.towich.cosmicintrigue.ui.fragment.tasks

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentClikerBinding
import com.towich.cosmicintrigue.databinding.FragmentDeathBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.DeathViewModel

// навигацию начать  с этого тестирования для тестирования
class ClikerFragment : Fragment() {

    private lateinit var clickButton: Button
    private var clicksCount = 0
    private var clickCooldown = false
    private var _binding: FragmentClikerBinding? = null
    private val binding get() = _binding!!

//    private val deathViewModel : DeathViewModel by viewModels{
//        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClikerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cliker()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun cliker() {
        clickButton = binding.taskSuccessButton
        binding.taskSuccessButton.setOnClickListener {
            if (clicksCount >= 10){
                findNavController().navigate(R.id.action_clikerFragment_to_MapFragment)
            }
            if (!clickCooldown) {
                clicksCount++
                clickButton.text = "Clicked $clicksCount times"
                clickCooldown = true
                Handler().postDelayed({
                    clickCooldown = false
                }, 3000) // 3 секунды
            }
        }
    }
}
