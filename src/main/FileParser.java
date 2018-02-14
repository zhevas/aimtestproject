package main;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FileParser implements Callable<Map<String, Set<String>>> {
    private String fileName;

    FileParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Map<String, Set<String>> call() throws Exception {
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\" + fileName;
        List<String> lines = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
        Map<String, Set<String>> params = new HashMap<>();
        String[] names = lines.remove(0).split(";");
        lines.forEach(line -> {
            String[] values = line.split(";");
            int length = names.length > values.length ? values.length : names.length;
            for (int i = 0; i < length; i++) {
                Set<String> set = params.computeIfAbsent(names[i], k -> new LinkedHashSet<>());
                set.add(values[i]);
            }
        });
        return params;
    }
}
