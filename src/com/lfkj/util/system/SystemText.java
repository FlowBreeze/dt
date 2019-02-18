package com.lfkj.util.system;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * 对 {@link javafx.scene.input.Clipboard} 进行封装后的对象，
 * 其内部是 操作系统剪切板对象 （通过 <code>new {@link SystemClipboard}</code> 创建）
 * 或者是 操作系统用户框选的文本监控对象 （通过 <code>new {@link SystemSelection}</code> 创建）
 * <br/>
 * {@link #addSelectionListener(Consumer)}
 * 可以对其添加监听器 当事件发生后将进行回调（有轻微延迟）
 * <br/>
 * {@link #getStringFlavor()}
 * 可以获取上一次用户剪切、选中的文本
 */
public abstract class SystemText {

    Clipboard clipboard;

    /**
     * 此方法响应速度较慢，且精度差，不建议使用
     *
     * @param callBack 要执行的回调方法 <{@link String}>:事件响应后返回的文本
     */
    @Deprecated
    public void addSelectionListener(Consumer<String> callBack) {
        clipboard.addFlavorListener(e -> {
            String data = null;
            try {
                data = (String) clipboard.getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e1) {
                e1.printStackTrace();
            }
            callBack.accept(data);
        });
    }

    public String getStringFlavor() {
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            return null;
        }
    }

}
