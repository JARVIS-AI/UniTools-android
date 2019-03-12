package com.github.ali77gh.unitools.uI.activities;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.StringCoder;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.Locale;

public class InputLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHolder.initStatics(this);
        setContentView(R.layout.activity_input_link);
        SetupLang();

        EditText name = findViewById(R.id.text_input_link_activity);
        Button add = findViewById(R.id.btn_input_link_activity_add);
        Button cancel = findViewById(R.id.btn_input_link_activity_cancel);

        cancel.setOnClickListener(v -> finish());

        Friend.MinimalFriend minimalFriend;
        try {
            Uri data = getIntent().getData();

            String friendStrBase64 = data.toString().split("unitools/")[1];
            String friendStr = StringCoder.Decode(friendStrBase64);
            minimalFriend = new Gson().fromJson(friendStr, Friend.MinimalFriend.class);
        } catch (JsonParseException e) {
            Toast.makeText(this, getString(R.string.cant_add_friend), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Friend friend = Friend.MinimalToFull(minimalFriend);

        add.setOnClickListener(v -> {
            friend.name = name.getText().toString();
            FriendRepo.Insert(friend);
            Toast.makeText(this, getString(R.string.friend_added_successfully), Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void SetupLang() {

        String lang = UserInfoRepo.getUserInfo().LangId;

        if (lang.equals(getString(R.string.LangID))) return;

        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        recreate();
    }

}