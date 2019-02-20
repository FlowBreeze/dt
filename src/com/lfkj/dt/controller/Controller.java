package com.lfkj.dt.controller;

import com.lfkj.dt.Configuration;
import com.lfkj.dt.TranslatableApp;
import com.lfkj.util.Pair;
import javafx.stage.Stage;

import java.util.function.Supplier;

/**
 * 用于处理所有用户可能的操作
 * 通过 {@link com.lfkj.dt.Dictionary} 类加载
 *
 * @see com.lfkj.dt.Dictionary#start(Stage)
 */
public interface Controller {

    void init(TranslatableApp app, Configuration conf);

    /**
     * 逆初始化，用于处理事件残留
     */
    void unInit();

    /**
     * 处理鼠标按下事件、此处代码不宜过多
     * 因为当双击时，鼠标抬起事件会优先于按下事件响应
     *
     * @param position 鼠标按下的位置
     */
    void onMousePressed(Pair<Integer, Integer> position);

    /**
     * 处理鼠标抬起事件，参数中包含获取搜索文本的函数
     *
     * @param position     鼠标抬起的位置
     * @param textSupplier 获取需要搜索文本的方法
     */
    void onMouseReleased(Pair<Integer, Integer> position, Supplier<String> textSupplier);

    /**
     * 当展示界面的快捷键按下时触发
     *
     * @param textSupplier 获取需要搜索文本的方法
     */
    void onDisplayKeyPressed(Supplier<String> textSupplier);

    /**
     * 当界面被固定、解除固定模式时触发
     *
     * @param isPin 当前界面是否固定
     */
    void onPin(boolean isPin);

    /**
     * 当界面关闭时触发
     */
    void onClose();
}
