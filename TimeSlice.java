import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;
import java.util.Date;

public class TimeSlice
{
    Date startTime;
    Date endTime;

    
    long endTimeInMS;

    //********** MASTER INDEX */
    Map<String, Post> masterIndex;

    //****BOTH OF THESE FOR COMMUNITY INDEX */
    //Master Hash Structure maps Id (as String) to location/interactions
    //the list<string> will consist of two elements, the location as a string, and the number of interactions as a string
    Map<String, List<String>> masterHashStructure;
    //maps a topic 
    Map<String, Map<String, List<String>>> invertedIndex;

    //*****User Index******
    //map userId (as a string) to a list of (posts and locations, which make up their own object as a list of two values)
    Map<String, List<List<String>>> userIndex;
    
    public TimeSlice(Date startTime, Date endTime)
    {

        masterIndex = new HashMap<>();
        masterHashStructure = new HashMap<>();
        invertedIndex = new HashMap<>();
        userIndex = new HashMap<>();

        this.startTime = startTime;
        this.endTime = endTime;
    }

    //interactions - a list of (user, post)
    //posts - a list of (id, count, location)
    public void indexUpdate(List<Interaction> interactions, List<Post> posts)
    {
        //index into master structure
        for(Post post: posts)
        {
            masterIndex.put(post.postId, post);

            // String postId = post.get(0);
            // String count = post.get(1);
            // String location = post.get(2);

            // List<String> masterHashStuctureValue = new ArrayList<>();
            // masterHashStuctureValue.add(location);
            // masterHashStuctureValue.add(count);

            // masterHashStructure.put(postId, masterHashStuctureValue);

            
        }

        //index into user hash structure

        for(Interaction interaction: interactions)
        {

            String userId = interaction.userId;
            Post p = interaction.post;
            String postId = p.postId;
            String location = p.location;
            List<String> keywords = p.keywords;
            

            //update userIndex
            if(userIndex.containsKey(userId))
            {
                List<List<String>> userIndexValues = userIndex.get(userId);
                List<String> newValue = new ArrayList<>();
                newValue.add(postId);
                newValue.add(location);

                userIndexValues.add(newValue);

            }
            else
            {
                List<List<String>> userIndexValues = new ArrayList<>();
                List<String> newValue = new ArrayList<>();
                newValue.add(postId);
                newValue.add(location);

                userIndexValues.add(newValue);
            }

            //update master hash structure in community index
            if(masterHashStructure.containsKey(postId))
            {
                List<String> postValue = masterHashStructure.get(postId);
                int count = Integer.parseInt(postValue.get(1));

                postValue.set(1,  ""+(count+1));  

                masterHashStructure.put(postId, postValue);
            }
            else
            {
                List<String> postValue = new ArrayList<>();
                postValue.add(location);
                postValue.add("1");

                masterHashStructure.put(postId, postValue);
            }

            for(String keyword: keywords)
            {
                if(invertedIndex.containsKey(keyword))
                {
                    Map<String, List<String>> keywordHashStructure = invertedIndex.get(keyword);

                    List<String> postValue = keywordHashStructure.get(postId);
                    int count = Integer.parseInt(postValue.get(1));

                    postValue.set(1,  ""+(count+1)); 
                    
                    keywordHashStructure.put(postId, postValue);

                }
                else
                {
                    Map<String, List<String>> keywordHashStructure = new HashMap<>();

                    List<String> postValue = new ArrayList<>();
                    postValue.add(location);
                    postValue.add("1");

                    keywordHashStructure.put(postId, postValue);
                }
            }


        }
    }





    
    
    // public void appendPostData(String postID, String location, String interactions, List<String> keyWords)
    // {
    //     List<String> masterHashStructureValue = new ArrayList<>();
    //     masterHashStructureValue.add(location); 
    //     masterHashStructureValue.add(interactions);

    //     masterHashStructure.put(postID, masterHashStructureValue);

    //     for(String keyWord: keyWords)
    //     {
    //         if(invertedIndex.get(keyWord) == null)
    //         {
    //             //inner hash structure for each key word in the inverted index
    //             Map<String, List<String>> innerHashStructure = new HashMap<>();

    //             //create values for innerHashStructure
    //             List<String> innerHashStructureValue = new ArrayList<>();
    //             innerHashStructureValue.add(location);
    //             innerHashStructureValue.add(interactions);

    //             innerHashStructure.put(postID, innerHashStructureValue);

    //             invertedIndex.put(keyWord, innerHashStructure);

    //         }
    //         else
    //         {
    //             Map<String, List<String>> innerHashStructure = invertedIndex.get(keyWord);

    //             //create values for innerHashStructure
    //             List<String> innerHashStructureValue = new ArrayList<>();
    //             innerHashStructureValue.add(location);
    //             innerHashStructureValue.add(interactions);

    //             innerHashStructure.put(postID, innerHashStructureValue);

    //             invertedIndex.put(keyWord, innerHashStructure);


    //         }
    //     }
    // }



}
