package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class CellEvent {

    private Integer turn;
    private CellInfo state;
    private ICoordinate coord;

    public Integer getTurn() {
        return turn;
    }

    public CellInfo getState() {
        return state;
    }

    public ICoordinate getCoord() {
        return coord;
    }

}
