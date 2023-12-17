package model;

import java.util.Map;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Classe representant le chasseur et les informations qu'il possede
 * 
 * @see CellEvent
 * @see Coordinate
 */
public class Hunter {

    // Tableau de booleens representant les murs connus par le chasseur
    private boolean[][] knowWall;
    // Tableau de booléens représentant les cases vides du labyrinte connus par le
    // chasseur
    private boolean[][] knowEmpty;
    // Coordonnées des monstres connus par le chasseur
    private Map<Integer, ICoordinate> knowMonsterCoords;
    // Coordonnees du chasseur
    private ICoordinate hunterCoord;

    /**
     * Constructeur de la classe Hunter<br>
     * Instancie les differents attributs de la classe<br>
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
     * Methode permettant d'ajouter les coordonnees d'un monstre dans la liste des
     * coordonees connus par le chasseur
     * 
     * @param monster Les coordonnees du monstre
     */
    public void addKnowMonsterCoords(Integer turn, ICoordinate monster) {
        this.knowMonsterCoords.put(turn, monster);
    }

    /**
     * Methode permettant de recuperer les murs connus par le chasseur
     * 
     * @return Les murs connus par le chasseur
     */
    public boolean[][] getKnowWall() {
        return this.knowWall;
    }

    /**
     * Méthode permettant de récupérer les cases vides connus par le chasseur
     * 
     * @return Les cases vides connus par le chasseur
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
     * passe sur la case donnee en parametre
     * 
     * @param coord Les coordonnees de la case
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
        try {
            this.hunterCoord = event.getCoord();
            if (event.getState() == CellInfo.MONSTER) {
                this.addKnowMonsterCoords(event.getTurn(), event.getCoord());
            } else if (event.getState() == CellInfo.WALL) {
                this.knowWall[event.getCoord().getRow()][event.getCoord().getCol()] = true;
            } else {
                this.knowEmpty[event.getCoord().getRow()][event.getCoord().getCol()] = true;
            }
        } catch (NullPointerException e) {
        }
    }
}
