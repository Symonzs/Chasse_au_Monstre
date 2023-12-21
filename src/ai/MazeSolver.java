package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;
import model.Maze;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Nov 09, 2023
 */
public class MazeSolver implements IMonsterStrategy {
    private Maze maze;
    private boolean[][] wall;
    private List<CursiveCoordinate> movements;

    private static Logger logger = Logger.getLogger(MazeSolver.class.getName());

    /**
     * 
     * @param maze
     */
    public MazeSolver(Maze maze) {
        this.maze = maze;
        initialize(maze.getWall());
    }

    public void loadMovements() {
        CursiveCoordinate start = new CursiveCoordinate(maze.getMonster().get(1).getRow(),
                maze.getMonster().get(1).getCol(), null);
        CursiveCoordinate end = new CursiveCoordinate(maze.getExit().getRow(), maze.getExit().getCol(), null);
        movements = findPath(start, end);
    }

    /**
     * 
     * @return the next movement of the AI
     */
    public ICoordinate play() {
        if (!movements.isEmpty()) {
            ICoordinate nextMove = movements.get(0);
            movements.remove(0);
            return nextMove;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Maze maze = new Maze(11, 11, 50);
            MazeSolver solver = new MazeSolver(maze);
            ICoordinate cell = solver.play();
            while (cell != null) {
                String info = "Cell coord: " + cell.toString();
                logger.log(Level.INFO, info);
                cell = solver.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a path from the start coordinate to the end coordinate in a maze.
     * Uses the A* algorithm to find the shortest path.
     *
     * @param start The starting coordinate.
     * @param end   The ending coordinate.
     * @return A list of CursiveCoordinates representing the path from start to end,
     *         or an empty list of CursiveCoordinates if no path is found.
     */
    private List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate end) {
        Set<CursiveCoordinate> openSet = new HashSet<>();
        Set<CursiveCoordinate> closedSet = new HashSet<>();

        openSet.add(start);

        Map<CursiveCoordinate, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        Map<CursiveCoordinate, Double> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, end));

        while (!openSet.isEmpty()) {
            CursiveCoordinate current = getLowestFScore(openSet, fScore);

            if (current != null && current.equals(end)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            if (current != null)
                for (CursiveCoordinate neighbor : getNeighbors(current)) {
                    if (closedSet.contains(neighbor)) {
                        continue;
                    }

                    double tentativeGScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY)
                            + distanceBetween(current, neighbor);

                    if (!openSet.contains(neighbor)
                            || tentativeGScore < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                        neighbor.setParent(current);
                        gScore.put(neighbor, tentativeGScore);
                        fScore.put(neighbor, tentativeGScore + heuristicCostEstimate(neighbor, end));

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
        }

        return new ArrayList<>();
    }

    private double distanceBetween(CursiveCoordinate a, CursiveCoordinate b) {
        return Math.abs(a.getCol() - b.getCol()) + (double) Math.abs(a.getRow() - b.getRow());
    }

    private double heuristicCostEstimate(CursiveCoordinate current, CursiveCoordinate end) {
        return Math.abs(current.getRow() - end.getRow()) + (double) Math.abs(current.getCol() - end.getCol());
    }

    private CursiveCoordinate getLowestFScore(Set<CursiveCoordinate> openSet,
            Map<CursiveCoordinate, Double> fScore) {
        return openSet.stream().min(Comparator.comparingDouble(fScore::get)).orElse(null);
    }

    private List<CursiveCoordinate> reconstructPath(CursiveCoordinate current) {
        List<CursiveCoordinate> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
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
        if (lowerCell.getRow() < maze.getWall().length && !wall[lowerCell.getRow()][lowerCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row + 1, col, current));
        }
        ICoordinate leftCell = new Coordinate(row, col - 1);
        if (leftCell.getCol() >= 0 && !wall[leftCell.getRow()][leftCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col - 1, current));
        }
        ICoordinate rightCell = new Coordinate(row, col + 1);
        if (rightCell.getCol() < maze.getWall()[1].length && !wall[rightCell.getRow()][rightCell.getCol()]) {
            neighbors.add(new CursiveCoordinate(row, col + 1, current));
        }
        // neighbors.add(new CursiveCoordinate(row - 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row - 1, col + 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col + 1, current));

        return neighbors;
    }

    @Override
    public void update(ICellEvent arg0) {
        // DO NOTHING
    }

    @Override
    public void initialize(boolean[][] arg0) {
        wall = arg0;
        loadMovements();
    }
}
