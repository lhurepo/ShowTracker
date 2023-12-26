package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ShowTest {

    private Show testShow;

    @BeforeEach
    public void setUp() {
        testShow = new Show("Vinland Saga", 10.0f, 24, 12, "watching", "action");

    }


    @Test
    public void testGetters() {
        assertEquals("Vinland Saga", testShow.getName());
        assertEquals(10.0f, testShow.getScore());
        assertEquals(24, testShow.getEpisodes());
        assertEquals(12, testShow.getProgress());
        assertEquals("watching", testShow.getStatus());
        assertEquals("action", testShow.getGenre());

    }


}
