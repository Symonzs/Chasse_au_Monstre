package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;

public class Maze extends Subject {

    private boolean[][] wall;
    private Map<Integer, ICoordinate> monster;
    private ICoordinate hunter;
    private ICoordinate exit;
    public static Integer turn = 1;

    Logger log = Logger.getLogger(getClass().getName());

    public Maze(String fileName) {
        loadMaze(fileName);
    }

    public Maze(Integer nbRows, Integer nbCols) {
        loadMaze(String.format("%dx%d.csv", nbRows, nbCols));
    }

    public void loadMaze(String fileName) {
        try {
            List<String> lines = Files.readAllLines(importFile(fileName).toPath(), StandardCharsets.UTF_8);
            Integer nbRows = lines.size();
            Integer nbCols = lines.get(0).split(",").length;
            this.wall = new boolean[nbRows][nbCols];
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] cols = line.split(",");
                for (int j = 0; j < cols.length; j++) {
                    switch (cols[j]) {
                        case "E" -> this.wall[i][j] = false;
                        case "W" -> this.wall[i][j] = true;
                        case "X" -> {
                            this.exit = new Coordinate(i, j);
                            this.wall[i][j] = false;
                        }
                        case "M" -> {
                            this.monster = new TreeMap<>();
                            this.monster.put(turn, new Coordinate(i, j));
                            this.wall[i][j] = false;
                        }
                        default -> throw new InputMismatchException("Caractère non reconnu");
                    }
                }
            }
        } catch (IOException | InputMismatchException e) {
            log.warning(e.getMessage());
        }
    }

    public File importFile(String fileName) throws FileNotFoundException {
        File csv = FileFinder.find(fileName);
        if (csv == null) {
            throw new FileNotFoundException(String.format("Le fichier '%s' n'a pas été trouvé", fileName));
        }
        return csv;
    }

    public void cellUpdate(CellEvent eventRequest) {
        if (CellInfo.HUNTER.equals(eventRequest.getState())) {
            cellUpdateHunter(eventRequest.getCoord(), eventRequest.getTurn());
        } else if (CellInfo.MONSTER.equals(eventRequest.getState())) {
            cellUpdateMonster(eventRequest.getCoord(), eventRequest.getTurn());
        }
    }

    private void cellUpdateMonster(ICoordinate eventCoord, Integer eventTurn) {
        this.monster.put(eventTurn, eventCoord);
        if (this.monsterAtExit()) {
            this.end(CellInfo.MONSTER);
        } else {
            this.notifyObserver(null, new CellEvent(eventCoord, eventTurn, CellInfo.MONSTER));
        }
    }

    private void cellUpdateHunter(ICoordinate eventCoord, Integer eventTurn) {
        CellEvent eventHunter = null;
        CellEvent eventMonster = null;
        this.hunter = eventCoord;
        if (this.monsterWasHere(eventCoord)) {
            if (this.monsterIsHere(eventCoord)) {
                this.end(CellInfo.HUNTER);
            } else {
                eventHunter = new CellEvent(eventCoord, eventTurn, CellInfo.MONSTER);
            }
        } else {
            if (this.cellIsWall(eventCoord)) {
                eventHunter = new CellEvent(eventCoord, CellInfo.WALL);
            } else {
                eventHunter = new CellEvent(eventCoord, CellInfo.EMPTY);
            }
        }
        eventMonster = new CellEvent(eventCoord, CellInfo.HUNTER);
        this.notifyObserver(eventHunter, eventMonster);
    }

    public boolean monsterWasHere(ICoordinate eventCoord) {
        return this.monster.containsValue(eventCoord);
    }

    public boolean monsterIsHere(ICoordinate eventCoord) {
        return this.monster.get(Maze.turn).equals(eventCoord);
    }

    public boolean cellIsWall(ICoordinate eventCoord) {
        return this.wall[eventCoord.getRow()][eventCoord.getCol()];
    }

    public boolean monsterAtExit() {
        return exit.equals(this.monster.get(Maze.turn));
    }

    public void end(CellInfo victoryInfo) {
        throw new UnsupportedOperationException();
    }

    public static void incrementTurn() {
        Maze.turn++;
    }

    public static void resetTurn() {
        Maze.turn = 1;
    }

    public boolean[][] getWall() {
        return this.wall;
    }

    public ICoordinate getExit() {
        return this.exit;
    }

    public Map<Integer, ICoordinate> getMonster() {
        return this.monster;
    }

    public ICoordinate getHunter() {
        return this.hunter;
    }

    public void notifyObserver(Object hunterData, Object monsterData) {
        for (Observer observer : this.attached) {
            if (Hunter.class == observer.getClass()) {
                Hunter hunterTemp = (Hunter) observer;
                if (hunterData != null) {
                    hunterTemp.update(this, hunterData);
                }
            }
            if (Monster.class == observer.getClass()) {
                Monster monsterTemp = (Monster) observer;
                monsterTemp.update(this, hunterData, monsterData);
            }
        }
    }

}
