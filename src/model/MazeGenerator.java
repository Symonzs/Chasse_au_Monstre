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

    public MazeGenerator(Integer nbRows, Integer nbCols, Integer wallPercentage) {
        this.wall = new boolean[nbRows][nbCols];
        this.monster = new TreeMap<>();
        this.hunter = new TreeMap<>();
        chooseMonsterCoord();
        System.out.println("monster : " + this.monster.get(1).getRow() + " " + this.monster.get(1).getCol());
        chooseExitCoord();
        System.out.println("exit : " + this.exit.getRow() + " " + this.exit.getCol());
        generateWall(wallPercentage);
    }

    public ICoordinate chooseCoord() {
        Random random = new Random();
        Integer row = random.nextInt(0, this.wall.length);
        Integer col = random.nextInt(0, this.wall[0].length);
        return new Coordinate(row, col);
    }

    public void chooseMonsterCoord() {
        this.monster.put(Maze.turn, chooseCoord());
    }

    public void chooseExitCoord() {
        ICoordinate coord;
        do {
            coord = chooseCoord();
        } while (!exitIsValid(coord));
        this.exit = coord;
    }

    public boolean exitIsValid(ICoordinate exit) {
        Integer row1 = this.getMonsterCoord().getRow();
        Integer col1 = this.getMonsterCoord().getCol();

        Integer row2 = exit.getRow();
        Integer col2 = exit.getCol();

        return Math.abs(row1 - row2) > 1 || Math.abs(col1 - col2) > 1;
    }

    public void generateWall(Integer wallPercentage) {
        Random random = new Random();
        this.wall = new boolean[this.wall.length][this.wall[0].length];
        for (int i = 0; i < this.wall.length; i++) {
            for (int j = 0; j < this.wall[i].length; j++) {
                this.wall[i][j] = random.nextInt(0, 2) == 0;
            }

        }

    }

    public boolean mazeIsValid() {
        List<ICoordinate> marque = new ArrayList<>();
        Stack<ICoordinate> pile = new Stack<>();
        pile.push(this.getMonsterCoord());
        while (!pile.empty()) {
            ICoordinate c = pile.peek();
            if (c.equals(exit)) {
                System.out.println("There is a way !");
                return true;
            } else {
                try {
                    ICoordinate cHaut = new Coordinate(c.getRow() - 1, c.getCol());
                    if (!isWall(cHaut) && !marque.contains(cHaut)) {
                        pile.push(cHaut);
                        marque.add(cHaut);
                        continue;
                    }
                } catch (Exception e) {
                }

                // Bas
                try {

                    ICoordinate cBas = new Coordinate(c.getRow() + 1, c.getCol());
                    if (!isWall(cBas) && !marque.contains(cBas)) {
                        pile.push(cBas);
                        marque.add(cBas);
                        continue;
                    }
                } catch (Exception e) {
                }

                // Gauche
                try {
                    ICoordinate cGauche = new Coordinate(c.getRow(), c.getCol() - 1);
                    if (!isWall(cGauche) && !marque.contains(cGauche)) {
                        pile.push(cGauche);
                        marque.add(cGauche);
                        continue;
                    }
                } catch (Exception e) {
                }

                // Droite
                try {
                    ICoordinate cDroite = new Coordinate(c.getRow(), c.getCol() + 1);
                    if (!isWall(cDroite) && !marque.contains(cDroite)) {
                        pile.push(cDroite);
                        marque.add(cDroite);
                        continue;
                    }
                } catch (Exception e) {
                }

                pile.pop();
            }
        }
        System.out.println("There is no way !");
        return false;
    }

    private boolean isWall(ICoordinate coord) {
        return this.wall[coord.getRow()][coord.getCol()];
    }

    private ICoordinate getMonsterCoord() {
        return this.monster.get(Maze.turn);
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
