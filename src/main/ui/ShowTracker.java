package ui;

import model.Show;
import model.ShowList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static model.ShowList.*;

// A Show Tracker application to log your favorite shows
public class ShowTracker {

    private static final String JSON_STORE = "./data/showtracker.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static ShowList showList;
    static Scanner sc = new Scanner(System.in);

    // EFFECTS: runs the ShowTracker Application
    public ShowTracker() throws FileNotFoundException {
        showList = new ShowList("My Show List");
        sc = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runShowTracker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    @SuppressWarnings("methodlength")
    private void runShowTracker() {

        while (true) {
            System.out.println("1. Add show");
            System.out.println("2. View shows");
            System.out.println("3. View stats");
            System.out.println("4. Remove show");
            System.out.println("5. Save ShowList");
            System.out.println("6. Load ShowList");
            System.out.println("7: Exit");
            System.out.print("Enter your choice (1-7): ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addShowToList();
                    break;
                case 2:
                    viewShows();
                    break;
                case 3:
                    viewStats();
                    break;
                case 4:
                    removeShowFromList();
                    break;
                case 5:
                    saveShowList();
                    break;
                case 6:
                    loadShowList();
                    break;
                case 7:
                    System.out.println("Exiting ShowTracker...");
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }


    // REQUIRES: same show has not already been added
    // MODIFIES: this
    // EFFECTS: adds a show to the list
    private static void addShowToList() {
        String name = getShowName();
        Float rating = getShowRating();
        int episodes = getNumberOfEpisodes();
        String status = getShowStatus(episodes);
        int progress = getShowProgress(episodes, status);
        String genre = getShowGenre();

        Show show = new Show(name, rating, episodes, progress, status, genre);
        showList.addShow(show);
        addOneTotalShowsWatched();

        System.out.println(name + " was added to your list!");
    }

    private static String getShowName() {
        System.out.print("Enter show name: ");
        return sc.nextLine();
    }

    private static Float getShowRating() {
        System.out.print("Enter show rating (or '-' to skip): ");
        String ratingInput = sc.nextLine();
        return ratingInput.equals("-") ? null : Float.parseFloat(ratingInput);
    }

    private static int getNumberOfEpisodes() {
        System.out.print("Enter number of episodes (or '-' if its ongoing): ");
        String episodesInput = sc.nextLine();
        return episodesInput.equals("-") ? -1 : Integer.parseInt(episodesInput);
    }

    private static String getShowStatus(int episodes) {
        String status;
        boolean isOngoing = episodes == -1;
        while (true) {
            System.out.print("Enter show status (completed, watching, plan to watch): ");
            status = sc.nextLine();
            if (isOngoing && status.equals("completed")) {
                System.out.println("You can't complete an ongoing show!");
            } else {
                break;
            }
        }
        return status;
    }

    @SuppressWarnings("methodlength")
    private static int getShowProgress(int episodes, String status) {
        int progress = 0;
        switch (status) {
            case "completed":
                progress = episodes;
                addToTotalEpisodesWatched(episodes);
                addOneNumCompleted();
                break;
            case "plan to watch":
                addOneNumPlanToWatch();
                break;
            case "watching":
                if (episodes == -1) {
                    System.out.print("Enter number of episodes watched: ");
                    progress = sc.nextInt();
                    sc.nextLine();
                    addToTotalEpisodesWatched(progress);
                    addOneNumWatching();
                } else {
                    while (true) {
                        System.out.print("Enter number of episodes watched: ");
                        progress = sc.nextInt();
                        if (progress > episodes) {
                            System.out.println("The show doesn't have that many episodes!");
                            continue;
                        }
                        sc.nextLine();
                        addToTotalEpisodesWatched(progress);
                        addOneNumWatching();
                        break;
                    }
                }
                break;
        }
        return progress;
    }


    private static String getShowGenre() {
        System.out.print("Enter show genre: ");
        return sc.nextLine();
    }


    // MODIFIES: this
    // EFFECTS: removes a show from the list, and removes it from stat calculations
    @SuppressWarnings("methodlength")
    private static void removeShowFromList() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter the name show to remove: ");
        String name = scn.nextLine();
        Show showToRemove = null;
        for (int i = 0; i < showList.getNumberOfShowsInList(); i++) {
            if (showList.getShow(i).getName().equals(name)) {
                showToRemove = showList.getShow(i);
                break;
            }
        }
        if (showToRemove != null && showList.removeShow(showToRemove)) {
            subOneTotalShowsWatched();
            decrementEpisodeWatchedCountBy(showToRemove.getProgress());
            switch (showToRemove.getStatus()) {
                case "completed":
                    subOneNumCompleted();
                    break;
                case "watching":
                    subOneNumWatching();
                    break;
                case "plan to watch":
                    subOneNumPlanToWatch();
                    break;
            }
            System.out.println("The show was removed from your list");
        } else {
            System.out.println("That show was not found in your list");
        }
    }


    // EFFECTS: displays shows in list with their fields
    private static void viewShows() {
        System.out.println("Shows:");
        if (getTotalShowsWatched() > 0) {
            for (Show show : showList.getShowList()) {
                String episodesStr;
                if (show.getEpisodes() == -1) {
                    episodesStr = "-, ";
                } else {
                    episodesStr = show.getEpisodes() + " episodes, ";
                }
                System.out.println("- " + show.getName() + ": " + "Score: " + show.getScore() + " "
                        + "(" + "Progress: " + show.getProgress() + "/" + episodesStr
                        + "Status: " + show.getStatus() + ", " + "Genre: " + show.getGenre() + ")");
            }
        } else {
            System.out.println("You have not added any entries yet");
        }

    }

    // EFFECTS: displays the cumulative stats of all the shows in the list
    private static void viewStats() {
        System.out.println("Here are your stats:");
        System.out.println("Total # of Shows In List: " + getTotalShowsWatched());
        System.out.println("Total Episodes Watched: " + getTotalEpisodesWatched());
        System.out.println(outputMeanScore(showList));
        System.out.println("Number of Completed: " + getNumCompleted());
        System.out.println("Number of Watching: " + getNumWatching());
        System.out.println("Number of Plan-To-Watch: " + getNumPlanToWatch());

    }

    // EFFECTS: saves the show list to file
    private void saveShowList() {
        try {
            jsonWriter.open();
            jsonWriter.write(showList);
            jsonWriter.close();
            System.out.println("Saved " + showList.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads show list from file
    private void loadShowList() {
        try {
            showList = jsonReader.read();
            System.out.println("Loaded " + showList.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}







