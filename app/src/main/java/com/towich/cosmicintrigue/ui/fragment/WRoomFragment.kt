package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.ReadyPlayer
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentWroomBinding
import com.towich.cosmicintrigue.ui.adapters.VoteAdapter
import com.towich.cosmicintrigue.ui.adapters.WaitAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WRoomFragment : Fragment() {

    private var _binding: FragmentWroomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWroomBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var l = arrayListOf(ReadyPlayer(2,"user",false), ReadyPlayer(3,"123",true))
        val adap = WaitAdapter(2)
        binding.rec.adapter = adap//TODO
        binding.rec.layoutManager = LinearLayoutManager(context)
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_WRoomFragment_to_gameActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}