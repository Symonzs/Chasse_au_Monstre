package model;

import java.util.Map;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Classe représentant le chasseur et les informations qu'il possède
 * 
 * @see CellEvent
 * @see Coordinate
 */
public class Hunter implements Observer {

    // Tableau de booléens représentant les murs du labyrinte connus par le chasseur
    private boolean[][] knowWall;
    // Tableau de booléens représentant les cases vides du labyrinte connus par le
    // chasseur
    private boolean[][] knowEmpty;
    // Coordonnées des monstres connus par le chasseur
    private Map<Integer, ICoordinate> knowMonsterCoords;
    // Coordonnées du chasseur
    private ICoordinate hunterCoord;

    /**
     * Constructeur de la classe Hunter
     * Instancie les différents attributs de la classe
     * - knowWall -> En créant un tableau de booléens de la taille du labyrinthe
     * - knowMonsterCoords -> En créant une nouvelle TreeMap
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
     * Méthode permettant d'ajouter les coordonnées d'un monstre dans la liste des
     * coordonées connus par le chasseur
     * 
     * @param monster Les coordonnées du monstre
     */
    public void addKnowMonsterCoords(Integer turn, ICoordinate monster) {
        this.knowMonsterCoords.put(turn, monster);
    }

    /**
     * Méthode permettant de récupérer les murs connus par le chasseur
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
     * Méthode permettant de récupérer les coordonnées des monstres connus par le
     * chasseur
     * 
     * @return Les coordonnées des monstres connus par le chasseur
     */
    public Map<Integer, ICoordinate> getKnowMonsterCoords() {
        return this.knowMonsterCoords;
    }

    /**
     * Méthode permettant de récupérer le tour durant lequel le monstre est passé
     * sur la case donnée en paramètre
     * 
     * @param coord Les coordonnées de la case
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

    @Override
    public void update(Subject arg0) {
        // Methode non utilisée
    }

    /**
     * Méthode permettant de mettre à jour les coordonnées connus par le chasseur
     * Vérifie si l'objet passé en paramètre est une instance de CellEvent
     * Si c'est le cas, vérifie l'état de la case et met à jour les coordonnées
     * connus par le chasseur
     * Si l'état de la case est MONSTER, ajoute les coordonnées du monstre dans la
     * liste des coordonnées connus par le chasseur
     * Si l'état de la case est WALL, met à jour la case dans le tableau de booléens
     * avec la valeur true
     * Sinon met à jour la case dans le tableau de booléens avec la valeur false
     * 
     * @param arg0 Le labyrinth
     * @param arg1 Le CellEvent contenant les informations sur la case touchée
     * 
     */
    @Override
    public void update(Subject arg0, Object arg1) {
        if (CellEvent.class == arg1.getClass()) {
            CellEvent event = (CellEvent) arg1;
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
            System.out.println("CellEvent null");
        }

    }

    public ICoordinate getHunterCoord() {
        return this.hunterCoord;
    }

}
