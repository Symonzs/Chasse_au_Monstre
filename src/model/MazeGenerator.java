package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class MazeGenerator {

    private final Random random = new Random();

    private List<ICoordinate> path;
    private List<ICoordinate> visited;

    private boolean[][] wall;
    private Map<Integer, ICoordinate> monster;
    private Map<Integer, ICoordinate> hunter;
    private ICoordinate exit;

    public MazeGenerator(Integer nbRows, Integer nbCols, Integer wallPercent) {
        wall = new boolean[nbRows][nbCols];
        path = new ArrayList<>();
        visited = new ArrayList<>();
        monster = new TreeMap<>();
        hunter = new TreeMap<>();
        tunnel();
        breakWall(wallPercent);
    }

    private void breakWall(Integer wallPercent) {
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                if (this.wall[i][j] && (random.nextInt(0, 101) > wallPercent)) {
                    this.wall[i][j] = false;

                }
            }
        }
    }

    private ICoordinate chooseBorderCoord() {
        int side = random.nextInt(0, 4);
        int row = 0;
        int col = 0;
        switch (side) {
            case 0:
                row = 0;
                col = random.nextInt(0, this.wall[0].length);
                break;
            case 1:
                row = this.wall.length - 1;
                col = random.nextInt(0, this.wall[0].length);
                break;
            case 2:
                row = random.nextInt(0, this.wall.length);
                col = 0;
                break;
            case 3:
                row = random.nextInt(0, this.wall.length);
                col = this.wall[0].length - 1;
                break;
        }
        return new Coordinate(row, col);
    }

    public void initCells() {
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[0].length; j++) {
                this.wall[i][j] = true;
            }
        }
    }

    public void tunnel() {
        initCells();
        monster.put(Maze.turn, chooseBorderCoord());
        this.wall[monster.get(Maze.turn).getRow()][monster.get(Maze.turn).getCol()] = false;
        path.add(monster.get(Maze.turn));
        List<ICoordinate> maxPath = new ArrayList<>();
        while (!path.isEmpty()) {
            ICoordinate current = path.get(path.size() - 1);
            List<ICoordinate> neighbors = getNeighbors(current);
            if (!neighbors.isEmpty()) {
                ICoordinate next = neighbors.get(random.nextInt(0, neighbors.size()));
                path.add(next);
                wall[next.getRow()][next.getCol()] = false;
            } else {
                visited.add(current);
                path.remove(current);
            }
            if (path.size() > maxPath.size()) {
                maxPath = new ArrayList<>(path);
            }
        }
        exit = maxPath.get(random.nextInt(maxPath.size() / 2, maxPath.size()));
    }

    public List<ICoordinate> getNeighbors(ICoordinate coord) {
        List<ICoordinate> neighbors = new ArrayList<>();
        int row = coord.getRow();
        int col = coord.getCol();
        ICoordinate up = new Coordinate(row - 1, col);
        ICoordinate down = new Coordinate(row + 1, col);
        ICoordinate left = new Coordinate(row, col - 1);
        ICoordinate right = new Coordinate(row, col + 1);
        if (row > 0 && nbWall(up) >= 3 && !visited.contains(up)) {
            neighbors.add(up);
        }
        if (row < this.wall.length - 1 && nbWall(down) >= 3 && !visited.contains(down)) {
            neighbors.add(down);
        }
        if (col > 0 && nbWall(left) >= 3 && !visited.contains(left)) {
            neighbors.add(left);
        }
        if (col < this.wall[0].length - 1 && nbWall(right) >= 3 && !visited.contains(right)) {
            neighbors.add(right);
        }
        return neighbors;
    }

    private int nbWall(ICoordinate up) {
        int nb = 0;
        int row = up.getRow();
        int col = up.getCol();
        if (row == 0 || row == this.wall.length - 1 || col == 0 || col == this.wall[0].length - 1) {
            nb++;
        }
        if (row > 0 && this.wall[row - 1][col]) {
            nb++;
        }
        if (row < this.wall.length - 1 && this.wall[row + 1][col]) {
            nb++;
        }
        if (col > 0 && this.wall[row][col - 1]) {
            nb++;
        }
        if (col < this.wall[0].length - 1 && this.wall[row][col + 1]) {
            nb++;
        }
        return nb;
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
}
