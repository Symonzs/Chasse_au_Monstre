package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestMaze {

    protected Maze maze;
    protected Monster monster;
    protected Hunter hunter;

    @BeforeEach
    public void setup() {

        Maze.resetTurn();
        // Pensé a changer le chemin du fichier csv (Pour le moment il est en absolu
        // dans les tests)
        try {
            maze = new Maze("/home/infoetu/raphael.kiecken.etu/S3/S3.02/J1_SAE3A/resources/map", "4x4.csv");
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
        }
        monster = new Monster(maze);
        hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.cellUpdate(new CellEvent(new Coordinate(0, 3), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 0), CellInfo.HUNTER));

        maze.cellUpdate(new CellEvent(new Coordinate(2, 0), Maze.currentTurn, CellInfo.MONSTER));
        maze.cellUpdate(new CellEvent(new Coordinate(0, 1), CellInfo.HUNTER));

    }

    // test qui verifie si l'état d'une case se modifie bien apres avoir recu une
    // modification
    @Test
    public void test_cell_update() {
        // partie monster
        // pas valide car le monstre n'est pas ici
        assertFalse(this.maze.monsterIsHere(new Coordinate(1, 0)));// test si le monstre est en 1,0
        // pas valide car le monstre n'est pas ici

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));// deplacement du
                                                                                                 // monstre en 1,0

        // valide car apres le deplacement le monstre se trouve bien ici
        assertTrue(this.maze.monsterIsHere(new Coordinate(1, 0)));// test si le monstre est 1,0
        // valide car apres le deplacement le monstre se trouve bien ici
        // partie monster

        // partie hunter
        // pas valide car le chasseur n'a pas tiré ici
        assertNotEquals(this.maze.getLastHunterCoordinate(), new Coordinate(3, 3));// test si le chasseur a tiré en 3,3
        // pas valide car le chasseur n'a pas tiré ici

        maze.cellUpdate(new CellEvent(new Coordinate(3, 3), CellInfo.HUNTER));// tir du chasseur en 3,3

        // valide car apres modification le chasseur a bien tiré sur cette case
        assertEquals(this.maze.getLastHunterCoordinate(), new Coordinate(3, 3));// test si le chasseur a tiré en 3,3
        // valide car apres modification le chasseur a bien tiré sur cette case
        // partie hunter
    }

    /*
     * @Test
     * public void test_wrong_char_in_csv(){
     * InputMismatchException thrown = assertThrows(
     * InputMismatchException.class,
     * () -> maze = new
     * Maze("/home/infoetu/simon.hayart.etu/J1_SAE3A/resources/map/5x5.csv"),
     * "Caractère non reconnu"
     * );
     * 
     * assertTrue(thrown.getMessage().contains("Caractère non reconnu"));
     * }
     */

    @Test
    public void test_right_maze_size_at_creation() {
        assertEquals(maze.getWall().length, maze.getWall()[0].length);// test si la longueur est égale a la hauteur du
                                                                      // // maze
    }

    // Test qui verifie si le labyrinthe est bien initialisé avec des murs
    @Test
    public void test_right_wall_initiation_from_maze() {
        // valide car il y a 2 murs au total dans le labyrinthe
        assertEquals(2, count_wall(maze)); // test si il y a 2 murs dans tout le labyrinthe
        // valide car il y a 2 murs au total dans le labyrinthe

        // pas valide car il y a 2 murs au total dans le labyrinthe
        assertNotEquals(3, count_wall(maze));// test si il y a 3 murs dans tout le labyrinthe
        // valide car il y a 2 murs au total dans le labyrinthe
    }

    // methode pour compter les mur uniquement utile aux tests
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

    // Test qui regarde précisement les cases pour voir si les murs sont bien placés
    @Test
    public void is_this_cell_a_wall() {
        // valide car il y a bien un mur a cet emplacement
        assertTrue(this.maze.getWall()[1][2]);// test si il y a un mur en 1,2
        assertTrue(this.maze.getWall()[2][1]);// test si il y a un mur en 2,1
        // valide car il y a bien un mur a cet emplacement

        // pas valide car les cases visé ne sont pas des murs
        assertFalse(this.maze.getWall()[0][0]);// test si il y a un mur en 0,0
        assertFalse(this.maze.getWall()[3][0]);// test si il y a un mur en 3,0
        // pas valide car les cases visé ne sont pas des murs
    }

    // Test qui sonde une case pour regarder si il s'agit de la sortie
    @Test
    public void is_this_cell_the_exit() {
        // valide car il s'agit bien de la sortie du labyrinthe
        assertEquals(this.maze.getExit(), new Coordinate(3, 0));// test si la case 3,0 est la sortie
        // valide car il s'agit bien de la sortie du labyrinthe

        // pas valide car il ne s'agit pas de la sortie
        assertNotEquals(this.maze.getExit(), new Coordinate(0, 0));// test si la case 0,0 est la sortie
        assertNotEquals(this.maze.getExit(), new Coordinate(1, 2));// test si la case 1,2 est la sortie
        // pas valide car il ne s'agit pas de la sortie
    }

    // test qui regarde si le monstre est DEJA passé par cette case
    @Test
    public void test_monster_was_here() {
        // valide car le monstre est déjà passé par la
        assertTrue(maze.monsterhasBeenHere(new Coordinate(0, 0)));// test si le monstre est passé par la position de
                                                                  // départ
        assertTrue(maze.monsterhasBeenHere(new Coordinate(1, 0)));// test si le monstre est passé par 1,0
        // valide car le monstre est déjà passé par la

        // pas valide car case actuelle du monstre
        assertTrue(maze.monsterhasBeenHere(new Coordinate(2, 0)));// test si le monstre est passé par 2,0
        // pas valide car case actuelle du monstre

        // pas valide le monstre n'est pas passer la
        assertFalse(maze.monsterhasBeenHere(new Coordinate(2, 1)));// test si le monstre est passé par 2,1
        assertFalse(maze.monsterhasBeenHere(new Coordinate(2, 2)));// test si le monstre est passé par 2,2
        // pas valide le monstre n'est pas passer la

        // pas valide coordonnées en dehors du tableaux
        assertFalse(maze.monsterhasBeenHere(new Coordinate(5, 7)));// test si le monstre est passé par 5,7
        assertFalse(maze.monsterhasBeenHere(new Coordinate(3, 3)));// test si le monstre est passé par 3,3
        // pas valide coordonnées en dehors du tableaux
    }

    // test si le monstre est ACTUELLEMENT ici
    @Test
    public void test_is_monster_here() {
        // valide car c'est la position actuelle du monstre
        assertTrue(maze.monsterIsHere(new Coordinate(2, 0)));// test si le monstre est en 2,0
        // valide car c'est la position actuelle du monstre

        // pas valide car le monstre n'est pas la ACTUELLEMENT
        assertFalse(maze.monsterIsHere(new Coordinate(0, 0)));// test si le monstre est en 0,0
        assertFalse(maze.monsterIsHere(new Coordinate(1, 1)));// test si le monstre est en 1,1
        assertFalse(maze.monsterIsHere(new Coordinate(2, 1)));// test si le monstre est en 2,1
        // pas valide car le monstre n'est pas la ACTUELLEMENT

        // pas valide car en dehors du tableau
        assertFalse(maze.monsterIsHere(new Coordinate(5, 3)));
        assertFalse(maze.monsterIsHere(new Coordinate(2, 9)));
        // pas valide car en dehors du tableau

    }

    // test qui verifie que le compteur de tour s'incremente bien à chaque nouveau
    // tir de chasseur
    @Test
    public void test_increment_turn() {
        // valide car c'est le tour actuel apres le setup
        assertEquals(4, Maze.currentTurn);// test si le tour actuel est 4
        // valide car c'est le tour actuel apres le setup

        // pas valide car ce n'est pas le tour actuel du setup
        assertNotEquals(1, Maze.currentTurn);// test si le tour actuel est 1
        // pas valide car ce n'est pas le tour actuel du setup

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));// deplacement du
                                                                                                 // monstre en 1,0

        // pas valide car le tour ne s'incremente pas après un deplacement du monstre
        assertNotEquals(5, Maze.currentTurn);// test si le tour actuel est 5
        // pas valide car le tour ne s'incremente pas après un deplacement du monstre

        maze.cellUpdate(new CellEvent(new Coordinate(3, 3), CellInfo.HUNTER));// tir du chasseur en 3,3

        // valide car apres le tour du chasseur le tour doit s'incémenter
        assertEquals(5, Maze.currentTurn);// test si le tour actuel est 5
        // valide car apres le tour du chasseur le tour doit s'incémenter
    }

    // test qui regarde si le monstre se trouve sur la case sortie
    @Test
    public void test_monster_is_at_exit() {
        // pas valide car il n'est pas actuellement sur la case sortie
        assertFalse(maze.monsterIsAtExit());// test si le monstre est sur la sortie
        // pas valide car il n'est pas actuellement sur la case sortie

        maze.cellUpdate(new CellEvent(new Coordinate(3, 0), Maze.currentTurn, CellInfo.MONSTER));// deplacement du
                                                                                                 // monstre en 3,0 ou se
                                                                                                 // trouve la sortie

        // valide car le monstre se trouve maintenant sur la sortie
        assertTrue(maze.monsterIsAtExit());// test si le monstre est sur la sortie
        // valide car le monstre se trouve maintenant sur la sortie
    }

    // test qui regarde les dernière coordonnées du monstre
    @Test
    public void test_get_last_monster_coordinate() {
        // valide car c'est les dernières coordonnées du monstre dans le setup
        assertEquals(new Coordinate(2, 0), this.maze.getLastMonsterCoordinate());// test si les dernières coordonnées du
                                                                                 // monstre sont 2,0
        // valide car c'est les dernières coordonnées du monstre dans le setup

        // pas valide car ce n'est pas les dernières coordonnées du monstre dans le
        // setup
        assertNotEquals(new Coordinate(1, 0), this.maze.getLastMonsterCoordinate());// test si les dernières coordonnées
                                                                                    // du monstre sont 1,0
        // pas valide car ce n'est pas les dernières coordonnées du monstre dans le
        // setup
    }

    // test qui regarde les dernières coordonnées du chasseur (dernier tir)
    @Test
    public void test_get_last_hunter_coordinate() {
        // valide car c'est les dernières coordonnées du hunter dans le setup
        assertEquals(new Coordinate(0, 1), this.maze.getLastHunterCoordinate());// test si les dernières coordonnées du
                                                                                // hunter sont 1,0
        // valide car c'est les dernières coordonnées du hunter dans le setup

        // pas valide car ce n'est pas les dernières coordonnées du hunter dans le setup
        assertNotEquals(new Coordinate(1, 0), this.maze.getLastHunterCoordinate());// test si les dernières coordonnées
                                                                                   // du hunter sont 1,0
        // pas valide car ce n'est pas les dernières coordonnées du hunter dans le setup
    }

    // test qui regarde si la prtie est finie par le MONSTRE
    @Test
    public void test_game_is_end_with_monster() {
        // valide car le monstre n'est pas sur la case sortie
        assertEquals(this.maze.getWinner(), null);// test si le jeu a un winner
        // valide car le monstre n'est pas sur la case sortie

        maze.cellUpdate(new CellEvent(new Coordinate(3, 0), Maze.currentTurn, CellInfo.MONSTER));// deplacement du
                                                                                                 // monstre en 3,0 (sur
                                                                                                 // la sortie)

        // pas valide car le hunter n'a pas tiré sur le monstre
        assertNotEquals(CellInfo.HUNTER, this.maze.getWinner());// test si le jeu a un winner hunter
        // pas valide car le hunter n'a pas tiré sur le monstre

        // valide car le monstre est maintenant sur la sortie
        assertEquals(CellInfo.MONSTER, this.maze.getWinner());// test si le jeu a un winner monstre
        // valide car le monstre est maintenant sur la sortie
    }

    // test si la partie est finie par le HUNTER
    @Test
    public void test_game_is_end_with_hunter() {
        // valide car le huntre n'a pas tuer le monstre
        assertEquals(this.maze.getWinner(), null);// test si le jeu a un winner hunter
        // valide car le huntre n'a pas tuer le monstre

        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), Maze.currentTurn, CellInfo.MONSTER));// deplacement du
                                                                                                 // monstre en 1,0
        maze.cellUpdate(new CellEvent(new Coordinate(1, 0), CellInfo.HUNTER));// tir du chasseur en 1,0 (sur le monstre)

        // valide car le hunter a tuer le monstre
        assertEquals(CellInfo.HUNTER, this.maze.getWinner());// test si le jeu a un winner hunter
        // valide car le hunter a tuer le monstre

        // pas valide car le monstre n'est pas sur la case de sortie
        assertNotEquals(CellInfo.MONSTER, this.maze.getWinner());// test si le jeu a un winner monstre
        // pas valide car le monstre n'est pas sur la case de sortie
    }

    // test si le reboot du compteur de tour fonctionne
    @Test
    public void test_reset_turn() {
        // pas valide car ce n'est pas le tour actuel
        assertNotEquals(1, Maze.currentTurn);// test si le tour actuel est 1
        // pas valide car ce n'est pas le tour actuel

        // valide car le tour actuel est 4
        assertEquals(4, Maze.currentTurn);// test si le tour actuel est 4
        // valide car le tour actuel est 4

        Maze.resetTurn();// reset du compteur a 1

        // valide car le tour actuel est maintenant 1
        assertEquals(1, Maze.currentTurn);// test si le tour actuel est 1
        // valide car le tour actuel est maintenant 1
    }

    // test si l'historique de deplacement du monstre est valide
    @Test
    public void test_get_monster_history() {
        ArrayList<Coordinate> fictive_sorted_monster_history = new ArrayList<>();// creation d'une liste pour stocker
                                                                                 // les deplacement du monstre du setup
        fictive_sorted_monster_history.add(new Coordinate(1, 0));// ajout du premier deplacement
        fictive_sorted_monster_history.add(new Coordinate(2, 0));// ajout du deuxieme deplacement
        ArrayList<Coordinate> real_sorted_monster_history = new ArrayList<>();// creation d'une liste pour stocker les
                                                                              // deplacement du monstre que la methode
                                                                              // recupère
        real_sorted_monster_history.add((Coordinate) this.maze.getMonster().get(2));// premier tour du monstre
        real_sorted_monster_history.add((Coordinate) this.maze.getMonster().get(3));// deuxieme tour du monstre

        // valide car les déplacement sont identique dans les deux listes
        assertEquals(fictive_sorted_monster_history.get(0).getCol(), real_sorted_monster_history.get(0).getCol());// test
                                                                                                                  // si
                                                                                                                  // le
                                                                                                                  // tour
                                                                                                                  // 1
                                                                                                                  // des
                                                                                                                  // deux
                                                                                                                  // liste
                                                                                                                  // sont
                                                                                                                  // identiques
        assertEquals(fictive_sorted_monster_history.get(0).getRow(), real_sorted_monster_history.get(0).getRow());// test
                                                                                                                  // si
                                                                                                                  // le
                                                                                                                  // tour
                                                                                                                  // 1
                                                                                                                  // des
                                                                                                                  // deux
                                                                                                                  // liste
                                                                                                                  // sont
                                                                                                                  // identiques
        assertEquals(fictive_sorted_monster_history.get(1).getCol(), real_sorted_monster_history.get(1).getCol());// test
                                                                                                                  // si
                                                                                                                  // le
                                                                                                                  // tour
                                                                                                                  // 2
                                                                                                                  // des
                                                                                                                  // deux
                                                                                                                  // liste
                                                                                                                  // sont
                                                                                                                  // identiques
        assertEquals(fictive_sorted_monster_history.get(1).getRow(), real_sorted_monster_history.get(1).getRow());// test
                                                                                                                  // si
                                                                                                                  // le
                                                                                                                  // tour
                                                                                                                  // 2
                                                                                                                  // des
                                                                                                                  // deux
                                                                                                                  // liste
                                                                                                                  // sont
                                                                                                                  // identiques
        // valide car les déplacement sont identique dans les deux listes

        // pas valide car car le tour 0 est bien différent du tour 2
        assertNotEquals(fictive_sorted_monster_history.get(0).getRow(), real_sorted_monster_history.get(1).getRow());// test
                                                                                                                     // si
                                                                                                                     // le
                                                                                                                     // tour
                                                                                                                     // 1
                                                                                                                     // est
                                                                                                                     // le
                                                                                                                     // meme
                                                                                                                     // que
                                                                                                                     // le
                                                                                                                     // tour
                                                                                                                     // 2
        // pas valide car car le tour 0 est bien différent du tour 2
    }

    // test si l'historique du chasseur est valide
    @Test
    public void test_get_hunter_history() {
        ArrayList<Coordinate> fictive_sorted_hunter_history = new ArrayList<>();// creation d'une liste pour stocker les
                                                                                // tir du chasseur du setup
        fictive_sorted_hunter_history.add(new Coordinate(0, 3));// ajout du premier tir
        fictive_sorted_hunter_history.add(new Coordinate(0, 0));// ajout du deuxieme tir
        fictive_sorted_hunter_history.add(new Coordinate(0, 1));// ajout du troisième tir
        ArrayList<Coordinate> real_sorted_hunter_history = new ArrayList<>();// creation d'une liste pour stocker les
                                                                             // tir du chasseur que la méthode récupère
        real_sorted_hunter_history.add((Coordinate) this.maze.getHunter().get(1));// premier tour du chasseur
        real_sorted_hunter_history.add((Coordinate) this.maze.getHunter().get(2));// deuxieme tour du chasseur
        real_sorted_hunter_history.add((Coordinate) this.maze.getHunter().get(3));// troisieme tour du chasseur
        // valide car les tours correspondent bien
        assertEquals(fictive_sorted_hunter_history.get(0).getCol(), real_sorted_hunter_history.get(0).getCol());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 1 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        assertEquals(fictive_sorted_hunter_history.get(0).getRow(), real_sorted_hunter_history.get(0).getRow());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 1 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        assertEquals(fictive_sorted_hunter_history.get(1).getCol(), real_sorted_hunter_history.get(1).getCol());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 2 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        assertEquals(fictive_sorted_hunter_history.get(1).getRow(), real_sorted_hunter_history.get(1).getRow());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 2 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        assertEquals(fictive_sorted_hunter_history.get(2).getCol(), real_sorted_hunter_history.get(2).getCol());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 3 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        assertEquals(fictive_sorted_hunter_history.get(2).getRow(), real_sorted_hunter_history.get(2).getRow());// test
                                                                                                                // si
                                                                                                                // les
                                                                                                                // tour
                                                                                                                // 3 du
                                                                                                                // chasseursont
                                                                                                                // identiques
        // valide car les tours correspondent bien

        // pas valide car les tour sont différents
        assertNotEquals(fictive_sorted_hunter_history.get(0).getCol(), real_sorted_hunter_history.get(1).getCol());// test
                                                                                                                   // si
                                                                                                                   // le
                                                                                                                   // tour
                                                                                                                   // 1
                                                                                                                   // et
                                                                                                                   // 2
                                                                                                                   // sont
                                                                                                                   // identiques
        assertNotEquals(fictive_sorted_hunter_history.get(1).getCol(), real_sorted_hunter_history.get(2).getCol());// test
                                                                                                                   // si
                                                                                                                   // le
                                                                                                                   // tour
                                                                                                                   // 2
                                                                                                                   // et
                                                                                                                   // 3
                                                                                                                   // sont
                                                                                                                   // identiques
        // pas valide car les tour sont différents

    }

    // test la construction d'un labyrinthe avec la largeur et la longueur en
    // paramètre
    @Test
    public void test_maze_constructor_with_only_heigth_and_width() {
        Maze mazetest = new Maze(5, 6);// créer un labyrinthe de longueur 5 et de hauteur 6

        // valide car les tailles correspondent bien
        assertEquals(mazetest.getWall().length, 5);// test si la largeur du labyrinthe est 5
        assertEquals(mazetest.getWall()[0].length, 6);// test si la largeur du labyrinthe est 6
        // valide car les tailles correspondent bien
    }

    // test la construction d'un labyrinthe avec la largeur,la longueur et le
    // pourcentage de mur en parametre
    @Test
    public void test_maze_constructor_with_wall_percentage() {
        Maze mazetest = new Maze(5, 6, 50);// créer un labyrinthe de longueur 5 et de hauteur 6 et avec 50% de murs

        // valide
        assertEquals(mazetest.getWall().length, 5);// test si la largeur du labyrinthe est 5
        assertEquals(mazetest.getWall()[0].length, 6);// test si la largeur du labyrinthe est 6
        assertTrue(count_wall(mazetest) > 12 && count_wall(mazetest) < 18);// test si le nombre de mur est en accord
                                                                           // avec le pourcentage demandé
        // valide
    }

}