package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * Classe representant le monstre et les informations qu'il possede
 * 
 * @see CellEvent
 * @see Coordinate
 */
public class Monster {

    // Tableau de booleens representant les murs du labyrinte
    private final boolean[][] WALL;
    // Coordonnees actuel du monstre
    private ICoordinate monsterCoord;
    // Coordonnees du chasseur
    private ICoordinate hunterCoord;
    // Coordonnees de la sortie
    private final ICoordinate EXIT;

    /**
     * Constructeur de la classe Monster<br>
     * Instancie les differents attributs de la classe<br>
     * 
     * @param maze Le labyrinthe
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
     * Methode permettant de mettre à jour les coordonnees du chasseur
     * 
     * @param hunterCoord Les nouvelles coordonnees du chasseur
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
     * Methode permettant de recuperer les coordonnees du chasseur
     * 
     * @return Les coordonnees du chasseur
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

    public void update(CellEvent[] events) {
        for (CellEvent event : events) {
            try {
                if (event.getState() == CellInfo.MONSTER) {
                    this.setMonsterCoord(event.getCoord());
                }
            } catch (NullPointerException e) {
                System.out.println("CellEvent null");
            }
            try {
                if (event.getState() == CellInfo.HUNTER) {
                    this.setHunterCoord(event.getCoord());
                }
            } catch (NullPointerException e) {
                System.out.println("CellEvent null");
            }
        }
    }

    /**
     * Methode permettant de mettre à jour les coordonnees du monstre et du
     * chasseur<br>
     * Verifie si les objets passes en parametre ne sont pas null et si ce sont des
     * CellEvent<br>
     * Si c'est le cas, verifie si l'etat du premier CellEvent est egal à HUNTER
     * puis met à jour les coordonnees du chasseur<br>
     * Verifie si l'etat du second CellEvent est egal à MONSTER puis met à jour les
     * coordonnees du monstre<br>
     * 
     * @param arg0 Le labyrinthe
     * @param arg1 Le CellEvent contenant les nouvelles coordonnees du monstre
     * @param arg2 Le CellEvent contenant les nouvelles coordonnees du chasseur
     */
    public void update(Subject arg0, Object arg1, Object arg2) {
        if (arg1 != null && CellEvent.class == arg1.getClass()) {
            CellEvent eventHunter = (CellEvent) arg1;
            this.setHunterCoord(eventHunter.getCoord());
        }
        if (arg2 != null && CellEvent.class == arg2.getClass()) {
            CellEvent eventMonster = (CellEvent) arg2;
            if (eventMonster.getState() == CellInfo.MONSTER) {
                this.setMonsterCoord(eventMonster.getCoord());
            }
        }
    }

}
