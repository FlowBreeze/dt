package com.lfkj.dt;

import com.lfkj.dt.controller.Controller;
import com.lfkj.util.Pair;
import com.lfkj.util.system.*;
import org.jnativehook.NativeHookException;

import java.util.HashSet;
import java.util.function.Supplier;

import static com.lfkj.util.cast.LfkjCommonCast.pairOf;

/**
 * 通过加载 {@link Configuration} 和 {@link Controller} 两个对象
 * 将 {@link Controller} 与底层鼠标、键盘监听进行对接
 */
class NativeEventBuilder {

    private final Configuration conf;
    private final MouseListenerFacade mouseListener;
    private final Controller controller;
    private final HashSet<KeyListenerFacade> keyListenerFacadeSet = new HashSet<>();

    NativeEventBuilder(Configuration conf, Controller controller) {
        this.conf = conf;
        this.controller = controller;
        this.mouseListener = new MouseListenerFacade();
    }

    /**
     * 通过 #conf 查询需要初始化的底层监听
     * 按需构建鼠标监听、键盘监听
     */
    void addEvent() throws NativeHookException {
        SystemSelection systemSelection = new SystemSelection();
        initMouseListener(systemSelection::getStringFlavor);
        if (conf.useSelection() && conf.selectionTiggerByKey()) {
            addDisplayKeyListener(systemSelection::getStringFlavor, conf.selectionTiggerKey());
        }
        if (conf.useClipBoard()) {
            addDisplayKeyListener(new SystemClipboard()::getStringFlavor, conf.selectionTiggerKey());
        }
    }

    /**
     * 删除所有监听
     */
    void removeAll() {
        keyListenerFacadeSet.forEach(JNativeHookFacade::removeFromGlobalScreen);
        mouseListener.removeFromGlobalScreen();
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
