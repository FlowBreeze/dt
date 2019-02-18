package com.lfkj.util.system.test;

import com.lfkj.util.system.SystemSelection;
import org.junit.Test;

public class SystemSelectionListenerTest {

    @Test
    @Deprecated
    public void testSelectionListener() throws InterruptedException {
        SystemSelection systemSelectionListener = new SystemSelection();
        systemSelectionListener.addSelectionListener(s -> {
        });
        System.out.println(systemSelectionListener.getStringFlavor());
        Thread.sleep(100000000);
    }
}