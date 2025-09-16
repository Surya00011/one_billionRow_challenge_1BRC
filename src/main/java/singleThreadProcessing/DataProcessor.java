package singleThreadProcessing;

import model.Stats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
public class DataProcessor {
    public static void main(String[] args) {
        processData("H:\\My WorkSpace\\Open Source Contribution\\One_BRC\\data.txt");
    }
    private static void processData(String filePath){
        Map<String, Stats> statsMap = new TreeMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath));){
            String line;
            while((line=reader.readLine()) != null){
                String[] data = line.split(";");
                if(statsMap.containsKey(data[0])){
                    statsMap.put(data[0],statsMap.get(data[0]).accept(Double.parseDouble(data[1])));
                }else {
                    Stats stats = new Stats();
                    stats.accept(Double.parseDouble(data[1]));
                    statsMap.put(data[0],stats);
                }
            }
            printData(statsMap);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    private static void printData(Map<String, Stats> statsMap){
        int count=0;
        for(Map.Entry<String, Stats> entry : statsMap.entrySet()){
            System.out.println(++count +" {"+entry.getKey()+"="+entry.getValue().min+"/"+entry.getValue().average()+"/"+entry.getValue().max+"}");
        }
    }
}
