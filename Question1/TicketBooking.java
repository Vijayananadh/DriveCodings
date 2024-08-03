package Question1;

import java.util.*;

//It is the class where either ticket booking/canceling is handled
class TicketCounter {
    private int tickets = 150;

    public void book(int val) {
        tickets = tickets - val;
    }

    public void cancel(int val) {
        tickets = tickets + val;
    }

    public int getTicketCount() {
        return tickets;
    }
}

// Whenever an customer try to access the ticket counter, an separate thread is
// created and the process is handles in an synchronized manner
class Customer implements Runnable {
    TicketCounter tc;
    int val;

    Customer(TicketCounter obj, int val) {
        this.tc = obj;
        this.val = val;
    }

    // if the user provided value is greater than 0, it is assumed that the ticket
    // need to booked and the book() is called.
    // if the user provided value is lesser than 0, then it calls the cancel()
    // method in which the user specified number of tickets is cancelled.

    public synchronized void run() {
        if (val > 0) {
            tc.book(val);
        } else {
            tc.cancel(Math.abs(val));
        }
    }
}

public class TicketBooking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TicketCounter ticket = new TicketCounter();
        // user can provide space separated inputs such that each value is considered as
        // an separate thread and it is processed accordingly.
        while (sc.hasNextInt()) {
            int val = sc.nextInt();
            Thread t = new Thread(new Customer(ticket, val));

            // if the user expects more number of tickets than available then the request is
            // cancelled with an message.
            if (val > ticket.getTicketCount()) {
                System.out.println("Tickets Not Available");
            } else {
                t.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Also if the user tries to cancel more number of tickets than the
                // permittable/booked tickets then it also denied.
                if (ticket.getTicketCount() > 150) {
                    System.out.println("Tickets not cancellable");
                    ticket.book(Math.abs(val));

                }
                // After each thread is processed the ticket status is printed.
                System.out.println("Current Ticket Status : " + ticket.getTicketCount());
            }
        }
        sc.close();
    }
}
