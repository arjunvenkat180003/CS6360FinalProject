import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class BaselineCOMQTest {
    List<TimeSlice> timeSlices;
    String pattern = "yyyy-MM-dd HH:mm:ss";

    SimpleDateFormat sdf;

    BaselineCOMQ bscq;

    BaselineCOMQTest(List<TimeSlice> timeSlices)
    {
        this.timeSlices = timeSlices;
        sdf = new SimpleDateFormat(pattern);
        bscq = new BaselineCOMQ(timeSlices);
    }

    public void test1() throws ParseException
    {


        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");

        List<String> output = bscq.evalQuery(null, startDate, endDate, 10, 45, -35, 4, keywords);

        System.out.println("Test 1 "+output);
    }

    public void test2() throws ParseException
    {

        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("week");
        keywords.add("suddenly");

        List<String> output = bscq.evalQuery(null, startDate, endDate, 10, 45, -35, 4, keywords);

        System.out.println("Test 2 "+output);
    }

    public void testVaryingk(int kValue) throws ParseException
    {
        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");

        List<String> output = bscq.evalQuery(null, startDate, endDate, kValue, 45, -35, 4, keywords);

        System.out.println("test varying k "+kValue+": "+output);

    }

    public void testVaryingIntervalLength(String startTimeString, String endTimeString) throws ParseException
    {
        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");

        List<String> output = bscq.evalQuery(null, startDate, endDate, 5, 45, -35, 4, keywords);

        System.out.println("test varying startdate "+startTimeString+" "+endTimeString+": "+output);
    }

    public void testVaryingKeyWords(List<String> keywords) throws ParseException
    {
        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> output = bscq.evalQuery(null, startDate, endDate, 5, 45, -35, 4, keywords);

        System.out.println("test varying keywords "+keywords+": "+output);

    }

    public void testVaryingSpatialRange(int r) throws ParseException
    {
        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");

        List<String> output = bscq.evalQuery(null, startDate, endDate, 5, 45, -35, r, keywords);

        System.out.println("test varying r "+r+": "+output);

    }

    public void testVaryingkWithKeywords(int k, List<String> keywords) throws ParseException
    {
        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);


        List<String> output = bscq.evalQuery(null, startDate, endDate, k, 45, -35, 4, keywords);

        System.out.println("test varying k with keywords "+keywords+": "+output);
    }

    public void testVaryingTimeWithKeywords(String startTimeString, String endTimeString, List<String> keywords) throws ParseException
    {
        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> output = bscq.evalQuery(null, startDate, endDate, 5, 45, -35, 4, keywords);

        System.out.println("test varying kw with time "+keywords+": "+output);
    }
    
}
