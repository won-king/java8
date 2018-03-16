/**
 * Created by kewangk on 2018/1/27.
 */
public class Producer implements Runnable {
    private Bucket bucket;
    private final Object full;
    private final Object empty;

    public Producer(Bucket b, Object f, Object e){
        bucket=b;
        full=f;
        empty=e;
    }

    public void run() {
        while (!Thread.interrupted()){

            Bread b=new Bread();
            bucket.put(b);
        }
    }
}
