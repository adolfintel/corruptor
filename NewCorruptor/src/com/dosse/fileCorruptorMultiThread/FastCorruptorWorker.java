package com.dosse.fileCorruptorMultiThread;

import java.util.concurrent.ThreadLocalRandom;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dosse
 */
public class FastCorruptorWorker extends WorkerThread {

    private ThreadLocalRandom r = ThreadLocalRandom.current();

    @Override
    public Packet elaborate(Packet p) {
        byte[] toCorrupt = (byte[]) p.getData();
        byte[] corrupted = new byte[toCorrupt.length];
        double pCorruption = (double) p.getOptions()[0];
        long cLen = (long) p.getOptions()[1];
        for (int i = 0; i < toCorrupt.length; i++) {
            if (i < cLen) {
                corrupted[i] = (r.nextDouble() < pCorruption) ? (byte) (r.nextDouble() * 256) : toCorrupt[i];
            } else {
                corrupted[i] = toCorrupt[i];
            }
        }
        return new Packet(corrupted, null);
    }
}
