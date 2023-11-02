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
    // Coordonnées des monstres connus par le chasseur
    private Map<Integer, ICoordinate> knowMonsterCoords;

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
        this.knowMonsterCoords = new TreeMap<>();
    }

    /**
     * Méthode permettant d'ajouter les coordonnées d'un monstre dans la liste des
     * coordonées connus par le chasseur
     * 
     * @param monster Les coordonnées du monstre
     */
    public void addKnowMonsterCoords(ICoordinate monster) {
        this.knowMonsterCoords.put(Maze.turn, monster);
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
    public Integer getMonsterTurnFromCoordinate(ICoordinate coord) {
        for (Map.Entry<Integer, ICoordinate> entry : this.knowMonsterCoords.entrySet()) {
            if (entry.getValue().equals(coord)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hunter (");
        sb.append(Maze.turn + ") :\n");
        for (int i = 0; i < this.knowWall.length; i++) {
            for (int j = 0; j < this.knowWall[0].length; j++) {
                ICoordinate coord = new Coordinate(i, j);
                if (this.knowMonsterCoords.containsValue(coord)) {
                    Integer monsterTurn = this.getMonsterTurnFromCoordinate(coord);
                    if (Maze.turn.equals(monsterTurn)) {
                        sb.append("M ");
                    } else if (monsterTurn != null) {
                        sb.append(monsterTurn + " ");
                    }
                } else if (this.knowWall[i][j]) {
                    sb.append("W ");
                } else {
                    sb.append("E ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
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
            if (event.getState() == CellInfo.MONSTER) {
                this.addKnowMonsterCoords(event.getCoord());
            } else if (event.getState() == CellInfo.WALL) {
                this.knowWall[event.getCoord().getRow()][event.getCoord().getCol()] = true;
            } else {
                this.knowWall[event.getCoord().getRow()][event.getCoord().getCol()] = false;
            }
        }
    }

}
