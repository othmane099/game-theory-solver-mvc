package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Strategy {
    
    private String name;
    private ArrayList<Integer> values;

    public Strategy(String name, ArrayList<Integer> values){
        this.name = name;
        this.values = values;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public int getMinValue(){
        return Collections.min(getValues());
    }
}
