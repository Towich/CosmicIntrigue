package com.towich.cosmicintrigue.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.databinding.FragmentLoginBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import com.towich.cosmicintrigue.ui.viewmodel.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        (requireActivity().applicationContext as App).appComponent.viewModelsFactory()
    }

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
        connectToServerViaWebSocket()
        initObservers()
    }

    private fun initButtons(){
        binding.loginButton.setOnClickListener {
            loginViewModel.sendLogin(binding.loginEditText.text.toString())
        }

        binding.statusRefreshButton.setOnClickListener {
            connectToServerViaWebSocket()
        }
    }

    private fun initObservers(){
        val userObserver = Observer<Player> { player ->
            if (player.id != null) {
                loginViewModel.saveCurrentPlayer(player = player)
                findNavController().navigate(R.id.action_Login_to_WRoom)
            }
        }

        loginViewModel.currentPlayer.observe(viewLifecycleOwner, userObserver)
    }

    private fun connectToServerViaWebSocket() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            compositeDisposable = CompositeDisposable()
        }

        loginViewModel.initGeoPositionsStompClient(
            compositeDisposable = compositeDisposable,
            onOpened = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.GREEN)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.connected)
                    Toast.makeText(
                        requireContext().applicationContext,
                        getString(R.string.connection_opened),
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            onError = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.RED)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.error)
                    Toast.makeText(
                        requireContext().applicationContext,
                        "${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            onFailedServerHeartbeat = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.RED)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.error)
                }
            },
            onClosed = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.GRAY)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.not_connected)
                    Toast.makeText(
                        requireContext().applicationContext,
                        getString(R.string.connection_closed),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}