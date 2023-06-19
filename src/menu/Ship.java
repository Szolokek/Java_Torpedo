package menu;

import java.io.Serializable;
import java.util.ArrayList;
/*
A hajókat tároló osztály
 */
public class Ship implements Serializable {
    private String size;
    private ArrayList<Field> fields;



    public Ship(String size){
        this.size = size;
        fields = new ArrayList<Field>();
    }

    public String getSize(){
        return size;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }
/*
visszaadja a hajó hosszát, tehát a második koordinátát ("1x2" -> 2)
 */
    public int getLength(){
        String shipsize[] = size.split("x");
        return Integer.parseInt(shipsize[1]);
    }
}
