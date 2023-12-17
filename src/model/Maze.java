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

/**
 * Classe representant le labyrinthe
 * 
 * @see CellEvent
 * @see Coordinate
 */
public class Maze extends Subject {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int WALL_PERCENT = 50;

    public static Integer currentTurn = 1;

    private boolean[][] wall;
    private Map<Integer, ICoordinate> monster;
    private Map<Integer, ICoordinate> hunter;
    private ICoordinate exit;

    private CellInfo winner;

    private boolean gameIsClosed = false;
    private boolean isReadyToNext = false;

    private boolean hunterHasPlayed = false;
    private boolean monsterHasPlayed = false;

    /**
     * Constructeur de la classe Maze
     * Charge un labyrinthe
     * 
     * @param path     Chemin d'acces du fichier contenant le labyrinthe à charger
     * @param fileName Nom du fichier contenant le labyrinthe à charger
     * @throws IOException            Si le fichier n'existe pas
     * @throws InputMismatchException Si le fichier contient des caracteres
     *                                invalides
     */
    public Maze(String path, String fileName) throws IOException, InputMismatchException {
        this.loadMaze(this.importFile(path, fileName));
    }

    /**
     * Constructeur de la classe Maze
     * Charge un labyrinthe
     * 
     * @param fileName Nom du fichier contenant le labyrinthe à charger
     * @throws IOException            Si le fichier n'existe pas
     * @throws InputMismatchException Si le fichier contient des caracteres
     *                                invalides
     */
    public Maze(String fileName) throws IOException, InputMismatchException {
        this.loadMaze(this.importFile(fileName));
    }

    /**
     * Constructeur de la classe Maze
     * Genere un labyrinthe
     * 
     * @param nbRows      Nombre de lignes du labyrinthe
     * @param nbCols      Nombre de colonnes du labyrinthe
     * @param wallPercent Pourcentage de murs souhaites dans le labyrinthe
     */
    public Maze(Integer nbRows, Integer nbCols, Integer wallPercentage) {
        MazeGenerator mazeGen = new MazeGenerator(nbRows, nbCols, wallPercentage);
        this.wall = mazeGen.getWall();
        this.monster = mazeGen.getMonster();
        this.hunter = mazeGen.getHunter();
        this.exit = mazeGen.getExit();
        this.winner = null;
    }

    /**
     * Constructeur de la classe Maze
     * Genere un labyrinthe
     * 
     * @param nbRows Nombre de lignes du labyrinthe
     * @param nbCols Nombre de colonnes du labyrinthe
     */
    public Maze(Integer nbRows, Integer nbCols) {
        this(nbRows, nbCols, WALL_PERCENT);
    }

    /**
     * Constructeur de la classe Maze
     * Genere un labyrinthe
     */
    public Maze() {
        this(ROWS, COLS, WALL_PERCENT);
    }

    /**
     * Charge un labyrinthe à partir d'un fichier
     * 
     * @param file Fichier contenant le labyrinthe à charger
     * @throws IOException            Si le fichier n'existe pas
     * @throws InputMismatchException Si le fichier contient des caracteres
     *                                invalides
     */
    public void loadMaze(File file) throws IOException, InputMismatchException {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        Integer nbRows = lines.size();
        Integer nbCols = lines.get(0).split(",").length;

        this.wall = new boolean[nbRows][nbCols];
        this.monster = new TreeMap<>();
        this.hunter = new TreeMap<>();
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
                        this.monster.put(currentTurn, new Coordinate(i, j));
                        this.wall[i][j] = false;
                    }
                    default -> throw new InputMismatchException("Le fichier contient des caracteres invalides. " +
                            "Les caracteres valides sont : E(Empty), W(Wall), X(Exit), M(Monster)");
                }
            }
        }
        this.winner = null;
    }

    /**
     * Importe un fichier à partir de son nom et du chemin d'acces
     * 
     * @param path     Chemin d'acces du fichier
     * @param fileName Nom du fichier
     * @return Fichier importe
     */
    public File importFile(String path, String fileName) throws FileNotFoundException {
        return FileFinder.find(path, fileName);
    }

    /**
     * Importe un fichier à partir de son nom
     * 
     * @param fileName Nom du fichier
     * @return Fichier importe
     */
    public File importFile(String fileName) throws FileNotFoundException {
        return FileFinder.find(fileName);
    }

    /**
     * Met à jour la case du labyrinthe ciclee par le CellEvent
     * Si l'etat du CellEvent est HUNTER, appel la methode cellUpdateHunter
     * Si l'etat du CellEvent est MONSTER, appel la methode cellUpdateMonster
     * 
     * @param eventRequest CellEvent à traiter
     */
    public void cellUpdate(CellEvent eventRequest) {
        if (CellInfo.HUNTER.equals(eventRequest.getState())) {
            cellUpdateHunter(eventRequest.getCoord(), Maze.currentTurn);
        } else if (CellInfo.MONSTER.equals(eventRequest.getState())) {
            cellUpdateMonster(eventRequest.getCoord(), eventRequest.getTurn());
        }
    }

    /**
     * Met à jour la case du labyrinthe ciclee par le CellEvent
     * Ajoute les coordonnees du monstre dans la map
     * Si après l'ajout des coordonnees du monstre, le monstre est sur la sortie,
     * appel la methode end
     * Sinon, appel la methode notifyObserver
     * 
     * @param eventCoord Nouvelle coordonnees du monstre
     * @param eventTurn  Tour du deplacement du monstre
     */
    private void cellUpdateMonster(ICoordinate eventCoord, Integer eventTurn) {
        CellEvent[] events = new CellEvent[2];
        this.monster.put(eventTurn, eventCoord);
        if (this.monsterAtExit()) {
            this.end(CellInfo.MONSTER);
        } else {
            events[0] = null;
            events[1] = new CellEvent(eventCoord, CellInfo.MONSTER);
            this.notifyObservers(events);
        }
    }

    /**
     * Met à jour la case du labyrinthe ciclee par le CellEvent
     * Ajoute les coordonnees du chasseur dans la map
     * Si le monstre a dejà ete sur cette case, on verifie si le monstre y est
     * toujours
     * Si le monstre y est toujours, appel la methode end
     * Sinon, on stocke dans eventHunter qui est un CellEvent(Coordonnees, Tour,
     * Etat)
     * - Les coordonnees de la case
     * - Le tour du tir
     * - L'etat de la case (MONSTER)
     * Si le monstre n'a jamais ete sur cette case, on stocke dans eventHunter qui
     * est un CellEvent(Coordonnees, Etat)
     * - Les coordonnees de la case
     * - L'etat de la case (EMPTY ou WALL selon si la case est vide ou non)
     * 
     * On stocke egalement dans eventMonster qui est un CellEvent(Coordonnees, Etat)
     * - Les coordonnees de la case
     * - L'etat de la case (HUNTER)
     * 
     * Enfin, on appel la methode notifyObserver
     * 
     * @param eventCoord Nouvelle coordonnees du chasseur
     * @param eventTurn  Tour du deplacement du chasseur
     */
    private void cellUpdateHunter(ICoordinate eventCoord, Integer eventTurn) {
        CellEvent[] events = new CellEvent[2];
        this.hunter.put(eventTurn, eventCoord);
        if (this.monsterWasHere(eventCoord)) {
            if (this.monsterIsHere(eventCoord)) {
                this.end(CellInfo.HUNTER);

            } else {
                events[0] = new CellEvent(eventCoord, this.getLastTurnFromCoordinate(eventCoord),
                        CellInfo.MONSTER);
            }
        } else {
            if (this.cellIsWall(eventCoord)) {
                events[0] = new CellEvent(eventCoord, CellInfo.WALL);
            } else {
                events[0] = new CellEvent(eventCoord, CellInfo.EMPTY);
            }
        }
        events[1] = new CellEvent(eventCoord, CellInfo.HUNTER);
        System.out.println(events[0]);
        System.out.println(events[1]);
        Maze.incrementTurn();
        notifyObservers(events);
    }

    /**
     * Verifie si le monstre est dejà passe sur la case
     * 
     * @param eventCoord
     * @return true si le monstre est dejà passe sur la case, false sinon
     */
    public boolean monsterWasHere(ICoordinate eventCoord) {
        return this.monster.containsValue(eventCoord);
    }

    /**
     * Verifie si le monstre est sur la case
     * 
     * @param eventCoord
     * @return true si le monstre est sur la case, false sinon
     */
    public boolean monsterIsHere(ICoordinate eventCoord) {
        return this.getLastMonsterCoordinate().equals(eventCoord);
    }

    /**
     * Verifie si la case est un mur
     * 
     * @param eventCoord
     * @return true si la case est un mur, false sinon
     */
    public boolean cellIsWall(ICoordinate eventCoord) {
        return this.wall[eventCoord.getRow()][eventCoord.getCol()];
    }

    /**
     * Verifie si le monstre est sur la sortie
     * 
     * @return true si le monstre est sur la sortie, false sinon
     */
    public boolean monsterAtExit() {
        return exit.equals(this.monster.get(Maze.currentTurn));
    }

    /**
     * Met fin à la partie
     * Pas encore implemente
     * 
     * @param victoryInfo Qui du monstre ou du chasseur a gagne
     */
    public void end(CellInfo victoryInfo) {
        this.winner = victoryInfo;
    }

    /**
     * Incremente le compteur de tour
     */
    public static void incrementTurn() {
        Maze.currentTurn++;
    }

    /**
     * Reinitialise le compteur de tour
     */
    public static void resetTurn() {
        Maze.currentTurn = 1;
    }

    /**
     * Retourne le tableau de booleens representant les murs du labyrinthe
     * 
     * @return Tableau de booleens representant les murs du labyrinthe
     */
    public boolean[][] getWall() {
        return this.wall;
    }

    /**
     * Retourne les coordonnees de la sortie
     * 
     * @return Coordonnees de la sortie
     */
    public ICoordinate getExit() {
        return this.exit;
    }

    /**
     * Retourne la map des coordonnees du monstre
     * 
     * @return Map des coordonnees du monstre
     */
    public Map<Integer, ICoordinate> getMonster() {
        return this.monster;
    }

    /**
     * Retourne la map des coordonnees du chasseur
     * 
     * @return Map des coordonnees du chasseur
     */
    public Map<Integer, ICoordinate> getHunter() {
        return this.hunter;
    }

    /**
     * Retourne le vainqueur
     * 
     * @return Vainqueur
     */
    public CellInfo getWinner() {
        return this.winner;
    }

    /**
     * Retourne les coordonnees du monstre au tour actuel si elles existent, sinon
     * retourne les coordonnees du monstre au tour precedent
     * 
     * @return Coordonnees du monstre au tour actuel ou au tour precedent
     */
    public ICoordinate getLastMonsterCoordinate() {
        ICoordinate lastMonsterCoord = this.monster.get(Maze.currentTurn);
        if (lastMonsterCoord == null) {
            lastMonsterCoord = this.monster.get(Maze.currentTurn - 1);
        }
        return lastMonsterCoord;
    }

    /**
     * Retourne les coordonnees du chasseur au tour actuel si elles existent, sinon
     * retourne les coordonnees du chasseur au tour precedent
     * 
     * @return Coordonnees du chasseur au tour actuel ou au tour precedent
     */
    public ICoordinate getLastHunterCoordinate() {
        ICoordinate lastHunterCoord = this.hunter.get(Maze.currentTurn);
        System.out.println(lastHunterCoord);
        if (lastHunterCoord == null) {
            lastHunterCoord = this.hunter.get(Maze.currentTurn - 1);
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

    public void setIsReadyToNext(boolean b) {
        this.isReadyToNext = b;
        notifyObservers(isReadyToNext);
    }

    public boolean getIsReadyToNext() {
        return this.isReadyToNext;
    }

    public void setHunterHasPlayed(boolean b) {
        this.hunterHasPlayed = b;
    }

    public boolean getHunterHasPlayed() {
        return this.hunterHasPlayed;
    }

    public void setMonsterHasPlayed(boolean b) {
        this.monsterHasPlayed = b;
    }

    public boolean getMonsterHasPlayed() {
        return this.monsterHasPlayed;
    }

}
