package com.lfkj.util.system.test;

import com.lfkj.util.system.MouseListenerFacade;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.Test;

import java.util.logging.LogManager;

public class MouseListenerFacadeTest {

    @Test
    public void test() throws InterruptedException, NativeHookException {
        LogManager.getLogManager().reset();
        MouseListenerFacade listener = new MouseListenerFacade();
        listener.onPressed(this::count);
        listener.addToGlobalScreen();
        Thread.sleep(112233);
    }

    private int i = 0;

    private void count(NativeMouseEvent nativeMouseEvent) {
        System.out.println("click" + ++i + "time");
    }

}
