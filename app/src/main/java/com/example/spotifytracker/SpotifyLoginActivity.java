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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SpotifyLoginActivity extends Activity {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID; // see here on api key variables https://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "user-read-private user-read-email"; // Add required scopes
    private static final String activity = "SpotifyLoginActivity";

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);
        setupWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.startsWith(REDIRECT_URI)) {
                    handleCallback(url);
                    view.stopLoading();
                }
            }
        });
        String authorizationUrl = buildAuthorizationUrl();
        webView.loadUrl(authorizationUrl);
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

    private void handleCallback(String callbackUrl) {
        // Extract authorization code from callback URL and exchange it for access token
        String code = getCodeFromCallbackUrl(callbackUrl);
        if (code != null) {
            exchangeCodeForToken(code);
        } else {
            Log.e(activity, "Error in handleCallback" + code);
        }
    }

    private String getCodeFromCallbackUrl(String callbackUrl) {
        // Extract authorization code from callback URL
        // You need to implement this method based on your callback URL structure
        return null;
    }

    private void exchangeCodeForToken(String code) {
        // Exchange authorization code for access token
        Executor executor = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            // Make HTTP request to Spotify token endpoint to exchange code for token
            // You need to implement this part based on your networking library or method of choice
            return "access_token_here"; // Replace with actual access token
        });
        executor.execute(futureTask);
        try {
            String accessToken = futureTask.get();
            // Handle access token
            // For example, you can save it to SharedPreferences and start the next activity
            startScrollingActivity(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
        }
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
