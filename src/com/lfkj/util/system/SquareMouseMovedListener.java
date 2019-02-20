package com.lfkj.util.system;

import com.lfkj.util.fx.Shape;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

/**
 * 封装了 {@link NativeMouseMotionListener} 的调用和注册
 * 使使用 lambda 表达式变为可能
 * 在屏幕中指定一个矩形范围，当鼠标移入、移出这个矩形边界时触发
 */
@NotThreadSafe
public class SquareMouseMovedListener implements NativeMouseMotionListener, JNativeHookFacade {

    private Shape.Rectangle rectangle;
    private boolean disable = true;
    /**
     * 是否已经触发过一次 {@link #nativeMouseMoved(NativeMouseEvent)} 鼠标移动方法
     * 用以判断目前鼠标位置
     */
    private boolean isMoved = false;
    private boolean lastOutSquare;
    private Consumer<NativeMouseEvent> moved;

    /**
     * 一套龙式构建
     */
    public SquareMouseMovedListener(int startX, int startY, int endX, int endY, Consumer<NativeMouseEvent> moved) {
        rectangle = Shape.rectangle(startX, startY, endX, endY);
        this.moved = moved;
    }

    /**
     * 使用这个方法需要手动调用 {@link #setDisable(boolean)},并传入 <code>true</code>
     */
    public SquareMouseMovedListener() {
    }

    public void setNewSquare(int startX, int startY, int endX, int endY) {
        this.rectangle = Shape.rectangle(startX, startY, endX, endY);
    }


    public void onMovedTouchSquare(Consumer<NativeMouseEvent> moved) {
        this.moved = moved;
    }

    private void reset() {
        this.isMoved = false;
    }


    public void setDisable(boolean disable) {
        this.disable = disable;
        if (!disable)
            reset();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        if (disable)
            return;
        // 进行位置判断,改写 lastOutSquare
        boolean thisOutSquare = rectangle.isOutside(nativeEvent.getX(), nativeEvent.getY());
        if (!isMoved) {
            isMoved = true;
        } else if (lastOutSquare != thisOutSquare) {
            if (nonNull(moved))
                moved.accept(nativeEvent);
        }
        this.lastOutSquare = thisOutSquare;

    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
    }

    @Override
    public void addToGlobalScreen() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeMouseMotionListener(this);
    }

    @Override
    public void removeFromGlobalScreen() {
        GlobalScreen.removeNativeMouseMotionListener(this);
    }
}
