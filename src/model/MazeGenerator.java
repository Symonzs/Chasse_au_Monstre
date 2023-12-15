package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class MazeGenerator {

    private boolean[][] wall;
    private Map<Integer, ICoordinate> monster;
    private Map<Integer, ICoordinate> hunter;
    private ICoordinate exit;

    private Integer cellsNumber;
    private Integer emptyCellsNeeded;
    private Stack<ICoordinate> path;
    private Cardinals direction;

    private Random random = new Random();

    public MazeGenerator(Integer nbRows, Integer nbCols, Integer wallPercent) {
        Maze.resetTurn();
        wall = new boolean[nbRows][nbCols];
        monster = new TreeMap<>();
        hunter = new TreeMap<>();
        cellsNumber = nbRows * nbCols;
        emptyCellsNeeded = (cellsNumber) * (100 - wallPercent) / 100;
        initMazeWall();
        if (emptyCellsNeeded < 5) {
            generateSmallestPaths();
            List<ICoordinate> emptyCells = getAllEmptyCells();
            monster.put(Maze.turn, emptyCells.get(random.nextInt(0, emptyCells.size())));
            emptyCells.remove(monster.get(Maze.turn));
            exit = emptyCells.get(0);
        } else {
            generatePaths();
        }
    }

    private List<ICoordinate> getAllEmptyCells() {
        List<ICoordinate> emptyCells = new ArrayList<>();
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                if (!this.wall[i][j]) {
                    emptyCells.add(new Coordinate(i, j));
                }
            }
        }
        return emptyCells;
    }

    private void initMazeWall() {
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                this.wall[i][j] = true;
            }
        }
    }

    private void generatePaths() {
        int baseEmptyCells = emptyCellsNeeded / 4;
        int remainder = emptyCellsNeeded % 4;

        int emptyCellsNorth = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsWest = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsEast = baseEmptyCells + (remainder-- > 0 ? 1 : 0);
        int emptyCellsSouth = baseEmptyCells;

        path = new Stack<>();
        ICoordinate mazeCenter = new Coordinate(wall.length / 2, wall[0].length / 2);

        for (Cardinals direction : Cardinals.values()) {
            path.push(mazeCenter);
            this.direction = direction;
            switch (direction) {
                case NORTH -> emptyCellsWest += makePath(emptyCellsNorth);
                case WEST -> emptyCellsEast += makePath(emptyCellsWest);
                case EAST -> emptyCellsSouth += makePath(emptyCellsEast);
                case SOUTH -> makePath(emptyCellsSouth);
            }
        }

        do {
            placeMonsterAndExit();
        } while (!areMonsterAndExitFarEnough());
    }

    private void placeMonsterAndExit() {
        List<ICoordinate> emptyCells = getAllEmptyCells();
        monster.put(Maze.turn, emptyCells.get(random.nextInt(0, emptyCells.size())));
        emptyCells.remove(monster.get(Maze.turn));
        exit = emptyCells.get(random.nextInt(0, emptyCells.size()));
    }

    private boolean areMonsterAndExitFarEnough() {
        ICoordinate monsterLocation = monster.get(Maze.turn);
        int minDistance = getEmptyCellsNumber() * 20 / 100;
        int distance = Math.abs(exit.getRow() - monsterLocation.getRow())
                + Math.abs(exit.getCol() - monsterLocation.getCol());
        return distance >= minDistance;
    }

    private void generateSmallestPaths() {
        ICoordinate coord = chooseRandomCell();
        this.breakWall(coord);
        do {
            direction = Cardinals.values()[random.nextInt(0, 4)];
        } while (directionLeadToBorder(coord, direction));
        this.breakWall(getNextCell(coord));
    }

    private ICoordinate chooseRandomCell() {
        return new Coordinate(random.nextInt(0, this.wall.length), random.nextInt(0, this.wall[0].length));
    }

    private Integer makePath(Integer emptyCellsByPath) {
        Integer emptyCellsCreated = breakLine(chooseFirstLineLength());
        while (emptyCellsCreated < emptyCellsByPath) {
            emptyCellsCreated += breakLine(chooseLineLength());
            Cardinals nextDirection;
            do {
                nextDirection = Cardinals.values()[random.nextInt(0, 4)];
            } while (nextDirection == Cardinals.getOpposite(direction)
                    || directionLeadToBorder(path.peek(), nextDirection));
            direction = nextDirection;
        }
        return emptyCellsByPath - emptyCellsCreated;
    }

    private Integer chooseFirstLineLength() {
        Integer origin;
        Integer bound;
        if (direction == Cardinals.NORTH || direction == Cardinals.SOUTH) {
            origin = this.wall.length / 5;
            bound = (int) ((this.wall.length / 5.0) * 2.0);
        } else {
            origin = this.wall[0].length / 5;
            bound = (int) ((this.wall[0].length / 5.0) * 2.0);
        }
        if (origin == 0) {
            origin++;
            bound++;
        }
        return random.nextInt(origin, bound);
    }

    private Integer chooseLineLength() {
        Integer origin;
        Integer bound;
        if (direction == Cardinals.NORTH || direction == Cardinals.SOUTH) {
            origin = (this.wall.length / 5) + 1;
            bound = this.wall.length;
        } else {
            origin = (this.wall[0].length / 5) + 1;
            bound = this.wall[0].length;
        }
        return random.nextInt(origin, bound);
    }

    private Integer breakLine(Integer lineLength) {
        Integer emptyCellsByLine = 0;
        for (int i = 0; i < lineLength; i++) {
            ICoordinate current = path.pop();
            if (this.cellIsWall(current)) {
                this.breakWall(current);
                emptyCellsByLine++;
            }
            ICoordinate next = this.getNextCell(current);
            if (isWithinBounds(next)) {
                if (this.cellIsWall(next)) {
                    this.breakWall(next);
                    emptyCellsByLine++;
                }
                path.push(next);
            } else {
                path.push(current);
                break;
            }
        }
        return emptyCellsByLine;
    }

    private ICoordinate getNextCell(ICoordinate coord) {
        ICoordinate next = new Coordinate(0, 0);
        switch (direction) {
            case NORTH -> next = new Coordinate(coord.getRow() - 1, coord.getCol());
            case WEST -> next = new Coordinate(coord.getRow(), coord.getCol() - 1);
            case EAST -> next = new Coordinate(coord.getRow(), coord.getCol() + 1);
            case SOUTH -> next = new Coordinate(coord.getRow() + 1, coord.getCol());
        }
        return next;
    }

    private void breakWall(ICoordinate coord) {
        this.wall[coord.getRow()][coord.getCol()] = false;
    }

    // Fonction condition

    private boolean cellIsWall(ICoordinate coord) {
        return this.wall[coord.getRow()][coord.getCol()];
    }

    private boolean isWithinBounds(ICoordinate coord) {
        return coord.getRow() >= 0 && coord.getRow() < this.wall.length &&
                coord.getCol() >= 0 && coord.getCol() < this.wall[0].length;
    }

    private boolean directionLeadToBorder(ICoordinate current, Cardinals nextDirection) {
        boolean leadToBorder = false;
        switch (nextDirection) {
            case NORTH -> leadToBorder = current.getRow() == 0;
            case WEST -> leadToBorder = current.getCol() == 0;
            case EAST -> leadToBorder = current.getCol() == this.wall[0].length - 1;
            case SOUTH -> leadToBorder = current.getRow() == this.wall.length - 1;
        }
        return leadToBorder;
    }

    // Getters

    public boolean[][] getWall() {
        return this.wall;
    }

    public Map<Integer, ICoordinate> getMonster() {
        return this.monster;
    }

    public Map<Integer, ICoordinate> getHunter() {
        return this.hunter;
    }

    public ICoordinate getExit() {
        return this.exit;
    }

    private Integer getEmptyCellsNumber() {
        int emptyCells = 0;
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                if (!this.wall[i][j]) {
                    emptyCells++;
                }
            }
        }
        return emptyCells;
    }
}
