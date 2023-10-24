package model;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Monster {
    
    private boolean[][] wall;
    private ICoordinate exit;
    private ICoordinate coord;
    private ICoordinate shot;

    public void setShot(ICoordinate coord){
        this.shot =coord;
    }

}
