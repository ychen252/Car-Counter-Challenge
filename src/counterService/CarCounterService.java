package counterService;

import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarCounterService implements ICounterService {
    public static final String WHITE_SPACE_DELIMITER = "\\s+";
    private TreeMap<String, Integer> data = new TreeMap<>();
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
    public File aggregateByDay ( String outputFilePath ) throws Exception {
        TreeMap<String, Integer> result = new TreeMap<>();
        data.entrySet().stream().collect(Collectors.groupingBy(e -> e.getKey().substring(0, 10),
                Collectors.summingInt(e -> e.getValue())))
                .forEach(( date, count ) -> result.put(date.substring(0, 10), count));

        return writeMapToFile(result, outputFilePath);
    }

    @Override
    //create a priority queue and get top k most, time complexity O(N log k)
    public File getKTimeIntervalsWithMostCount ( int k, String outputFilePath ) throws Exception {
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare ( String a, String b ) {
                return data.get(a) - data.get(b);
            }
        });
        for (String key : data.keySet()) {
            //if priority queue size less than k, always add to the priority queue
            if (priorityQueue.size() < k) {
                priorityQueue.add(key);
            }
            //if current line great than biggest count in the priority queue, then remove the smallest and add this key
            else if (data.get(key) > data.get(priorityQueue.peek())) {
                priorityQueue.remove();
                priorityQueue.add(key);
            }
        }

        TreeMap<String, Integer> result = new TreeMap<>();
        while (!priorityQueue.isEmpty()) {
            String key = priorityQueue.poll();
            result.put(key, data.get(key));
        }
        return writeMapToFile(result, outputFilePath);

    }

    @Override
    //maintain a sliding window and get the k consecutive time intervals with smallest window sum, time complexity O(N)
    public File getKConsecutiveTimeIntervalsWithLeastCountSum ( int k, String outputFilePath ) throws Exception {
        TreeMap<String, Integer> result = new TreeMap<>();
        if (data.size() < k) {
            return writeMapToFile(data, outputFilePath);
        }

        List<Map.Entry<String,Integer>> entries = new ArrayList<>(data.entrySet());
        int minSum = 0;
        //non-inclusive right index and inclusive left index,i.e. [left,right)
        int minSumWindowRightIndex = k;

        //first window sum
        for(int i=0;i < k; i++){
            minSum += entries.get(i).getValue();
        }

        int windowSum = minSum;
        for(int i=k; i< data.size(); i++){
            windowSum = windowSum + entries.get(i).getValue() - entries.get(i-k).getValue();
            if(windowSum < minSum){
                minSumWindowRightIndex = i + 1;
                minSum = windowSum;
            }
        }

        int minSumWindowLeftIndex = minSumWindowRightIndex - k;

        for(int i = minSumWindowLeftIndex; i< minSumWindowRightIndex; i++){

            result.put(entries.get(i).getKey(), entries.get(i).getValue());
        }

        System.out.println(result);

        return writeMapToFile(result, outputFilePath);
    }

    //Private Helper
    public File writeMapToFile ( Map<String, Integer> hashMap, String outputFilePath ) throws Exception {
        File file = new File(outputFilePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        boolean isFirstLine = true;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String string = entry.getKey() + " " + entry.getValue().toString();
            if (!isFirstLine) {
                bufferedWriter.newLine();
            }
            bufferedWriter.append(string);
            isFirstLine = false;
        }
        bufferedWriter.close();

        return file;
    }
}
