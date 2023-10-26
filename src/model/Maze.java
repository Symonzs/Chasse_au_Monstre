package model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import main.MonsterHunter;

public class Maze extends Subject {

    private boolean[][] wall;
    private Map<ICoordinate, Integer> monster;
    private ICoordinate hunter;
    private ICoordinate exit;
    public static Integer turn = 1;

    Logger log = Logger.getLogger(getClass().getName());

    public Maze(Integer nbRows, Integer nbCols) {
        importMaze(String.format("%dx%d.csv", nbRows, nbCols));
    }

    public Maze(String fileName) {
        importMaze(fileName);
    }

    public void importMaze(String fileName) {
        File csv = Maze.find(fileName);
        try {
            List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
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
                            this.monster = new HashMap<>();
                            this.monster.put(new Coordinate(i, j), turn);
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

    public void cellUpdate(CellEvent eventRequest) {
        ICoordinate eventCoord = eventRequest.getCoord();
        CellInfo eventInfo = eventRequest.getState();
        Integer eventTurn = eventRequest.getTurn();
        if (CellInfo.HUNTER.equals(eventInfo)) {
            cellUpdateHunter(eventCoord, eventTurn);
        } else if (CellInfo.MONSTER.equals(eventInfo)) {
            cellUpdateMonster(eventCoord, eventTurn);
        }
    }

    private void cellUpdateMonster(ICoordinate eventCoord, Integer eventTurn) {
        this.monster.put(eventCoord, eventTurn);
        if (this.monsterAtTheEnd(eventCoord)) {
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
            if (this.isWall(eventCoord)) {
                eventHunter = new CellEvent(eventCoord, CellInfo.WALL);
            } else {
                eventHunter = new CellEvent(eventCoord, CellInfo.EMPTY);
            }
        }
        eventMonster = new CellEvent(eventCoord, CellInfo.HUNTER);
        this.notifyObserver(eventHunter, eventMonster);
    }

    public boolean monsterWasHere(ICoordinate coord) {
        return this.monster.containsKey(coord);
    }

    public boolean monsterIsHere(ICoordinate eventCoord) {
        if (this.monsterWasHere(eventCoord)) {
            Integer eventTurn = this.monster.get(eventCoord);
            return eventTurn.equals(Maze.turn);
        }
        return false;
    }

    public boolean isWall(ICoordinate eventCoord) {
        Integer i = eventCoord.getRow();
        Integer j = eventCoord.getCol();
        return this.wall[i][j];
    }

    public boolean monsterAtTheEnd(ICoordinate eventCoord) {
        return this.exit.equals(eventCoord);
    }

    public void end(CellInfo victoryInfo) {
        throw new UnsupportedOperationException();
    }

    public static void incrementTurn() {
        Maze.turn++;
    }

    public boolean[][] getWall() {
        return this.wall;
    }

    public ICoordinate getExit() {
        return this.exit;
    }

    public Map<ICoordinate, Integer> getMonster() {
        return this.monster;
    }

    public ICoordinate getHunter() {
        return this.hunter;
    }

    public void notifyObserver(Object hunterData, Object monsterData) {
        for (Observer observer : this.attached) {
            if (observer instanceof Hunter) {
                Hunter hunterTemp = (Hunter) observer;
                hunterTemp.update(this, hunterData);
            }
            if (observer instanceof Monster) {
                Monster monsterTemp = (Monster) observer;
                monsterTemp.update(this, monsterData);
            }
        }
    }

    public static File find(String path, String fileName) {
        path += File.separator;
        File folder = new File(path);
        File file = null;

        if (!folder.exists())
            return null;
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.isFile() && f.getName().equals(fileName))
                    file = f;
                if (f.isDirectory())
                    file = find(path + f.getName(), fileName);
                if (file != null)
                    return file;
            }
        }

        return file;
    }

    public static File find(String fileName) {
        return find(MonsterHunter.PATH, fileName);
    }

    

}
