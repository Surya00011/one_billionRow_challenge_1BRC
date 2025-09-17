package singleThreadProcessing;

import model.Stats;
import util.FileLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
public class SingleThreadDataProcessor {
    public static void main(String[] args) {
        long startTime = System.nanoTime(); // start
        processData(FileLoader.getFilePath());
        long endTime = System.nanoTime(); // end
        long duration = endTime - startTime;
        System.out.printf("Execution time of SingleThread: %.3f seconds%n", duration / 1_000_000_000.0);
    }
    private static void processData(String filePath){
        Map<String, Stats> statsMap = new TreeMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
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
            System.out.println(++count +" {"+entry.getKey()+"="+entry.getValue().getMin()+"/"+entry.getValue().average()+"/"+entry.getValue().getMax()+"}");
        }
    }
}
