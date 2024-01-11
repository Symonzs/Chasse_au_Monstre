package ai.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import ai.CursiveCoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Jan 03, 2024
 */
public class ThetaStarAlgorithm implements IMazeSolverAlgorithm {
    private boolean[][] wall;

    @Override
    public void setWall(boolean[][] wall) {
        this.wall = wall;
    }

    /**
     * Finds the path from the start coordinate to the goal coordinate using the
     * Theta* algorithm.
     * 
     * @param start The starting coordinate.
     * @param goal  The goal coordinate.
     * @return The list of coordinates representing the path from start to goal, or
     *         an empty list if no path is found.
     */
    @Override
    public List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate goal) {
        PriorityQueue<CursiveCoordinate> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(CursiveCoordinate::getF));
        Set<CursiveCoordinate> closedSet = new HashSet<>();
        Map<CursiveCoordinate, Double> gScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            CursiveCoordinate current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (CursiveCoordinate neighbor : getNeighbors(current, goal)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.get(current) + distance(current, neighbor);

                // Theta* specific: Line of sight check
                if (hasLineOfSight(current, neighbor)) {
                    // Update parent only if there is a clear line of sight
                    neighbor.setParent(current);
                }

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    gScore.put(neighbor, tentativeGScore);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private boolean hasLineOfSight(CursiveCoordinate from, CursiveCoordinate to) {
        double distance = distance(from, to);
        int steps = (int) Math.ceil(distance);

        for (int i = 1; i <= steps; i++) {
            double ratio = i / distance;
            int checkRow = (int) Math.round(from.getRow() + (to.getRow() - from.getRow()) * ratio);
            int checkCol = (int) Math.round(from.getCol() + (to.getCol() - from.getCol()) * ratio);

            if (wall[checkRow][checkCol]) {
                return false;
            }
        }

        return true;
    }

    private List<CursiveCoordinate> getNeighbors(CursiveCoordinate current, CursiveCoordinate goal) {
        List<CursiveCoordinate> neighbors = new ArrayList<>();
        int row = current.getRow();
        int col = current.getCol();

        // Add neighbors
        ICoordinate upperCell = new Coordinate(row - 1, col);
        if (upperCell.getRow() >= 0 && !wall[upperCell.getRow()][upperCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row - 1, col, current, goal));
        }
        ICoordinate lowerCell = new Coordinate(row + 1, col);
        if (lowerCell.getRow() < wall.length && !wall[lowerCell.getRow()][lowerCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row + 1, col, current, goal));
        }
        ICoordinate leftCell = new Coordinate(row, col - 1);
        if (leftCell.getCol() >= 0 && !wall[leftCell.getRow()][leftCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col - 1, current, goal));
        }
        ICoordinate rightCell = new Coordinate(row, col + 1);
        if (rightCell.getCol() < wall[1].length && !wall[rightCell.getRow()][rightCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col + 1, current, goal));
        }

        // Add diagonal neighbors
        ICoordinate upperLeftCell = new Coordinate(row - 1, col - 1);
        if (upperLeftCell.getRow() >= 0 && upperLeftCell.getCol() >= 0
                && !wall[upperLeftCell.getRow()][upperLeftCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row - 1, col - 1, current, goal));
        }
        ICoordinate upperRightCell = new Coordinate(row - 1, col + 1);
        if (upperRightCell.getRow() >= 0 && upperRightCell.getCol() < wall[1].length
                && !wall[upperRightCell.getRow()][upperRightCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row - 1, col + 1, current, goal));
        }
        ICoordinate lowerLeftCell = new Coordinate(row + 1, col - 1);
        if (lowerLeftCell.getRow() < wall.length && lowerLeftCell.getCol() >= 0
                && !wall[lowerLeftCell.getRow()][lowerLeftCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row + 1, col - 1, current, goal));
        }
        ICoordinate lowerRightCell = new Coordinate(row + 1, col + 1);
        if (lowerRightCell.getRow() < wall.length && lowerRightCell.getCol() < wall[1].length
                && !wall[lowerRightCell.getRow()][lowerRightCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row + 1, col + 1, current, goal));
        }

        return neighbors;
    }

    private double distance(CursiveCoordinate a, CursiveCoordinate b) {
        return Math.hypot((double) b.getRow() - a.getRow(), (double) b.getCol() - a.getCol());
    }

    private List<CursiveCoordinate> reconstructPath(CursiveCoordinate goal) {
        List<CursiveCoordinate> path = new ArrayList<>();
        CursiveCoordinate current = goal;

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        Collections.reverse(path);
        return path;
    }
}
