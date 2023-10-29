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
                System.out.println("\033[H\033[2J");
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
                        ICoordinate lastCoordinate = monster.getCoordinate(Maze.turn - 1);
                        monsterCanMove(maze.getWall(), coord, lastCoordinate);
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

    public static boolean coordIsNeighbor(ICoordinate coordinate1, ICoordinate coordinate2) {
        Integer row1 = coordinate1.getRow();
        Integer col1 = coordinate1.getCol();

        Integer row2 = coordinate2.getRow();
        Integer col2 = coordinate2.getCol();

        if (row1.equals(row2)) {
            return col1.equals(col2 + 1) ^ col1.equals(col2 - 1);
        }
        if (col1.equals(col2)) {
            return row1.equals(row2 + 1) ^ row1.equals(row2 - 1);
        }
        return false;
    }

    public static boolean coordIsDifferent(ICoordinate coordinate1, ICoordinate coordinate2) {
        return !coordinate1.equals(coordinate2);
    }

    public static boolean cellIsWall(boolean[][] wall, ICoordinate coordinate) {
        return wall[coordinate.getRow()][coordinate.getCol()];
    }

    public static boolean monsterCanMove(boolean[][] wall, ICoordinate coordinate1, ICoordinate coordinate2) {
        if (!coordIsDifferent(coordinate1, coordinate2)) {
            throw new InputMismatchException("Le monstre ne peut pas rester sur place");
        }
        if (!coordIsNeighbor(coordinate1, coordinate2)) {
            throw new InputMismatchException("La case n'est pas atteignable par le monstre");
        }
        if (cellIsWall(wall, coordinate1)) {
            throw new InputMismatchException("La case contient un mur");
        }
        return true;
    }
}