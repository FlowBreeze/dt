package com.lfkj.dt.translator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import static com.lfkj.dt.Constant.FXML_DIR;

public class TranslatorComposite implements Translator {
    private final LinkedList<Translator> translators = new LinkedList<>();
    @FXML
    private TabPane root;

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
                root.getTabs().add(tab);
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
