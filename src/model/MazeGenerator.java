package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class MazeGenerator {

    private boolean[][] wall;
    private TreeMap<Integer, ICoordinate> monster;
    private TreeMap<Integer, ICoordinate> hunter;
    private ICoordinate exit;

    private Integer emptyCellsExpected;
    private ICoordinate currentCell;
    private Cardinals direction;

    private List<ICoordinate> emptyCells;

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public MazeGenerator(Integer nbRows, Integer nbCols, Integer wallPercent) {
        Maze.resetTurn();
        wall = new boolean[nbRows][nbCols];
        monster = new TreeMap<>();
        hunter = new TreeMap<>();
        emptyCellsExpected = (nbRows * nbCols) * (100 - wallPercent) / 100;
        emptyCells = new ArrayList<>();

        direction = Cardinals.NORTH;

        for (boolean[] row : wall) {
            Arrays.fill(row, true);
        }
        if (emptyCellsExpected < 5) {
            generateSmallestMaze();
        } else {
            generateMaze();
        }
        placeMonsterAndExit();
    }

    private void generateSmallestMaze() {
        currentCell = chooseRandomCell();
        this.dig(currentCell);
        chooseNextDirection();
        this.dig(direction.getNextCell(currentCell));
    }

    private void generateMaze() {
        int baseEmptyCells = emptyCellsExpected / 4;
        int remainder = emptyCellsExpected % 4;

        int emptyCellsNorth = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsWest = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsEast = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsSouth = baseEmptyCells;

        currentCell = new Coordinate(this.wall.length / 2, this.wall[0].length / 2);

        this.dig(currentCell);
        emptyCellsNorth--;

        for (Cardinals direction : Cardinals.values()) {
            currentCell = new Coordinate(this.wall.length / 2, this.wall[0].length / 2);
            this.direction = direction;
            switch (this.direction) {
                case NORTH -> emptyCellsWest += makePath(emptyCellsNorth);
                case WEST -> emptyCellsEast += makePath(emptyCellsWest);
                case EAST -> emptyCellsSouth += makePath(emptyCellsEast);
                case SOUTH -> makePath(emptyCellsSouth);
            }
        }
    }

    private Integer chooseFirstLineLength() {
        int origin;
        int bound;
        int length = this.wall[0].length;
        if (direction == Cardinals.NORTH || direction == Cardinals.SOUTH) {
            length = this.wall.length;
        }
        origin = (length / 5) + 1;
        bound = length;
        return random.nextInt(origin, bound);
    }

    private Integer chooseLineLength() {
        int origin;
        int bound;
        int length = this.wall[0].length;
        if (direction == Cardinals.NORTH || direction == Cardinals.SOUTH) {
            length = this.wall.length;
        }
        origin = (int) (length / 5.0);
        bound = Math.max(2, origin * 2);
        origin = Math.max(1, origin);
        return random.nextInt(origin, bound);
    }

    private Integer breakLine(Integer lineLength) {
        Integer emptyCellsByLine = 0;
        int i = 0;
        while (i < lineLength) {
            ICoordinate next = direction.getNextCell(currentCell);
            if (!isWithinBounds(next)) {
                break;
            }
            emptyCellsByLine += this.dig(next) ? 1 : 0;
            currentCell = next;
            i++;
        }
        return emptyCellsByLine;
    }

    private Integer makePath(Integer emptyCellsByPath) {
        Integer emptyCellsCreated = breakLine(chooseFirstLineLength());
        while (emptyCellsCreated < emptyCellsByPath) {
            emptyCellsCreated += breakLine(chooseLineLength());
            chooseNextDirection();
        }
        return emptyCellsByPath - emptyCellsCreated;
    }

    private void chooseNextDirection() {
        Cardinals nextDirection;
        do {
            nextDirection = Cardinals.values()[random.nextInt(0, Cardinals.values().length)];
        } while (nextDirection == direction.opposite()
                || nextDirection.leadToBorder(currentCell, this.wall[0].length, this.wall.length));
        direction = nextDirection;
    }

    private ICoordinate chooseRandomCell() {
        return new Coordinate(random.nextInt(0, this.wall.length),
                random.nextInt(0, this.wall[0].length));
    }

    private void placeMonsterAndExit() {
        List<ICoordinate> tempEmptyCells = new ArrayList<>(emptyCells);
        monster.put(Maze.currentTurn, getRandomElement(tempEmptyCells));
        tempEmptyCells.remove(monster.get(Maze.currentTurn));
        exit = getRandomElement(tempEmptyCells);
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private boolean dig(ICoordinate coord) {
        boolean hasBeenDug = false;
        if (this.cellIsWall(coord)) {
            this.emptyCells.add(coord);
            this.wall[coord.getRow()][coord.getCol()] = false;
            hasBeenDug = true;
        }
        return hasBeenDug;
    }

    private boolean cellIsWall(ICoordinate coord) {
        return this.wall[coord.getRow()][coord.getCol()];
    }

    private boolean isWithinBounds(ICoordinate coord) {
        return coord.getRow() >= 0 && coord.getRow() < this.wall.length &&
                coord.getCol() >= 0 && coord.getCol() < this.wall[0].length;
    }

    // Getters

    public boolean[][] getWall() {
        return this.wall;
    }

    public TreeMap<Integer, ICoordinate> getMonster() {
        return this.monster;
    }

    public TreeMap<Integer, ICoordinate> getHunter() {
        return this.hunter;
    }

    public ICoordinate getExit() {
        return this.exit;
    }

    public Integer getEmptyCellsNumber() {
        return this.emptyCells.size();
    }

    private enum Cardinals {
        NORTH(-1, 0), WEST(0, -1), EAST(0, 1), SOUTH(1, 0);

        private final int row;
        private final int col;

        private Cardinals(int row, int col) {
            this.row = row;
            this.col = col;
        }

        private ICoordinate getNextCell(ICoordinate coord) {
            return new Coordinate(coord.getRow() + this.row, coord.getCol() + this.col);
        }

        private boolean leadToBorder(ICoordinate current, Integer width, Integer height) {
            boolean leadToBorder = false;
            switch (this) {
                case NORTH -> leadToBorder = current.getRow() == 0;
                case WEST -> leadToBorder = current.getCol() == 0;
                case EAST -> leadToBorder = current.getCol() == width - 1;
                case SOUTH -> leadToBorder = current.getRow() == height - 1;
            }
            return leadToBorder;
        }

        private Cardinals opposite() {
            Cardinals opposite = null;
            switch (this) {
                case NORTH -> opposite = SOUTH;
                case WEST -> opposite = EAST;
                case EAST -> opposite = WEST;
                case SOUTH -> opposite = NORTH;
            }
            return opposite;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                if (this.wall[i][j]) {
                    sb.append("X");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int y = 0; y < 10; y++) {
            MazeGenerator maze = new MazeGenerator(11, 11, 40);
            System.out.println(maze);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
