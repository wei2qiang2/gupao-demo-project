package chapter.one;

public class ThreadTest extends Thread {

    @Override
    public void run() {
        System.out.println("I'm a child thread");
    }

    public static void main(String[] args) {

        ThreadTest threadTest = new ThreadTest();
        threadTest.start();
    }

}
