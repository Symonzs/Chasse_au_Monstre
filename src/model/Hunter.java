package model;

import java.util.Map;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Classe representant le chasseur
 */
public class Hunter {

    private boolean[][] knowWall;
    private boolean[][] knowEmpty;
    private Map<Integer, ICoordinate> knowMonsterCoords;
    private ICoordinate hunterCoord;

    /**
     * Constructeur de la classe Hunter
     * 
     * @param rows Le nombre de lignes du labyrinthe
     * @param cols Le nombre de colonnes du labyrinthe
     */
    public Hunter(Integer rows, Integer cols) {
        this.knowWall = new boolean[rows][cols];
        this.knowEmpty = new boolean[rows][cols];
        this.knowMonsterCoords = new TreeMap<>();
        this.hunterCoord = null;
    }

    /**
     * Methode permettant d'ajouter les coordonnees d'un monstre connu par le
     * chasseur
     * 
     * @param turn    Le tour durant lequel le monstre est passe sur la case
     * @param monster Les coordonnees du monstre
     */
    public void addKnowMonsterCoords(Integer turn, ICoordinate monster) {
        this.knowMonsterCoords.put(turn, monster);
    }

    /**
     * Methode permettant de recuperer les cases murs connus par le chasseur
     * 
     * @return Les cases murs connus par le chasseur
     */
    public boolean[][] getKnowWall() {
        return this.knowWall;
    }

    /**
     * Methode permettant de recuperer les cases vides connues par le chasseur
     * 
     * @return Les cases vides connues par le chasseur
     */
    public boolean[][] getKnowEmpty() {
        return this.knowEmpty;
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
     * Methode permettant de recuperer les coordonnees des monstres connus par le
     * chasseur
     * 
     * @return Les coordonnees des monstres connus par le chasseur
     */
    public Map<Integer, ICoordinate> getKnowMonsterCoords() {
        return this.knowMonsterCoords;
    }

    /**
     * Methode permettant de recuperer le dernier tour durant lequel le monstre est
     * passe par les coordonnees passees en parametre
     * 
     * @param coord Les coordonnees du monstre
     * @return Un entier representant le tour
     */
    public Integer getLastTurnFromCoordinate(ICoordinate coord) {
        Integer lastTurn = null;
        for (Map.Entry<Integer, ICoordinate> entry : this.knowMonsterCoords.entrySet()) {
            if (entry.getValue().equals(coord)) {
                lastTurn = entry.getKey();
            }
        }
        return lastTurn;
    }

    public void update(CellEvent event) {
        if (event != null) {
            this.hunterCoord = event.getCoord();
            if (event.getState() == CellInfo.MONSTER) {
                this.addKnowMonsterCoords(event.getTurn(), event.getCoord());
            } else if (event.getState() == CellInfo.WALL) {
                this.knowWall[event.getCoord().getRow()][event.getCoord().getCol()] = true;
            } else {
                this.knowEmpty[event.getCoord().getRow()][event.getCoord().getCol()] = true;
            }
        }
    }
}
