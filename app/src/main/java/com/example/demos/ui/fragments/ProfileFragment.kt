package com.example.demos.ui.fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.demos.databinding.FragmentProfileBinding

import com.example.demos.ui.*
import com.example.demos.ui.viewmodels.LoginViewModel
import com.example.demos.utils.SessionManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: LoginViewModel

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
            logout()
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun logout() {
        val intent = Intent (activity, LoginActivity::class.java)
        activity?.startActivity(intent)
    }

}