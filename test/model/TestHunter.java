package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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

    }

    @Test
    public void test_get_hunter_know_walls(){
        assertTrue(hunter.getKnowWall()[2][1]);
        assertFalse(hunter.getKnowWall()[1][2]);
    }

    @Test
    public void test_get_hunter_know_empty_cases(){
        assertTrue(hunter.getKnowEmpty()[0][3]);
        assertFalse(hunter.getKnowEmpty()[0][0]);
        assertFalse(hunter.getKnowEmpty()[2][1]);
        assertFalse(hunter.getKnowEmpty()[1][3]);
    }

    @Test
    public void test_get_hunter_last_cord(){
        assertEquals(new Coordinate(2, 1), hunter.getHunterCoord());
        assertNotEquals(new Coordinate(0, 0), hunter.getHunterCoord());
    }

    @Test
    public void test_hunter_knowed_monster_coord(){
        ArrayList<Coordinate> fictive_sorted_monster_history = new ArrayList<>();
        fictive_sorted_monster_history.add(new Coordinate(0, 0));
        ArrayList<Coordinate> real_sorted_monster_history = new ArrayList<>();
        real_sorted_monster_history.add((Coordinate)this.hunter.getKnowMonsterCoords().get(1));//premier tour du monstre
        assertEquals(fictive_sorted_monster_history.get(0).getCol(), real_sorted_monster_history.get(0).getCol());
        assertEquals(fictive_sorted_monster_history.get(0).getRow(), real_sorted_monster_history.get(0).getRow());
    }

    @Test
    public void test_get_when_monster_was_here_for_last_time(){
        assertEquals((Integer)1, this.hunter.getLastTurnFromCoordinate(new Coordinate(0, 0)));
        assertNotEquals((Integer)2, this.hunter.getLastTurnFromCoordinate(new Coordinate(0, 0)));
        assertNotEquals((Integer)2, this.hunter.getLastTurnFromCoordinate(new Coordinate(1, 0)));//false car il ne connait pas cette position du monstre
    }
}