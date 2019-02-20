package com.lfkj.util.system.test;

import com.lfkj.util.system.KeyListenerFacade;
import org.jnativehook.NativeHookException;
import org.junit.Test;

import java.util.logging.LogManager;

import static com.lfkj.util.system.Keys.*;

public class KeyListenerFacadeTest {

    @Test
    public void test() throws InterruptedException, NativeHookException {
        LogManager.getLogManager().reset();
        KeyListenerFacade listener = new KeyListenerFacade(ctrl, shift, alt);
        listener.onAllKeyPressed(() -> System.out.println("key pressed"));
        listener.onOneOfTheKeyReleasedAfterPressed(() -> System.out.println("key released"));
        listener.addToGlobalScreen();
        Thread.sleep(112233);
    }
}
