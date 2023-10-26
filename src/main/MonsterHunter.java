package main;

import java.io.File;
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
                    coord = new Coordinate(Integer.parseInt(response[0]), Integer.parseInt(response[1]));
                }
                maze.cellUpdate(new CellEvent(coord, CellInfo.HUNTER));
                System.out.println("\033[H\033[2J");
                System.out.println(hunter);
                Thread.sleep(1000);
                System.out.println("\033[H\033[2J");
                System.out.println(monster);
                Maze.incrementTurn();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}