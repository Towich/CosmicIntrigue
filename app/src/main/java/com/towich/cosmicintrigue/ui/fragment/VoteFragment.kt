package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.databinding.FragmentVoteBinding
import com.towich.cosmicintrigue.ui.adapters.VoteAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class VoteFragment : Fragment() {

    private var _binding: FragmentVoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rec.adapter = VoteAdapter({},arrayListOf(Player(2,"user"),Player(3,"123")),2)//TODO
        binding.rec.layoutManager = LinearLayoutManager(context)
        binding.button5.setOnClickListener{
            findNavController().navigate(R.id.action_MapFragment_to_VoteFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}