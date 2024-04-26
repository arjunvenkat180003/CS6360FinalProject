import java.util.Map;
import java.util.Comparator;
class PostIdComparator implements Comparator<String>
    {
        Map<String, Integer> map = null;
        PostIdComparator(Map<String, Integer> map)
        {
            this.map = map;
            System.out.println(map);
        }
        public int compare(String postId1, String postId2)
        {
            if(map.get(postId1) > map.get(postId2))
            {
                //System.out.println("less");
                return 1;
            }
            else if (map.get(postId1) < map.get(postId2))
            {
                //System.out.println("greater");
                return -1;
            }
            else
            {
                //System.out.println("equal");
                return 0;
            }
        }
    }
