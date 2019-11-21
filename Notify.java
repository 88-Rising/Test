public class Notify {
    private static volatile int COUNT;

    public synchronized static void produce() {  //对当前类进行加锁 这样是不行的 要给当前对象加锁
        COUNT += 3;

    }

    public synchronized static void consume() {
        COUNT--;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {  //生产者
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for(int j=0;j<10;j++){
                            synchronized (Notify.class){

                                if (COUNT + 3 > 100) {//如果库存满了则进行等待
                                  Notify.class.wait();
                                }
                                produce();
                                System.out.println(Thread.currentThread().getName()+"生产，库存总量："+COUNT);
                                Thread.sleep(500);
                                Notify.class.notifyAll(); //唤醒锁

                            }
                            Thread.sleep(500);


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }).start();
    }
        for (int i = 0; i < 3; i++) { //消费者
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            synchronized (Notify.class) {
                                if (COUNT == 0) {   //如果库存为0 则进入等待
                                    Notify.class.wait();
                                }
                                consume();
                                System.out.println(Thread.currentThread().getName() + "消费，库存总量：" + COUNT);
                                Thread.sleep(500);
                                Notify.class.notifyAll();  //唤醒锁
                            }
                            Thread.sleep(500);
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

}
