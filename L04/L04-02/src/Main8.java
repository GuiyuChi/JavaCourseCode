public class Main8 {
    // wait/notify机制 (不安全 如果子线程先获取到了object的同步块 可能会出现lost wake up问题)
    public static final Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();

        subThread.start();
        System.out.println("this is main thread.");

        synchronized (object) {
            object.wait();
        }


        System.out.println("child thread return:" + subThread.getResult());
    }

    static class SubThread extends Thread {
        String result = "";

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

            synchronized (object){
                this.result = "sub result";
                object.notify();
            }


        }
    }
}
