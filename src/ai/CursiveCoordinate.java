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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CursiveCoordinate other = (CursiveCoordinate) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        return true;
    }
}
