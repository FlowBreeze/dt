package com.lfkj.util.system;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import java.util.function.Consumer;

import static java.util.Objects.nonNull;

/**
 * 外观模式，封装了 {@link NativeMouseListener} 的调用和注册
 * 使使用 lambda 表达式变为可能
 */
public class MouseListenerFacade implements NativeMouseListener, JNativeHookFacade {
    private Consumer<NativeMouseEvent> pressed;
    private Consumer<NativeMouseEvent> released;

    public MouseListenerFacade(Consumer<NativeMouseEvent> onPressed, Consumer<NativeMouseEvent> onReleased) {
        this.pressed = onPressed;
        this.released = onReleased;
    }

    public MouseListenerFacade() {
    }

    public void onPressed(Consumer<NativeMouseEvent> onPressed) {
        this.pressed = onPressed;
    }

    public void onReleased(Consumer<NativeMouseEvent> onReleased) {
        this.released = onReleased;
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        if (nonNull(pressed))
            pressed.accept(nativeEvent);
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        if (nonNull(released))
            released.accept(nativeEvent);
    }

    @Override
    public void addToGlobalScreen() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeMouseListener(this);
    }

    @Override
    public void removeFromGlobalScreen() {
        GlobalScreen.removeNativeMouseListener(this);
    }
}
