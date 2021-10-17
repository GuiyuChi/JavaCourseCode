import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main1 {
    // FutureTask
    public static void main(String[] args) {
        ChildTask childTask = new ChildTask();
        FutureTask<String> futureTask = new FutureTask<>(childTask);
        // 启动子线程执行
        new Thread(futureTask).start();
        System.out.println("this is main thread.");
        try {
            System.out.println("child thread return:"+futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
