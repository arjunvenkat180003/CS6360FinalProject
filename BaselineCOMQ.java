import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

public class BaselineCOMQ {
    List<TimeSlice> timeSlices;

    BaselineCOMQ(List<TimeSlice> timeSlices)
    {
        this.timeSlices = timeSlices;
    }

    public List<String> evalQuery(String community, Date startTimestamp, Date endTimestamp, int k, double cLatitude, double cLongitude, int r, List<String> keywords)
    {
        //contains a list of lists which contain (post id, loc, count), represented as a list
        List<List<List<String>>> queryLists = new ArrayList<>();
        //map postId to count
        Map<String, Integer> hashStructureA = new HashMap<>();

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

            Map<String, Post> masterIndex = timeSlice.masterIndex;
            Map<String, List<String>> masterHashStructure = timeSlice.masterHashStructure;

            //System.out.println("master index "+masterIndex.keySet());
            //System.out.println("master hash structure "+masterHashStructure.keySet());

            Map<String, Map<String, List<String>>> invertedIndex = timeSlice.invertedIndex;

            List<Map<String, List<String>>> invertedIndexEntries = new ArrayList<>();

            for(String keyword: keywords)
            {
                //System.out.println("keyword (test1) "+keyword);
                //System.out.println(" inverted index key set "+invertedIndex.keySet());
                //System.out.println(invertedIndex.get(keyword));
                if(invertedIndex.containsKey(keyword))
                    invertedIndexEntries.add(invertedIndex.get(keyword));
            }

            //System.out.println("inverted index entries "+invertedIndexEntries);

            for(Map<String, List<String>> invertedIndexEntry: invertedIndexEntries)
            {
                //System.out.println(invertedIndexEntry);
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
        //System.out.println(queryLists);
        for(List<List<String>> entries: queryLists)
        {
            for (List<String> entry: entries)
            {
                String postId = entry.get(0);
                String location = entry.get(1);

                String[] latitudeAndLongitude = location.split(",");

                double postLatitude = Double.parseDouble(latitudeAndLongitude[0]);
                double postLongitude = Double.parseDouble(latitudeAndLongitude[1]);

                double distance = Math.sqrt((postLatitude-cLatitude)*(postLatitude-cLatitude) + (postLongitude-cLongitude)*(postLongitude-cLongitude));

                if(distance < r)
                {
                    if(hashStructureA.containsKey(postId))
                    {
                        //System.out.println("adding another +1 to the key "+postId);
                        hashStructureA.put(postId, hashStructureA.get(postId)+1);
                    }
                    else
                    {
                        hashStructureA.put(postId, Integer.parseInt(entry.get(2)));
                    }
                }
            }
        }
        
        List<String> topPostIds = new ArrayList<>(hashStructureA.keySet());
        //System.out.println("hash structure a keyset "+hashStructureA.keySet());

        Collections.sort(topPostIds, new PostIdComparator(hashStructureA));
        //System.out.println("k "+k);
        if(k <= topPostIds.size())
        {
            return topPostIds.subList(0, k);
        }
        else
        {
            return topPostIds;
        }
    }


}
