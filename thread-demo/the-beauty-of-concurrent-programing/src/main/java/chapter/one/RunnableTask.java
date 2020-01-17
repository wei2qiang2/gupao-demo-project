package chapter.one;

public class RunnableTask implements Runnable {

    public void run() {
        System.out.println("I'm a child thread");
    }

    public static void main(String[] args) {

        RunnableTask one = new RunnableTask();
        RunnableTask two = new RunnableTask();
    }
}
