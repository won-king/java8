/**
 * Created by kewangk on 2018/1/27.
 */
public class Bread {
    private static int count=0;
    private final int id=count++;

    @Override
    public String toString() {
        return "id->"+id;
    }
}
