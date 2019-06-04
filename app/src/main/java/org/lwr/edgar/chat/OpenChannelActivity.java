package org.lwr.edgar.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sendbird.android.ApplicationUserListQuery;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBirdException;

import org.lwr.edgar.R;

import java.util.List;

public class OpenChannelActivity extends AppCompatActivity {
    String name="", url="";
    public static final String EXTRA_OPEN_CHANNEL_URL = "OPEN_CHANNEL_URL";
    private GroupChannelListQuery mChannelListQuery;
    private ApplicationUserListQuery mUserListQuery;

    private List<String> mSelectedIds;
    private boolean mIsDistinct = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_channel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_open_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }

        /*mSelectedIds = new ArrayList<>();
        mIsDistinct = PreferenceUtils.getGroupChannelDistinct();
        mSelectedIds.add("Williams");
        mSelectedIds.add("Gabriela Lagos");

        System.out.println(mSelectedIds.toString());
        createGroupChannel(mSelectedIds, mIsDistinct);*/


        /*mChannelListQuery = GroupChannel.createMyGroupChannelListQuery();
        mChannelListQuery.setLimit(10);

        mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                for (GroupChannel user : list) {
                    System.out.println("participantes "+user.getUrl());
                    System.out.println("participantes =====");


                }

                System.out.println(list.size());
            }
        });*/


        /*mUserListQuery = SendBird.createApplicationUserListQuery();

        mUserListQuery.setLimit(15);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                for (User user : list) {
                    System.out.println("participantes "+user.getUserId());
                    System.out.println("participantes "+user.getNickname());
                    System.out.println("participantes =====");


                }
            }
        });*/



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            url = extras.getString("url");
        }

        System.out.println("url "+url);

        if (savedInstanceState == null) {
            // Load list of Open Channels
            if(!url.toString().equals("")){


                GroupChatFragment fragment = GroupChatFragment.newInstance(url);

                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.container_open_channel, fragment)
                        .addToBackStack(null)
                        .commit();


                /*OpenChatFragment fragment = OpenChatFragment.newInstance(url);

                FragmentManager manager = getSupportFragmentManager();
               // manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_open_channel, fragment)
                        .addToBackStack(null)
                        .commit();*/
            }

        }
    }


    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                System.out.println(groupChannel.getUrl());
            }
        });
    }

    interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        /*if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }*/
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
