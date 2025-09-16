package streamProcessing;

import model.Stats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class StreamDataProcessor {
    public static void main(String[] args) {
        processData("H:\\My WorkSpace\\Open Source Contribution\\One_BRC\\data.txt");
    }

    private static void processData(String filePath) {
        Map<String, Stats> statsMap = new TreeMap<>();

        try {
            Files.lines(Path.of(filePath))
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


            statsMap.forEach((key, stats) -> {
                System.out.println("{" + key + "=" + stats.min + "/" + stats.average() + "/" + stats.max + "}");
            });

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
