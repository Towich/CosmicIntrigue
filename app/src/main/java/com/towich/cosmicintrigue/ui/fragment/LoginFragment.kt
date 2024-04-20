package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentLoginBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val loginViewModel : LoginViewModel by viewModels{
            (requireContext().applicationContext as App).viewModelFactory
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.proceed.setOnClickListener{
            loginViewModel.SendLogin(binding.Login.text.toString())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}