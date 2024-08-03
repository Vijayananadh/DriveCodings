package Question2.Method2;

import java.util.*;

class NoticeBoard {
    LinkedList<Integer> noticeboard = new LinkedList<>();
    int value = 1;

    void produce() {
        synchronized (this) {
            // here noticeboard size is limited to 20 such that the producer thread would
            // stop its production and allows the consumer thread to consume the data.
            while (noticeboard.size() >= 20) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            noticeboard.add(value++);
            notifyAll();
        }
    }

    void consume() {
        synchronized (this) {
            while (noticeboard.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int val = noticeboard.removeFirst();
            System.out.println(Thread.currentThread().getName() + " consumed " + val);
            notifyAll();
        }
    }
}

class Producer implements Runnable {
    NoticeBoard nb;

    Producer(NoticeBoard obj) {
        nb = obj;
    }

    public void run() {
        while (true) {
            nb.produce();
        }
    }
}

class Consumer implements Runnable {
    NoticeBoard nb;

    Consumer(NoticeBoard obj) {
        nb = obj;
    }

    public void run() {
        while (true) {
            nb.consume();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class AtleastConsumers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the atleast number of consumers");
        int key = sc.nextInt();
        NoticeBoard nb = new NoticeBoard();
        int flag = 0;
        int count = 0;
        // Multiple consumers(user input) can enter the process, but the production will
        // get started only if the atleast_number of consumer are available.
        while (sc.hasNextInt()) {
            sc.nextInt();
            Thread consumer = new Thread(new Consumer(nb));
            consumer.start();
            count++;
            // once if the required count of consumers are available, then those consumers
            // can access the noticeboard in a synchronized manner.
            if (count > key && flag == 0) {
                flag = 1;
                Thread producer = new Thread(new Producer(nb));
                producer.start();
                System.out.println("Production starts");
            }

        }
        sc.close();
    }
}
