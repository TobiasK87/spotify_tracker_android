package com.example.spotifytracker;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import com.example.spotifytracker.databinding.ActivitySpotifyLoginBinding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyLoginActivity extends Activity {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID; // see here on api key variables https://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String AUTHORIZATION_URL = "https://accounts.spotify.com/authorize";
    private static final String BASE_URL = "https://api.spotify.com/";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "user-read-private user-read-email"; // Add required scopes
    private static final String activity = "SpotifyLoginActivity";
    private static final String SCOPES = "user-read-private user-read-email"; // Add your required scopes
    private SpotifyApiService service;
    private String codeVerifier; // Declare codeVerifier as a field

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);

        // Generate codeVerifier
        codeVerifier = PKCEUtil.generateCodeVerifier();

        // Initialize Retrofit service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SpotifyApiService.class);
        webView = findViewById(R.id.webView);
        setupWebView();

        String codeVerifier = PKCEUtil.generateCodeVerifier();
        startAuthorization(codeVerifier);
    }

    private void setupWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(REDIRECT_URI)) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    String state = uri.getQueryParameter("state");

                    // Check state if necessary

                    if (code != null) {
                        exchangeCodeForToken(code, codeVerifier);
                    }
                    Log.d("SpotifyLoginActivity", "Callback URL: " + url);
                    return true;
                }
                return false;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void startAuthorization(String codeVerifier) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("response_type", RESPONSE_TYPE);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("code_challenge_method", "S256");
        params.put("code_challenge", PKCEUtil.generateCodeChallenge(codeVerifier));
        params.put("scope", SCOPES);

        StringBuilder sb = new StringBuilder(AUTHORIZATION_URL);
        sb.append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(Uri.encode(entry.getValue()));
            sb.append("&");
        }

        webView.loadUrl(sb.toString());
    }

    private String buildAuthorizationUrl() {
        // Generate code verifier and code challenge
        String codeVerifier = PKCEUtil.generateCodeVerifier();
        String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);

        // Construct authorization URL with code challenge
        return "https://accounts.spotify.com/authorize" +
                "?client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + REDIRECT_URI +
                "&code_challenge_method=S256" +
                "&code_challenge=" + codeChallenge +
                "&scope=" + SCOPE;
    }


    private String getCodeFromCallbackUrl(String callbackUrl) {
        // Extract authorization code from callback URL
        // You need to implement this method based on your callback URL structure
        return null;
    }

    private void exchangeCodeForToken(String code, String codeVerifier) {
        TokenRequestBody requestBody = new TokenRequestBody(
                CLIENT_ID,
                REDIRECT_URI,
                "authorization_code",
                code,
                codeVerifier
        );

        // Retrofit service call to exchange code for token
        // Assuming 'service' is your Retrofit service interface
        Call<TokenResponse> call = service.getAccessToken(requestBody);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse tokenResponse = response.body();
                    String accessToken = tokenResponse.getAccessToken();

                    // Pass the access token to MainActivity
                    startScrollingActivity(accessToken);
                } else {
                    // Handle error
                    Log.e("SpotifyLoginActivity", "Token exchange failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("SpotifyLoginActivity", "Token exchange failed", t);
            }
        });
    }

    private void startScrollingActivity(String accessToken) {
        // Start MainActivity and pass the access token
        // You need to implement this method based on your ScrollingActivity class
        Intent intent = new Intent(SpotifyLoginActivity.this, ScrollingActivity.class);
        intent.putExtra("ACCESS_TOKEN", accessToken);
        startActivity(intent);
        finish();
    }
}
