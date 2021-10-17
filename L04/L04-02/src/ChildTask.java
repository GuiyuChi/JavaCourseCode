import java.util.concurrent.Callable;

public class ChildTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("child thread begin to run.");
        // 模拟实际耗时
        Thread.sleep(500);
        return "sub result";
    }
}
