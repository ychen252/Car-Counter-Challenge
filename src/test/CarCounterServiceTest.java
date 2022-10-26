package test;

import counterService.CarCounterService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class CarCounterServiceTest {
    static final String TEST_CASE_1_INPUT = "src/test/case1/input.txt";
    static final String TEST_CASE_1_OUTPUT_1 = "src/test/case1/output1.txt";
    static final String TEST_CASE_1_EXPECT_1 = "src/test/case1/expect1.txt";
    static final String TEST_CASE_1_OUTPUT_2 = "src/test/case1/output2.txt";
    static final String TEST_CASE_1_EXPECT_2 = "src/test/case1/expect2.txt";
    static final String TEST_CASE_1_OUTPUT_3 = "src/test/case1/output3.txt";
    static final String TEST_CASE_1_EXPECT_3 = "src/test/case1/expect3.txt";


    CarCounterService carCounter1 = new CarCounterService(
            getClass().getResourceAsStream("case1/input.txt"));

    @Test
    //Test 1 - Aggregate car counts by each day
    public void aggregateByDay() throws Exception {
        File outputFile = carCounter1.aggregateByDay(TEST_CASE_1_OUTPUT_1);
        File expectFile = new File(TEST_CASE_1_EXPECT_1);
        Assertions.assertEquals(FileUtils.readFileToString(expectFile,"utf-8"),
                FileUtils.readFileToString(outputFile,"utf-8"));
    }

    @Test
    //Test 2 - top 3 time intervals with most cars
    public void getKTimeIntervalsWithMostCount() throws Exception{
        File outputFile = carCounter1.getKTimeIntervalsWithMostCount(3,TEST_CASE_1_OUTPUT_2);
        File expectFile = new File(TEST_CASE_1_EXPECT_2);
        Assertions.assertEquals(FileUtils.readFileToString(expectFile,"utf-8"),
                FileUtils.readFileToString(outputFile,"utf-8"));
    }

    @Test
    //Test 3 - 4 consecutive time intervals (1.5h) with least cars
    public void getKConsecutiveTimeIntervalsWithLeastCountSum() throws Exception{
        File outputFile = carCounter1.getKConsecutiveTimeIntervalsWithLeastCountSum(4,TEST_CASE_1_OUTPUT_3);
        File expectFile = new File(TEST_CASE_1_EXPECT_3);
        Assertions.assertEquals(FileUtils.readFileToString(expectFile,"utf-8"),
                FileUtils.readFileToString(outputFile,"utf-8"));

    }

    @Test
    //Test 4 - total Count
    public void getTotalCount() throws Exception{
        Assertions.assertEquals(398, carCounter1.getTotalCount());
    }
}
