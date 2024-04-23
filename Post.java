import java.util.List;
public class Post {
    String postId;
    String location;
    int count;
    List<List<String>> parentSlice;

    Post(String pId, String loc, int c, List<List<String>> l)
    {
        postId = pId;
        location = loc;
        count = c;
        parentSlice = l;
    }

    public int getCount(){
        return count;
    }

    public String getId(){return postId;}
}
