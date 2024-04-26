import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class FastComCQTest {
    List<TimeSlice> timeSlices;
    String pattern = "yyyy-MM-dd HH:mm:ss";

    SimpleDateFormat sdf;

    FastComCQ fccq;

    FastComCQTest(List<TimeSlice> timeSlices)
    {
        this.timeSlices = timeSlices;
        sdf = new SimpleDateFormat(pattern);
        fccq = new FastComCQ(timeSlices);
    }

    public void test1() throws ParseException
    {


        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");

        List<List<String>> output = fccq.evalQuery(null, startDate, endDate, 10, 45, -35, 4, keywords);

        System.out.println("FastComCQ Test 1 "+output);
    }

    public void test2() throws ParseException
    {


        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";

        Date startDate = sdf.parse(startTimeString);
        Date endDate = sdf.parse(endTimeString);

        List<String> keywords = new ArrayList<>();
        keywords.add("pay");

        List<List<String>> output = fccq.evalQuery(null, startDate, endDate, 10, 45, -35, 4, keywords);

        System.out.println("FastComCQ Test 2 "+output);
    }
}
