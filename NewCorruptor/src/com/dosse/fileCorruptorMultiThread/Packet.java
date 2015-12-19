package com.dosse.fileCorruptorMultiThread;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dosse
 */
public class Packet {
    private Object data;
    private Object[] options;

    public Packet(Object data, Object[] options) {
        this.data = data;
        this.options = options;
    }

    public Object getData() {
        return data;
    }

    public Object[] getOptions() {
        return options;
    }
    
}
