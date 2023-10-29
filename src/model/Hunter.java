package model;

import java.util.Map;
import java.util.TreeMap;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Hunter implements Observer {

    private boolean[][] knowWall;
    private Map<Integer, ICoordinate> knowMonsterCoords;
    private ICoordinate hunterCoord;

    public Hunter(Integer rows, Integer cols) {
        this.knowWall = new boolean[rows][cols];
        this.knowMonsterCoords = new TreeMap<>();
    }

    public Integer getMonsterTurn(ICoordinate coord) {
        Integer turn = 0;
        for (Map.Entry<Integer, ICoordinate> entry : knowMonsterCoords.entrySet()) {
            if (entry.getValue().equals(coord)) {
                turn = entry.getKey();
            }
        }
        return turn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hunter (");
        sb.append(Maze.turn + ") :\n");
        for (int i = 0; i < this.knowWall.length; i++) {
            for (int j = 0; j < this.knowWall[0].length; j++) {
                ICoordinate coord = new Coordinate(i, j);
                if (this.knowMonsterCoords.containsValue(coord)) {
                    Integer monsterTurn = this.getMonsterTurn(coord);
                    if (Maze.turn.equals(monsterTurn)) {
                        sb.append("M ");
                    } else if (monsterTurn != null) {
                        sb.append(monsterTurn + " ");
                    }
                } else if (coord.equals(this.hunterCoord)) {
                    sb.append("H ");
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

    public void addKnowMonsterCoords(ICoordinate monster) {
        this.knowMonsterCoords.put(Maze.turn, monster);
    }

    @Override
    public void update(Subject arg0) {
        // Method neverUsed
    }

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
