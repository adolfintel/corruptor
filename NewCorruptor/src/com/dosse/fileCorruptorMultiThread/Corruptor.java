package com.dosse.fileCorruptorMultiThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class Corruptor extends Thread {

    private FileInputStream fis;
    private FileOutputStream fos;
    private double p;
    private boolean bitwise;
    private long length;
    private long processed = 0;
    private static final int BUFFER_SIZE = 32768;
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int MAX_MEMORY_IN = 32;
    private static final int MAX_MEMORY_OUT = 32;
    private final WorkerThread[] wt = new WorkerThread[N_THREADS];
    private boolean destroyASAP = false;
    private long corruptionEndsAt;

    public Corruptor(String in, String out, double p, boolean bitwise, int bytesToSkip, int bytesToSkipFromEnd) throws FileNotFoundException, IOException {
        fis = new FileInputStream(in);
        this.length = new File(in).length();
        fos = new FileOutputStream(out);
        this.p = p;
        this.bitwise = bitwise;
        byte[] skipped = new byte[bytesToSkip];
        int n = fis.read(skipped);
        if (n == -1) {
            return;
        }
        fos.write(skipped, 0, n);
        processed += n;
        corruptionEndsAt = length - bytesToSkipFromEnd;
    }

    public void stopWorking() {
        destroyASAP = true;
        for (WorkerThread w : wt) {
            w.stopWorking();
            try {
                w.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    public double getProgress() {
        return (double) processed / (double) length;
    }

    public void run() {
        for (int i = 0; i < wt.length; i++) {
            wt[i] = bitwise ? new AccurateCorruptorWorker() : new FastCorruptorWorker();
            wt[i].setPriority(MIN_PRIORITY);
            wt[i].start();
        }
        Thread writer = new Thread() {
            public void run() {
                for (;;) {
                    if (destroyASAP) {
                        try {
                            fos.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                    boolean ok = true;
                    for (int i = 0; i < wt.length; i++) {
                        if (wt[i] != null && wt[i].isAlive() && wt[i].available() == 0) {
                            ok = false;
                            break;
                        }
                    }
                    if (!ok) {
                        try {
                            sleep(0, 100);
                        } catch (InterruptedException ex) {
                        }
                    } else {
                        for (int i = 0; i < wt.length; i++) {
                            if (wt[i] == null || !wt[i].isAlive()) {
                                try {
                                    fos.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return;
                            }
                            try {
                                byte[] toWrite = (byte[]) (wt[i].removeFromQueue().getData());
                                if (toWrite.length == 0) {
                                    fos.close();
                                    return;
                                }
                                fos.write(toWrite);
                                processed += toWrite.length;
                            } catch (IOException ex) {
                                Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        };
        writer.start();
        long rPos = 0;
        for (;;) {
            if (destroyASAP) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            boolean done = false;
            for (int i = 0; i < wt.length; i++) {
                if (wt[i] == null || !wt[i].isAlive()) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
                try {
                    byte[] in = new byte[BUFFER_SIZE];
                    int n = fis.read(in);
                    if (n == -1) {
                        done = true;
                        in = new byte[0];
                    } else {
                        if (n != in.length) {
                            byte[] newIn = new byte[n];
                            System.arraycopy(in, 0, newIn, 0, n);
                            in = newIn;
                        }
                    }
                    if (rPos + in.length <= corruptionEndsAt) {
                        wt[i].addToQueue(new Packet(in, new Object[]{p, (long)in.length}));
                    } else {
                        wt[i].addToQueue(new Packet(in, new Object[]{p, (long)(in.length+(corruptionEndsAt-(rPos+in.length)))}));
                    }
                    rPos += in.length;
                } catch (IOException ex) {
                    Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (done) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (writer != null && writer.isAlive()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }
                return;
            } else {
                long totMIn = 0, totMOut = 0;
                do {
                    if (destroyASAP) {
                        try {
                            fis.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                    for (WorkerThread w : wt) {
                        totMIn += w.getInputQueueLength() * BUFFER_SIZE;
                        totMOut += w.available() * BUFFER_SIZE;
                    }
                    totMIn /= 1048576;
                    totMOut /= 1048576;
                    if (totMIn > MAX_MEMORY_IN || totMOut > MAX_MEMORY_OUT) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Corruptor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } while (totMIn > MAX_MEMORY_IN || totMOut > MAX_MEMORY_OUT);
            }
        }
    }
}
