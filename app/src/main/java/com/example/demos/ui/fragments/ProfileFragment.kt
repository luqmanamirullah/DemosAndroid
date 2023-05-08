package com.example.demos.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.demos.R
import com.example.demos.databinding.FragmentHomeBinding
import com.example.demos.databinding.FragmentProfileBinding
import com.example.demos.ui.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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