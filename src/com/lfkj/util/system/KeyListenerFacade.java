package com.lfkj.util.system;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public class KeyListenerFacade implements NativeKeyListener {

    private Runnable allKeyPressed;
    private Runnable oneOfTheKeyReleasedAfterPressed;
    private Keys[] keys;
    private Set<Integer> pressedKeyCodes = new HashSet<>();
    private boolean isTiggerd;

    public KeyListenerFacade(Runnable allKeyPressed, Runnable oneOfTheKeyReleasedAfterPressed, Keys... keys) {
        this.allKeyPressed = allKeyPressed;
        this.oneOfTheKeyReleasedAfterPressed = oneOfTheKeyReleasedAfterPressed;
        this.keys = keys;
    }

    public KeyListenerFacade(Keys... keys) {
        this.keys = keys;
    }

    public void onAllKeyPressed(Runnable allKeyPressed) {
        this.allKeyPressed = allKeyPressed;
    }

    public void onOneOfTheKeyReleasedAfterPressed(Runnable oneOfTheKeyReleasedAfterPressed) {
        this.oneOfTheKeyReleasedAfterPressed = oneOfTheKeyReleasedAfterPressed;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        pressedKeyCodes.add(nativeEvent.getKeyCode());
        Logger logger = Logger.getLogger(KeyListenerFacade.class.getSimpleName());
        logger.info(NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
        if (!isTiggerd && Stream.of(keys).map(Keys::getKeyCode).allMatch(pressedKeyCodes::contains)) {
            isTiggerd = true;
            if (nonNull(allKeyPressed))
                allKeyPressed.run();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        pressedKeyCodes.remove(nativeEvent.getKeyCode());
        if (isTiggerd && Stream.of(keys).map(Keys::getKeyCode).anyMatch(pressedKeyCodes::contains)) {
            isTiggerd = false;
            if (nonNull(oneOfTheKeyReleasedAfterPressed)) {
                oneOfTheKeyReleasedAfterPressed.run();
            }
        }
    }

    public void addToGlobalScreen() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this);
    }
}
