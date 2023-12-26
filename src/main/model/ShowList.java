package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



// a class for a list of Shows
public class ShowList {

    static int totalShowsWatched = 0;
    static int totalEpisodesWatched = 0;
    static int numCompleted = 0;
    static int numWatching = 0;
    static int numPlanToWatch = 0;
    static float meanScore = 0;

    private String name;
    private ArrayList<Show> shows;


    // EFFECTS: Constructs a new list of shows
    public ShowList(String name) {
        this.name = name;
        shows = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a show to the list
    public void addShow(Show show) {
        shows.add(show);
        EventLog.getInstance().logEvent(new Event(show.getName()
                + " was added to the list! \nShow list and Show stats updated!"));
    }

    // MODIFIES: this
    // EFFECTS: removes the show from the list and returns true if successful
    public boolean removeShow(Show show) {
        if (shows.contains(show)) {
            shows.remove(show);
            EventLog.getInstance().logEvent(new Event(show.getName()
                    + " was removed from the list! \nShow list and Show stats updated!"));
            return true;
        }
        return false;
    }


    // EFFECTS: returns the name of the show list
    public String getName() {
        return name;
    }


    // EFFECTS: returns the show at index i
    public Show getShow(int i) {
        return shows.get(i);
    }

    // EFFECTS: returns the list of shows
    public ArrayList<Show> getShowList() {
        return this.shows;
    }

    // EFFECTS: returns the # of shows in the list
    public int getNumberOfShowsInList() {
        return shows.size();
    }

    // EFFECTS: returns true if the given show is in the list, false otherwise
    public boolean containsShow(Show show) {
        return shows.contains(show);
    }


    // EFFECTS: returns true if the list of shows is empty, false otherwise
    public boolean isEmpty() {
        return shows.isEmpty();
    }

    // Getters and Setters for totalShowsWatched
    public static int getTotalShowsWatched() {
        return totalShowsWatched;
    }

    public static void setTotalShowsWatched(int watched) {
        totalShowsWatched = watched;
    }

    public static void addOneTotalShowsWatched() {
        totalShowsWatched++;
    }

    public static void subOneTotalShowsWatched() {
        totalShowsWatched--;
    }


    // Getters and Setters for totalEpisodesWatched
    public static int getTotalEpisodesWatched() {
        return totalEpisodesWatched;
    }

    public static void setTotalEpisodesWatched(int watched) {
        totalEpisodesWatched = watched;
    }

    public static void addToTotalEpisodesWatched(int eps) {
        totalEpisodesWatched += eps;
    }

    public static void decrementEpisodeWatchedCountBy(int progress) {
        totalEpisodesWatched -= progress;
    }

    // Getters and Setters for numCompleted
    public static int getNumCompleted() {
        return numCompleted;
    }

    public static void setNumCompleted(int c) {
        numCompleted = c;
    }

    public static void addOneNumCompleted() {
        numCompleted++;
    }

    public static void subOneNumCompleted() {
        numCompleted--;
    }

    // Getters and Setters for numWatching
    public static int getNumWatching() {
        return numWatching;
    }

    public static void setNumWatching(int w) {
        numWatching = w;
    }

    public static void addOneNumWatching() {
        numWatching++;
    }

    public static void subOneNumWatching() {
        numWatching--;
    }


    // Getters and Setters for numPlanToWatch
    public static int getNumPlanToWatch() {
        return numPlanToWatch;
    }

    public static void setNumPlanToWatch(int ptw) {
        numPlanToWatch = ptw;
    }

    public static void addOneNumPlanToWatch() {
        numPlanToWatch++;
    }

    public static void subOneNumPlanToWatch() {
        numPlanToWatch--;
    }

    // Getters and Setters for Mean Score
    public static float getMeanScore() {
        return meanScore;
    }

    public static void setMeanScore(float mean) {
        meanScore = mean;
    }

    // EFFECTS: totals all the ratings that are not null and divides it by the number of shows
    public static float showMeanScore(ShowList showlist) {
        float showCount = 0;
        float totalRatingsSum = 0;
        if (showlist.getNumberOfShowsInList() == 0 || allShowsRatingsAreNull(showlist)) {
            return 0;
        } else {
            for (Show show : showlist.getShowList()) {
                if (show.getScore() != null && show.getScore() != 0f) {
                    showCount++;
                    totalRatingsSum += show.getScore();
                    meanScore = totalRatingsSum / showCount;
                }
            }
            return meanScore;
        }
    }

    public static boolean allShowsRatingsAreNull(ShowList showlist) {
        for (Show show : showlist.getShowList()) {
            if (show.getScore() != null) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns the string N/A if there are no shows with ratings in the list,
    //          otherwise returns the mean score
    public static String outputMeanScore(ShowList showlist) {
        if (showMeanScore(showlist) > 0) {
            return "Mean Score: " + showMeanScore(showlist);
        } else {
            return "Mean Score: N/A";
        }
    }

    public void viewStatsConsoleMessage() {
        EventLog.getInstance().logEvent(new Event("Viewed Stats List"));
    }

    public void viewShowsConsoleMessage() {
        EventLog.getInstance().logEvent(new Event("Viewed Show List"));
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listName", name);
        json.put("shows", showsToJson());
        json.put("totalShowsWatched", totalShowsWatched);
        json.put("totalEpisodesWatched", totalEpisodesWatched);
        json.put("numCompleted", numCompleted);
        json.put("numWatching", numWatching);
        json.put("numPlanToWatch", numPlanToWatch);
        json.put("meanScore", meanScore);

        return json;
    }

    // EFFECTS: returns shows in show list as a JSON array
    private JSONArray showsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Show show : shows) {
            jsonArray.put(show.toJson());
        }

        return jsonArray;
    }


}



