package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.twitch.profile.ProfileActivity
import kotlinx.coroutines.runBlocking


class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private lateinit var adapter: StreamsAdapter
    private lateinit var cursorResponse: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // TODO: Get Streams
        callGetStreams()
    }

    private fun callGetStreams(cursor: String? = null) {
        val twitchService = TwitchApiService(Network.createHttpClient(this))
        runBlocking {
            try {
                getAndSetStreams(twitchService, cursor)
            }catch (e: UnauthorizedException) {
                SessionManager(applicationContext).clearAccessToken()
                val refreshToken = SessionManager(applicationContext).getRefreshToken()
                try {
                    val response = refreshToken?.let { twitchService.getNewTokens(it) }
                    if (response != null) {
                        Log.i(TAG, "Desde New Token")
                        SessionManager(applicationContext).clearRefreshToken()
                        SessionManager(applicationContext).saveAccessToken(response.accessToken)
                        SessionManager(applicationContext).saveRefreshToken(response.refreshToken.toString())
                        getAndSetStreams(twitchService, cursor)
                    }
                }catch (e: UnauthorizedException) {
                    Log.i(TAG, "Redireccionando a Login")
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }
        }
    }

    //fun to call getStreams
    private suspend fun getAndSetStreams(twitchService: TwitchApiService, cursor: String?) {
        val response = twitchService.getStreams(cursor)
        if (response != null) {
            response.data?.let { adapter.setStreams(it) }
            cursorResponse = response.pagination?.cursor.toString()
        }
    }

    private fun initRecyclerView() {
        // TODO: Implement
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = StreamsAdapter(emptyList())
        recyclerView.adapter = adapter

        //validate Scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    callGetStreams(cursorResponse)
                }
            }
        })
    }

    //fun to menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    //fun to selected item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        return if (id == R.id.menu_profile) {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
            return true
        } else super.onOptionsItemSelected(item)
    }

}