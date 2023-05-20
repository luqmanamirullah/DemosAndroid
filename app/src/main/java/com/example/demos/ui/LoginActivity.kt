package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.demos.R
import com.example.demos.databinding.ActivityLoginBinding
import com.example.demos.models.login.Login
import com.example.demos.repository.LoginRepository
import com.example.demos.ui.components.LoadingDialog
import com.example.demos.ui.viewmodels.LoginViewModel
import com.example.demos.ui.viewmodels.LoginViewModelProviderFactory
import com.example.demos.utils.GoogleSignInHelper
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var txtForgetPassword: TextView
    private lateinit var txtSignUp: TextView
    lateinit var loginViewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding

    private val RC_SIGN_IN = 123
    private val googleSignInHelper by lazy {
        GoogleSignInHelper(this, RC_SIGN_IN)
    }

    private val loading = LoadingDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginRepository = LoginRepository()
        loginViewModel = ViewModelProvider(this, LoginViewModelProviderFactory(loginRepository))[LoginViewModel::class.java]

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()){
            navigateToHome()
        }

        loginViewModel.loginResult.observe(this){
            when(it){
                is Resource.Success -> {
                    stopLoading()
                    it.data?.let { res -> SessionManager.saveCurrentUser(this, res.data) }
                    processLogin(it.data)
                }
                is Resource.Error -> {
                    stopLoading()
                    processError(it.message)
                }
                is Resource.Loading -> {
                    showLoading()
                }
                else -> {
                    stopLoading()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
        binding.txtForgotPassword.setOnClickListener {
            intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        binding.txtSignUp.setOnClickListener {
            register()
        }

        binding.btnGoogle.setOnClickListener {
            googleSignInHelper.login()
        }

        // Initialize the TextView
        txtForgetPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)

        // Set underline text
        setUnderlineText1("Forgot Password")
        setUnderlineText2("Sign Up")

        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val account = googleSignInHelper.handleSignInResult(data)
            Log.e("Login Google", "${account}")
            if (account !== null){
                lifecycleScope.launch {
                    loginViewModel.loginGoogle(account.email, account.id, account.displayName, account.photoUrl.toString())
                }
            } else {
                showToast("Sign in with google is failed")
            }
        }
    }

    private fun navigateToHome(){
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finishAffinity()
    }

    private suspend fun login(){
        val email = binding.etEmail.text.toString()
        val pwd = binding.etPass.text.toString()
        loginViewModel.login(email, pwd)
    }

    private fun register(){

    }

    private fun showLoading(){
        loading.startLoading()
    }

    private fun stopLoading(){
        loading.isDismiss()
    }

    private fun processLogin(data: Login?){
        showToast("Berhasil Login: ${data?.message}" )
        if (!data?.access_token.isNullOrEmpty() && !data?.token_type.isNullOrEmpty()){
            data?.let {
                if (it.token_type != null && it.access_token !== null) {
                    SessionManager.saveAuthToken(this, it.token_type + " " + it.access_token)
                }
                navigateToHome()
            }
        }
    }

    private fun processError(message: String?){
        showToast("Error: $message")
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUnderlineText1(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtForgetPassword.text = spannableString
    }

    private fun setUnderlineText2(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtSignUp.text = spannableString
    }
}