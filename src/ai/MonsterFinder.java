package ai;

import java.util.Random;

import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;
import model.Maze;
import view.play.HunterView;

public class MonsterFinder implements IHunterStrategy {
    private final Random rand = new Random();

    private int maxCol;
    private int maxRow;

    private HunterView hunterView;

    public MonsterFinder(Maze maze) {
        maxCol = maze.getWall()[0].length - 1;
        maxRow = maze.getWall().length - 1;

        hunterView = new HunterView(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(hunterView);
    }

    public MonsterFinder() {
        maxCol = 0;
        maxRow = 0;
    }

    public HunterView getHunterView() {
        return hunterView;
    }

    public static void main(String[] args) {
        MonsterFinder mf = new MonsterFinder();
        mf.initialize(11, 11);
        for (int i = 0; i < 50; ++i) {
            mf.play();
        }
    }

    @Override
    public ICoordinate play() {
        int randomRow = rand.nextInt(maxRow + 1);
        int randomCol = rand.nextInt(maxCol + 1);
        return new Coordinate(randomRow, randomCol);
    }

    @Override
    public void update(ICellEvent arg0) {
        // DO NOTHING
    }

    @Override
    public void initialize(int arg0, int arg1) {
        maxCol = arg0;
        maxRow = arg1;
    }
}
