package persistence;

import model.Show;
import model.ShowList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

// Represents tests for JsonReader class
public class JsonReaderTest extends JsonTest {
    private ShowList showList;

    @BeforeEach
    void setUp() {
        showList = new ShowList("My Show List");
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ShowList sl = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyShowList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyShowList.json");
        try {
            ShowList sl = reader.read();
            assertEquals("My Show List", sl.getName());
            assertEquals(0, sl.getNumberOfShowsInList());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralShowList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralShowList.json");
        try {
            ShowList sl = reader.read();
            assertEquals("Test Show List", sl.getName());
            assertEquals(2, sl.getNumberOfShowsInList());
            checkShow("One Piece", 10f, 1054, 1000, "watching", "Shonen", sl.getShow(0));
            checkShow("Attack On Titan", 9.0f, 90, 90, "completed", "Action", sl.getShow(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
