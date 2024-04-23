import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.sql.Timestamp;
import java.util.*;

public class FastComCQ {
    List<TimeSlice> timeSlices;

    FastComCQ(List<TimeSlice> timeSlices)
    {
        this.timeSlices = timeSlices;
    }

    public List<List<String>> evalQuery(String community, Timestamp startTimestamp, Timestamp endTimestamp, int k, double cLatitude, double cLongitude, int r, List<String> keywords)
    {
        //contains a list of lists which contain (post id, loc, count), represented as a list
        List<List<List<String>>> queryLists = new ArrayList<>();
        PriorityQueue<Post> pq = new PriorityQueue<Post>(5,(l1,l2) -> {
            return Integer.compare(l1.getCount(), l2.getCount());
        });

        for(TimeSlice timeSlice: timeSlices)
        {
            if(timeSlice.endTime.compareTo(startTimestamp) < 0)
            {
                continue;
            }
            if(timeSlice.startTime.compareTo(endTimestamp) > 0)
            {
                break;
            }

            List<List<String>> entries = new ArrayList<>();


            Map<String, Map<String, List<String>>> invertedIndex = timeSlice.invertedIndex;

            List<Map<String, List<String>>> invertedIndexEntries = new ArrayList<>();

            for(String keyword: keywords)
            {
                invertedIndexEntries.add(invertedIndex.get(keyword));
            }

            for(Map<String, List<String>> invertedIndexEntry: invertedIndexEntries)
            {
                for(String postId: invertedIndexEntry.keySet())
                {
                    List<String> entry = new ArrayList<>();
                    entry.add(postId);
                    entry.addAll(invertedIndexEntry.get(postId));

                    entries.add(entry);
                }
            }

            queryLists.add(entries);
 
        }

        for(List<List<String>> entries: queryLists)
        {
            for (List<String> entry: entries)
            {
                String postId = entry.get(0);
                String location = entry.get(1);
                int count = Integer.parseInt(entry.get(2));
                List<List<String>> parentSlice = entries;
                Post e = new Post(postId, location, count, parentSlice);
                //Use IDcomparator to sort pq, store as lists of above variables
                String[] latitudeAndLongitude = location.split(",");

                double postLatitude = Double.parseDouble(latitudeAndLongitude[0]);
                double postLongitude = Double.parseDouble(latitudeAndLongitude[1]);

                double distance = Math.sqrt((postLatitude-cLatitude)*(postLatitude-cLatitude) + (postLongitude-cLongitude)*(postLongitude-cLongitude));

                if(distance < r)
                {
                    pq.add(e);
                    break;
                }
            }
        }
        List<List<String>> ans = new ArrayList<>();
        Iterator<Post> temp = pq.iterator();
        int sumQ = 0;
        while(temp.hasNext()){ 
            sumQ += temp.next().getCount();
        }

        while(!pq.isEmpty()){
            Post e = pq.poll(); 
                for(List<String> entry: e.parentSlice){
                    String postId = entry.get(0);
                    String location = entry.get(1);
                    int count = Integer.parseInt(entry.get(2));
                    //Use IDcomparator to sort pq, store as lists of above variables
                    String[] latitudeAndLongitude = location.split(",");

                    double postLatitude = Double.parseDouble(latitudeAndLongitude[0]);
                    double postLongitude = Double.parseDouble(latitudeAndLongitude[1]);

                    double distance = Math.sqrt((postLatitude-cLatitude)*(postLatitude-cLatitude) + (postLongitude-cLongitude)*(postLongitude-cLongitude));

                    if(distance < r)
                    {
                       pq.add(new Post(postId,location, count, e.parentSlice));
                       sumQ -= e.getCount();
                       sumQ += count;
                       break;
                    }
                }
            
            int count_total = e.getCount();
            for(List<List<String>> entries : queryLists){
                if(entries != e.parentSlice){
                    for(List<String> entry : entries){
                        String postId = entry.get(0);
                        if(postId.equals(e.getId())){
                            count_total += Integer.parseInt(entry.get(2));
                        } 
                    }
                }
            }
            List<String> t = new ArrayList<>();
            String s = "" + count_total;
            t.add(0,e.getId());
            t.add(1,s);
            ans.add(t);
            if(ans.size() < k){
                ans.add(t);
            }
            else if(count_total > Integer.parseInt(ans.get(k).get(1))){
                ans.remove(k);
                ans.add(t);
            }
            if(ans.size() > k && Integer.parseInt(ans.get(k).get(1)) < sumQ)
                break;
        }
        return ans;
    }
}