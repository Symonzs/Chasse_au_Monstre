package model;

import java.util.HashMap;
import java.util.Map;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.r304.utils.ConnectableProperty;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Hunter implements Observer {

    private boolean[][] knowWall;
    private Map<ICoordinate, Integer> knowMonsterCoords;
    private ICoordinate hunterCoord;

    public Hunter(Integer rows, Integer cols) {
        this.knowWall = new boolean[rows][cols];
        this.knowMonsterCoords = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hunter (");
        sb.append(Maze.turn + ") :\n");
        for (int i = 0; i < this.knowWall.length; i++) {
            for (int j = 0; j < this.knowWall[0].length; j++) {
                ICoordinate coord = new Coordinate(i, j);
                if (this.knowMonsterCoords.containsKey(coord)) {
                    Integer monsterTurn = this.knowMonsterCoords.get(coord);
                    if (Maze.turn.equals(monsterTurn)) {
                        sb.append("M ");
                    } else {
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
        this.knowMonsterCoords.put(monster, Maze.turn);
    }

    @Override
    public void update(Subject arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        if (arg1 instanceof CellEvent) {
            CellEvent eventHunter = (CellEvent) arg1;
            if (eventHunter.getState() == CellInfo.MONSTER) {
                this.knowMonsterCoords.put(eventHunter.getCoord(), Maze.turn - 1);
            } else if (eventHunter.getState() == CellInfo.WALL) {
                this.knowWall[eventHunter.getCoord().getRow()][eventHunter.getCoord().getCol()] = true;
            } else {
                this.knowWall[eventHunter.getCoord().getRow()][eventHunter.getCoord().getCol()] = false;
            }
        }
    }

}
