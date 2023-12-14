package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Subject;

/**
 * Classe représentant le monstre et les informations qu'il possède
 * 
 * @see CellEvent
 * @see Coordinate
 */
public class Monster {

    // Tableau de booléens représentant les murs du labyrinte
    private final boolean[][] WALL;
    // Coordonnées actuel du monstre
    private ICoordinate monsterCoord;
    // Coordonnées du chasseur
    private ICoordinate hunterCoord;
    // Coordonnées de la sortie
    private final ICoordinate EXIT;

    /**
     * Constructeur de la classe Monster
     * Instancie les différents attributs de la classe
     * - WALL -> En appelant la méthode getWall() de la classe Maze
     * - monsterCoord -> En appelant la méthode getLastMonsterCoordinate() de la
     * classe Maze
     * - hunterCoord -> En appelant la méthode getLastHunterCoordinate() de la
     * classe Maze
     * - EXIT -> En appelant la méthode getExit() de la classe Maze
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
     * Méthode permettant de mettre à jour les coordonnées du monstre
     * 
     * @param monsterCoord Les nouvelles coordonnées du monstre
     */
    public void setMonsterCoord(ICoordinate monsterCoord) {
        this.monsterCoord = monsterCoord;
    }

    /**
     * Méthode permettant de mettre à jour les coordonnées du chasseur
     * 
     * @param hunterCoord Les nouvelles coordonnées du chasseur
     */
    public void setHunterCoord(ICoordinate hunterCoord) {
        this.hunterCoord = hunterCoord;
    }

    /**
     * Méthode permettant de récupérer les coordonnées du monstre
     * 
     * @return Les coordonnées du monstre
     */
    public ICoordinate getMonsterCoord() {
        return this.monsterCoord;
    }

    /**
     * Méthode permettant de récupérer les coordonnées du chasseur
     * 
     * @return Les coordonnées du chasseur
     */
    public ICoordinate getHunterCoord() {
        return this.hunterCoord;
    }

    /**
     * Méthode permettant de récupérer les coordonnées de la sortie
     * 
     * @return Les coordonnées de la sortie
     */
    public ICoordinate getExit() {
        return this.EXIT;
    }

    /**
     * Méthode permettant de récupérer le tableau de booléens représentant les
     * murs du labyrinthe
     * 
     * @return Le tableau de booléens représentant les murs du labyrinthe
     */
    public boolean[][] getWall() {
        return this.WALL;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Monster (");
        sb.append(Maze.turn + ") :\n");
        for (int i = 0; i < this.WALL.length; i++) {
            for (int j = 0; j < this.WALL[0].length; j++) {
                ICoordinate coord = new Coordinate(i, j);
                if (coord.equals(monsterCoord)) {
                    sb.append("M ");
                } else if (coord.equals(this.hunterCoord)) {
                    sb.append("H ");
                } else if (this.EXIT.equals(coord)) {
                    sb.append("X ");
                } else if (this.WALL[i][j]) {
                    sb.append("W ");
                } else {
                    sb.append("E ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
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
     * Méthode permettant de mettre à jour les coordonnées du monstre et du chasseur
     * Vérifie si les objets passés en paramètre ne sont pas null et si ce sont des
     * CellEvent
     * Si c'est le cas, vérifie si l'état du premier CellEvent est égal à HUNTER
     * puis met à jour les coordonnées du chasseur
     * Vérifie si l'état du second CellEvent est égal à MONSTER puis met à jour les
     * coordonnées du monstre
     * 
     * @param arg0 Le labyrinthe
     * @param arg1 Le CellEvent contenant les nouvelles coordonnées du monstre
     * @param arg2 Le CellEvent contenant les nouvelles coordonnées du chasseur
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
