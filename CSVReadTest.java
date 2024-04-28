import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class CSVReadTest {
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
        BufferedReader br = new BufferedReader(new FileReader("data/edited_twitter_posts.csv"));

        String line;

        int numTimeSlices = 6;

        String startTimeString = "2023-01-01 00:00:00";
        String endTimeString = "2023-06-30 23:59:59";
        String pattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date startDate = sdf.parse(startTimeString);
        long epochTime = startDate.getTime(); 

        Date endDate = sdf.parse(endTimeString);
        long endEpochTime = endDate.getTime();

        long millisecondsPerPeriod = (endEpochTime-epochTime)/numTimeSlices;

        List<TimeSlice> listOfTimeSlices = new ArrayList<>();
        Map<String, Post> globalPosts = new HashMap<>();
        long startTimeForSlice = epochTime;
        for(int i = 0; i<numTimeSlices; i++)
        {
            listOfTimeSlices.add(new TimeSlice(new Date(startTimeForSlice), new Date(startTimeForSlice+millisecondsPerPeriod-1)));
            TimeSlice t = listOfTimeSlices.get(i);
            startTimeForSlice += millisecondsPerPeriod;
        }


        List<List<Post>> postsPerTimeSlice = new ArrayList<>();

        for(int i = 0; i<numTimeSlices; i++)
        {
            postsPerTimeSlice.add(new ArrayList<Post>());
        }

        int lineNum = 0;
        while((line = br.readLine()) != null)
        {
            if(lineNum == 0)
            {
                lineNum = 1;
                continue;
            }
            //Data is contained within 01/01/2023 to 06/30/2023
            String[] values = line.split(",");
            
            //7th index has the time stamp
            Date postDate = sdf.parse(values[8]);

            //get postId, lat, and long at these specifc indices on the line
            String postId = values[3];
            String latitude = values[10];
            String longitude = values[11];

            for(int i = 0; i<numTimeSlices; i++)
            {
                TimeSlice tSlice = listOfTimeSlices.get(i);

                if(postDate.compareTo(tSlice.startTime) >= 0 && postDate.compareTo(tSlice.endTime) <= 0)
                {
                    ArrayList<String> keywords = new ArrayList<>();
                    keywords.add(values[9]);
                    Post post = new Post(postId, latitude+","+longitude, keywords);
                    globalPosts.put(postId, post);
                    postsPerTimeSlice.get(i).add(post);
                }
            }

        }
        
        List<List<Interaction>> interactionPerTimeSlice = new ArrayList<>();
        for(int i = 0; i<numTimeSlices; i++)
        {
            interactionPerTimeSlice.add(new ArrayList<Interaction>());
        }
        BufferedReader br2 = new BufferedReader(new FileReader("./data/twitter_interactions.csv"));
        
        lineNum = 0;
        while((line = br2.readLine()) != null)
        {
            if(lineNum == 0)
            {
                lineNum = 1;
                continue;
            }
            String[] values = line.split(",");

            String userId = values[1];
            String postId = values[2];
            String date = values[3];
            String latitude = values[4];
            String longitude = values[5];

            Date postDate = sdf.parse(date);

            for(int i = 0; i<numTimeSlices; i++)
            {
                TimeSlice tSlice = listOfTimeSlices.get(i);
                

                if(postDate.compareTo(tSlice.startTime) >= 0 && postDate.compareTo(tSlice.endTime) <= 0)
                {
                    ArrayList<String> keywords = new ArrayList<>();
                    Post post = new Post(postId, latitude+","+longitude, globalPosts.get(postId).keywords);
                    interactionPerTimeSlice.get(i).add(new Interaction(userId, post));
                    
                }
            }

        }

        for(int x = 0; x < listOfTimeSlices.size(); x++){
            listOfTimeSlices.get(x).indexUpdate( interactionPerTimeSlice.get(x), postsPerTimeSlice.get(x));
        }
        
        BaselineCOMQTest bscqTest = new BaselineCOMQTest(listOfTimeSlices);
        //bscqTest.test1();
        //bscqTest.test2();

        FastComCQTest fccqTest = new FastComCQTest(listOfTimeSlices);

        //fccqTest.test2();

        List<String> keywords = new ArrayList<>();
        keywords.add("receive");
        keywords.add("continue");
        keywords.add("yard");
        keywords.add("less");
        keywords.add("you");

       



        long startTime = System.currentTimeMillis();
        bscqTest.testVaryingTimeWithKeywords("2023-01-01 00:00:00", "2023-06-30 23:59:59", keywords);
        long endTime = System.currentTimeMillis();
        long difference = endTime-startTime;
        System.out.println("For r=7 ");
        System.out.println("Time to eval : "+difference);


    }
}
