package com.lfkj.util.system;

import org.jnativehook.NativeHookException;

public interface JNativeHookFacade {

    void addToGlobalScreen() throws NativeHookException;

    void removeFromGlobalScreen();
}
