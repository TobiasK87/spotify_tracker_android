package com.example.spotifytracker;

import com.google.gson.annotations.SerializedName;

public class TokenRequestBody {
    @SerializedName("client_id")
    private String clientId;

    @SerializedName("redirect_uri")
    private String redirectUri;

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("code")
    private String code;

    @SerializedName("code_verifier")
    private String codeVerifier;

    // Constructor
    public TokenRequestBody(String clientId, String redirectUri, String grantType, String code, String codeVerifier) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
        this.code = code;
        this.codeVerifier = codeVerifier;
    }

    // Getters and setters
    // Depending on your requirements, you may or may not need getters and setters for this class
}

