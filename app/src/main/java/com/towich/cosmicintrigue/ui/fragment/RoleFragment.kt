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
import com.towich.cosmicintrigue.databinding.FragmentRoleBinding
import com.towich.cosmicintrigue.ui.viewmodel.RoleViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RoleFragment : Fragment() {

    private var _binding: FragmentRoleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val roleViewModel : RoleViewModel by viewModels{
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRoleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val role = roleViewModel.getRole()
        binding.roleName.text = if(role) "Предатель" else "Мирный"
        binding.RoleText.text = if(role) "Убить всех " else "Выполнить задания или найти предателя"
        binding.roleButton.setOnClickListener {
            findNavController().navigate(R.id.action_Role_to_Map)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}