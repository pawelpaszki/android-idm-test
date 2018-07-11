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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        AuthService authService = MobileCore.getInstance().getService(AuthService.class);
        AuthServiceConfiguration authServiceConfig = new AuthServiceConfiguration
                .AuthConfigurationBuilder()
                .withRedirectUri("com.aerogear.androidshowcase:/callback")
                .build();
        authService.init(this, authServiceConfig);
        DefaultAuthenticateOptions options = new DefaultAuthenticateOptions(this, LOGIN_RESULT_CODE);
        authService.login(options, new Callback<UserPrincipal>() {
            @Override
            public void onSuccess(UserPrincipal principal) {
                Log.d("", "Success");
            }

            @Override
            public void onError(Throwable error) {
                Log.d("", error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_RESULT_CODE) {
            AuthService authService = MobileCore.getInstance().getService(AuthService.class);
            authService.handleAuthResult(data);
        }
    }
}
