import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeSlice
{
    //Master Hash Structure maps Id (as String) to location/interactions
    //the list<string> will consist of two elements, the location as a string, and the number of interactions as a string
    Map<String, List<String>> masterHashStructure;
    //maps a topic 
    Map<String, Map<String, List<String>>> invertedIndex;
    
    public TimeSlice()
    {
        masterHashStructure = new HashMap<>();
        invertedIndex = new HashMap<>();

    }

    public void appendPostData(String postID, String location, String interactions, List<String> keyWords)
    {
        List<String> masterHashStructureValue = new ArrayList<>();
        masterHashStructureValue.add(location); 
        masterHashStructureValue.add(interactions);

        masterHashStructure.put(postID, masterHashStructureValue);

        for(String keyWord: keyWords)
        {
            if(invertedIndex.get(keyWord) == null)
            {
                //inner hash structure for each key word in the inverted index
                Map<String, List<String>> innerHashStructure = new HashMap<>();

                //create values for innerHashStructure
                List<String> innerHashStructureValue = new ArrayList<>();
                innerHashStructureValue.add(location);
                innerHashStructureValue.add(interactions);

                innerHashStructure.put(postID, innerHashStructureValue);

                invertedIndex.put(keyWord, innerHashStructure);

            }
            else
            {
                Map<String, List<String>> innerHashStructure = invertedIndex.get(keyWord);

                //create values for innerHashStructure
                List<String> innerHashStructureValue = new ArrayList<>();
                innerHashStructureValue.add(location);
                innerHashStructureValue.add(interactions);

                innerHashStructure.put(postID, innerHashStructureValue);

                invertedIndex.put(keyWord, innerHashStructure);


            }
        }
    }



}