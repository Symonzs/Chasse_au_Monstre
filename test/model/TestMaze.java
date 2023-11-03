package model;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;


public class TestMaze {

    protected Maze maze;
    protected Monster monster;
    protected Hunter hunter;

    @BeforeEach
    public void setup() {

        Maze.resetTurn();
        maze = new Maze("C:\\Users\\simon\\OneDrive\\Desktop\\J1_SAE3A\\J1_SAE3A\\resources\\4x4.csv");
        monster = new Monster(maze);
        hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(monster);
        maze.attach(hunter);
        maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.HUNTER));
        Maze.incrementTurn();
        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.turn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));
        Maze.incrementTurn();
        maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.turn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 1), CellInfo.HUNTER));

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
    public void test_creation_maze_taille() {
        assertEquals(maze.getWall().length, maze.getWall()[0].length);// test si la longueur est égale a la hauteur du                                                                      // maze
    }

    @Test
    public void test_creation_maze_mur() {
        assertEquals(2, count_wall(maze));
        assertNotEquals(3, count_wall(maze));
    }

    private static int count_wall(Maze maze) {
        int nb = 0;
        for (boolean[] bs : maze.getWall()) {
            for (boolean b : bs) {
                if (b) {
                    nb++;
                }
            }
        }
        return nb;
    }

    @Test

    public void is_this_cell_a_wall() {
        assertTrue(this.maze.getWall()[1][2]);
        assertTrue(this.maze.getWall()[2][1]);
        assertFalse(this.maze.getWall()[0][0]);
        assertFalse(this.maze.getWall()[3][0]);
    }

    @Test
    public void is_this_cell_the_exit() {
        assertEquals(this.maze.getExit(), new Coordinate(3, 0));
        assertNotEquals(this.maze.getExit(), new Coordinate(0, 0));
        assertEquals(this.maze.getExit(), new Coordinate(3, 0));
        assertNotEquals(this.maze.getExit(), new Coordinate(1, 2));
    }

    @Test
    public void test_monster_was_here() {
        // valide
        assertTrue(maze.monsterWasHere(new Coordinate(0, 0)));// test si le monstre est passé par la position de départ
        assertTrue(maze.monsterWasHere(new Coordinate(1, 0)));// test si le monstre est passé par 1,0
        // valide

        // pas valide car case actuelle du monstre
        assertTrue(maze.monsterWasHere(new Coordinate(2, 0)));// test si le monstre est passé par 2,0
        // valide car case actuelle du monstre

        // pas valide le monstre n'est pas passer la
        assertFalse(maze.monsterWasHere(new Coordinate(2, 1)));// test si le monstre est passé par 2,1
        assertFalse(maze.monsterWasHere(new Coordinate(2, 2)));// test si le monstre est passé par 2,2
        // pas valide le monstre n'est pas passer la

        // pas valide coordonnées en dehors du tableaux
        assertFalse(maze.monsterWasHere(new Coordinate(5, 7)));// test si le monstre est passé par 5,7
        assertFalse(maze.monsterWasHere(new Coordinate(3, 3)));// test si le monstre est passé par 3,3
        // pas valide coordonnées en dehors du tableaux
    }

    @Test
    public void test_monster_is_here() {
        // valide
        assertTrue(maze.monsterIsHere(new Coordinate(2, 0)));// test si le monstre est en 2,0
        // valide

        // pas valide car le monstre n'est pas la ACTUELLEMENT
        assertFalse(maze.monsterIsHere(new Coordinate(0, 0)));
        assertFalse(maze.monsterIsHere(new Coordinate(1, 1)));
        assertFalse(maze.monsterIsHere(new Coordinate(2, 1)));
        // pas valide car le monstre n'est pas la ACTUELLEMENT

        // pas valide car en dehors du tableau
        assertFalse(maze.monsterIsHere(new Coordinate(5, 3)));
        assertFalse(maze.monsterIsHere(new Coordinate(2, 9)));
        // pas valide car en dehors du tableau

    }

    @Test
    public void test_end() {
        // pas fait jsais pas comment le tester

        // valide le chasseur tire sur la pose du monstre

        // valide le chasseur tire sur la pose du monstre

        // valide le monstre arrive a la fin du maze

        // valide le monstre arrive a la fin du maze

        // pas valide le chasseur n'a pas tiré sur le monstre et le monstre n'est pas a
        // la sortie

        // pas valide le chasseur n'a pas tiré sur le monstre et le monstre n'est pas a
        // la sortie
    }

    @Test
    public void test_increment_turn() {
        assertEquals(3, Maze.turn);
        assertNotEquals(1, Maze.turn);
    }

    @Test
    public void test_update_monster(){
        this.maze.cellUpdateMonster(new Coordinate(1, 0), 4);
        assertTrue(maze.monsterIsHere(new Coordinate(1, 0)));
        assertFalse(maze.monsterIsHere(new Coordinate(2, 0)));
    }


    @Test
    public void test_cell_update_hunter(){
        this.maze.cellUpdateHunter(new Coordinate(1, 3), 4);
        assertEquals();
    }

    

}