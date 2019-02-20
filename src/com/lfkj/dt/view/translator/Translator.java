package com.lfkj.dt.view.translator;

/**
 * 由 {@link com.lfkj.dt.Dictionary} 持有；由 {@link com.lfkj.dt.view.translator} 包进行实现
 * 负责与 FXML 进行交互，提供 {@link #translate(String)}方法调用
 */
public interface Translator {

    void translate(String wordOrParagraph);

    /**
     * 获取应用标题名称
     */
    String getTittle();

}
