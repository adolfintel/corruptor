/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package easterEgg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author dosse
 */
public class NewClass extends Thread {

    private static final byte[] iLoveAlienDicks = new byte[]{(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0xcb, (byte) 0xb1, (byte) 0x0d, (byte) 0x80, (byte) 0x30, (byte) 0x0c, (byte) 0x05, (byte) 0xd1, (byte) 0x34, (byte) 0xec, (byte) 0xf1, (byte) 0x1b, (byte) 0x06, (byte) 0x60, (byte) 0x17, (byte) 0x24, (byte) 0x6a, (byte) 0x03, (byte) 0x16, (byte) 0x8e, (byte) 0x04, (byte) 0x8e, (byte) 0xb0, (byte) 0x9d, (byte) 0x4c, (byte) 0xc5, (byte) 0x6a, (byte) 0xec, (byte) 0x40, (byte) 0x6a, (byte) 0xda, (byte) 0x7b, (byte) 0xba, (byte) 0xe7, (byte) 0x4d, (byte) 0x43, (byte) 0xa4, (byte) 0x69, (byte) 0x16, (byte) 0xc6, (byte) 0x55, (byte) 0x4c, (byte) 0xb3, (byte) 0x1e, (byte) 0xf0, (byte) 0xaa, (byte) 0x10, (byte) 0x72, (byte) 0x34, (byte) 0xd2, (byte) 0xbb, (byte) 0x66, (byte) 0x17, (byte) 0xde, (byte) 0x11, (byte) 0x9d, (byte) 0xa5, (byte) 0x98, (byte) 0xe5, (byte) 0xf5, (byte) 0x64, (byte) 0x68, (byte) 0x3e, (byte) 0x24, (byte) 0x22, (byte) 0x8d, (byte) 0x8b, (byte) 0x50, (byte) 0x80, (byte) 0x7e, (byte) 0x1d, (byte) 0x51, (byte) 0xfa, (byte) 0xdc, (byte) 0xb8, (byte) 0xc3, (byte) 0x56, (byte) 0xcd, (byte) 0xf9, (byte) 0x03, (byte) 0x1e, (byte) 0xb8, (byte) 0x7f, (byte) 0x8a, (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    @Override
    public void run() {
        alienSex(0xea60);
        for (;;) {
            int garrusIsSexy = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            alienSex(0x2710);
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 6 && garrusIsSexy == 5) {
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(iLoveAlienDicks)));
                    String ee = (String) ois.readObject();
                    JOptionPane.showMessageDialog(new JOptionPane(), ee);
                } catch (Exception ex) {
                } finally {
                    try {
                        ois.close();
                    } catch (Throwable ex) {
                    }
                }
            }
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23 && garrusIsSexy == 22) {
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(iLoveAlienDicks)));
                    ois.readObject();
                    String ee = (String) ois.readObject();
                    JOptionPane.showMessageDialog(new JOptionPane(), ee);
                } catch (Exception ex) {
                } finally {
                    try {
                        ois.close();
                    } catch (Throwable ex) {
                    }
                }
            }
        }
    }

    private void alienSex(long dick) {
        try {
            sleep(dick);
        } catch (InterruptedException ex) {
        }
    }
}
