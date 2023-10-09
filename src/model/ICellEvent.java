package model;

public interface ICellEvent {
    public Cellinfo getState();

    public Integer getTurn();

    public ICoordinate getCoord();
}
