import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main2 {
    // 线程池方式执行Callable任务
    public static void main(String[] args) {
        ChildTask childTask = new ChildTask();
        ExecutorService executorService = null;
        try {
            executorService = Executors.newSingleThreadExecutor();
            Future<String> future = executorService.submit(childTask);
            System.out.println("this is main thread.");
            System.out.println("child thread return:"+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }


}