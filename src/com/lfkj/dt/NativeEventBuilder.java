package com.lfkj.dt;

import com.lfkj.dt.controller.Controller;
import com.lfkj.util.Pair;
import com.lfkj.util.system.*;
import org.jnativehook.NativeHookException;

import java.util.HashSet;
import java.util.function.Supplier;

import static com.lfkj.util.cast.LfkjCommonCast.pairOf;

/**
 * 与底层 {{@link org.jnativehook}} 交互，负责监听事件的注册与销毁
 * 将事件的处理交给 @{@link Controller}
 * 需要通过 {@link Configuration} 和 {@link Controller} 两个对象进行构建
 */
class NativeEventBuilder {

    private final Configuration conf;
    private final MouseListenerFacade mouseListener;
    private final HashSet<KeyListenerFacade> keyListenerFacadeSet = new HashSet<>();
    /**
     * 用于控制其他监听是否启动的源监听器
     */
    private final KeyListenerFacade metaKeyListener;
    private final Controller controller;
    private boolean enable = true;

    NativeEventBuilder(Configuration conf, Controller controller) throws NativeHookException {
        this.conf = conf;
        this.controller = controller;
        this.mouseListener = new MouseListenerFacade();
        this.metaKeyListener = new KeyListenerFacade(conf.enableKey());
        metaKeyListener.onAllKeyPressed(() -> controller.onEnableKeyPressed(enable = !enable)); // enable 取反
        metaKeyListener.addToGlobalScreen();
    }

    /**
     * 通过 {@link #conf} 查询需要初始化的底层监听
     * 按需构建鼠标监听、键盘监听
     */
    void addEvent() {
        SystemSelection systemSelection = new SystemSelection();
        try {
            initMouseListener(() -> systemSelection.getStringFlavor().orElse(null));
            if (conf.useSelection() && conf.selectionTiggerByKey()) {
                addDisplayKeyListener(() -> systemSelection.getStringFlavor().orElse(null), conf.selectionTiggerKey());
            }
            if (conf.useClipBoard()) {
                addDisplayKeyListener(() -> new SystemClipboard().getStringFlavor().orElse(null), conf.selectionTiggerKey());
            }
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有监听
     *
     * @param removeMetaEvent 是否删除源监听 {@link #metaKeyListener} 删除后将不能通过键盘监听启用其他监听
     */
    void removeEvent(boolean removeMetaEvent) {
        keyListenerFacadeSet.forEach(JNativeHookFacade::removeFromGlobalScreen);
        mouseListener.removeFromGlobalScreen();
        if (removeMetaEvent)
            metaKeyListener.removeFromGlobalScreen();
    }

    /**
     * 初始化鼠标监听并绑定到 {@link Controller}
     * 的 {@link Controller#onMousePressed(Pair)}, {@link Controller#onMouseReleased(Pair, Supplier)} 方法
     *
     * @param textSupplier 获取的翻译文本的函数
     */
    private void initMouseListener(Supplier<String> textSupplier) throws NativeHookException {
        mouseListener.onPressed(event -> controller.onMousePressed(pairOf(event.getX(), event.getY())));
        mouseListener.onReleased(event -> controller.onMouseReleased(pairOf(event.getX(), event.getY()), textSupplier));
        mouseListener.addToGlobalScreen();
    }


    /**
     * 添加按键监听
     * 当按键全按下时调用 {@link Controller#onDisplayKeyPressed(Supplier)}
     *
     * @param textSupplier 获取的翻译文本的函数
     * @param keys         触发操作所需要的所有按键（全部按下时触发）
     */
    private void addDisplayKeyListener(Supplier<String> textSupplier, Keys... keys) throws NativeHookException {
        KeyListenerFacade keyListener = new KeyListenerFacade(keys);
        keyListenerFacadeSet.add(keyListener);
        keyListener.onAllKeyPressed(() -> controller.onDisplayKeyPressed(textSupplier));
        keyListener.addToGlobalScreen();
    }


}
