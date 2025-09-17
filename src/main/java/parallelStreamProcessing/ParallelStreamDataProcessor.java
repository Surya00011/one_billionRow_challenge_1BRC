package parallelStreamProcessing;

import model.Stats;
import util.FileLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelStreamDataProcessor {
    public static void main(String[] args) {
        long startTime = System.nanoTime(); // start
        processData(FileLoader.getFilePath());
        long endTime = System.nanoTime(); // end
        long duration = endTime - startTime;
        System.out.printf("Execution time of ParallelStream: %.3f seconds%n", duration / 1_000_000_000.0);
    }
    private static void processData(String filePath) {
        Map<String, Stats> statsMap = new ConcurrentHashMap<>();

        try {
            Files.lines(Path.of(filePath))
                    .parallel()
                    .forEach(line -> {
                        String[] data = line.split(";");
                        String key = data[0];
                        double value = Double.parseDouble(data[1]);


                        statsMap.compute(key, (k, stats) -> {
                            if (stats == null) stats = new Stats();
                            stats.accept(value);
                            return stats;
                        });
                    });

            Map<String,Stats> sortedMap = new TreeMap<>(statsMap);
            sortedMap.forEach((key, stats) -> {
                System.out.println("{" + key + "=" + stats.getMin() + "/" + stats.average() + "/" + stats.getMax() + "}");
            });

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
