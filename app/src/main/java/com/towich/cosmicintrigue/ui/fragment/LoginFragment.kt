package com.towich.cosmicintrigue.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.application.App
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants
import com.towich.cosmicintrigue.databinding.FragmentLoginBinding
import com.towich.cosmicintrigue.ui.viewmodel.LoginViewModel
import io.reactivex.disposables.CompositeDisposable


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

    private fun initButtons() {
        binding.loginButton.setOnClickListener {
            loginViewModel.sendLogin(binding.loginEditText.text.toString())
        }

        binding.statusRefreshButton.setOnClickListener {
            connectToServerViaWebSocket()
        }

        binding.restartServerButton.setOnClickListener {
            loginViewModel.restartServer()
        }

        binding.configureServerRouteButton.setOnClickListener {

            val sharedPreferences = requireActivity().getSharedPreferences(
                Constants.APP_PREFERENCES,
                Context.MODE_PRIVATE
            )

            val txtUrl = EditText(requireContext())
            txtUrl.hint = "https://9a21-185-122-29-131.ngrok-free.app"
            txtUrl.setText(sharedPreferences.getString(Constants.BASE_URL_APP_PREFERENCES, ""))

            AlertDialog.Builder(requireContext())
                .setTitle("URL сервера")
                .setMessage("Вставьте адрес сервера:")
                .setView(txtUrl)
                .setPositiveButton(
                    "OK"
                ) { dialog, whichButton ->
                    val url = txtUrl.text.toString()
                    sharedPreferences.edit()
                        .putString(Constants.BASE_URL_APP_PREFERENCES, url)
                        .apply()

                    Toast.makeText(requireContext(), "Перезапустите приложение!", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton(
                    "Отмена"
                ) { dialog, whichButton ->

                }
                .show()


        }
    }

    private fun initObservers() {
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

                    binding.loginButton.isEnabled = true
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.primary))

                    Toast.makeText(
                        requireContext().applicationContext,
                        getString(R.string.connection_opened),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
            onError = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.RED)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.error)

                    binding.loginButton.isEnabled = false
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.gray))

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

                    binding.loginButton.isEnabled = false
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.gray))
                }
            },
            onClosed = {
                if (_binding != null) {
                    binding.statusBox.setBackgroundColor(Color.GRAY)
                    binding.statusTextView.text =
                        getString(R.string.status) + " " + getString(R.string.not_connected)

                    binding.loginButton.isEnabled = false
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.gray))

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