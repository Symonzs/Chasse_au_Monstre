package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestMazeGenerator{
    
    protected Maze maze;

    @Test 
    public void generate_smallest_maze(){
        MazeGenerator maze = new MazeGenerator(5,5,100);
        assertEquals(2,maze.getEmptyCellsNumber());

        assertNotEquals(0,maze.getEmptyCellsNumber());
    }

    

}