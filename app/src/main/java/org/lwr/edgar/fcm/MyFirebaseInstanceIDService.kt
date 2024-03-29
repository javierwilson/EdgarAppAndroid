/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lwr.edgar.fcm

import android.util.Log
import android.widget.Toast

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.sendbird.android.SendBird


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        SendBird.registerPushTokenForCurrentUser(token, SendBird.RegisterPushTokenWithStatusHandler { pushTokenRegistrationStatus, e ->
            if (e != null) {
                Toast.makeText(this@MyFirebaseInstanceIDService, "" + e.code + ":" + e.message, Toast.LENGTH_SHORT).show()
                return@RegisterPushTokenWithStatusHandler
            }

            if (pushTokenRegistrationStatus == SendBird.PushTokenRegistrationStatus.PENDING) {
                Toast.makeText(this@MyFirebaseInstanceIDService, "Connection required to register push token.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {

        private val TAG = "MyFirebaseIIDService"
    }
}