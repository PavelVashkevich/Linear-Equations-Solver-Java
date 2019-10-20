package range;

import java.util.ArrayList;

public class Row {
    private String name;
    private ArrayList<Object> coefficients;

    Row(String name){
        this.name = name;
        coefficients = new ArrayList<>();
    }

    void addCoefficient(Object coefficient){
        coefficients.add(coefficient);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCoefficientByPosition(int position){
        return coefficients.get(position);
    }

    public Object getConstant(){
        return coefficients.get(coefficients.size() - 1);
    }

    public void replaceCoefficient(int index, Object element){
        coefficients.set(index, element);
    }

    int getNumberOfCoefficients(){
        return coefficients.size();
    }
}
