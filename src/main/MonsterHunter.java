package main;

import java.io.File;

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
    }
}
