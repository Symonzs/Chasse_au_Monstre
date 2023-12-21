package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * Classe representant le monstre
 */
public class Monster {

    private final boolean[][] WALL;
    private ICoordinate monsterCoord;
    private ICoordinate hunterCoord;
    private final ICoordinate EXIT;

    /**
     * Constructeur de la classe Monster
     * 
     * @param maze Le labyrinthe dans lequel le monstre se trouve
     */
    public Monster(Maze maze) {
        this.WALL = maze.getWall();
        this.monsterCoord = maze.getLastMonsterCoordinate();
        this.hunterCoord = maze.getLastHunterCoordinate();
        this.EXIT = maze.getExit();
    }

    /**
     * Methode permettant de mettre à jour les coordonnees du monstre
     * 
     * @param monsterCoord Les nouvelles coordonnees du monstre
     */
    public void setMonsterCoord(ICoordinate monsterCoord) {
        this.monsterCoord = monsterCoord;
    }

    /**
     * Methode permettant de mettre à jour les coordonnees du tir du chasseur
     * 
     * @param hunterCoord Les nouvelles coordonnees du tir du chasseur
     */
    public void setHunterCoord(ICoordinate hunterCoord) {
        this.hunterCoord = hunterCoord;
    }

    /**
     * Methode permettant de recuperer les coordonnees du monstre
     * 
     * @return Les coordonnees du monstre
     */
    public ICoordinate getMonsterCoord() {
        return this.monsterCoord;
    }

    /**
     * Methode permettant de recuperer les coordonnees du tir du chasseur
     * 
     * @return Les coordonnees du tir du chasseur
     */
    public ICoordinate getHunterCoord() {
        return this.hunterCoord;
    }

    /**
     * Methode permettant de recuperer les coordonnees de la sortie
     * 
     * @return Les coordonnees de la sortie
     */
    public ICoordinate getExit() {
        return this.EXIT;
    }

    /**
     * Methode permettant de recuperer le tableau de booleens representant les
     * murs du labyrinthe
     * 
     * @return Le tableau de booleens representant les murs du labyrinthe
     */
    public boolean[][] getWall() {
        return this.WALL;
    }

    /**
     * Methode permettant de mettre à jour les coordonnees du monstre et du tir
     * du chasseur
     * 
     * @param events Les evenements qui se sont produits
     */
    public void update(CellEvent[] events) {
        for (CellEvent event : events) {
            if (event != null) {
                if (event.getState() == CellInfo.MONSTER) {
                    this.setMonsterCoord(event.getCoord());
                }
                if (event.getState() == CellInfo.HUNTER) {
                    this.setHunterCoord(event.getCoord());
                }
            }
        }
    }

}
