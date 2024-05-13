package com.example.demoappchat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private val sharedPrefFile = "com.example.demoappchat.sharedprefs"
    // Lưu tên người dùng và mật khẩu vào SharedPreferences
    private fun saveCredentials(username: String, password: String) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USERNAME", username)
        editor.putString("PASSWORD", password)
        editor.apply()
    }

    // Kiểm tra và tự động đăng nhập từ SharedPreferences
    private fun autoLogin() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")
        val password = sharedPreferences.getString("PASSWORD", "")
        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            loginUser(username, password)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        autoLogin()
        // Ánh xạ các view từ layout
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.passWord)
        loginButton = findViewById(R.id.btn_login)
        forgotPasswordTextView = findViewById(R.id.forgot_password)

        // Xử lý sự kiện khi nhấn nút Đăng nhập
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Gọi hàm đăng nhập
            loginUser(email, password)
            Log.d("TAG", "onCreate: $email,$password")
        }

        // Xử lý sự kiện khi nhấn vào Quên mật khẩu
        forgotPasswordTextView.setOnClickListener {
            // Xử lý logic cho việc quên mật khẩu
        }
    }


    private fun loginUser(username: String, password: String) {
        // Tạo request body từ thông tin người dùng
        val requestBody = LoginInfo(username, password)

        // Gọi API đăng nhập bằng Retrofit
        val call: Call<LoginResponse> = RetrofitClient.instance.login(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    // Lưu thông tin đăng nhập vào SharedPreferences
                    val sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.apply()
                    saveCredentials(username,password)
                    val user_id = response.body()?.user_id
                    Toast.makeText(this@LoginActivity, "Xin chào, $username!", Toast.LENGTH_SHORT).show()

                    // Chuyển đến màn hình chính
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                    finish() // Kết thúc activity hiện tại
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Xử lý khi gặp lỗi kết nối hoặc lỗi server
                Toast.makeText(this@LoginActivity, "Failed to connect to server: ${t.message}", Toast.LENGTH_SHORT).show()
                // Log thông tin chi tiết về lỗi
                Log.e("LoginActivity", "Failed to connect to server", t)
            }
        })
    }


}
