package com.towich.cosmicintrigue.ui.fragment.tasks

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentClikerBinding

// навигацию начать  с этого тестирования для тестирования
class ClikerFragment : Fragment() {

    private lateinit var clickButton: Button
    private var clicksCount = 0
    private var clickCooldown = false
    private var _binding: FragmentClikerBinding? = null
    private val binding get() = _binding!!

    private val MAX_CLICKS = 10
    private val COOLDOWN_DURATION = 3000L
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
        setupClickListener()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun setupClickListener() {
        clickButton = binding.taskSuccessButton
        binding.taskSuccessButton.setOnClickListener {
            if (clicksCount >= MAX_CLICKS){
            findNavController().navigate(R.id.action_clikerFragment_to_MapFragment)
            }
            if (!clickCooldown) {
                incrementClickCount()
                startClickCooldown()
            }
        }
    }

    private fun incrementClickCount() {
        clicksCount++
        clickButton.text =  "Clicked $clicksCount times"
    }

    private fun startClickCooldown() {
        clickCooldown = true
        Handler(Looper.getMainLooper()).postDelayed({
            clickCooldown = false
        }, COOLDOWN_DURATION)
    }
}
