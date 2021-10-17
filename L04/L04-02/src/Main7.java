import java.util.concurrent.Semaphore;

public class Main7 {
    // Semaphore 信号量同步方式
    private static final Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();
        subThread.start();
        System.out.println("this is main thread.");

        semaphore.acquire();

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
            semaphore.release();
        }
    }
}
