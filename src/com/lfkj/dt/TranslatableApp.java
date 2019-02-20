package com.lfkj.dt;

import com.lfkj.util.Pair;
import com.lfkj.util.fx.Shape;
import javafx.scene.input.MouseEvent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * 将应用界面与事件处理层解耦
 * 抽象出事件处理需要的方法，由应用类实现并提供给事件处理层
 *
 * @see Dictionary 应用展示类
 * @see com.lfkj.dt.controller.Controller 事件处理类
 */
public interface TranslatableApp {
    /**
     * 默认从指定 leftTop 的位置(加偏移量)向右下进行扩展展示。
     * 当超出屏幕时，从指定位置(减偏移量)向上方/左方进行展示
     *
     * @param leftTop 展示位置（左上角） 为空时为上一次隐藏的位置
     * @param search  为空时不进行搜索
     * @param offset  展示位置的偏移量,当leftTop不为空时才有效
     */
    void displayTranslateView(@Nullable Pair<Integer, Integer> leftTop, @Nullable Pair<Integer, Integer> offset, @Nullable String search);

    /**
     * 隐藏所展示的界面
     * 当界面未被展示不会隐藏、也不会抛出异常
     */
    void hide();

    /**
     * 返回一个矩形描述当前窗口位置，可以通过矩形判断屏幕上的一个点是否在窗口内
     *
     * @return 描述位置信息的矩形
     */
    Shape.Rectangle getDisplayRect();

    /**
     * 创建一个当鼠标离开当前窗口会触发的监听(非一次性)
     *
     * @param listener 事件回调监听器
     */
    void addMouseExitListener(Consumer<MouseEvent> listener);

    /**
     * 返回当前界面是否未被隐藏
     */
    boolean isShowing();
}
