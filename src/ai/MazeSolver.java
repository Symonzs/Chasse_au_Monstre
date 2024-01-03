package ai;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ai.algorithm.AstarAlgorithm;
import ai.algorithm.IMazeSolverAlgorithm;
import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
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
    private List<CursiveCoordinate> movements;

    private IMazeSolverAlgorithm choosedAlgorithm;

    private static Logger logger = Logger.getLogger(MazeSolver.class.getName());

    /**
     * 
     * @param maze
     */
    public MazeSolver(Maze maze) {
        this.maze = maze;
        this.choosedAlgorithm = new AstarAlgorithm();
        initialize(maze.getWall());
    }

    /**
     * 
     * @param algo
     * @param maze
     */
    public MazeSolver(IMazeSolverAlgorithm algo, Maze maze) {
        this.maze = maze;
        this.choosedAlgorithm = algo;
        initialize(maze.getWall());
    }

    public void loadMovements() {
        CursiveCoordinate start = new CursiveCoordinate(maze.getMonster().get(1).getRow(),
                maze.getMonster().get(1).getCol(), null);
        CursiveCoordinate end = new CursiveCoordinate(maze.getExit().getRow(), maze.getExit().getCol(), null);
        movements = choosedAlgorithm.findPath(start, end);
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
            Maze maze = new Maze("E:\\iutinfo\\S3Custom\\J1_SAE3A\\resources\\map", "11x11.csv");
            MazeSolver solver = new MazeSolver(new AstarAlgorithm(), maze);
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

    @Override
    public void update(ICellEvent arg0) {
        // DO NOTHING
    }

    @Override
    public void initialize(boolean[][] arg0) {
        choosedAlgorithm.setWall(arg0);
        loadMovements();
    }
}
