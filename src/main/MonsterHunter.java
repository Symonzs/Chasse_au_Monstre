package main;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import model.CellEvent;
import model.Coordinate;
import model.Hunter;
import model.Maze;
import model.Monster;

public class MonsterHunter {

    public static final String PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PATH + File.separator + "resources";

    public static void main(String[] args) {
        Maze maze = new Maze(11, 11);
        Monster monster = new Monster(maze);
        Hunter hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(monster);
        maze.attach(hunter);
        try (Scanner scan = new Scanner(System.in)) {
            while (Maze.turn < 5) {
                System.out.println(hunter);
                ICoordinate coord = null;
                while (coord == null) {
                    System.out.print("\nOù voulez-vous tirer (ligne,colone) : ");
                    String[] response = scan.next().split(",");
                    try {
                        coord = new Coordinate(Integer.parseInt(response[0]), Integer.parseInt(response[1]));
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                    }
                }
                maze.cellUpdate(new CellEvent(coord, CellInfo.HUNTER));
                System.out.println("\033[H\033[2J");
                System.out.println(hunter);
                Thread.sleep(4000);
                System.out.println("\033[H\033[2J");
                System.out.println(monster);
                Maze.incrementTurn();
                coord = null;
                while (coord == null) {
                    System.out.print("\nOù voulez-vous vous déplacer (ligne,colone) : ");
                    String[] response = scan.next().split(",");
                    try {
                        coord = new Coordinate(Integer.parseInt(response[0]), Integer.parseInt(response[1]));
                        ICoordinate lastCoordinate = monster.getLastCoordinate();
                        if (!cellIsNeighbor(coord, lastCoordinate)) {
                            throw new InputMismatchException("La case n'est pas atteignable par le monstre");
                        }
                    } catch (NumberFormatException | InputMismatchException e) {
                        coord = null;
                        System.err.println(e.getMessage());
                    }
                }
                maze.cellUpdate(new CellEvent(coord, Maze.turn, CellInfo.MONSTER));
                System.out.println("\033[H\033[2J");
                System.out.println(monster);
                Thread.sleep(4000);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean cellIsNeighbor(ICoordinate coordinate1, ICoordinate coordinate2) {
        Integer col1 = coordinate1.getCol();
        Integer row1 = coordinate1.getRow();

        Integer col2 = coordinate2.getCol();
        Integer row2 = coordinate2.getRow();

        return (col1.equals(col2 - 1) || col1.equals(col2 + 1)) && (row1.equals(row2 - 1) || row1.equals(row2 + 1));

    }
}