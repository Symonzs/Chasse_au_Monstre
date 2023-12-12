package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;

/**
 * Classe représentant le labyrinthe
 * 
 * @see CellEvent
 * @see Coordinate
 * @author Simon HAYART, Raphael KIECKEN, Charles COUDE, Hugo
 */
public class Maze extends Subject {

    // Tableau de booléens représentant les murs du labyrinte
    private boolean[][] wall;
    // Map représentant les coordonnées des monstres
    private Map<Integer, ICoordinate> monster;
    // Map représentant les coordonnées du chasseur
    private Map<Integer, ICoordinate> hunter;
    // Coordonnées de la sortie
    private ICoordinate exit;
    // Tour actuel
    public static Integer turn = 1;
    // Vainqueur
    private CellInfo winner;
    private boolean exitedGame = false;

    /**
     * Constructeur de la classe Maze
     * 
     * @param fileName Nom du fichier contenant le labyrinthe
     */
    public Maze(String fileName) {
        loadMaze(fileName);
    }

    /**
     * Constructeur de la classe Maze
     * Créer un nom de fichier à partir du nombre de lignes et de colonnes sous la
     * forme "'nbRows'x'nbCols'.csv"
     * 
     * @param nbRows Nombre de lignes du labyrinthe
     * @param nbCols Nombre de colonnes du labyrinthe
     */
    public Maze(Integer nbRows, Integer nbCols) {
        loadMaze(String.format("%dx%d.csv", nbRows, nbCols));
    }

    /**
     * Charge le labyrinthe à partir d'un fichier
     * Instancie les attributs de la classe (wall, monster, hunter, exit)
     * Selon le caractère lu dans le fichier :
     * - Rempli le tableau de booléens représentant les murs du labyrinthe
     * - Ajoute les coordonnées des monstres dans la map
     * - Met à jour les coordonnées de la sortie
     * 
     * Le fichier doit être au format CSV
     * 
     * Si un caractère non reconnu est lu, une exception est levée
     * 
     * @param fileName Nom du fichier contenant le labyrinthe
     */
    public void loadMaze(String fileName) {
        try {
            List<String> lines = Files.readAllLines(importFileWithPath(fileName).toPath(), StandardCharsets.UTF_8);
            Integer nbRows = lines.size();
            Integer nbCols = lines.get(0).split(",").length;
            this.wall = new boolean[nbRows][nbCols];
            this.monster = new TreeMap<>();
            this.hunter = new TreeMap<>();
            this.winner = null;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] cols = line.split(",");
                for (int j = 0; j < cols.length; j++) {
                    switch (cols[j]) {
                        case "E" -> this.wall[i][j] = false;
                        case "W" -> this.wall[i][j] = true;
                        case "X" -> {
                            this.exit = new Coordinate(i, j);
                            this.wall[i][j] = false;
                        }
                        case "M" -> {
                            this.monster.put(turn, new Coordinate(i, j));
                            this.wall[i][j] = false;
                        }
                        default -> throw new InputMismatchException("Caractère non reconnu");
                    }
                }
            }
        } catch (IOException | InputMismatchException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Importe un fichier à partir de son nom
     * 
     * @param fileName Nom du fichier à importer
     * @return Fichier importé
     * @throws FileNotFoundException Si le fichier n'est pas trouvé
     */
    public File importFile(String fileName) throws FileNotFoundException {
        File csv = FileFinder.find(fileName);
        if (csv == null) {
            throw new FileNotFoundException(String.format("Le fichier '%s' n'a pas été trouvé", fileName));
        }
        return csv;
    }

    /**
     * Importe un fichier à partir de son chemin absolu
     * 
     * @param pathName Chemin du fichier à importer
     * @return Fichier importé
     * @throws NullPointerException Si le chemin est null
     */
    public File importFileWithPath(String fileName) throws NullPointerException {
        return new File(fileName);
    }

    /**
     * Met à jour la case du labyrinthe ciclée par le CellEvent
     * Si l'état du CellEvent est HUNTER, appel la méthode cellUpdateHunter
     * Si l'état du CellEvent est MONSTER, appel la méthode cellUpdateMonster
     * 
     * @param eventRequest CellEvent à traiter
     */
    public void cellUpdate(CellEvent eventRequest) {
        if (CellInfo.HUNTER.equals(eventRequest.getState())) {
            cellUpdateHunter(eventRequest.getCoord(), eventRequest.getTurn());
        } else if (CellInfo.MONSTER.equals(eventRequest.getState())) {
            cellUpdateMonster(eventRequest.getCoord(), eventRequest.getTurn());
        }
    }

    /**
     * Met à jour la case du labyrinthe ciclée par le CellEvent
     * Ajoute les coordonnées du monstre dans la map
     * Si après l'ajout des coordonnées du monstre, le monstre est sur la sortie,
     * appel la méthode end
     * Sinon, appel la méthode notifyObserver
     * 
     * @param eventCoord Nouvelle coordonnées du monstre
     * @param eventTurn  Tour du déplacement du monstre
     */
    private void cellUpdateMonster(ICoordinate eventCoord, Integer eventTurn) {
        this.monster.put(eventTurn, eventCoord);
        if (this.monsterAtExit()) {
            this.end(CellInfo.MONSTER);
        } else {
            this.notifyObserver(null, new CellEvent(eventCoord, eventTurn, CellInfo.MONSTER));
        }
    }

    /**
     * Met à jour la case du labyrinthe ciclée par le CellEvent
     * Ajoute les coordonnées du chasseur dans la map
     * Si le monstre a déjà été sur cette case, on vérifie si le monstre y est
     * toujours
     * Si le monstre y est toujours, appel la méthode end
     * Sinon, on stocke dans eventHunter qui est un CellEvent(Coordonnées, Tour,
     * Etat)
     * - Les coordonnées de la case
     * - Le tour du tir
     * - L'état de la case (MONSTER)
     * Si le monstre n'a jamais été sur cette case, on stocke dans eventHunter qui
     * est un CellEvent(Coordonnées, Etat)
     * - Les coordonnées de la case
     * - L'état de la case (EMPTY ou WALL selon si la case est vide ou non)
     * 
     * On stocke également dans eventMonster qui est un CellEvent(Coordonnées, Etat)
     * - Les coordonnées de la case
     * - L'état de la case (HUNTER)
     * 
     * Enfin, on appel la méthode notifyObserver
     * 
     * @param eventCoord Nouvelle coordonnées du chasseur
     * @param eventTurn  Tour du déplacement du chasseur
     */
    private void cellUpdateHunter(ICoordinate eventCoord, Integer eventTurn) {
        CellEvent eventHunter = null;
        CellEvent eventMonster = null;
        this.hunter.put(eventTurn, eventCoord);
        if (this.monsterWasHere(eventCoord)) {
            if (this.monsterIsHere(eventCoord)) {
                this.end(CellInfo.HUNTER);

            } else {
                eventHunter = new CellEvent(eventCoord, this.getLastTurnFromCoordinate(eventCoord),
                        CellInfo.MONSTER);
            }
        } else {
            if (this.cellIsWall(eventCoord)) {
                eventHunter = new CellEvent(eventCoord, CellInfo.WALL);
            } else {
                eventHunter = new CellEvent(eventCoord, CellInfo.EMPTY);
            }
        }
        eventMonster = new CellEvent(eventCoord, CellInfo.HUNTER);
        Maze.incrementTurn();
        this.notifyObserver(eventHunter, eventMonster);
    }

    /**
     * Vérifie si le monstre est déjà passé sur la case
     * 
     * @param eventCoord
     * @return true si le monstre est déjà passé sur la case, false sinon
     */
    public boolean monsterWasHere(ICoordinate eventCoord) {
        return this.monster.containsValue(eventCoord);
    }

    /**
     * Vérifie si le monstre est sur la case
     * 
     * @param eventCoord
     * @return true si le monstre est sur la case, false sinon
     */
    public boolean monsterIsHere(ICoordinate eventCoord) {
        return this.getLastMonsterCoordinate().equals(eventCoord);
    }

    /**
     * Vérifie si la case est un mur
     * 
     * @param eventCoord
     * @return true si la case est un mur, false sinon
     */
    public boolean cellIsWall(ICoordinate eventCoord) {
        return this.wall[eventCoord.getRow()][eventCoord.getCol()];
    }

    /**
     * Vérifie si le monstre est sur la sortie
     * 
     * @return true si le monstre est sur la sortie, false sinon
     */
    public boolean monsterAtExit() {
        return exit.equals(this.monster.get(Maze.turn));
    }

    /**
     * Met fin à la partie
     * Pas encore implémenté
     * 
     * @param victoryInfo Qui du monstre ou du chasseur a gagné
     */
    public void end(CellInfo victoryInfo) {
        this.winner = victoryInfo;
        this.exitedGame = true;
    }

    /**
     * Incrémente le compteur de tour
     */
    public static void incrementTurn() {
        Maze.turn++;
    }

    /**
     * Réinitialise le compteur de tour
     */
    public static void resetTurn() {
        Maze.turn = 1;
    }

    /**
     * Retourne le tableau de booléens représentant les murs du labyrinthe
     * 
     * @return Tableau de booléens représentant les murs du labyrinthe
     */
    public boolean[][] getWall() {
        return this.wall;
    }

    /**
     * Retourne les coordonnées de la sortie
     * 
     * @return Coordonnées de la sortie
     */
    public ICoordinate getExit() {
        return this.exit;
    }

    /**
     * Retourne la map des coordonnées du monstre
     * 
     * @return Map des coordonnées du monstre
     */
    public Map<Integer, ICoordinate> getMonster() {
        return this.monster;
    }

    /**
     * Retourne la map des coordonnées du chasseur
     * 
     * @return Map des coordonnées du chasseur
     */
    public Map<Integer, ICoordinate> getHunter() {
        return this.hunter;
    }

    /**
     * Retourne les coordonnées du monstre au tour actuel si elles existent, sinon
     * retourne les coordonnées du monstre au tour précédent
     * 
     * @return Coordonnées du monstre au tour actuel ou au tour précédent
     */
    public ICoordinate getLastMonsterCoordinate() {
        ICoordinate lastMonsterCoord = this.monster.get(Maze.turn);
        if (lastMonsterCoord == null) {
            lastMonsterCoord = this.monster.get(Maze.turn - 1);
        }
        return lastMonsterCoord;
    }

    /**
     * Retourne les coordonnées du chasseur au tour actuel si elles existent, sinon
     * retourne les coordonnées du chasseur au tour précédent
     * 
     * @return Coordonnées du chasseur au tour actuel ou au tour précédent
     */
    public ICoordinate getLastHunterCoordinate() {
        ICoordinate lastHunterCoord = this.hunter.get(Maze.turn);
        if (lastHunterCoord == null) {
            lastHunterCoord = this.hunter.get(Maze.turn - 1);
        }
        return lastHunterCoord;
    }

    public Integer getLastTurnFromCoordinate(ICoordinate coord) {
        Integer lastTurn = null;
        for (Map.Entry<Integer, ICoordinate> entry : this.monster.entrySet()) {
            if (entry.getValue().equals(coord)) {
                lastTurn = entry.getKey();
            }
        }
        return lastTurn;
    }

    /**
     * Notifie les observateurs de la classe
     * Si l'observateur est un chasseur et que ses données ne sont pas null,
     * appel la méthode update de la classe Hunter
     * Si l'observateur est un monstre, appel la méthode update de la classe Monster
     */
    public void notifyObserver(Object hunterData, Object monsterData) {
        for (Observer observer : this.attached) {
            if (Hunter.class == observer.getClass()) {
                Hunter hunterTemp = (Hunter) observer;
                if (hunterData != null) {
                    hunterTemp.update(this, hunterData);
                }
            }
            if (Monster.class == observer.getClass()) {
                Monster monsterTemp = (Monster) observer;
                monsterTemp.update(this, hunterData, monsterData);
            }
        }
    }

    public boolean gameIsExited() {
        return exitedGame;
    }

    public void SetgameIsExited(boolean gameIsExited) {
        this.exitedGame = gameIsExited;
    }

    public CellInfo getWinner() {
        return this.winner;
    }

}
