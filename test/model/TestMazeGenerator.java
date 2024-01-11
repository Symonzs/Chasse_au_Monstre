package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class TestMazeGenerator {

    protected Maze maze;

    @Test
    public void generate_smallest_maze() {
        MazeGenerator maze = new MazeGenerator(5, 5, 100);
        assertEquals(2, maze.getEmptyCellsNumber());

        assertNotEquals(0, maze.getEmptyCellsNumber());
    }

}