package com.lfkj.util.system.test;

import com.lfkj.util.system.KeyListenerFacade;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.LogManager;

import static com.lfkj.util.system.Keys.*;

public class KeyListenerFacadeTest {
    @Before
    public void register() throws NativeHookException {
        GlobalScreen.registerNativeHook();
    }

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
