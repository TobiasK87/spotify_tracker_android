package com.example.spotifytracker;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyApiService {
    @GET("v1/playlists/{playlist_id}")
    Call<PlaylistResponse> getPlaylist(
            @Header("Authorization") String accessToken,
            @Path("playlist_id") String playlistId
    );

    @POST("api/token")
    Call<TokenResponse> getAccessToken(@Body TokenRequestBody requestBody);
}
