import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kewangk on 2018/1/27.
 */
public class TestPSModel {
    public static void main(String[] args) {
        Bucket bucket=new Bucket();
        Object full=new Object();
        Object empty=new Object();
        //ExecutorService executorService= Executors.newCachedThreadPool();
        //executorService.submit(new Producer(bucket,full, empty));
        //executorService.submit(new Consumer(bucket,full, empty));
        new Thread(new Producer(bucket,full, empty),"producer").start();
        new Thread(new Consumer(bucket,full, empty),"consumer").start();
    }
}
