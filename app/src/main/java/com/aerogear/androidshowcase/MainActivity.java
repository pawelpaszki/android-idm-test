package com.aerogear.androidshowcase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.aerogear.mobile.auth.AuthService;
import org.aerogear.mobile.auth.authenticator.DefaultAuthenticateOptions;
import org.aerogear.mobile.auth.configuration.AuthServiceConfiguration;
import org.aerogear.mobile.auth.user.UserPrincipal;
import org.aerogear.mobile.core.Callback;
import org.aerogear.mobile.core.MobileCore;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_RESULT_CODE = 1;

    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        authService = MobileCore.getInstance().getService(AuthService.class);
        AuthServiceConfiguration authServiceConfig = new AuthServiceConfiguration
                .AuthConfigurationBuilder()
                .withRedirectUri("com.aerogear.androidshowcase:/callback")
                .build();
        authService.init(this, authServiceConfig);
        DefaultAuthenticateOptions options = new DefaultAuthenticateOptions(this, LOGIN_RESULT_CODE);
        authService.login(options, new Callback<UserPrincipal>() {
            @Override
            public void onSuccess(UserPrincipal principal) {
                UserPrincipal currentUser = authService.currentUser();
                boolean hasAdminPermissions = currentUser.hasRealmRole("user_admin");
                boolean isModerator = currentUser.hasResourceRole("my_resource", "user_moderator");
                Log.d("hasAdminPermissions", String.valueOf(hasAdminPermissions));
                Log.d("isModerator", String.valueOf(isModerator));
                // do stuff here
            }

            @Override
            public void onError(Throwable error) {
                // do stuff here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_RESULT_CODE) {
            authService.handleAuthResult(data);
        }
    }

    public void logout(View view) {
        UserPrincipal currentUser = authService.currentUser();
        if (currentUser != null) {
            authService.logout(currentUser, new Callback<UserPrincipal>() {
                @Override
                public void onSuccess() {
                    Log.d("", "logout");
                    // User Logged Out Successfully and local Auth tokens were Deleted
                }

                @Override
                public void onError(Throwable error) {
                    Log.d("", "error");
                }
            });
        }
    }
}
