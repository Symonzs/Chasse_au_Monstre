package model;

import java.util.Map;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class Maze {

    private boolean[][] wall;
    private Map<ICoordinate, Integer> monster;
    private ICoordinate hunter;
    private ICoordinate exit;
    public Integer turn;

    public update(CellEvent cell){

    }

    public Maze(Integer nbRows, Integer nbCols) {

    }

    public Maze(String fileName) {

    }

    public boolean monsterWasHere(ICoordinate coord) {

    }

    public boolean monsterIsHere(ICoordinate coord) {

    }

    public void end(CellInfo cellinfo) {

    }

    public void incrementTurn() {

    }

    public void notifyObserver(Object hunterData, Object monsterData) {

    }

    public boolean isMonsterAtTheEnd(ICoordinate coord) {

    }

    public boolean[][] getWall() {
        return wall;
    }

}
