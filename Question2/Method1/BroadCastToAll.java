package Question2.Method1;

import java.util.*;

// It is the noticeboard(storage unit) in which writer(producer) writes the data and the reader(consumer) reads the data in synchronzed order.
class NoticeBoard {
    LinkedList<Integer> noticeboard = new LinkedList<>();
    int value = 1;
    // The noticeboard can store upto a maximum of 20 notices/data/information
    int max_limit = 20;

    // if the noticeboard has any empty spaces, producer will continuously writes
    // data to it until it reaches its max_limit.
    void produce() {
        synchronized (this) {
            while (noticeboard.size() == max_limit) {
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

    // If the noticeboard contain any information, then all the available
    // consumer(threads) consume the data form it in synchronized manner.
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

// In Producer class, the producer thread is created and allowed to perform
// changes in noticeboard.
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

// In consumer class, the specified number of consumer threads are created and
// are made to consume data from noticeboard if available.
class Consumer implements Runnable {
    NoticeBoard nb;

    Consumer(NoticeBoard obj) {
        nb = obj;
    }

    public void run() {
        while (true) {
            nb.consume();
            // sleep() is used to make the output slow and readble. And also to ensure that
            // the required operation is performed and the data has been updated/consumed.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class BroadCastToAll {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Consumer count");
        int key = sc.nextInt();
        NoticeBoard nb = new NoticeBoard();
        Thread producer = new Thread(new Producer(nb));
        // Initially producer is started
        producer.start();
        // then the noticeboard is made accessible to all the consumers through consumer
        // threads.
        for (int i = 0; i < key; i++) {
            Thread consumer = new Thread(new Consumer(nb));
            consumer.start();
        }
        sc.close();
    }
}
