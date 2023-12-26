package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import org.json.JSONArray;
import org.json.JSONObject;
import model.Show;
import model.ShowList;

// Represents a reader that reads show data from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads show list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ShowList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        parseShowListStats(jsonObject);
        return parseShowList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses show list from JSON object and returns it
    private ShowList parseShowList(JSONObject jsonObject) {
        String name = jsonObject.getString("listName");
        ShowList showList = new ShowList(name);
        addShows(showList, jsonObject);
        return showList;
    }

    // MODIFIES: showList
    // EFFECTS: parses shows from JSON object and adds them to show list
    private void addShows(ShowList showList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("shows");
        for (Object json : jsonArray) {
            JSONObject nextShow = (JSONObject) json;
            addShow(showList, nextShow);
        }
    }


    // MODIFIES: showList
    // EFFECTS: parses show from JSON object and adds it to show list
    private void addShow(ShowList showList, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Float score = jsonObject.getFloat("score");
        int episodes = jsonObject.getInt("episodes");
        int progress = jsonObject.getInt("progress");
        String status = jsonObject.getString("status");
        String genre = jsonObject.getString("genre");
        Show show = new Show(name, score, episodes, progress, status, genre);
        showList.addShow(show);
    }

    // EFFECTS: parses static fields from JSON object
    private void parseShowListStats(JSONObject jsonObject) {
        int totalShowsWatched = jsonObject.getInt("totalShowsWatched");
        int totalEpisodesWatched = jsonObject.getInt("totalEpisodesWatched");
        int numCompleted = jsonObject.getInt("numCompleted");
        int numWatching = jsonObject.getInt("numWatching");
        int numPlanToWatch = jsonObject.getInt("numPlanToWatch");
        float meanScore = jsonObject.getFloat("meanScore");
        ShowList.setTotalShowsWatched(totalShowsWatched);
        ShowList.setTotalEpisodesWatched(totalEpisodesWatched);
        ShowList.setNumCompleted(numCompleted);
        ShowList.setNumWatching(numWatching);
        ShowList.setNumPlanToWatch(numPlanToWatch);
        ShowList.setMeanScore(meanScore);
    }
}
