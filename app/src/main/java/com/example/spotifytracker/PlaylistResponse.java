package com.example.spotifytracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("tracks")
    private Tracks tracks;

    // Define getters and setters for the fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    // Define any other fields and methods as needed
}

class Tracks {
    @SerializedName("items")
    private List<TrackItem> items;

    // Define getters and setters for the fields

    public List<TrackItem> getItems() {
        return items;
    }

    public void setItems(List<TrackItem> items) {
        this.items = items;
    }
}

class TrackItem {
    @SerializedName("track")
    private Track track;

    // Define getters and setters for the fields

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}

class Track {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    // Define getters and setters for the fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
