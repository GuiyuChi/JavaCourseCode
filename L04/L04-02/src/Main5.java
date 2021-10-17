public class Main5 {
    // 主线程中轮询调用子线程判断是否停止的 isAlive方法 (轮询会产生不必要的开销)
    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();
        subThread.start();
        System.out.println("this is main thread.");

        while(subThread.isAlive()){
            Thread.sleep(10);
        }
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
        }
    }
}
