package bgu.atd.a1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {

    private Promise<R> promise;
    private callback callback;
    protected ActorThreadPool pool;
    protected String actorId;
    protected PrivateState ps;
    private String actionName;

    protected Action() {
        promise = new Promise<>();
        callback = null;
        pool = null;
        actorId = null;
        ps = null;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start() throws IllegalAccessException;


    /**
     * start/continue handling the action
     * <p>
     * this method should be called in order to start this action
     * or continue its execution in the case where it has been already started.
     * <p>
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     */
    /*package*/
    final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) throws IllegalAccessException {
        if (callback == null) {
            this.pool = pool;
            this.actorId = actorId;
            this.ps = actorState;
            start();
        }
        else
            callback.call();
    }


    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * <p>
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions the list of dependencies to complete before executing the callback
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) throws IllegalAccessException {
        this.callback = callback;
        AtomicInteger dependencies = new AtomicInteger(actions.size());
        for (Action<?> action : actions) {
            action.promise.subscribe(() -> {
                int oldVal, newVal;
                do {
                    oldVal = dependencies.get();
                    newVal = oldVal - 1;
                } while (!dependencies.compareAndSet(oldVal, newVal));
                if (newVal==0){
                    sendMessage(this, actorId, ps);
                }
            });
        }
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) throws IllegalAccessException {
        promise.resolve(result);
    }

    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
        return promise;
    }

    /**
     * send an action to an other actor
     *
     * @param action     the action
     * @param actorId    actor's id
     * @param actorState actor's private state (actor's information)
     */
    public void sendMessage(Action<?> action, String actorId, PrivateState actorState) {
        pool.submit(action, actorId, actorState);
    }

    /**
     * set action's name
     *
     * @param actionName
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * @return action's name
     */
    public String getActionName() {
        return actionName;
    }
}
