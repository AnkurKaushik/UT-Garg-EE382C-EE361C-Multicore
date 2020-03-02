package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
    final Lock lock = new ReentrantLock();
    final Condition ropeNotFull = lock.newCondition();
    final Condition ropeEmpty = lock.newCondition();

    int numMonkeysOnRope = 0;
    int ropeDirection = -2;
    boolean kongTime = false;

    public Monkey() {
    }

    public void ClimbRope(int direction) throws InterruptedException {
        if (direction == -1) {
            kongTime = true;
        }
        lock.lock();
        try {
            while ((kongTime && (direction != -1 || numMonkeysOnRope > 0)) ||
                    numMonkeysOnRope >= 3 ||
                    (numMonkeysOnRope > 0 && ropeDirection != direction)) {
                System.out.println("dir: "+direction);
                System.out.println("num: "+numMonkeysOnRope);
                ropeNotFull.signalAll();
                ropeNotFull.await();
            }

            numMonkeysOnRope++;

            ropeDirection = direction;
        } finally {
            lock.unlock();
        }
    }

    public void LeaveRope() {
        lock.lock();

        numMonkeysOnRope--;

        ropeNotFull.signalAll();

        lock.unlock();
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return numMonkeysOnRope;
    }

}

