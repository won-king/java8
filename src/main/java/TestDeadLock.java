import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by kewangk on 2018/1/27.
 */
public class TestDeadLock {
    public static void main(String[] args) {
        ExecutorService executorService= Executors.newCachedThreadPool();
        SharedResource resource=new SharedResource();
        executorService.submit(new Task1(resource));
        executorService.submit(new Task2(resource));
        try {
            TimeUnit.SECONDS.sleep(60*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();
        System.out.println("system is forced to shutdown");
    }
}

class SharedResource{
    private volatile boolean done1=false;
    private volatile boolean done2=false;

    public void method1(){
        if(done2){
            done1=true;
            System.out.println("task1 done");
        }
    }
    public void method2(){
        if(done1){
            done2=true;
            System.out.println("task2 done");
        }
    }

    public boolean isDone1() {
        return done1;
    }

    public void setDone1(boolean done1) {
        this.done1 = done1;
    }

    public boolean isDone2() {
        return done2;
    }

    public void setDone2(boolean done2) {
        this.done2 = done2;
    }
}

class Task1 implements Runnable{
    private SharedResource resource;
    public Task1(SharedResource r){
        resource=r;
    }

    public void run() {
        while(!resource.isDone2() && !Thread.interrupted()){
            resource.method1();
        }
        System.out.println("task1 interrupted");
    }
}
class Task2 implements Runnable{
    private SharedResource resource;
    public Task2(SharedResource r){
        resource=r;
    }

    public void run() {
        while(!resource.isDone1() && !Thread.interrupted()){
            resource.method2();
        }
        System.out.println("task2 interrupted");
    }
}
