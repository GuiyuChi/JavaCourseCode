import java.util.concurrent.TimeUnit;

public class Main4 {
    // volatile 变量监控子线程是否执行完成 轮询变量会有额外的开销
    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();
        subThread.start();
        System.out.println("this is main thread.");
        while (!subThread.isFinish()) {
            Thread.sleep(10);
        }
        System.out.println("child thread return:" + subThread.getResult());
    }

    static class SubThread extends Thread {
        volatile boolean finish = false;
        String result = "";

        public boolean isFinish() {
            return finish;
        }

        public String getResult() {
            return result;
        }

        @Override
        public void run() {
            System.out.println("child thread begin to run.");
            // 模拟实际耗时
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.result = "sub result";
            this.finish = true;
        }
    }
}
