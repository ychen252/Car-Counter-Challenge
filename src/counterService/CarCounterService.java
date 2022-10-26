package counterService;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarCounterService implements ICounterService {
    public static final String WHITE_SPACE_DELIMITER = "\\s+";
    private LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
    private int totalCount = 0;

    public CarCounterService ( InputStream inputStream ) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArray = line.split(this.WHITE_SPACE_DELIMITER);
                if (!this.data.containsKey(lineArray[0])) {
                    int count = Integer.parseInt(lineArray[1]);
                    this.data.put(lineArray[0], count);
                    this.totalCount = this.totalCount + count;
                } else throw new Exception("Dirty data, two or more lines having the same timestamp.");
            }
            inputStream.close();
            bufferedReader.close();

        } catch (Exception e) {
            //Add to exception log to capture this exception
        }
    }

    @Override
    public int getTotalCount () {
        return totalCount;
    }

    @Override
    public File aggregateByDay ( String outputFilePath ) {
    }

    @Override
    public File getKTimeIntervalsWithMostCount ( int k, String outputFilePath ) {
        return null;
    }

    @Override
    public File getKConsecutiveTimeIntervalsWithLeastCountSum ( int k, String outputFilePath ) {
        return null;
    }

    //Private Helper
    public File writeHashMapToFile ( LinkedHashMap<String, Integer> hashMap, String outputFilePath ) throws Exception {
        File file = new File(outputFilePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        boolean isFirstLine = true;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String string = entry.getKey() + " " + entry.getValue().toString();
            if(!isFirstLine){
                bufferedWriter.newLine();
            }
            bufferedWriter.append(string);
            isFirstLine = false;
        }
        bufferedWriter.close();

        return file;
    }
}
