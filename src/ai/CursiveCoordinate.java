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
    private double g;
    private double h;

    public CursiveCoordinate(Integer row, Integer col, CursiveCoordinate cor) {
        super(row, col);
        parent = cor;
    }

    public CursiveCoordinate(Integer row, Integer col, CursiveCoordinate cor, CursiveCoordinate goal) {
        this(row, col, cor);
        this.g = 1;
        this.h = calculateHeuristic(this, goal);
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

    public double getF() {
        return g + h;
    }

    private double calculateHeuristic(CursiveCoordinate current, CursiveCoordinate goal) {
        return Math.hypot((double) goal.getRow() - current.getRow(), (double) goal.getCol() - current.getCol());
    }

}
