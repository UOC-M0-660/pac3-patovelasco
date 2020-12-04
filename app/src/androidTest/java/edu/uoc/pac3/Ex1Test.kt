package edu.uoc.pac3

import android.net.Uri
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.oauth.OAuthActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Created by alex on 24/10/2020.
 */

@LargeTest
class Ex1Test {

    @Test
    fun createsOauthUri() {
        val scenario = ActivityScenario.launch(OAuthActivity::class.java)
        scenario.onActivity {
            val uri = it.buildOAuthUri()
            assert(uri != Uri.EMPTY)
            assert(uri.getQueryParameter("response_type") == "code") {
                "OAuth Flow must be Authorization Code"
            }
        }
        scenario.close()
    }

    @Test
    fun invalidAuthCodeReturnsNullTokens() {
        val scenario = ActivityScenario.launch(OAuthActivity::class.java)
        scenario.onActivity {
            runBlocking {
                val apiService = TestData.provideTwitchService(it)
                try{
                    val response = apiService.getTokens("12345")
                    assert(response?.accessToken == null) {
                        "Invalid code should return null access token"
                    }
                }catch (e: UnauthorizedException) {
                    Log.i("Ex1Test",  "Invalid code should return null access token")
                }
            }
        }
        scenario.close()
    }
}