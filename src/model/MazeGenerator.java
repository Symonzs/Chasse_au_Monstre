package model;

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

    private Random random = new Random();

    public MazeGenerator(Integer nbRows, Integer nbCols, Integer wallPercent) {
        wall = new boolean[nbRows][nbCols];
        monster = new TreeMap<>();
        hunter = new TreeMap<>();
        initMazeWall();
        generatePaths(wallPercent);
    }

    /*
     * private void breakWall(Integer wallPercent) {
     * for (int i = 0; i < this.wall.length; i++) {
     * for (int j = 0; j < this.wall[0].length; j++) {
     * if (this.wall[i][j] && (random.nextInt(0, 101) > wallPercent)) {
     * this.wall[i][j] = false;
     * 
     * }
     * }
     * }
     * }
     * 
     * private ICoordinate chooseBorderCoord() {
     * int side = random.nextInt(0, 4);
     * int row = 0;
     * int col = 0;
     * switch (side) {
     * case 0:
     * row = 0;
     * col = random.nextInt(0, this.wall[0].length);
     * break;
     * case 1:
     * row = this.wall.length - 1;
     * col = random.nextInt(0, this.wall[0].length);
     * break;
     * case 2:
     * row = random.nextInt(0, this.wall.length);
     * col = 0;
     * break;
     * case 3:
     * row = random.nextInt(0, this.wall.length);
     * col = this.wall[0].length - 1;
     * break;
     * }
     * return new Coordinate(row, col);
     * }
     * 
     * public void initCells() {
     * for (int i = 0; i < this.wall.length; i++) {
     * for (int j = 0; j < this.wall[0].length; j++) {
     * this.wall[i][j] = true;
     * }
     * }
     * }
     * 
     * public void tunnel() {
     * initCells();
     * monster.put(Maze.turn, chooseBorderCoord());
     * this.wall[monster.get(Maze.turn).getRow()][monster.get(Maze.turn).getCol()] =
     * false;
     * path.add(monster.get(Maze.turn));
     * List<ICoordinate> maxPath = new ArrayList<>();
     * while (!path.isEmpty()) {
     * ICoordinate current = path.get(path.size() - 1);
     * List<ICoordinate> neighbors = getNeighbors(current);
     * if (!neighbors.isEmpty()) {
     * ICoordinate next = neighbors.get(random.nextInt(0, neighbors.size()));
     * path.add(next);
     * wall[next.getRow()][next.getCol()] = false;
     * } else {
     * visited.add(current);
     * path.remove(current);
     * }
     * if (path.size() > maxPath.size()) {
     * maxPath = new ArrayList<>(path);
     * }
     * }
     * exit = maxPath.get(random.nextInt(maxPath.size() / 2, maxPath.size()));
     * }
     * 
     * public List<ICoordinate> getNeighbors(ICoordinate coord) {
     * List<ICoordinate> neighbors = new ArrayList<>();
     * int row = coord.getRow();
     * int col = coord.getCol();
     * ICoordinate up = new Coordinate(row - 1, col);
     * ICoordinate down = new Coordinate(row + 1, col);
     * ICoordinate left = new Coordinate(row, col - 1);
     * ICoordinate right = new Coordinate(row, col + 1);
     * if (row > 0 && nbWall(up) >= 3 && !visited.contains(up)) {
     * neighbors.add(up);
     * }
     * if (row < this.wall.length - 1 && nbWall(down) >= 3 &&
     * !visited.contains(down)) {
     * neighbors.add(down);
     * }
     * if (col > 0 && nbWall(left) >= 3 && !visited.contains(left)) {
     * neighbors.add(left);
     * }
     * if (col < this.wall[0].length - 1 && nbWall(right) >= 3 &&
     * !visited.contains(right)) {
     * neighbors.add(right);
     * }
     * return neighbors;
     * }
     * 
     * private int nbWall(ICoordinate up) {
     * int nb = 0;
     * int row = up.getRow();
     * int col = up.getCol();
     * if (row == 0 || row == this.wall.length - 1 || col == 0 || col ==
     * this.wall[0].length - 1) {
     * nb++;
     * }
     * if (row > 0 && this.wall[row - 1][col]) {
     * nb++;
     * }
     * if (row < this.wall.length - 1 && this.wall[row + 1][col]) {
     * nb++;
     * }
     * if (col > 0 && this.wall[row][col - 1]) {
     * nb++;
     * }
     * if (col < this.wall[0].length - 1 && this.wall[row][col + 1]) {
     * nb++;
     * }
     * return nb;
     * }
     */

    public void initMazeWall() {
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                this.wall[i][j] = true;
            }
        }
    }

    /*
     * - partir du centre du laby
     * - calcul le nombre de case a creuser par rapport au % de mur
     * le diviser par 4
     * - on va crsuer le laby en 4 partie en partant de chaque direction du centre
     * dans l'ordre nord-ouest-est sud
     * - on va cresuer par ligne de taille taillelaby/5 arrondis inferieur a pareil
     * x 2 (ex laby 11x11 entre 2 et 4)
     * - sauf la premiere ligne de chaque morceaux qui fait taillelaby/5 arrondis
     * haut -> taille laby
     * - on continue a creuser des chemin jusqu'a depassé le nombre requis il faut
     * donc retenir le nombre de case CREUSER par chemin et un total
     * - pour choisir la direction on regarde le nombre de voisin on genere un
     * nombre aléatoire represantant le nombre de direction possible
     * - et choisir avec tjr l'ordre nord ouest est sud
     * - exemple si nord et est sont pas dispo on random entre 1 et 2 pour choisir
     * la direction et l'ordre c'est ouest sud
     */

    public void generatePaths(Integer wallPercent) {
        this.initMazeWall();
        Integer cellsNumber = this.wall.length * this.wall[0].length;
        if (cellsNumber % 2 != 0) {
            generatePathsOdd(wallPercent);
        } else {
            generatePathsEven(wallPercent);
        }
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void generatePathsOdd(Integer wallPercent) {
        Integer cellsNumber = this.wall.length * this.wall[0].length;
        Integer emptyCells = (cellsNumber) * (100 - wallPercent) / 100;
        Integer emptyCellsNorth = emptyCells / 4;
        if (emptyCells % 4 != 0) {
            emptyCellsNorth += emptyCells % 4;
        }
        Integer emptyCellsWest = emptyCells / 4;
        Integer emptyCellsEast = emptyCells / 4;
        Integer emptyCellsSouth = emptyCells / 4;
        Stack<ICoordinate> path = new Stack<>();

        path.push(new Coordinate(this.wall.length / 2, this.wall[0].length / 2));
        emptyCellsWest += this.makePath(path, emptyCellsNorth, Cardinals.NORTH, 1);

        path.push(new Coordinate(this.wall.length / 2, this.wall[0].length / 2));
        emptyCellsEast += this.makePath(path, emptyCellsWest, Cardinals.WEST, 1);

        path.push(new Coordinate(this.wall.length / 2, this.wall[0].length / 2));
        emptyCellsSouth += this.makePath(path, emptyCellsEast, Cardinals.EAST, 1);

        path.push(new Coordinate(this.wall.length / 2, this.wall[0].length / 2));
        this.makePath(path, emptyCellsSouth, Cardinals.SOUTH, 1);
    }

    private void generatePathsEven(Integer wallPercent) {
    }

    private Integer makePath(Stack<ICoordinate> path, Integer emptyCellsByPath, Cardinals direction,
            Integer iteration) {
        if (emptyCellsByPath <= 0) {
            path.clear();
            return emptyCellsByPath;
        }
        Integer lineLength = random.nextInt(this.wall.length / 5, (this.wall.length / 5) * 2);
        if (iteration == 1) {
            lineLength = random.nextInt(this.wall.length / 5, this.wall.length / 2);
        }
        emptyCellsByPath -= this.breakLine(path, lineLength, direction);
        Cardinals nextDirection;
        do {
            nextDirection = Cardinals.values()[random.nextInt(0, 4)];
        } while (nextDirection == Cardinals.getOpposite(direction) || directionLeadToBorder(path, nextDirection));
        return this.makePath(path, emptyCellsByPath, nextDirection, iteration + 1);
    }

    private boolean directionLeadToBorder(Stack<ICoordinate> path, Cardinals nextDirection) {
        ICoordinate current = path.peek();
        switch (nextDirection) {
            case NORTH -> {
                if (current.getRow() == 0) {
                    return true;
                }
            }
            case WEST -> {
                if (current.getCol() == 0) {
                    return true;
                }
            }
            case EAST -> {
                if (current.getCol() == this.wall[0].length - 1) {
                    return true;
                }
            }
            case SOUTH -> {
                if (current.getRow() == this.wall.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private Integer breakLine(Stack<ICoordinate> path, Integer lineLength, Cardinals direction) {
        Integer emptyCellsByLine = 0;
        for (int i = 0; i < lineLength; i++) {
            ICoordinate current = path.pop();
            ICoordinate next = new Coordinate(0, 0);
            switch (direction) {
                case NORTH -> next = new Coordinate(current.getRow() - 1, current.getCol());
                case WEST -> next = new Coordinate(current.getRow(), current.getCol() - 1);
                case EAST -> next = new Coordinate(current.getRow(), current.getCol() + 1);
                case SOUTH -> next = new Coordinate(current.getRow() + 1, current.getCol());
            }
            try {
                if (this.breakWall(next)) {
                    emptyCellsByLine++;
                }
                path.push(next);
            } catch (ArrayIndexOutOfBoundsException e) {
                path.push(current);
                break;
            }
        }
        return emptyCellsByLine;
    }

    public boolean breakWall(ICoordinate coord) {
        if (cellIsWall(coord)) {
            this.wall[coord.getRow()][coord.getCol()] = false;
            return true;
        }
        return false;

    }

    public boolean cellIsWall(ICoordinate coord) {
        return this.wall[coord.getRow()][coord.getCol()];
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                if (this.wall[i][j]) {
                    str += "X";
                } else {
                    str += ".";
                }
            }
            str += "\n";
        }
        return str;
    }

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

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            MazeGenerator maze = new MazeGenerator(11, 11, i);
            System.out.println("Pourcentage de mur : " + i + "%");
            System.out.println(maze);
            MazeGenerator.sleep(1000);
        }
    }
}
