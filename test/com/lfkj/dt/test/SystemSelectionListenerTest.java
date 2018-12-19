package com.lfkj.dt.test;

import com.lfkj.util.system.SystemSelectionListener;
import org.junit.Test;

public class SystemSelectionListenerTest {
    public SystemSelectionListenerTest() {
    }

    @Test
    public void testSelectionListener() throws InterruptedException {
        new Thread(() -> {
            while (true) {
            }
        }).start();
        SystemSelectionListener.addSelectionListener(s -> {
        });
        System.out.println(SystemSelectionListener.getStringFlavor());
        Thread.sleep(100000000);
    }
}