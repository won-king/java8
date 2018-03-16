import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kewangk on 2018/1/27.
 */
public class Bucket {
    private static final int SIZE=10;
    private List<Bread> bucket=new ArrayList<Bread>(SIZE);
    //private final Object full=new Object();
    //private final Object empty=new Object();

    public synchronized void put(Bread b){
        if(bucket.size()==SIZE){
            /*empty.notify();
            try {
                System.out.println("producer sleep");
                full.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("produced a bread");
        bucket.add(b);
        this.notify();
    }

    public synchronized Bread get(){
        if(bucket.size()==0){
            /*full.notify();
            try {
                empty.wait();
                System.out.println("consumer sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Bread bread=bucket.get(bucket.size()-1);
        bucket.remove(bucket.size()-1);
        this.notify();
        return bread;
    }

}
