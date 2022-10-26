package counterService;

import java.io.File;

public interface ICounterService {
    public int getTotalCount();
    public File aggregateByDay( String outputFilePath) throws Exception;
    public File getKTimeIntervalsWithMostCount(int k, String outputFilePath);
    public File getKConsecutiveTimeIntervalsWithLeastCountSum(int k, String outputFilePath);
}
