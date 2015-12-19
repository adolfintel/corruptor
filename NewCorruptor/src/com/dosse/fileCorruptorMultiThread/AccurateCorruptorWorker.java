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
public class AccurateCorruptorWorker extends WorkerThread {

    private ThreadLocalRandom r = ThreadLocalRandom.current();
    private static final int[] mask = new int[8];

    static {
        for (int i = 0; i < 8; i++) {
            mask[i] = 1 << i;
        }
    }

    @Override
    public Packet elaborate(Packet p) {
        byte[] toCorrupt = (byte[]) p.getData();
        byte[] corrupted = new byte[toCorrupt.length];
        double pCorruption = (double) p.getOptions()[0];
        long cLen = (long) p.getOptions()[1];
        int b; //it's faster if i declare it here for some reason
        boolean currentBit;
        for (int i = 0; i < toCorrupt.length; i++) {
            if (i < cLen) {
                for (b = 0; b < 8; b++) {
                    currentBit = (toCorrupt[i] & mask[b]) > 0;
                    corrupted[i] = (byte) (corrupted[i] | (r.nextDouble() < pCorruption ? (currentBit ? 0 : mask[b]) : (currentBit ? mask[b] : 0)));
                }
            } else {
                corrupted[i] = toCorrupt[i];
            }
        }
        return new Packet(corrupted, null);
    }
}
