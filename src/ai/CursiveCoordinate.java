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

}
