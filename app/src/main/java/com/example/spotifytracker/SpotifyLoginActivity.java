package com.example.spotifytracker;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

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

public class SpotifyLoginActivity extends Activity {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID; // see here on api key variables https://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "user-read-private user-read-email"; // Add required scopes

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);

        webView = findViewById(R.id.webView);
        setupWebView();
        startAuthorization();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(REDIRECT_URI)) {
                    handleCallback(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void startAuthorization() {
        String url = AUTH_URL + "?client_id=" + CLIENT_ID + "&response_type=" + RESPONSE_TYPE +
                "&redirect_uri=" + REDIRECT_URI + "&scope=" + SCOPE;
        Log.d("SpotifyLoginActivitylog", url);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.9999.99 Mobile Safari/537.36");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private void handleCallback(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);
        String code = uri.getQueryParameter("code");
        if (code != null) {
            // Exchange the authorization code for an access token
            // Implement this part according to Spotify's API documentation
            // You can use Retrofit or Volley to make a POST request to the token endpoint
            // and handle the response to get the access token
        } else {
            // Handle error
        }
    }
}
