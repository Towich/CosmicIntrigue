package com.towich.cosmicintrigue.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.FragmentWroomBinding
import com.towich.cosmicintrigue.ui.adapters.WaitAdapter
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.WRoomViewModel


class WRoomFragment : Fragment() {

    private var _binding: FragmentWroomBinding? = null

    private val binding get() = _binding!!
    private val wRoomViewModel : WRoomViewModel by viewModels{
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWroomBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = WaitAdapter(wRoomViewModel.getId())
        wRoomViewModel.players.observe(viewLifecycleOwner) {
            adapter.setReady(it)
        }
        //var l = arrayListOf(ReadyPlayer(2,"user",false), ReadyPlayer(3,"123",true))
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(context)
        binding.wroomButtonEnable.setOnClickListener {
            binding.wroomButtonEnable.visibility = View.GONE
            binding.wroomButtonDisable.visibility = View.VISIBLE
            wRoomViewModel.ready.value = true

            wRoomViewModel.toggleReadyPlayer()
            wRoomViewModel.sendPlayerInUsersTopic()
        }
        binding.wroomButtonDisable.setOnClickListener {
            binding.wroomButtonEnable.visibility = View.VISIBLE
            binding.wroomButtonDisable.visibility = View.GONE
            wRoomViewModel.ready.value = false

            wRoomViewModel.toggleReadyPlayer()
            wRoomViewModel.sendPlayerInUsersTopic()
        }
//        wRoomViewModel.setStartCallback {
//            findNavController().navigate(R.id.action_WRoom_to_game)
//        }

        wRoomViewModel.subscribeUsersTopic { players ->
            val currId = wRoomViewModel.getId()
            for(player in players){
                if(currId == player.id){
                    wRoomViewModel.updateIsImposter(player.isImposter)
                }
            }

            if(players.isNotEmpty() && players[0].isImposter != null){
                wRoomViewModel.unsubscribeUsersTopic()
                findNavController().navigate(R.id.action_WRoom_to_game)
            }
            adapter.setReady(players.asList())
        }

        wRoomViewModel.sendPlayerInUsersTopic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}