package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Maze;

/**
 * Filename:
 * Package:
 *
 * @author <a href=hugo.vallee2.etu@univ-lille.fr>Hugo Vallée</a>
 * @version Nov 09, 2023
 */
public class MazeSolver {
    private Maze maze;
    private List<ICoordinate> path;
    private List<ICoordinate> cellExplored;
    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public MazeSolver(Maze maze) {
        this.cellExplored = new ArrayList<>();
        this.maze = maze;
        this.path = solve(this.maze);
    }

    private List<ICoordinate> backtrackPath(
            ICoordinate cur) {
        List<ICoordinate> path = new ArrayList<>();
        CursiveCoordinate iter = (CursiveCoordinate) cur;

        while (iter != null) {
            path.add(iter);
            iter = iter.getParent();
        }

        return path;
    }

    public List<ICoordinate> solve(Maze maze) {
        LinkedList<ICoordinate> nextToVisit = new LinkedList<>();
        ICoordinate start = maze.getMonster().get(1);
        nextToVisit.add(start);

        while (!nextToVisit.isEmpty()) {
            ICoordinate cur = nextToVisit.remove();

            if (maze.cellIsWall(cur) || cellExplored.contains(cur)) {
                continue;
            }

            if (maze.cellIsWall(cur)) {
                cellExplored.add(cur);
                continue;
            }

            if (maze.getExit().getCol() == cur.getCol() && maze.getExit().getRow() == cur.getRow()) {
                return backtrackPath(cur);
            }

            for (int[] direction : DIRECTIONS) {
                CursiveCoordinate coordinate = new CursiveCoordinate(
                        (Integer) (cur.getRow() + direction[0]),
                        (Integer) (cur.getCol() + direction[1]), (CursiveCoordinate) cur);
                nextToVisit.add(coordinate);
                cellExplored.add(cur);
            }
        }
        return Collections.emptyList();
    }

    /**
     * @return the path
     */
    public List<ICoordinate> getPath() {
        return path;
    }

    public static void main(String[] args) {
        Maze maze = new Maze("/home/infoetu/hugo.vallee2.etu/S3/sae/J1_SAE3A/resources/11x11.csv");
        MazeSolver ms = new MazeSolver(maze);
        System.out.println(ms.getPath());
    }

}
