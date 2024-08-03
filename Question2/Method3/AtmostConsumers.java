package Question2.Method3;

import java.util.*;

class NoticeBoard {
    LinkedList<Integer> noticeboard = new LinkedList<>();
    int value = 1;

    void produce() {
        synchronized (this) {
            // here noticeboard size is limited to 20 such that the producer thread would
            // stop its production and allows those specific 'atmost_number' of consumer
            // thread to consume the data.
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

public class AtmostConsumers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the atmost number of consumers");
        int key = sc.nextInt();
        NoticeBoard nb = new NoticeBoard();
        Thread producer = new Thread(new Producer(nb));
        // Initially the producer thread starts its process.
        producer.start();
        int count = 0;
        // Each user inputed value is considered as an consumer thread and only the
        // 'atmost_number' of consumer threads are allowed to access the noticeboard.
        while (sc.hasNextInt()) {
            sc.nextInt();
            count++;
            // If any excess user inputs(consumer threads) are provided , then those excess
            // threads are denied access to the noticeboard which could be repeatedly
            // accessed by only those 'atmost_number' of consumer threads.

            if (count > key) {
                System.out.println("Excess number of consumers");
            } else {
                Thread consumer = new Thread(new Consumer(nb));
                consumer.start();
            }
        }
    }
}
