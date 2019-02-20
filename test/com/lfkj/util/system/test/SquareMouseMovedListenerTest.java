package com.lfkj.util.system.test;

import com.lfkj.util.system.SquareMouseMovedListener;
import org.jnativehook.NativeHookException;
import org.junit.Test;

import java.util.logging.LogManager;

public class SquareMouseMovedListenerTest {

    @Test
    public void test() throws InterruptedException, NativeHookException {
        LogManager.getLogManager().reset();
        SquareMouseMovedListener listener = new SquareMouseMovedListener();
        listener.setNewSquare(0, 0, 50, 50);
        listener.onMovedTouchSquare(e -> System.out.println("touched"));
        listener.setDisable(false);
        listener.addToGlobalScreen();
        Thread.sleep(112233);
    }
}
