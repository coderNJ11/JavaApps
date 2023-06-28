import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadingRunnable {

    public static void threadinRun(){

        Thread t1 = new Thread(() ->{
            System.out.println("Thread 1 :::: " + Thread.currentThread().getId());
        });

        Runnable r2 = () ->{
            System.out.println("Runnable thread 2 :::: " + Thread.currentThread().getId());
        };

        Thread t2 = new Thread(r2);

        Runnable r3 = () ->{
            System.out.println("Runnable threading 3 :::: " + Thread.currentThread().getId());
        };
        Thread t3 = new Thread(r3);

        Callable c4 = () ->{
          return "callable thread:::::::: "+ Thread.currentThread().getId();
        };

        FutureTask<String> ft = new FutureTask<>(c4);
        Thread t4 = new Thread(ft);
        t4.start();
        try {
            System.out.println(ft.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        t1.start();
        t2.start();
        t3.start();
    }
}
