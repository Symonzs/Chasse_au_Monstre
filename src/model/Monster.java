package model;

import java.util.Map;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class Monster implements Observer {

    private final boolean[][] WALL;
    private Map<Integer, ICoordinate> monsterCoords;
    private ICoordinate hunterCoord;
    private final ICoordinate EXIT;

    public Monster(Maze maze) {
        this.WALL = maze.getWall();
        this.monsterCoords = maze.getMonster();
        this.hunterCoord = maze.getHunter();
        this.EXIT = maze.getExit();
    }

    public void setHunterCoord(ICoordinate hunterCoord) {
        this.hunterCoord = hunterCoord;
    }

    public void addMonsterCoords(Integer turn, ICoordinate monster) {
        this.monsterCoords.put(turn, monster);
    }

    public Integer getMonsterTurn(ICoordinate coord) {
        Integer turn = 0;
        for (Map.Entry<Integer, ICoordinate> entry : monsterCoords.entrySet()) {
            if (entry.getValue().equals(coord)) {
                turn = entry.getKey();
            }
        }
        return turn;
    }

    public ICoordinate getCoordinate(Integer turn) {
        return this.monsterCoords.get(turn);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Monster (");
        sb.append(Maze.turn + ") :\n");
        for (int i = 0; i < this.WALL.length; i++) {
            for (int j = 0; j < this.WALL[0].length; j++) {
                ICoordinate coord = new Coordinate(i, j);
                if (this.monsterCoords.containsValue(coord)) {
                    Integer monsterTurn = this.getMonsterTurn(coord);
                    if (Maze.turn.equals(monsterTurn)) {
                        sb.append("M ");
                    } else {
                        sb.append(monsterTurn + " ");
                    }
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

    @Override
    public void update(Subject arg0) {
        // never used
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        if (arg1 instanceof CellEvent) {
            CellEvent eventMonster = (CellEvent) arg1;
            if (eventMonster.getState() == CellInfo.MONSTER) {
                this.addMonsterCoords(eventMonster.getTurn(), eventMonster.getCoord());
            }
        }
    }

    public void update(Subject arg0, Object arg1, Object arg2) {
        if (CellEvent.class == arg1.getClass()) {
            CellEvent eventHunter = (CellEvent) arg1;
            if (eventHunter.getState() == CellInfo.HUNTER) {
                this.setHunterCoord(eventHunter.getCoord());
            }
        }
        update(arg0, arg2);
    }

}
