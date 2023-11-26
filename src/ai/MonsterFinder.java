package ai;

import java.util.Random;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import model.Coordinate;
import model.Maze;

public class MonsterFinder {
    private static Maze maze;

    private static Random random = new Random();

    public static void main(String[] args) {
    }

    public static void findMonster() {
        int maxRow = maze.getWall().length;
        int maxCol = maze.getWall()[0].length;

        ICoordinate result = shoot(maxRow, maxCol);
    }

    private static ICoordinate shoot(int maxRow, int maxCol) {
        int randomRow = random.nextInt(maxRow) - 1;
        int randomCol = random.nextInt(maxCol) - 1;

        ICoordinate cell = new Coordinate(randomRow, randomCol);
        maze.monsterWasHere(cell);

        return cell != null ? cell : null;
    }

    private static void setSurrondingCell(ICoordinate cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        ICoordinate north = new Coordinate(row - 1, col);
        ICoordinate northEast = new Coordinate(row - 1, col + 1);
        ICoordinate east = new Coordinate(row, col + 1);
        ICoordinate southEast = new Coordinate(row + 1, col + 1);
        ICoordinate south = new Coordinate(row + 1, col);
        ICoordinate southWest = new Coordinate(row + 1, col - 1);
        ICoordinate west = new Coordinate(row, col - 1);
        ICoordinate northWest = new Coordinate(row - 1, col - 1);

        maze.monsterWasHere(north);
        maze.monsterWasHere(south);
        maze.monsterWasHere(east);
        maze.monsterWasHere(west);
        maze.monsterWasHere(northEast);
        maze.monsterWasHere(northWest);
        maze.monsterWasHere(southEast);
        maze.monsterWasHere(southWest);
    }
}
