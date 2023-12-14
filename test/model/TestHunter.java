package model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestHunter {
    protected Maze maze;
    protected Monster monster;
    protected Hunter hunter;

    @BeforeEach
    public void setup() {

        Maze.resetTurn();
        // Pensé a changer le chemin du fichier csv (Pour le moment il est en absolu
        // dans les tests)
        maze = new Maze("/home/infoetu/simon.hayart.etu/J1_SAE3A/resources/map/4x4.csv");
        monster = new Monster(maze);
        hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(monster);
        maze.attach(hunter);
        maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.turn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.turn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(2, 1), CellInfo.HUNTER));

        // to do faire une simulation de partie sur une grille 3x3

        /*
         * le monstre commence en 0,0 le chasseur tire jsp ou
         * la sortie est en 2,2
         * le monstre se deplace en 1,0 au premier tour le chasseur tire jsp ou
         * le monstre de deplace en 1,1 au deuxieme tour le chasseur tire jsp ou
         * le monstre se deplace en 1,2 au troisieme tour le chasseur ne tire pas !!
         */

    }

    @Test
    public void test_get_know_walls(){
        assertTrue(hunter.getKnowWall()[2][1]);
        assertFalse(hunter.getKnowWall()[1][2]);
    }

    @Test
    public void test_get_know_empty_cases(){
        assertTrue(hunter.getKnowEmpty()[0][3]);
        assertFalse(hunter.getKnowEmpty()[0][0]);
        assertFalse(hunter.getKnowEmpty()[2][1]);
        assertFalse(hunter.getKnowEmpty()[1][3]);
    }

    @Test
    public void test_get_hunter_cord(){
        assertEquals(new Coordinate(2, 1), hunter.getHunterCoord());
        assertNotEquals(new Coordinate(0, 0), hunter.getHunterCoord());
    }
}