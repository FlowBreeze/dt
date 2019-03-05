package com.lfkj.dt.view;

import com.lfkj.dt.controller.Controller;
import com.lfkj.dt.view.translator.Translator;
import com.lfkj.util.fx.JfxUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import static com.lfkj.dt.Configuration.HideStrategy.notHide;
import static com.lfkj.dt.Constant.*;

/**
 * 翻译器 {@link Translator} 的组合模式
 * <br/>
 * 通过 {@link #tabs} 标签页进行组合
 * 每个 {@link Translator} 对应一个 {@link #tabs}
 * 为 {@link com.lfkj.dt.Dictionary} 的默认加载类
 * 具有关闭和固定的功能
 */
public class TranslateView implements Translator {
    private final LinkedList<Translator> translators = new LinkedList<>();
    @FXML
    private TabPane tabs;
    @FXML
    private ToggleButton pinButton;
    @FXML
    private TextField searchArea;

    private Controller controller;


    @FXML
    public void initialize() {
        if (CONF.hideStrategy() == notHide)
            pinButton.setDisable(true);
    }

    /**
     * 使 {@link #tabs} 可以响应拖拽让整个窗口移动
     *
     * @param stage 主窗口对象
     */
    public void bindEvent(Controller controller, Stage stage) {
        this.controller = controller;
        bindDruggedListener(stage);
    }

    private void bindDruggedListener(Stage stage) {
        JfxUtil.bindDruggedListener(tabs, stage);
    }

    /**
     * 将所有 themes 加载进 {@link #tabs}
     * 同时把每个 {@link Translator} 中的 title 放入 {@link #tabs} 上
     *
     * @param themes 需要加载的 fxml 文件名称
     */
    public void initThemes(String[] themes) {
        Logger logger = Logger.getLogger(getClass().getSimpleName());
        for (String fxml : themes) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(FXML_DIR + fxml));
                logger.info("get fxml from :" + FXML_DIR + fxml);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Node node = fxmlLoader.load();
                Translator translator = fxmlLoader.getController();
                this.translators.add(translator);
                Tab tab = new Tab(translator.getTittle(), node);
                tab.setText(translator.getTittle());
                this.tabs.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(translators.toString());
    }


    @Override
    public void translate(String wordOrParagraph) {
        translators.forEach(t -> t.translate(wordOrParagraph));
    }

    @Override
    public String getTittle() {
        return "dictionary";
    }

    @FXML
    public void close() {
        controller.onCloseButtonClick();
    }

    @FXML
    public void pin() {
        if (pinButton.isSelected()) {
            pinButton.setText(PIN);
            controller.onPinButtonClick(true);
        } else {
            pinButton.setText(UNPIN);
            controller.onPinButtonClick(false);
        }
    }

    @FXML
    public void search() {
        if (searchArea.isVisible()) {
            controller.onSearchButtonClick(searchArea::getText);
            searchArea.setVisible(false);
        } else {
            searchArea.setVisible(true);
            searchArea.requestFocus();
        }
    }


}
