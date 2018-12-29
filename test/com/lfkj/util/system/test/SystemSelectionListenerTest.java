package com.lfkj.util.system.test;

import com.lfkj.util.system.SystemSelectionListener;
import org.junit.Test;

public class SystemSelectionListenerTest {

    @Test
    public void testSelectionListener() throws InterruptedException {
        SystemSelectionListener systemSelectionListener = new SystemSelectionListener();
        systemSelectionListener.addSelectionListener(s -> {
        });
        System.out.println(systemSelectionListener.getStringFlavor());
        Thread.sleep(100000000);
    }
}