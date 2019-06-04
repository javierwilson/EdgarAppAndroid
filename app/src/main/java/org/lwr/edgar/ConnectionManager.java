package org.lwr.edgar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.lwr.edgar.utils.PreferenceUtils;

public class ConnectionManager {

    public static void login(String userId, final SendBird.ConnectHandler handler) {
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (handler != null) {
                    handler.onConnected(user, e);
                }


                if (FirebaseInstanceId.getInstance().getToken() == null) return;

                System.out.println("token "+FirebaseInstanceId.getInstance().getToken());
                SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
                        new SendBird.RegisterPushTokenWithStatusHandler() {
                            @Override
                            public void onRegistered(SendBird.PushTokenRegistrationStatus status, SendBirdException e) {
                                if (e != null) {    // Error.
                                    return;
                                }
                            }
                        }
                );
            }

        });
    }

    public static void logout(final SendBird.DisconnectHandler handler) {
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                if (handler != null) {
                    handler.onDisconnected();
                }
            }
        });
    }

    public static void addConnectionManagementHandler(String handlerId, final ConnectionManagementHandler handler) {
        SendBird.addConnectionHandler(handlerId, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {
            }

            @Override
            public void onReconnectSucceeded() {
                if (handler != null) {
                    handler.onConnected(true);
                }
            }

            @Override
            public void onReconnectFailed() {
            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            if (handler != null) {
                handler.onConnected(false);
            }
        } else if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED) { // push notification or system kill
            String userId = PreferenceUtils.getUserId();
            SendBird.connect(userId, new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {
                        return;
                    }

                    if (handler != null) {
                        handler.onConnected(false);
                    }
                }
            });
        }
    }

    public static void removeConnectionManagementHandler(String handlerId) {
        SendBird.removeConnectionHandler(handlerId);
    }

    public interface ConnectionManagementHandler {
        /**
         * A callback for when connected or reconnected to refresh.
         *
         * @param reconnect Set false if connected, true if reconnected.
         */
        void onConnected(boolean reconnect);
    }
}
