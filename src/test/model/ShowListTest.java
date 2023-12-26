package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShowListTest {

    private ShowList testShowList;
    private Show testShow1;
    private Show testShow2;

    @BeforeEach
    public void setUp() {
        testShowList = new ShowList("Test Show List");
        testShow1 = new Show("Vinland Saga", 10.0f, 24, 12, "watching", "action");
        testShow2 = new Show("Tokyo Revengers", 7.5f, 24, 24, "completed", "suspense");

    }

    @Test
    public void testAddShow() {
        testShowList.addShow(testShow1);
        assertTrue(testShowList.containsShow(testShow1));
        testShowList.addShow(testShow2);
        assertTrue(testShowList.containsShow(testShow2));

    }

    @Test
    public void testRemoveShow() {
        // tests for successful removal and updates of static fields
        testShowList.addShow(testShow1);
        assertTrue(testShowList.containsShow(testShow1));
        assertEquals(1, testShowList.getNumberOfShowsInList());
        testShowList.removeShow(testShow1);
        assertFalse(testShowList.containsShow(testShow1));
        assertEquals(0, testShowList.getNumberOfShowsInList());
        assertTrue(testShowList.isEmpty());
        testShowList.removeShow(testShow2);
        assertFalse(testShowList.containsShow(testShow1));
        testShowList.addShow(testShow1);
        testShowList.addShow(testShow2);
        assertEquals(2, testShowList.getNumberOfShowsInList());
        testShowList.removeShow(testShow2);
        assertFalse(testShowList.containsShow(testShow2));
        assertTrue(testShowList.containsShow(testShow1));
        assertEquals(1, testShowList.getNumberOfShowsInList());
        testShowList.addShow(testShow2);
        testShowList.addShow(testShow1);
        testShowList.removeShow(testShow1);
        assertEquals(testShow1, testShowList.getShow(1));

        // direct method boolean return value test
        assertTrue(testShowList.removeShow(testShow2));
        assertTrue(testShowList.removeShow(testShow1));
        assertFalse(testShowList.removeShow(testShow2));

    }

    @Test
    public void testGetShow() {
        testShowList.addShow(testShow1);
        testShowList.addShow(testShow2);
        assertEquals(testShow1, testShowList.getShow(0)) ;
        assertEquals(testShow2, testShowList.getShow(1)) ;

    }

    @Test
    public void testGetShowList() {
        testShowList.addShow(testShow1);
        testShowList.addShow(testShow2);
        List<Show> expectedList = new ArrayList<>();
        expectedList.add(testShow1);
        expectedList.add(testShow2);
        List<Show> actualList = testShowList.getShowList();
        assertEquals(expectedList, actualList);

    }

    @Test
    public void testGetNumberOfShowsInList() {
        testShowList.addShow(testShow1);
        assertEquals(1, testShowList.getNumberOfShowsInList()) ;
        testShowList.addShow(testShow2);
        assertEquals(2, testShowList.getNumberOfShowsInList()) ;

    }

    @Test
    public void testIsEmpty() {
        assertTrue(testShowList.isEmpty());
        testShowList.addShow(testShow1);
        assertFalse(testShowList.isEmpty());
    }

    @Test
    void testOutputMeanScore() {
        ShowList testShowList2 = new ShowList("Test Show List 2");
        // Test with an empty show list
        assertEquals("Mean Score: N/A", ShowList.outputMeanScore(testShowList2));
        assertEquals(0, ShowList.showMeanScore(testShowList2));
        assertTrue(ShowList.allShowsRatingsAreNull(testShowList2));

        // Test with a show list containing shows with null ratings
        testShowList2.addShow(new Show("Death Note", null, 37, 13, "completed", "mystery"));
        assertEquals("Mean Score: N/A", ShowList.outputMeanScore(testShowList2));

        // Test with a show list containing one show with non-null ratings
        testShowList2.addShow(testShow2);
        assertEquals("Mean Score: 7.5", ShowList.outputMeanScore(testShowList2));

        // test with a show list containing two shows with non-null ratings
        testShowList2.addShow(testShow1);
        assertEquals("Mean Score: 8.75", ShowList.outputMeanScore(testShowList2));

        // add a null rating show, should not affect the mean score
        Show nullRatedShow = new Show("SAO", null, 100, 10, "watching", "fantasy");
        testShowList2.addShow(nullRatedShow);
        assertEquals("Mean Score: 8.75", ShowList.outputMeanScore(testShowList2));
        assertFalse(ShowList.allShowsRatingsAreNull(testShowList2));
    }

    @Test
    public void testGetSetMeanScore() {
        ShowList.setMeanScore(8.5f);
        assertEquals(8.5f, ShowList.getMeanScore());
    }




    @Test
    public void testGettersAndSettersOfStaticFields() {

        // Set and get totalShowsWatched
        ShowList.setTotalShowsWatched(10);
        assertEquals(10, ShowList.getTotalShowsWatched());

        // Add and subtract from totalShowsWatched
        ShowList.addOneTotalShowsWatched();
        assertEquals(11, ShowList.getTotalShowsWatched());
        ShowList.subOneTotalShowsWatched();
        assertEquals(10, ShowList.getTotalShowsWatched());

        // Set and get totalEpisodesWatched
        ShowList.setTotalEpisodesWatched(50);
        assertEquals(50, ShowList.getTotalEpisodesWatched());

        // Add and subtract from totalEpisodesWatched
        ShowList.addToTotalEpisodesWatched(5);
        assertEquals(55, ShowList.getTotalEpisodesWatched());
        ShowList.decrementEpisodeWatchedCountBy(10);
        assertEquals(45, ShowList.getTotalEpisodesWatched());

        // Set and get numCompleted
        ShowList.setNumCompleted(3);
        assertEquals(3, ShowList.getNumCompleted());

        // Add and subtract from numCompleted
        ShowList.addOneNumCompleted();
        assertEquals(4, ShowList.getNumCompleted());
        ShowList.subOneNumCompleted();
        assertEquals(3, ShowList.getNumCompleted());

        // Set and get numWatching
        ShowList.setNumWatching(5);
        assertEquals(5, ShowList.getNumWatching());

        // Add and subtract from numWatching
        ShowList.addOneNumWatching();
        assertEquals(6, ShowList.getNumWatching());
        ShowList.subOneNumWatching();
        assertEquals(5, ShowList.getNumWatching());

        // Set and get numPlanToWatch
        ShowList.setNumPlanToWatch(20);
        assertEquals(20, ShowList.getNumPlanToWatch());

        // Add and subtract from numPlanToWatch
        ShowList.addOneNumPlanToWatch();
        assertEquals(21, ShowList.getNumPlanToWatch());
        ShowList.subOneNumPlanToWatch();
        assertEquals(20, ShowList.getNumPlanToWatch());
    }


    @Test
    public void testViewStatsConsoleMessage() {
        testShowList.viewStatsConsoleMessage();
        Event lastEvent = null;
        for (Event event : EventLog.getInstance()) {
            lastEvent = event;
        }
        assertEquals("Viewed Stats List", lastEvent.getDescription());
    }

    @Test
    public void testViewShowsConsoleMessage() {
        testShowList.viewShowsConsoleMessage();
        Event lastEvent = null;
        for (Event event : EventLog.getInstance()) {
            lastEvent = event;
        }
        assertEquals("Viewed Show List", lastEvent.getDescription());
    }



}
