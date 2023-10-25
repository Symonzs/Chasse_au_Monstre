package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class CellEvent {

    private Integer turn;
    private CellInfo state;
    private ICoordinate coord;

    public CellEvent(ICoordinate coord, Integer turn, CellInfo state) {
        this.turn = turn;
        this.state = state;
        this.coord = coord;
    }

    public CellEvent(ICoordinate coord, CellInfo state) {
        this(coord, 0, state);
    }

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
