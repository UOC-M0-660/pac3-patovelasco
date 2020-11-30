package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints.authorizationUrl
import edu.uoc.pac3.data.network.Endpoints.redirectUri
import edu.uoc.pac3.data.network.Network.createHttpClient
import edu.uoc.pac3.data.oauth.OAuthConstants.clientID
import edu.uoc.pac3.data.oauth.OAuthConstants.responseType
import edu.uoc.pac3.data.oauth.OAuthConstants.scopes
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.*
import java.util.*

class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"
    private val uniqueState = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    fun buildOAuthUri(): Uri {
        // TODO: Create URI
        return Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter("client_id", clientID)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("response_type", responseType)
                .appendQueryParameter("scope", scopes.joinToString(separator = " "))
                .appendQueryParameter("state", uniqueState)
                .build()
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()
        // TODO: Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d(TAG, "Authorization code >>> $code")
                                onAuthorizationCodeRetrieved(code)
                            } ?: run {
                                // User cancelled the login flow
                                Toast.makeText(applicationContext, "Error to obtain the authorization code", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {
        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE
        // TODO: Create Twitch Service
        val twitchService = TwitchApiService(createHttpClient(this))
        // TODO: Get Tokens from Twitch
        runBlocking {
            val response = twitchService.getTokens(authorizationCode)
            if (response != null) {
                // TODO: Save access token and refresh token using the SessionManager class
                SessionManager(applicationContext).saveAccessToken(response.accessToken)
                SessionManager(applicationContext).saveRefreshToken(response.refreshToken.toString())
                startActivity(Intent(applicationContext, StreamsActivity::class.java))
            }
        }
    }
}