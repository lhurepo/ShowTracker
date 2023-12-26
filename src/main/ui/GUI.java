package ui;

import model.EventLog;
import model.Event;
import model.Show;
import model.ShowList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import static model.ShowList.*;

// A Show Tracker Graphic User Interface application to log your favorite shows
public class GUI extends JFrame implements ActionListener {

    private ShowList showList = new ShowList("My Show List");

    private static final String JSON_STORE = "./data/showtrackerGUI.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private EventLog eventLog;

    private JPanel mainMenu;
    private JButton addShowButton;
    private JButton removeShowButton;
    private JButton viewShowButton;
    private JButton viewStatsButton;
    private JButton loadListButton;
    private JButton saveListButton;
    private JButton quitButton;

    private JPanel addShowPanel;
    private JPanel removeShowPanel;
    private JTextField removeTextField;
    private JPanel viewStatsPanel;
    private JPanel mainMenuViewShowsPanel;
    private JPanel viewShowsPanel;
    private JPanel topMainMenuPanel = new JPanel();

    private JTextField nameField;
    private JComboBox<String> scoreField;
    private JTextField episodesField;
    private JCheckBox ongoingCheckBox;
    private JTextField progressField;
    private JComboBox<String> statusField;
    private JComboBox<String> genreField;


    // EFFECTS: Initializes GUI for Show Tracker
    public GUI() {
        super("Show Tracker");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));

        initializeMenu();

        JLabel mainScreenImage = new JLabel();
        addImageToLabel(mainScreenImage);

        initializeMenuButtons();

        addButtons(addShowButton, removeShowButton, viewShowButton,
                viewStatsButton, loadListButton, saveListButton, quitButton);
        addActionToButton();
        mainMenu.setVisible(true);
    }


    // EFFECTS: Initializes the main menu panel
    public void initializeMenu() {
        mainMenu = new JPanel();
        mainMenu.setBackground(Color.black);
        add(mainMenu);
    }


    // EFFECTS: Initializes main menu buttons and their labels
    public void initializeMenuButtons() {
        addShowButton = new JButton("Add Show");
        removeShowButton = new JButton("Remove Show");
        viewShowButton = new JButton("View Shows");
        viewStatsButton = new JButton("View Stats");
        loadListButton = new JButton("Load List");
        saveListButton = new JButton("Save List");
        quitButton = new JButton("Quit");
    }


    // MODIFIES: this
    // EFFECTS: Adds buttons to mainMenu
    public void addButton(JButton button1, JPanel panel) {
        button1.setFont(new Font("Arial", Font.BOLD, 12));
        button1.setBackground(Color.white);
        panel.add(button1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: Sets each button to their respective action
    public void addActionToButton() {
        addShowButton.addActionListener(this);
        addShowButton.setActionCommand("Add Show");
        removeShowButton.addActionListener(this);
        removeShowButton.setActionCommand("Remove Show");
        viewShowButton.addActionListener(this);
        viewShowButton.setActionCommand("View Shows");
        viewStatsButton.addActionListener(this);
        viewStatsButton.setActionCommand("View Stats");
        loadListButton.addActionListener(this);
        loadListButton.setActionCommand("Load List");
        saveListButton.addActionListener(this);
        saveListButton.setActionCommand("Save List");
        quitButton.addActionListener(this);
        quitButton.setActionCommand("Quit");
    }

    // EFFECTS: Calls the addButton method for each button
    public void addButtons(JButton button1, JButton button2, JButton button3, JButton button4,
                           JButton button5, JButton button6, JButton button7) {

        addButton(button1, mainMenu);
        addButton(button2, mainMenu);
        addButton(button3, mainMenu);
        addButton(button4, mainMenu);
        addButton(button5, mainMenu);
        addButton(button6, mainMenu);
        addButton(button7, mainMenu);
    }


    // EFFECTS: Creates the gif on the main menu and adds it to the panel
    public void addImageToLabel(JLabel j1) {
        ImageIcon icon = new ImageIcon("./data/showtracker.gif");
        Image img = icon.getImage().getScaledInstance(700, 370, Image.SCALE_DEFAULT);
        j1.setIcon(new ImageIcon(img));
        mainMenu.add(j1);
    }


    // EFFECTS: calls the given methods when a certain button is clicked on
    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Add Show")) {
            initializeAddShowPanel();
        } else if (ae.getActionCommand().equals("Remove Show")) {
            initializeRemoveShowsPanel();
        } else if (ae.getActionCommand().equals("View Shows")) {
            initializeViewShowsPanel();
        } else if (ae.getActionCommand().equals("View Stats")) {
            initializeViewStatsPanel();
        } else if (ae.getActionCommand().equals("Load List")) {
            loadListGUI();
        } else if (ae.getActionCommand().equals("Save List")) {
            saveListGUI();
        } else if (ae.getActionCommand().equals("Main Menu")) {
            showPanel(mainMenu);
            topMainMenuPanel.setVisible(false);
        } else if (ae.getActionCommand().equals("Add")) {
            addShowGUI();
        } else if (ae.getActionCommand().equals("Remove")) {
            removeShowGUI();
        } else if (ae.getActionCommand().equals("Quit")) {
            printEventLogToConsole();
            System.exit(0);
        }
    }

    @SuppressWarnings("methodlength")
    // REQUIRES: same show has not already been added
    // MODIFIES: this
    // EFFECTS: adds a show to the list
    private void addShowGUI() {
        if (nameField.getText().isEmpty() || episodesField.getText().isEmpty() || progressField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You ain't finished yet");
            return;
        }

        String name = nameField.getText();
        Float score = parseScore((String) scoreField.getSelectedItem());
        int episodes = Integer.parseInt(episodesField.getText());
        int progress = Integer.parseInt(progressField.getText());
        String status = (String) statusField.getSelectedItem();
        String genre = (String) genreField.getSelectedItem();

        if (progress > episodes) {
            JOptionPane.showMessageDialog(this,
                    "This show doesn't have that many episodes!");
            return;
        }

        if (progress == episodes && !status.equals("Completed")) {
            JOptionPane.showMessageDialog(this,
                    "If you've watched all the episodes, set it to completed!");
            return;
        }

        if (status.equals("Completed") && progress != episodes) {
            JOptionPane.showMessageDialog(this,
                    "If you're completed, progress should be equal to episodes!");
            return;
        }

        Show newShow = new Show(name, score, episodes, progress, status, genre);

        showList.addShow(newShow);
        addOneTotalShowsWatched();
        addToTotalEpisodesWatched(progress);
        switch (status) {
            case "Watching":
                addOneNumWatching();
                break;
            case "Completed":
                addOneNumCompleted();
                break;
            case "Plan-to-Watch":
                addOneNumPlanToWatch();
                break;
        }

        JOptionPane.showMessageDialog(this, "Show added successfully!");
        showPanel(mainMenu);
    }



    // EFFECTS: Initializes the add show panel and its attributes
    @SuppressWarnings("methodlength")
    private void initializeAddShowPanel() {
        addShowPanel = new JPanel();
        addShowPanel.setLayout(new BoxLayout(addShowPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        addShowPanel.add(namePanel);

        JLabel scoreLabel = new JLabel("Score (1-10):");
        String[] scoreOptions = {"Select", "10.0", "9.0", "8.0", "7.0",
                "6.0", "5.0", "4.0", "3.0", "2.0", "1.0"};
        scoreField = new JComboBox<>(scoreOptions);
        JPanel scorePanel = new JPanel();
        scorePanel.add(scoreLabel);
        scorePanel.add(scoreField);
        addShowPanel.add(scorePanel);

        JLabel episodesLabel = new JLabel("Episodes:");
        episodesField = new JTextField(20);
        JPanel episodesPanel = new JPanel();
        episodesPanel.add(episodesLabel);
        episodesPanel.add(episodesField);
        addShowPanel.add(episodesPanel);

        ongoingCheckBox = new JCheckBox("Ongoing");
        JPanel ongoingPanel = new JPanel();
        ongoingPanel.add(ongoingCheckBox);
        addShowPanel.add(ongoingPanel);

        JLabel progressLabel = new JLabel("Progress:");
        progressField = new JTextField(20);
        JPanel progressPanel = new JPanel();
        progressPanel.add(progressLabel);
        progressPanel.add(progressField);
        addShowPanel.add(progressPanel);

        JLabel statusLabel = new JLabel("Status:");
        String[] statusOptions = {"Watching", "Completed", "Plan-to-Watch"};
        statusField = new JComboBox<>(statusOptions);
        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);
        statusPanel.add(statusField);
        addShowPanel.add(statusPanel);

        JLabel genreLabel = new JLabel("Genre:");
        String[] genres = {"Action", "Adventure", "Comedy", "Drama", "Fantasy",
                "Horror", "Mystery", "Romance", "Sci-Fi", "Thriller",
                "Animation", "Crime", "Fighting", "Family", "Historical", "Music",
                "Sports", "Other"};
        genreField = new JComboBox<>(genres);
        JPanel genrePanel = new JPanel();
        genrePanel.add(genreLabel);
        genrePanel.add(genreField);
        addShowPanel.add(genrePanel);

        JPanel buttonPanel = new JPanel();
        JButton addShowButton = new JButton("Add");
        addShowButton.addActionListener(this);
        buttonPanel.add(addShowButton);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);
        buttonPanel.add(mainMenuButton);
        addShowPanel.add(buttonPanel);

        showPanel(addShowPanel);
        add(addShowPanel);
    }



    // EFFECTS: Initializes the view shows panel and its attributes
    @SuppressWarnings("methodlength")
    private void initializeViewShowsPanel() {
        viewShowsPanel = new JPanel();
        viewShowsPanel.setLayout(new BoxLayout(viewShowsPanel, BoxLayout.PAGE_AXIS));
        viewShowsPanel.setPreferredSize(new Dimension(600, 800));

        for (Show show : showList.getShowList()) {
            JPanel showPanel = new JPanel();
            showPanel.setLayout(new BoxLayout(showPanel, BoxLayout.PAGE_AXIS));
            showPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            showPanel.setPreferredSize(new Dimension(400, 5));

            JLabel nameLabel = new JLabel("Name: " + show.getName());
            JLabel scoreLabel = new JLabel("Score: " + (show.getScore() == 0.0 ? "Not Set" : show.getScore()));
            JLabel episodesLabel = new JLabel("Episodes: " + show.getEpisodes());
            JLabel progressLabel = new JLabel("Progress: " + show.getProgress());
            JLabel statusLabel = new JLabel("Status: " + show.getStatus());
            JLabel genreLabel = new JLabel("Genre: " + show.getGenre());

            showPanel.add(nameLabel);
            showPanel.add(scoreLabel);
            showPanel.add(episodesLabel);
            showPanel.add(progressLabel);
            showPanel.add(statusLabel);
            showPanel.add(genreLabel);

            JPanel outerPanel = new JPanel(new BorderLayout());
            outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            outerPanel.add(showPanel, BorderLayout.CENTER);
            viewShowsPanel.add(outerPanel);
        }

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);

        topMainMenuPanel = new JPanel();
        topMainMenuPanel.add(mainMenuButton);

        JScrollPane scrollPane = new JScrollPane(viewShowsPanel);

        mainMenuViewShowsPanel = new JPanel(new BorderLayout());
        mainMenuViewShowsPanel.add(topMainMenuPanel, BorderLayout.NORTH);
        mainMenuViewShowsPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainMenuViewShowsPanel);
        showPanel(mainMenuViewShowsPanel);
        showList.viewShowsConsoleMessage();
    }


    // EFFECTS: Returns a float parsed from a string. If the input is "Select", return 0.0f
    private float parseScore(String selectedScore) {
        if (selectedScore.equals("Select")) {
            return 0.0f;
        }
        return Float.parseFloat(selectedScore);
    }



    @SuppressWarnings("methodlength")
    // EFFECTS: Initializes the View Stats panel
    private void initializeViewStatsPanel() {
        viewStatsPanel = new JPanel();
        viewStatsPanel.setLayout(new BoxLayout(viewStatsPanel, BoxLayout.PAGE_AXIS));

        int totalShowsWatched = getTotalShowsWatched();
        int totalEpisodesWatched = getTotalEpisodesWatched();
        String meanScoreOutput = outputMeanScore(showList);
        int numCompleted = getNumCompleted();
        int numWatching = getNumWatching();
        int numPlanToWatch = getNumPlanToWatch();

        JLabel totalShowsWatchedLabel = new JLabel("Total # of Shows In List: " + totalShowsWatched);
        viewStatsPanel.add(totalShowsWatchedLabel);

        JLabel totalEpisodesWatchedLabel = new JLabel("Total Episodes Watched: " + totalEpisodesWatched);
        viewStatsPanel.add(totalEpisodesWatchedLabel);

        JLabel meanScoreOutputLabel = new JLabel(meanScoreOutput);
        viewStatsPanel.add(meanScoreOutputLabel);

        JLabel numWatchingLabel = new JLabel("Number of Watching: " + numWatching);
        viewStatsPanel.add(numWatchingLabel);

        JLabel numCompletedLabel = new JLabel("Number of Completed: " + numCompleted);
        viewStatsPanel.add(numCompletedLabel);

        JLabel numPlanToWatchLabel = new JLabel("Number of Plan-To-Watch: " + numPlanToWatch);
        viewStatsPanel.add(numPlanToWatchLabel);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);

        topMainMenuPanel = new JPanel();
        topMainMenuPanel.add(mainMenuButton);

        add(topMainMenuPanel, BorderLayout.NORTH);
        add(viewStatsPanel, BorderLayout.CENTER);
        showPanel(viewStatsPanel);
        showList.viewStatsConsoleMessage();
    }


    // EFFECTS: Initializes the Remove Shows panel
    private void initializeRemoveShowsPanel() {
        JLabel removeLabel = new JLabel("Type the name of the show you want to remove:");
        removeTextField = new JTextField(10);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(this);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);

        removeShowPanel = new JPanel();
        removeShowPanel.setLayout(new BoxLayout(removeShowPanel, BoxLayout.Y_AXIS));
        removeShowPanel.add(Box.createVerticalStrut(50));
        removeShowPanel.add(removeLabel);
        removeShowPanel.add(Box.createVerticalStrut(20));
        removeShowPanel.add(removeTextField);
        removeShowPanel.add(Box.createVerticalStrut(20));
        removeShowPanel.add(removeButton);
        removeShowPanel.add(Box.createVerticalStrut(50));

        topMainMenuPanel = new JPanel();
        topMainMenuPanel.add(mainMenuButton);
        removeShowPanel.add(topMainMenuPanel, BorderLayout.NORTH);

        add(removeShowPanel);
        showPanel(removeShowPanel);
    }


    @SuppressWarnings("methodlength")
    // MODIFIES: this
    // EFFECTS: removes a show from the list, and removes it from stat calculations
    private void removeShowGUI() {
        String name = removeTextField.getText();
        Show showToRemove = null;
        for (Show show : showList.getShowList()) {
            if (show.getName().equalsIgnoreCase(name)) {
                showToRemove = show;
                break;
            }
        }
        if (showToRemove != null) {
            boolean success = showList.removeShow(showToRemove);
            if (success) {
                switch (showToRemove.getStatus()) {
                    case "Completed":
                        subOneNumCompleted();
                        break;
                    case "Watching":
                        subOneNumWatching();
                        break;
                    case "Plan-to-Watch":
                        subOneNumPlanToWatch();
                        break;
                }
                subOneTotalShowsWatched();
                decrementEpisodeWatchedCountBy(showToRemove.getProgress());
                JOptionPane.showMessageDialog(this, showToRemove.getName() + " was removed successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "There is no show in your list with that name.");
        }
    }



    // EFFECTS: saves the show list to file
    private void saveListGUI() {
        try {
            jsonWriter.open();
            jsonWriter.write(showList);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Show list saved successfully!");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads show list from file
    private void loadListGUI() {
        try {
            showList = jsonReader.read();
            initializeViewShowsPanel();
            initializeViewStatsPanel();
            showPanel(mainMenu);
            topMainMenuPanel.setVisible(false);
            JOptionPane.showMessageDialog(this, "Loaded " + showList.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: "
                    + JSON_STORE, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Shows the given panel, hides all others
    private void showPanel(JPanel panelToShow) {
        if (addShowPanel != null) {
            addShowPanel.setVisible(false);
        }
        if (removeShowPanel != null) {
            removeShowPanel.setVisible(false);
        }
        if (viewShowsPanel != null) {
            viewShowsPanel.setVisible(false);
        }
        if (viewStatsPanel != null) {
            viewStatsPanel.setVisible(false);
        }
        if (mainMenu != null) {
            mainMenu.setVisible(false);
        }
        if (mainMenuViewShowsPanel != null) {
            mainMenuViewShowsPanel.setVisible(false);
        }

        panelToShow.setVisible(true);
    }

    private void printEventLogToConsole() {
        System.out.println("Event Log:");
        for (Event event : eventLog.getInstance()) {
            System.out.println(event.getDescription());
        }
    }


}
