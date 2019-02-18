package com.lfkj.dt.translator;

import com.lfkj.util.fx.JfxUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import static com.lfkj.dt.Constant.FXML_DIR;

/**
 * 翻译器 {@link Translator} 的组合模式
 * <br/>
 * 通过 {@link #tab} 标签页进行组合
 * 每个 {@link Translator} 对应一个 {@link #tab}
 */
public class TranslatorComposite implements Translator {
    private final LinkedList<Translator> translators = new LinkedList<>();
    @FXML
    private TabPane tab;

    /**
     * 使 {@link #tab} 可以响应拖拽让整个窗口移动
     *
     * @param stage 主窗口对象
     */
    public void bindDruggedListener(Stage stage) {
        JfxUtil.bindDruggedListener(tab, stage);
    }


    /**
     * 将所有 themes 加载进 {@link #tab}
     * 同时把每个 {@link Translator} 中的 title 放入 {@link #tab} 上
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
                this.tab.getTabs().add(tab);
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
}
