package com.example.demos.ui.fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.demos.databinding.FragmentProfileBinding
import com.example.demos.repository.LogoutRepository
import com.example.demos.ui.*
import com.example.demos.ui.components.LoadingDialog
import com.example.demos.ui.viewmodels.LogoutViewModel
import com.example.demos.ui.viewmodels.LogoutViewModelProviderFactory
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        SessionManager.getCurentUser(requireContext()).let {
            binding.apply {
                tvName.text = it.user_name
                if (it.user_verify == 0) {
                    tvJob.visibility = View.GONE
                } else {
                    tvJob.visibility = View.VISIBLE
                }
                tvEmail.text = it.user_email
            }

            Glide.with(this).load(it.user_picture).into(binding.ivPhotoProfile)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent (activity, DetailProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnInformation.setOnClickListener{
            val intent = Intent (activity, InformationActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnSetting.setOnClickListener{
            val intent = Intent (activity, SettingActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            showLogoutDialog()
        }

        return binding.root
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            lifecycleScope.launch {
                logout()
            }
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private suspend fun logout() {
        val logoutRepository = LogoutRepository()
        val logoutViewModel = ViewModelProvider(viewModelStore, LogoutViewModelProviderFactory(logoutRepository))[LogoutViewModel::class.java]
        val loading = LoadingDialog(requireActivity())
        SessionManager.getToken(requireContext())?.let { logoutViewModel.logout(it) }
        logoutViewModel.logoutResult.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    SessionManager.clearData(requireContext())
                    loading.isDismiss()
                    showToast("${response.data?.message}")
                    val intent = Intent (activity, LoginActivity::class.java)
                    activity?.startActivity(intent)
                }
                is Resource.Error -> {
                    loading.isDismiss()
                    showToast("${response.message}")
                }
                is Resource.Loading -> {
                    loading.startLoading()
                }
            }
        })
    }

}