/**
 * Created by kewangk on 2018/1/27.
 */
public class Consumer implements Runnable {
    private Bucket bucket;
    private final Object full;
    private final Object empty;
    public Consumer(Bucket b, Object f, Object e){
        bucket=b;
        full=f;
        empty=e;
    }

    public void run() {
        while (!Thread.interrupted()){
            Bread b=bucket.get();
            System.out.println(b);
        }
    }
}
