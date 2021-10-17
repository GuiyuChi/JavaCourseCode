import java.util.concurrent.CountDownLatch;

public class Main6 {
    // CountDownLatch设置单任务
    public static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();
        subThread.start();
        System.out.println("this is main thread.");

        latch.await();

        System.out.println("child thread return:"+subThread.getResult());
    }

    static class SubThread extends Thread{
        String result = "";
        public String getResult(){
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
            this.result =  "sub result";
            latch.countDown();
        }
    }
}
