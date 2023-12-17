package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import view.play.HunterView;
import view.play.MonsterView;

public class TestHunter {
    protected Maze maze;
    protected MonsterView monster;
    protected HunterView hunter;

    @BeforeEach
    public void setup() {

        Maze.resetTurn();
        // Pensé a changer le chemin du fichier csv (Pour le moment il est en absolu
        // dans les tests)
        try {
            maze = new Maze("D:\\Document\\IUT\\J1_SAE3A\\resources\\map", "4x4.csv");
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
        }

        maze.attach(monster);
        maze.attach(hunter);
        maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.currentTurn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(2, 1), CellInfo.HUNTER));

    }

    // test qui verifie les murs connus du chasseur
    @Test
    public void test_get_hunter_know_walls() {
        // valide car le hunter a déjà tiré ici et il y avait un mur
        assertTrue(hunter.getHunter().getKnowWall()[2][1]);// test si le chasseur a un mur en 2,1 dans sa liste de mur
        // valide car le hunter a déjà tiré ici et il y avait un mur

        // pas valide car le hunter n'a pas tiré sur le mur en 1,2 donc il ne le connait
        // pas
        assertFalse(hunter.getHunter().getKnowWall()[1][2]);// test si le chasseur a un mur en 1,2 dans sa liste
        // pas valide car le hunter n'a pas tiré sur le mur en 1,2 donc il ne le connait
        // pas
    }

    // test qui verifie les cases vides connus par le hunter
    @Test
    public void test_get_hunter_know_empty_cases() {
        // valide car le hunter a tiré ici et c'est une case vide
        assertTrue(hunter.getHunter().getKnowEmpty()[0][3]);// test si le chasseur a dans sa liste de case vide connus
                                                            // la case 0,3
        // valide car le hunter a tiré ici et c'est une case vide

        // pas valide car le hunter a tiré ici et ce n'etait pas une case vide ou le
        // hunter n'a pas tiré ici
        assertFalse(hunter.getHunter().getKnowEmpty()[0][0]);// test si le chasseur a dans sa liste de case vide connus
                                                             // la case 0,0
        assertFalse(hunter.getHunter().getKnowEmpty()[2][1]);// test si le chasseur a dans sa liste de case vide connus
                                                             // la case 2,1
        assertFalse(hunter.getHunter().getKnowEmpty()[1][3]);// test si le chasseur a dans sa liste de case vide connus
                                                             // la case 1,3
        // pas valide car le hunter a tiré ici et ce n'etait pas une case vide ou le
        // hunter n'a pas tiré ici
    }

    // test qui regarde les coordonnées du dernier tir du chasseur
    @Test
    public void test_get_hunter_last_cord() {
        // valide car c'est le dernier tir du chasseur dans le setup
        assertEquals(new Coordinate(2, 1), hunter.getHunter().getHunterCoord());// test si le dernier tir du chasseur
                                                                                // est en 2,1
        // valide car c'est le dernier tir du chasseur dans le setup

        // pas valide car c'est le deuxieme tour du chasseur dans le setup
        assertNotEquals(new Coordinate(0, 0), hunter.getHunter().getHunterCoord());// test si le dernier tir du chasseur
                                                                                   // est en 0,0
        // pas valide car c'est le deuxieme tour du chasseur dans le setup
    }

    // test qui regarde la liste des position connu du monstre par le chasseur
    @Test
    public void test_hunter_knowed_monster_coord() {
        ArrayList<Coordinate> fictive_sorted_monster_history = new ArrayList<>();// initialisation de la liste fictive
                                                                                 // des emplacement connnus du monstre
                                                                                 // connu par le chasseur
        fictive_sorted_monster_history.add(new Coordinate(0, 0));// ajout du deplacement
        ArrayList<Coordinate> real_sorted_monster_history = new ArrayList<>();// recupération de la liste des position
                                                                              // connu du monstre connu par le chasseur
                                                                              // du programme
        real_sorted_monster_history.add((Coordinate) this.hunter.getHunter().getKnowMonsterCoords());// premier tour du
        // monstre
        // valide car le monstre est deja passé par la et le chasseur a tiré ici
        assertEquals(fictive_sorted_monster_history.get(0).getCol(), real_sorted_monster_history.get(0).getCol());// test
                                                                                                                  // si
                                                                                                                  // l'information
                                                                                                                  // est
                                                                                                                  // bien
                                                                                                                  // contenue
                                                                                                                  // dans
                                                                                                                  // les
                                                                                                                  // deux
                                                                                                                  // listes
        assertEquals(fictive_sorted_monster_history.get(0).getRow(), real_sorted_monster_history.get(0).getRow());// test
                                                                                                                  // si
                                                                                                                  // l'information
                                                                                                                  // est
                                                                                                                  // bien
                                                                                                                  // contenue
                                                                                                                  // dans
                                                                                                                  // les
                                                                                                                  // deux
                                                                                                                  // listes
        // valide car le monstre est deja passé par la et le chasseur a tiré ici
    }

    // test qui regarde si le tour sur les positions connus du monstre par le
    // chasseur
    @Test
    public void test_get_when_monster_was_here_for_last_time() {
        // valide car le chasseur connait la position et obtient le bon tour
        assertEquals((Integer) 1, this.hunter.getHunter().getLastTurnFromCoordinate(new Coordinate(0, 0)));// test si le
                                                                                                           // chasseur
        // recuperent tour 1
        // comme information sur
        // la case 0,0
        // valide car le chasseur connait la position et obtient le bon tour
        // pas valide car le chasseur connait cette position mais le tour ne correspond
        // pas a celui du test
        assertNotEquals((Integer) 2, this.hunter.getHunter().getLastTurnFromCoordinate(new Coordinate(0, 0)));// test si
                                                                                                              // le
                                                                                                              // chasseur
        // recuperent tour 2
        // comme information
        // sur la case 0,0
        // pas valide car le chasseur connait cette position mais le tour ne correspond
        // pas a celui du test
        // pas valide car le chasseur nje connait pas cette position
        assertNotEquals((Integer) 2, this.hunter.getHunter().getLastTurnFromCoordinate(new Coordinate(1, 0)));//// test
                                                                                                              //// si
                                                                                                              //// le
        //// chasseur
        //// recuperent tour 2
        //// comme information
        //// sur la case 0,0
        // pas valide car le chasseur nje connait pas cette position
    }
}