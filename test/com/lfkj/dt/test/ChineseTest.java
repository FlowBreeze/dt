package com.lfkj.dt.test;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class ChineseTest extends Application {

    @FXML
    Button button;
    @FXML
    AnchorPane anchorPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ChineseTest.fxml"));
        fxmlLoader.load();
        ChineseTest chineseTest = fxmlLoader.getController();
        String text = "中文";
        chineseTest.button.setText(text);
        assertArrayEquals(text.getBytes(), chineseTest.button.getText().getBytes());
        primaryStage.show();
        primaryStage.close();
    }

    @Test
    public void testChinese() {
        launch();
    }

}
