package model;

public class Stats {
    public long count = 0;
    public double sum = 0;
    public double min = Double.POSITIVE_INFINITY;
    public double max = Double.NEGATIVE_INFINITY;

    public Stats accept(double value){
        sum += value;
        count++;
        if(value > max) max=value;
        if(value < min) min=value;
        return this;
    }
    public double average(){
        return sum/count;
    }
}
