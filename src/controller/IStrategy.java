package controller;

import model.ICellEvent;
import model.ICoordinate;

public interface IStrategy {
    public ICoordinate play();

    public void update(ICellEvent event);
}
