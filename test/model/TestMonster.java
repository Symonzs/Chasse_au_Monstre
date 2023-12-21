/*package model;

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
            maze = new Maze("/home/infoetu/raphael.kiecken.etu/S3/S3.02/J1_SAE3A/resources/map", "5x5.csv");
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
        }
        monster = new Monster(maze);
        
        CellEvent[] events = maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.MONSTER));
        monster.update(events[0]);

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));
        events = maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));
        monster.update(events[0]);

        maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.currentTurn, CellInfo.MONSTER));
        events = maze.cellUpdate(new CellEvent(new Coordinate(2, 1), CellInfo.HUNTER));
        monster.update(events[0]);

    }

    @Test
    public void test_set_monster_coord(){

    }
}*/
