package com.example.spotifytracker;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.spotifytracker.databinding.ActivityScrollingBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.android.appremote.api.PlayerApi;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScrollingActivity extends AppCompatActivity {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID; // see here on api key variables https://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files

    /* in order to get SHA1 Key, run this in PowerShell:
    keytool -list -v -keystore "C:\Users\Tobi\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android */
    // private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";
    private static final String REDIRECT_URI = "http://localhost:8080";
    private final String a = "mainActivity";
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi mPlayerApi;
    private TextView mTextViewSongs;
    private String mTimestamp = null;
    private Map<String, String> mSongDictionary = new HashMap<>();
    private static ArrayList<String> mSongs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.spotifytracker.databinding.ActivityScrollingBinding binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        /* Find Tablelayout defined in activity_scrolling.xml */
        TableLayout tl = (TableLayout) findViewById(R.id.TableLayout);
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */

        TextView t = new TextView(this);
        t.setTextSize(16);
        t.setText("Song");
        //t.setTextColor(getResources().getColor(R.color.black));
        t.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        t.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);

        TextView t2 = new TextView(this);
        t2.setTextSize(16);
        t2.setText("Last played");
        //t.setTextColor(getResources().getColor(R.color.black));
        t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        t2.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);

        Button b3 = new Button(this);
        b3.setTextSize(16);
        b3.setText("Dynamic Button 3");
        b3.setTextColor(getResources().getColor(R.color.white));
        b3.setBackgroundColor(getResources().getColor(R.color.purple_700));
        b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        //b3.setWidth(0);
        b3.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);
        //b3.setTextSize(R.dimen.text_margin);

        /* Add Button to row. */
        tr.addView(t);
        tr.addView(t2);
        tr.addView(b3);



        /* this was the button which should create the snackbar
        Button button = (Button) findViewById(R.id.button);
        mTextViewSongs = (TextView) findViewById(R.id.songs) ;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mSongs.size() > 0) {
                    Log.d("mainActivity", "Song: " + mSongs.get(0));
                    String track_str = mSongs.get(mSongs.size() - 1);
                    Snackbar.make(v, "Currently playing: " + track_str, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                Log.d("mainActivity", Integer.toString(mSongs.size()));
                if (mSongs.size() > 0){
                    String track_str = "";
                    for(int i = mSongs.size()-1; i>=0; i--) {
                        track_str = track_str + mSongs.get(i) + "\n";

                    }
                    mTextViewSongs.setText(track_str);
                    // Snackbar.make(v, "Songs played (reversed order): " + track_str, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });
        */


        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                /*
                CallResult<PlayerState> playerStateCall = mSpotifyAppRemote.getPlayerApi().getPlayerState();
                Result<PlayerState> playerStateResult = playerStateCall.await(10, TimeUnit.SECONDS);
                if (playerStateResult.isSuccessful()) {
                    PlayerState playerState = playerStateResult.getData();
                    Track track = playerState.track;
                    String track_str = track.name + " by " + track.artist.name;
                    Snackbar.make(view, track_str, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Throwable error = playerStateResult.getError();
                    Log.e("mainActivity", "Snackbar error: " + error.toString());
                }
                */

                mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                            final Track track = playerState.track;
                            if (track != null) {
                                final String track_str = track.name + " by " + track.artist.name;
                                mSongs.add(track_str);
                            }
                        });

                mPlayerApi = mSpotifyAppRemote.getPlayerApi();
                // mPlayerApi.pause();
                String s = mSpotifyAppRemote.getPlayerApi().getPlayerState().toString();
                Log.d("mainActinvity", "s is " + s);

                /*
                Subscription subscription = mPlayerApi.subscribeToPlayerState();
                subscription.setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    public void onEvent(PlayerState playerState) {
                        if(playerState.track.uri.equals(uri) && !playerState.isPaused) {
                            log("Track is ready, seeking into song");
                            subscription.cancel();
                            appRemote.getPlayerApi().seekTo(duration / 3);
                        }
                    }
                });
                */


                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("mainActivity", Integer.toString(mSongs.size()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // copied from https://developer.spotify.com/documentation/android/tutorials/getting-started
    @Override
    protected void onStart() {
        super.onStart();
        // Set the connection parameters

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {
        // Play a playlist
        //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
        // mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:5VWSaK5iRxg6ckUea97Cgy");
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:3FoxypbhZB8j5XivI8wQqU");

        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
            String uri = playerState.track.uri;
            /* This is a super dirty implementation to jump to the next song which does not work well at all
            if(uri != "6dJ2mSRaKE9ctYw9qWNGWQ"){
                try {
                    TimeUnit.SECONDS.sleep((long) 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mSpotifyAppRemote.getPlayerApi().skipNext();
                Log.d(a, "Skipping track: " + playerState.track.name + " with uri " + uri);
            }
            */
        });
        // would work mSpotifyAppRemote.getPlayerApi().skipToIndex("spotify:playlist:3FoxypbhZB8j5XivI8wQqU", 2);

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState() // maybe use getPlayerState instead? https://spotify.github.io/android-sdk/app-remote-lib/docs/com/spotify/android/appremote/api/PlayerApi.html#getPlayerState--
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    final long playbackPosition = playerState.playbackPosition;
                    if (track != null) {
                        String uri = track.uri;
                        Log.d("MainActivity", "in onStop:" + track.name + " by " + track.artist.name + " with uri " + uri);
                        String track_str = track.artist.name + " - " + track.name;

                        if(mSongDictionary.get(uri) == null) {
                            addRow(track_str, "last played", uri);
                        }

                        // Log.d(a, Boolean.toString(uri==null));
                        // Log.d(a, "Map is empty: "  + Boolean.toString(mSongDictionary.values().isEmpty()));


                        if(mSongDictionary.get(uri) == null);{
                            if (mTimestamp == null) {
                                mTimestamp = String.valueOf(System.currentTimeMillis());
                            } else {
                                mTimestamp = String.valueOf(System.currentTimeMillis() - Long.parseLong(mTimestamp));
                            }
                            // track_str = track_str + "stopped after: "+ mTimestamp;
                            mSongDictionary.put(uri, track_str);
                        }
                        /*
                        String all_tracks = "";
                        //Log.d("mainactivity", mSongDictionary.values().toString());
                        for(String tracks: mSongDictionary.values()){

                            all_tracks = all_tracks + tracks + "\n";
                        }
                        // String track_str = mTextViewSongs.getText() + "\n" +  track.name + " by " + track.artist.name;
                        //mTextViewSongs.setText(track_str);
                        mTextViewSongs.setText(all_tracks);
                        */
                    }
                });

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerContext().
                setEventCallback(playerContext -> {
                    String playlistUri = playerContext.uri;
                    String playlistTitle = playerContext.title;
                    String playlistSubTitle = playerContext.subtitle;

                    Log.d(a, "playlistinfo: " + playlistTitle + " " + playlistSubTitle + " " + playlistUri);
                });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void addRow(String song, String last_played, String uri){

        /* Find Tablelayout defined in activity_scrolling.xml */
        TableLayout tl = (TableLayout) findViewById(R.id.TableLayout);
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */

        TextView songTextView = new TextView(this);
        songTextView.setTextSize(16);
        songTextView.setText(song);
        //t.setTextColor(getResources().getColor(R.color.black));
        songTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        songTextView.setWidth(0);
        songTextView.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);

        TextView lastPlayedTextView = new TextView(this);
        lastPlayedTextView.setTextSize(16);
        lastPlayedTextView.setText(last_played);
        //t.setTextColor(getResources().getColor(R.color.black));
        lastPlayedTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        lastPlayedTextView.setWidth(0);
        lastPlayedTextView.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);

        Button playButton = new Button(this);
        playButton.setTextSize(16);
        playButton.setText("play");
        playButton.setTextColor(getResources().getColor(R.color.white));
        playButton.setBackgroundColor(getResources().getColor(R.color.purple_700));
        playButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        playButton.setWidth(0);
        playButton.setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().play(uri);
            }

        });
        //b3.setTextSize(R.dimen.text_margin);

        /* Add Button to row. */
        tr.addView(songTextView);
        tr.addView(lastPlayedTextView);
        tr.addView(playButton);
        /* Add row to TableLayout. */
        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }
}