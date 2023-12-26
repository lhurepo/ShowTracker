package persistence;

import model.Show;
import model.ShowList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

    private ShowList showList;
    private Show onePiece;
    private Show attackOnTitan;


    @BeforeEach
    void setUp() {
        showList = new ShowList("Test Show List");
        onePiece = new Show("One Piece", 10f, 1054, 1000, "watching", "Shonen");
        attackOnTitan = new Show("Attack On Titan", 9f, 90, 90, "completed", "Action");
        showList.addShow(onePiece);
        showList.addShow(attackOnTitan);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyShowList() {
        try {
            ShowList sl = new ShowList("My Show List");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyShowList.json");
            writer.open();
            writer.write(sl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyShowList.json");
            sl = reader.read();
            assertEquals("My Show List", sl.getName());
            assertEquals(0, sl.getNumberOfShowsInList());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralShowList() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralShowList.json");
            writer.open();
            writer.write(showList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralShowList.json");
            ShowList sl = reader.read();
            assertEquals("Test Show List", sl.getName());
            assertEquals(2, sl.getNumberOfShowsInList());
            checkShow("One Piece", 10f, 1054, 1000, "watching", "Shonen", sl.getShow(0));
            checkShow("Attack On Titan", 9.0f, 90, 90, "completed", "Action", sl.getShow(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }



}
