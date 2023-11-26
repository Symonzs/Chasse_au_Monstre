package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
public class MazeSolver {
    private static Maze maze;

    public MazeSolver(Maze maze) {
        setMaze(maze);
    }

    /**
     * @param maze the maze to set
     */
    public static void setMaze(Maze maze) {
        MazeSolver.maze = maze;
    }

    public static void main(String[] args) {
        maze = new Maze("E:\\iutinfo\\S3\\J1_SAE3A\\resources\\11x11.csv");
        CursiveCoordinate start = new CursiveCoordinate(maze.getMonster().get(1).getRow(),
                maze.getMonster().get(1).getCol(), null);
        CursiveCoordinate end = new CursiveCoordinate(maze.getExit().getRow(), maze.getExit().getCol(), null);

        System.out.println(findPath(start, end));
        // MazeSolver ms = new MazeSolver(maze);
        // System.out.println(ms.isMazeCorrect());
    }

    /**
     * Checks if the maze is correct by performing a breadth-first search.
     * 
     * @return true if the maze is correct, false otherwise.
     */
    public boolean isMazeCorrect() {
        Queue<CursiveCoordinate> f = new LinkedList<CursiveCoordinate>();
        List<CursiveCoordinate> discoveredCell = new ArrayList<>();

        CursiveCoordinate entry = new CursiveCoordinate(maze.getMonster().get(1).getRow(),
                maze.getMonster().get(1).getCol(), null);
        f.add(entry);
        discoveredCell.add(entry);

        boolean estSortieTrouve = false;
        while (!f.isEmpty() && !estSortieTrouve) {
            CursiveCoordinate c = f.peek();
            ICoordinate exit = maze.getExit();
            if (exit.getCol() == c.getCol() && exit.getRow() == c.getRow()) {
                estSortieTrouve = true;
                // return true;
            } else {
                // Haut
                if (c.getRow() - 1 >= 0) {
                    CursiveCoordinate cHaut = new CursiveCoordinate((Integer) (c.getRow() - 1), (Integer) (c.getCol()),
                            c);
                    if (!this.maze.cellIsWall((ICoordinate) cHaut) && !discoveredCell.contains(cHaut)) {
                        f.add(cHaut);
                        discoveredCell.add(cHaut);
                    }
                }

                // Bas
                if (c.getRow() + 1 < maze.getWall().length) {
                    CursiveCoordinate cBas = new CursiveCoordinate((Integer) (c.getRow() + 1), (Integer) (c.getCol()),
                            c);
                    if (!this.maze.cellIsWall((ICoordinate) cBas) && !discoveredCell.contains(cBas)) {
                        f.add(cBas);
                        discoveredCell.contains(cBas);
                    }
                }

                // Gauche
                if (c.getCol() - 1 >= 0) {
                    CursiveCoordinate cGauche = new CursiveCoordinate((Integer) (c.getRow()),
                            (Integer) (c.getCol() - 1), c);
                    if (!this.maze.cellIsWall((ICoordinate) cGauche) && !discoveredCell.contains(cGauche)) {
                        f.add(cGauche);
                        discoveredCell.add(cGauche);
                    }
                }

                // Droite
                if (c.getCol() + 1 < maze.getWall()[1].length) {
                    CursiveCoordinate cDroite = new CursiveCoordinate((Integer) c.getRow(), (Integer) (c.getCol() + 1),
                            c);
                    if (!this.maze.cellIsWall((ICoordinate) cDroite) && !discoveredCell.contains(cDroite)) {
                        f.add(cDroite);
                        discoveredCell.add(cDroite);
                    }
                }

                f.poll();
            }
        }

        for (CursiveCoordinate c : f) {
            int i = 0;
            while (c.getParent() != null) {
                System.out.println(c);
                c = c.getParent();
                ++i;
            }
            System.out.println(c);
            System.out.println(i);

            System.out.println("--------------------");
        }

        return false;
    }

    /**
     * Finds a path from the start coordinate to the end coordinate in a maze.
     * Uses the A* algorithm to find the shortest path.
     *
     * @param start The starting coordinate.
     * @param end   The ending coordinate.
     * @return A list of CursiveCoordinates representing the path from start to end,
     *         or null if no path is found.
     */
    public static List<CursiveCoordinate> findPath(CursiveCoordinate start, CursiveCoordinate end) {
        Set<CursiveCoordinate> openSet = new HashSet<>();
        Set<CursiveCoordinate> closedSet = new HashSet<>();

        openSet.add(start);

        Map<CursiveCoordinate, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        Map<CursiveCoordinate, Double> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, end));

        while (!openSet.isEmpty()) {
            CursiveCoordinate current = getLowestFScore(openSet, fScore);

            if (current.equals(end)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

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

        return null;
    }

    private static double distanceBetween(CursiveCoordinate a, CursiveCoordinate b) {
        return 1.0;
    }

    private static double heuristicCostEstimate(CursiveCoordinate current, CursiveCoordinate end) {
        return Math.abs(current.getRow() - end.getRow()) + Math.abs(current.getCol() - end.getCol());
    }

    private static CursiveCoordinate getLowestFScore(Set<CursiveCoordinate> openSet,
            Map<CursiveCoordinate, Double> fScore) {
        return openSet.stream().min(Comparator.comparingDouble(fScore::get)).orElse(null);
    }

    private static List<CursiveCoordinate> reconstructPath(CursiveCoordinate current) {
        List<CursiveCoordinate> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    private static List<CursiveCoordinate> getNeighbors(CursiveCoordinate current) {
        // Only 4 directions movements
        List<CursiveCoordinate> neighbors = new ArrayList<>();
        int row = current.getRow();
        int col = current.getCol();

        // Add neighbors
        ICoordinate upperCell = new Coordinate(row - 1, col);
        if (upperCell.getRow() >= 0 && !maze.cellIsWall(upperCell)) {
            neighbors.add(new CursiveCoordinate(row - 1, col, current));
        }
        ICoordinate lowerCell = new Coordinate(row + 1, col);
        if (lowerCell.getRow() < maze.getWall().length && !maze.cellIsWall(lowerCell)) {
            neighbors.add(new CursiveCoordinate(row + 1, col, current));
        }
        ICoordinate leftCell = new Coordinate(row, col - 1);
        if (leftCell.getCol() >= 0 && !maze.cellIsWall(leftCell)) {
            neighbors.add(new CursiveCoordinate(row, col - 1, current));
        }
        ICoordinate rightCell = new Coordinate(row, col + 1);
        if (rightCell.getCol() < maze.getWall()[1].length && !maze.cellIsWall(rightCell)) {
            neighbors.add(new CursiveCoordinate(row, col + 1, current));
        }
        // neighbors.add(new CursiveCoordinate(row - 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row - 1, col + 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col - 1, current));
        // neighbors.add(new CursiveCoordinate(row + 1, col + 1, current));

        return neighbors;
    }
}
