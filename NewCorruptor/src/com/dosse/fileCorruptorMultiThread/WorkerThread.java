package com.dosse.fileCorruptorMultiThread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dosse
 */
public abstract class WorkerThread extends Thread {

    private boolean destroyASAP = false;

    public class Queue {

        private final ArrayList<Packet> q = new ArrayList<>();

        public void addToQueue(Packet p) {
            synchronized (q) {
                q.add(p);
            }
        }

        public Packet removeFromQueue() {
            synchronized (q) {
                if (q.isEmpty()) {
                    return null;
                } else {
                    Packet p = q.get(0);
                    q.remove(0);
                    return p;
                }
            }
        }

        public int available() {
            synchronized (q) {
                return q.size();
            }
        }
    }
    
    private final Queue in = new Queue(), out = new Queue();

    public void run() {
        for (;;) {
            if (destroyASAP) {
                return;
            }
            if (in.available() == 0) {
                try {
                    sleep(0, 100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //i split this into 3 instructions because addToQueue and removeFromQueue are synchronized
                Packet toElaborate=in.removeFromQueue();
                Packet elaborated=elaborate(toElaborate);
                out.addToQueue(elaborated);
            }
        }
    }

    public abstract Packet elaborate(Packet p);

    public void addToQueue(Packet p) {
        synchronized (in) {
            in.addToQueue(p);
        }
    }

    public int available() {
        synchronized (out) {
            return out.available();
        }
    }

    public Packet removeFromQueue() {
        synchronized (out) {
            return out.removeFromQueue();
        }
    }

    public int getInputQueueLength() {
        synchronized (in) {
            return in.available();
        }
    }
    public void stopWorking(){
        destroyASAP=true;
    }
}
