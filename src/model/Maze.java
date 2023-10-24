package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import main.MonsterHunter;

public class Maze {

    private boolean[][] wall;
    private Map<ICoordinate, Integer> monster;
    private ICoordinate hunter;
    private ICoordinate exit;
    public Integer turn;

    Logger log = Logger.getLogger(getClass().getName());

    public Maze(Integer nbRows, Integer nbCols) {
        importMaze(String.format("%dx%d.csv", nbRows, nbCols));
    }

    public Maze(String fileName) {
        importMaze(fileName);
    }

    public void importMaze(String fileName) {
        File csv = new File(MonsterHunter.RESOURCES_PATH, fileName);
        List<String> lines;
        try {
            lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
            Integer nbRows = lines.size();
            Integer nbCols = lines.get(0).split(",").length;
            this.wall = new boolean[nbRows][nbCols];
            this.turn = 1;
            Integer i = 0;
            Integer j = 0;
            for (String line : lines) {
                String[] cols = line.split(",");
                for (String string : cols) {
                    switch (string) {
                        case "E" -> this.wall[i][j] = false;
                        case "W" -> this.wall[i][j] = true;
                        case "X" -> {
                            this.exit = new Coordinate(i, j);
                            this.wall[i][j] = false;
                        }
                        case "M" -> {
                            this.monster.put(new Coordinate(i, j), turn);
                            this.wall[i][j] = false;
                        }
                        default -> throw new InputMismatchException("Caractère non reconnu");
                    }
                    j++;
                }
                i++;
                j = 0;
            }
        } catch (IOException | InputMismatchException e) {
            log.warning(e.getMessage());
        }
    }

    public update(CellEvent cell){
        switch (cell.getState()) {
            case HUNTER -> {
                if (monsterIsHere(cell.getCoord())) {
                    this.end(CellInfo.HUNTER);
                } else if {

                }
            }
        }
    }

    /*
     * public void end(CellInfo cellinfo) {
     * 
     * }
     * 
     * public void incrementTurn() {
     * 
     * }
     * 
     * public void notifyObserver(Object hunterData, Object monsterData) {
     * 
     * }
     * 
     * public boolean monsterWasHere(ICoordinate coord) {
     * 
     * }
     * 
     * public boolean monsterIsHere(ICoordinate coord) {
     * 
     * }
     * 
     * public boolean isMonsterAtTheEnd(ICoordinate coord) {
     * 
     * }
     */

    public static void main(String[] args) {
        Maze maze = new Maze(11, 11);
    }

    public boolean[][] getWall() {
        return wall;
    }

}
