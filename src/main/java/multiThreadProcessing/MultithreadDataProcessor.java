package multiThreadProcessing;

import model.Stats;
import util.FileLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadDataProcessor {
    public static void main(String[] args) {
        long startTime = System.nanoTime(); // start
        processData(FileLoader.getFilePath());
        long endTime = System.nanoTime(); // end
        long duration = endTime - startTime;
        System.out.printf("Execution time of MultiThread: %.3f seconds%n", duration / 1_000_000_000.0);
    }

    private static void processData(String filePath){
        final int numOfThreads = Runtime.getRuntime().availableProcessors();
        final int BATCH_SIZE = 100_000; //One La
        List<Future<Map<String, Stats>>> futuresBatch = new ArrayList<>();
        try(ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
            BufferedReader reader = new BufferedReader(new FileReader(filePath))){

            List<String> batch = new ArrayList<>();
            String line;

            while((line=reader.readLine())!=null){
                batch.add(line);
                if(batch.size() >= BATCH_SIZE){
                    List<String> taskBatch = new ArrayList<>(batch);
                    futuresBatch.add(executor.submit(()->processBatch(taskBatch)));
                    batch.clear();
                }
            }
            if(!batch.isEmpty()){
                List<String> taskBatch = new ArrayList<>(batch);
                futuresBatch.add(executor.submit(()->processBatch(taskBatch)));
                batch.clear();
            }

        }catch (IOException e){
            System.err.println(e.getMessage());
        }

        Map<String, Stats> finalMap = new TreeMap<>();
        for (Future<Map<String, Stats>> future : futuresBatch) {
            try {
                Map<String, Stats> map = future.get();
                map.forEach((key, stats) -> {
                    finalMap.merge(key, stats, (oldStats, newStats) -> {
                        oldStats.merge(newStats);
                        return newStats;
                    });
                });
            }catch (InterruptedException | ExecutionException e){
                System.err.println(e.getMessage());
            }
        }
        finalMap.forEach((key, stats) -> {
            System.out.println("{" + key + "=" + stats.getMin() + "/" + stats.average() + "/" + stats.getMax() + "}");
        });
    }

    private static Map<String,Stats> processBatch(List<String> batch){
        Map<String,Stats> threadLocalMap = new HashMap<>();
        for(String line : batch){
            String[] data = line.split(";");
            String key = data[0];
            double value = Double.parseDouble(data[1]);
            threadLocalMap.computeIfAbsent(key,k -> new Stats()).accept(value);
        }
        return threadLocalMap;
    }
}
