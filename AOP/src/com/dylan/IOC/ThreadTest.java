package com.dylan.IOC;

//public class ThreadTest implements Runnable{
//    public static void main(String[] args){
//        Thread thread = new Thread(new ThreadTest());
//        thread.start();
//    }
//    @Override
//    public void run() {
//        System.out.println("2222");
//    }
//}

/**
 * 二 : 也是Runable使用匿名内部类
 */
//public class ThreadTest{
//    public static void main(String[] args){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("3333");
//            }
//        });
//        thread.start();
//    }
//}

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * 三 : Executor线程池，同样要实现runable接口,但是底层实现是使用了 new LinkedBlockingQueue
// * service.execute()就类似thread.start(),开始运行run
// */
//public class ThreadTest implements Runnable{
//    public static void main(String[] args){
//        ExecutorService service = Executors.newFixedThreadPool(10);
//        for (int i = 0; i < 11; i++) {
//            service.execute(new ThreadTest());
//        }
//        service.shutdownNow();//关闭线程池
//    }
//
//    @Override
//    public void run() {
//        System.out.println("3333");
//    }



/**
 *  ThreadLocal 第二题
 */
//public class ThreadTest {
//    private ThreadLocal<String> local = new ThreadLocal<>();
//    public void a(){
//        local.set("zhouyu");
//        b();
//    }
//    public void b(){
//        String s = local.get();
//        System.out.println(s);
//    }
//    public static void main(String[] args){
//        ThreadTest test = new ThreadTest();
//        test.a();
//    }
//}
public class ThreadTest extends Thread {
    public static void main(String[] args){
        ThreadTest threadTest = new ThreadTest();
        threadTest.start();
    }
    @Override
    public void run() {
        System.out.println(22);
    }
}


