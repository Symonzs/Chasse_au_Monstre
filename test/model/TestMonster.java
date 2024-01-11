package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestMonster {
    protected Maze maze;
    protected Monster monster;

    @BeforeEach
    public void setup() {

        Maze.resetTurn();
        // Pensé a changer le chemin du fichier csv (Pour le moment il est en absolu
        // dans les tests)
        try {
            maze = new Maze("C:\\Users\\Raphk\\Documents\\Taff\\J1_SAE3A\\resources\\map\\", "5x5.csv");
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
        }
        monster = new Monster(maze);

        CellEvent[] events = maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.HUNTER));
        monster.update(events[1]);

        events = maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));
        monster.update(events[1]);
        events = maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));
        monster.update(events[1]);

        events = maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.currentTurn, CellInfo.MONSTER));
        monster.update(events[1]);
        events = maze.cellUpdate(new CellEvent(new Coordinate(2, 1), CellInfo.HUNTER));
        monster.update(events[1]);

    }

    @Test
    public void test_monster_is_at_the_right_place() {
        assertEquals(new Coordinate(2, 0), monster.getMonsterCoord());
    }

    @Test
    public void test_hunter_is_at_the_right_place() {
        assertEquals(new Coordinate(2, 1), monster.getHunterCoord());
    }
}