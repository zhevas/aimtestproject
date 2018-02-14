package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) {
        Map<String, Set<String>> map = new HashMap<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<Map<String, Set<String>>>> futures;
        List<FileParser> tasks = new ArrayList<>();
        for (String s : args) {
            tasks.add(new FileParser(s));
        }
        try {
            futures = pool.invokeAll(tasks);
            futures.forEach(f -> {
                try {
                    map.putAll(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.forEach((key, value) -> {
            try {
                String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\" + key;
                byte[] fileLine = (String.join(";", value) + ";").getBytes();
                Files.write(Paths.get(filePath), fileLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pool.shutdown();
    }
}
