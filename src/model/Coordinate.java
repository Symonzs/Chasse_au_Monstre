package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Coordinate implements ICoordinate {

    private Integer row;
    private Integer col;

    public Coordinate(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((row == null) ? 0 : row.hashCode());
        result = prime * result + ((col == null) ? 0 : col.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        if (row == null) {
            if (other.row != null)
                return false;
        } else if (!row.equals(other.row))
            return false;
        if (col == null) {
            if (other.col != null)
                return false;
        } else if (!col.equals(other.col))
            return false;
        return true;
    }

}
