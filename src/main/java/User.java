/**
 * Created by wangke18 on 2018/12/7.
 */
public class User implements Comparable<User>, java.io.Serializable{
    private String name;
    private int age;

    public User myself(){
        return this;
    }

    //@Override
    public int compareTo(User o) {
        return 0;
    }

}
