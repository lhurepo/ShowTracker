package model;

import org.json.JSONObject;
import persistence.Writable;

// A class for an individual show
public class Show implements Writable {

    String name;
    Float score;
    int episodes;
    int progress;
    String status;
    String genre;

    // EFFECTS: Constructs a show
    public Show(String name, Float score, int episodes, int progress, String status, String genre) {
        this.name = name;
        this.score = score;
        this.episodes = episodes;
        this.progress = progress;
        this.status = status;
        this.genre = genre;

    }

    // getters

    public String getName() {
        return this.name;
    }

    public Float getScore() {
        return this.score;
    }

    public int getEpisodes() {
        return this.episodes;
    }

    public int getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }

    public String getGenre() {
        return genre;
    }


    // EFFECTS: returns JSON representation of this show
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("score", score);
        json.put("episodes", episodes);
        json.put("progress", progress);
        json.put("status", status);
        json.put("genre", genre);
        return json;
    }

}

