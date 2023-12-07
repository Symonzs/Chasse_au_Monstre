package ai;

import java.util.Random;

import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;

public class MonsterFinder implements IHunterStrategy {
    private final Random rand = new Random();

    private int maxCol;
    private int maxRow;

    public MonsterFinder() {
        maxCol = 0;
        maxRow = 0;
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
