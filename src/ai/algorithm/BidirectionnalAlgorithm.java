package ai.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ai.CursiveCoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Dec 21, 2023
 */
public class BidirectionnalAlgorithm implements IMazeSolverAlgorithm {
    private boolean[][] wall;

    @Override
    public void setWall(boolean[][] wall) {
        this.wall = wall;
    }

    /**
     * Finds a path between the given start and end coordinates using a
     * bidirectional search algorithm.
     * 
     * @param start The starting coordinate.
     * @param end   The ending coordinate.
     * @return A list of CursiveCoordinates representing the path from start to end,
     *         if a path is found.
     *         Otherwise, an empty list is returned.
     */
    @Override
    public List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate end) {
        Queue<CursiveCoordinate> forwardQueue = new LinkedList<>();
        Queue<CursiveCoordinate> backwardQueue = new LinkedList<>();
        Set<CursiveCoordinate> forwardVisited = new HashSet<>();
        Set<CursiveCoordinate> backwardVisited = new HashSet<>();

        forwardQueue.add(start);
        backwardQueue.add(end);

        while (!forwardQueue.isEmpty() && !backwardQueue.isEmpty()) {
            // Forward search
            CursiveCoordinate currentForward = forwardQueue.poll();
            forwardVisited.add(currentForward);

            for (CursiveCoordinate neighbor : getNeighbors(currentForward)) {
                if (!forwardVisited.contains(neighbor)) {
                    forwardQueue.add(neighbor);
                    neighbor.setParent(currentForward);
                }
            }

            // Backward search
            CursiveCoordinate currentBackward = backwardQueue.poll();
            backwardVisited.add(currentBackward);

            for (CursiveCoordinate neighbor : getNeighbors(currentBackward)) {
                if (forwardVisited.contains(neighbor)) {
                    CursiveCoordinate intersectionStart = null;
                    for (CursiveCoordinate cc : forwardVisited) {
                        if (cc.equals(neighbor)) {
                            intersectionStart = cc;
                            break;
                        }
                    }
                    return reconstructPath(intersectionStart, currentBackward);
                } else if (!backwardVisited.contains(neighbor)) {
                    backwardQueue.add(neighbor);
                    neighbor.setParent(currentBackward);
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    private List<CursiveCoordinate> reconstructPath(CursiveCoordinate intersectionStart,
            CursiveCoordinate intersectionEnd) {
        List<CursiveCoordinate> path = new ArrayList<>();
        CursiveCoordinate current = intersectionStart;

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);

        current = intersectionEnd;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        return path;
    }

    private Set<CursiveCoordinate> getNeighbors(CursiveCoordinate current) {
        Set<CursiveCoordinate> neighbors = new HashSet<>();
        int row = current.getRow();
        int col = current.getCol();

        // Add neighbors
        ICoordinate upperCell = new Coordinate(row - 1, col);
        if (upperCell.getRow() >= 0 && !wall[upperCell.getRow()][upperCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row - 1, col, current));
        }
        ICoordinate lowerCell = new Coordinate(row + 1, col);
        if (lowerCell.getRow() < wall.length && !wall[lowerCell.getRow()][lowerCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row + 1, col, current));
        }
        ICoordinate leftCell = new Coordinate(row, col - 1);
        if (leftCell.getCol() >= 0 && !wall[leftCell.getRow()][leftCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col - 1, current));
        }
        ICoordinate rightCell = new Coordinate(row, col + 1);
        if (rightCell.getCol() < wall[1].length && !wall[rightCell.getRow()][rightCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col + 1, current));
        }
        // neighbors.add(new CursiveCoordinate(row - 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row - 1, col + 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col + 1, current));

        return neighbors;
    }
}
