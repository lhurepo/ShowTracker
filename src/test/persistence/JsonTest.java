package persistence;

import model.Show;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    // EFFECTS: checks whether the given show has the given attributes
    protected void checkShow(String name, Float score, int episodes, int progress, String status, String genre, Show show) {
        assertEquals(name, show.getName());
        assertEquals(score, show.getScore());
        assertEquals(episodes, show.getEpisodes());
        assertEquals(progress, show.getProgress());
        assertEquals(status, show.getStatus());
        assertEquals(genre, show.getGenre());
    }

}
