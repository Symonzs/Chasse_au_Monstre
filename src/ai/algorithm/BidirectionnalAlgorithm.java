package ai.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate end) {
        Set<CursiveCoordinate> startVisited = new HashSet<>();
        Set<CursiveCoordinate> endVisited = new HashSet<>();

        Queue<CursiveCoordinate> startQueue = new LinkedList<>();
        Queue<CursiveCoordinate> endQueue = new LinkedList<>();

        Map<CursiveCoordinate, CursiveCoordinate> startParents = new HashMap<>();
        Map<CursiveCoordinate, CursiveCoordinate> endParents = new HashMap<>();

        startQueue.offer(start);
        endQueue.offer(end);

        startVisited.add(start);
        endVisited.add(end);

        while (!startQueue.isEmpty() && !endQueue.isEmpty()) {
            CursiveCoordinate meetingPoint = expandFrontier(startQueue, startVisited, endVisited, startParents);
            if (meetingPoint != null) {
                return reconstructPath(meetingPoint, startParents, endParents);
            }

            meetingPoint = expandFrontier(endQueue, endVisited, startVisited, endParents);
            if (meetingPoint != null) {
                return reconstructPath(meetingPoint, startParents, endParents);
            }
        }

        return new ArrayList<>();
    }

    private CursiveCoordinate expandFrontier(Queue<CursiveCoordinate> queue, Set<CursiveCoordinate> visited,
            Set<CursiveCoordinate> oppositeVisited, Map<CursiveCoordinate, CursiveCoordinate> parents) {
        int size = queue.size();

        for (int i = 0; i < size; ++i) {
            CursiveCoordinate current = queue.poll();

            for (CursiveCoordinate neighbor : getNeighbors(current)) {
                if (oppositeVisited.contains(neighbor)) {
                    return neighbor;
                }

                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    parents.put(neighbor, current);
                }
            }
        }

        return null;
    }

    private List<CursiveCoordinate> getNeighbors(CursiveCoordinate current) {
        List<CursiveCoordinate> neighbors = new ArrayList<>();
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

        return neighbors;
    }

    private List<CursiveCoordinate> reconstructPath(CursiveCoordinate meetingPoint,
            Map<CursiveCoordinate, CursiveCoordinate> startParents,
            Map<CursiveCoordinate, CursiveCoordinate> endParents) {
        List<CursiveCoordinate> path = new ArrayList<>();

        CursiveCoordinate current = meetingPoint;
        while (current != null) {
            path.add(current);
            current = startParents.get(current);
        }

        if (meetingPoint != null) {
            current = endParents.get(meetingPoint.getParent());
            while (current != null) {
                path.add(current);
                current = endParents.get(current);
            }
        }

        Collections.reverse(path);

        return path;
    }
}
