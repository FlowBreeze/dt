package com.lfkj.dt.controller;

import com.lfkj.dt.Configuration;
import com.lfkj.dt.TranslatableApp;
import com.lfkj.util.Pair;
import com.lfkj.util.system.SquareMouseMovedListener;
import org.jnativehook.NativeHookException;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Supplier;

import static com.lfkj.dt.Configuration.HideStrategy.clickOut;
import static com.lfkj.dt.Configuration.HideStrategy.moveOrClickOut;
import static com.lfkj.util.Context.nullOrThen;
import static com.lfkj.util.cast.LfkjCommonCast.pairOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class EventController implements Controller {

    private Configuration conf;
    private TranslatableApp translatableApp;

    /**
     * 当 {@link Configuration#hideStrategy()} 为 {@link Configuration.HideStrategy#moveOrClickOut} 时存在
     * 需要监听鼠标移出一定范围后隐藏
     * 在翻译界面出现后生效 触发隐藏后移除
     */
    @Nullable
    private SquareMouseMovedListener squareMouseMovedListener;
    /**
     * 当 {@link Configuration#selectionTiggerByMouse()} 为 <code>true</code> 时存在
     * 检查并记录鼠标单击状态,过滤某些不必要的点击
     */
    @Nullable
    private ClickTimeChecker clickTimeChecker;
    /**
     * 保留上次查询的文本；
     * 当和本次查询一致时：鼠标事件不再响应，键盘展示后不再重新翻译
     */
    private String lastSearch;
    /**
     * 当执行隐藏 短时间内响应翻译事件将不再隐藏
     */
    private boolean reViewByClick = false;
    /**
     * 窗口是否被固定，被固定时将不会隐藏
     */
    private boolean isPin = false;

    @Override
    public void init(TranslatableApp app, Configuration conf) {
        this.translatableApp = app;
        this.conf = conf;
        this.clickTimeChecker = (conf.useSelection() && conf.selectionTiggerByMouse()) ? new ClickTimeChecker(conf.drugSelectInterval(), conf.doubleClickInterval()) : null;
        this.squareMouseMovedListener = (conf.hideStrategy() == moveOrClickOut) ? new SquareMouseMovedListener() : null;
        if (nonNull(squareMouseMovedListener)) {
            squareMouseMovedListener.onMovedTouchSquare((event) -> {
                if (!isPin)
                    hideIfMouseOut(event.getX(), event.getY());
                squareMouseMovedListener.setDisable(true);
            });
            translatableApp.addMouseExitListener(event -> {
                if (!isPin)
                    translatableApp.hide();
            });
            try {
                squareMouseMovedListener.addToGlobalScreen();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unInit() {
        nullOrThen(squareMouseMovedListener, SquareMouseMovedListener::removeFromGlobalScreen);
    }

    @Override
    public void onMousePressed(Pair<Integer, Integer> position) {
        if (nonNull(clickTimeChecker)) {
            clickTimeChecker.saveCurrentClick();
        }
    }

    /**
     * 根据 {@link #conf} 构建所有鼠标抬起事件
     * 运用其他内部方法抽象调用过程
     */
    @Override
    public void onMouseReleased(Pair<Integer, Integer> position, Supplier<String> textSupplier) {
        final String thisSearch = textSupplier.get();
        if (!Objects.equals(thisSearch, lastSearch) && nonNull(clickTimeChecker) && clickTimeChecker.check()) {
            reViewByClick = true;
            displayTranslateView(position._1, position._2, thisSearch);
            lastSearch = thisSearch;
            nullOrThen(squareMouseMovedListener, listener -> {
                listener.setNewSquare(position._1 - 50, position._2 - 25, position._1 + 50, position._2 + 25);
                listener.setDisable(false);
            });
        } else if (conf.hideStrategy() == clickOut || conf.hideStrategy() == moveOrClickOut)
            if (!isPin && translatableApp.isShowing()) {
                if (reViewByClick) {
                    reViewByClick = false;
                    return;
                }
                hideIfMouseOut(position._1, position._2);
            }
    }

    @Override
    public void onDisplayKeyPressed(Supplier<String> textSupplier) {
        if (translatableApp.isShowing())
            translatableApp.hide();
        else {
            Point location = MouseInfo.getPointerInfo().getLocation();
            displayTranslateView((int) location.getX(), (int) location.getY(), textSupplier.get());
        }
    }

    @Override
    public void onPin(boolean isPin) {
        this.isPin = isPin;
    }

    @Override
    public void onClose() {
        translatableApp.hide();
    }

    /**
     * 根据配置文件解析显示位置
     * 为 {@link Configuration.ShowPosition#nearMouse} 时，接触到屏幕边缘会反弹
     */
    private void displayTranslateView(int x, int y, String search) {
        if (isPin)
            translatableApp.displayTranslateView(null, null, search);
        else
            switch (conf.showPosition()) {
                case fixed:
                    translatableApp.displayTranslateView(pairOf(conf.offsetX(), conf.offsetY()), null, search);
                    return;
                case nearMouse:
                    translatableApp.displayTranslateView(pairOf(x, y), pairOf(conf.offsetX(), conf.offsetY()), search);
                    return;
                default:
            }
    }

    /**
     * 当前点(x,y)不在翻译界面时，隐藏界面，否则什么都不做
     */
    private void hideIfMouseOut(int x, int y) {
        if (translatableApp.getDisplayRect().isOutside(x, y)) {
            translatableApp.hide();
        }
    }

    private class ClickTimeChecker {
        private int drugSelectInterval;
        private int doubleClickInterval;
        private LocalTime lastPressed;
        private LocalTime thisPressed;

        private ClickTimeChecker(int drugSelectInterval, int doubleClickInterval) {
            this.drugSelectInterval = drugSelectInterval;
            this.doubleClickInterval = doubleClickInterval;
        }

        private void saveCurrentClick() {
            lastPressed = thisPressed;
            thisPressed = LocalTime.now();
        }

        private boolean check() {
            final LocalTime thisReleased = LocalTime.now();
            return Duration.between(thisPressed, thisReleased).toMillis() > drugSelectInterval
                    || isNull(lastPressed)
                    || Duration.between(lastPressed, thisReleased).toMillis() < doubleClickInterval;
        }
    }
}
