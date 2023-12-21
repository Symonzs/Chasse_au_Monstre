package ai.algorithm;

import java.util.List;

import ai.CursiveCoordinate;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Dec 21, 2023
 */
public interface IMazeSolverAlgorithm {

    public List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate end);

    public void setWall(boolean[][] wall);

}
