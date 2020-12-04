package edu.uoc.pac3.twitch.profile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.oauth.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"
    private lateinit var username: TextView
    private lateinit var views: TextView
    private lateinit var userDescription: TextView
    private lateinit var imageProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        username = findViewById(R.id.userNameTextView)
        views = findViewById(R.id.viewsText)
        imageProfile = findViewById(R.id.imageView)
        userDescription = findViewById(R.id.userDescriptionEditText)
        callGetUser()

        //update button
        updateDescriptionButton.setOnClickListener {
            callUpdateDescription()
        }

        //login button
        logoutButton.setOnClickListener {
            SessionManager(applicationContext).clearAccessToken()
            SessionManager(applicationContext).clearRefreshToken()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    //fun to call getUser
    private fun callGetUser() {
        val twitchService = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            val response = twitchService.getUser()
            if (response != null) {
                Glide.with(applicationContext)
                        .load(response.profileImageUrl)
                        .into(imageProfile)
                views.text = response.viewCount
                username.text = response.userName
                userDescription.text = response.description
            }
        }
    }

    //fun to Update Description
    private fun callUpdateDescription() {
        val twitchService = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            val response = twitchService.updateUserDescription(userDescription.text.toString())
            if (response != null) {
                Toast.makeText(applicationContext, "User description updated", Toast.LENGTH_LONG).show()
            }
        }
    }
}