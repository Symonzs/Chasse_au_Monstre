package ai;

import model.Coordinate;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Nov 09, 2023
 */
public class CursiveCoordinate extends Coordinate {
    private CursiveCoordinate parent;

    public CursiveCoordinate(Integer row, Integer col, CursiveCoordinate cor) {
        super(row, col);
        parent = cor;
    }

    /**
     * @return the parent
     */
    public CursiveCoordinate getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(CursiveCoordinate parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getRow() + ":" + getCol();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
