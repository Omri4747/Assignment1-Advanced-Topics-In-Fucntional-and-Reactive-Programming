package bgu.atd.a1;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

    private Map<String, PrivateState> actors;
    private Map<String, Queue<Action>> action_queue;
    private Thread[] threads;
    private AtomicBoolean active;
    private Map<String, Semaphore> queues_locks;
    private ActorThreadPool me;

    /**
     * creates a {@link ActorThreadPool} which has nthreads. Note, threads
     * should not get started until calling to the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this thread
     *                 pool
     */
    public ActorThreadPool(int nthreads) {
        this.me = this;
        active = new AtomicBoolean(true);
        this.actors = new ConcurrentHashMap<>();
        this.action_queue = new ConcurrentHashMap<>();
        this.threads = new Thread[nthreads];
        this.queues_locks = new ConcurrentHashMap<>();
        for (int i = 0; i < nthreads; i++) {
            //TODO: sync between threads (sleep and wake) and how to wake up
            Runnable runnable_task = () -> {
                while (active.get()) {
                    for (String actorId : action_queue.keySet()) {
                        Semaphore sem = queues_locks.get(actorId);
                        if (!sem.tryAcquire()) {
                            continue;
                        }
                        Queue<Action> queue = action_queue.get(actorId);
                        PrivateState ps = actors.get(actorId);
                        if (queue.size() == 0) {
                            sem.release();
                            continue;
                        }
                        Action<?> act = queue.poll();
                        System.out.println("hi1");
						try {
							act.handle(me, actorId, ps);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						sem.release();
                    }
                }
            };
            threads[i] = new Thread(runnable_task);
        }
    }

    /**
     * getter for actors
     *
     * @return actors
     */
    public Map<String, PrivateState> getActors() {
        return actors;
    }

    /**
     * getter for actor's private state
     *
     * @param actorId actor's id
     * @return actor's private state
     */
    public PrivateState getPrivateState(String actorId) {
        return actors.get(actorId);
    }

    /**
     * submits an action into an actor to be executed by a thread belongs to
     * this thread pool
     *
     * @param action     the action to execute
     * @param actorId    corresponding actor's id
     * @param actorState actor's private state (actor's information)
     */
    public void submit(Action<?> action, String actorId, PrivateState actorState) {
        System.out.println("hi " + actorId);
        actors.putIfAbsent(actorId, actorState);
        queues_locks.putIfAbsent(actorId, new Semaphore(1));
        action_queue.putIfAbsent(actorId, new ConcurrentLinkedQueue<>());
        action_queue.get(actorId).add(action);
    }

    /**
     * closes the thread pool - this method interrupts all the threads and waits
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is interrupted
     */
    public void shutdown() throws InterruptedException {
        active.compareAndSet(true, false); //???????
        //TODO: check how to interrupt the threads
        for (Thread t : threads) {
            t.interrupt();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (Thread t : threads) {
            t.start();
        }
    }
}
