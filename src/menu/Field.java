package menu;

import javax.swing.*;
import java.io.Serializable;
/*
A pályán lévő mezőket megvalósító osztály
 */
public class Field implements Serializable {
    private JButton btn;                        //ez a konkrét páylán elérhető gomb
    private boolean occupied = false;           //van-e rajta hajó
    private boolean hit = false;                //lőttek-e már ide
    private Ship ship;                          //a mezőn elhelyezkedő hajó

    public Field(JButton btn){
        this.btn = btn;
    }
    public void setOccupied(boolean value){
        occupied = value;
    }
    public boolean getOccupied(){
        return occupied;
    }
    public JButton getButton(){
        return this.btn;
    }
    public int getCordinateX(){
        String coordinates[] = btn.getName().split(",");
        return Integer.parseInt(coordinates[0]);
    }
    public int getCordinateY(){
        String coordinates[] = btn.getName().split(",");
        return Integer.parseInt(coordinates[1]);
    }

    public boolean getHit(){
        return hit;
    }

    public void setHit(boolean value){
        hit = value;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public String toString(){
        return "" + hit + occupied;
    }

}
