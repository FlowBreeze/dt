package com.lfkj.dt;

import com.lfkj.util.system.MouseListenerFacade;
import com.lfkj.util.system.SystemSelectionListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;

import java.net.URL;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Dictionary extends Application {
    private Stage stage;
    private Translator translator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        addMouseListener();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL resource = getClass().getResource("ICiBaTranslator.fxml");
        fxmlLoader.setLocation(resource);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent parent = fxmlLoader.load();
        stage = new Stage();
        translator = fxmlLoader.getController();
        stage.setTitle("dictionary");
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.initOwner(primaryStage);
        parent.addEventHandler(MouseEvent.MOUSE_EXITED, (event -> stage.hide()));
    }

    private static long lastPressed;
    private static long thisPressed;
    private static String lastSearch;

    private void addMouseListener() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        MouseListenerFacade mouseListener = new MouseListenerFacade();
        GlobalScreen.addNativeMouseListener(mouseListener);

        Consumer<NativeMouseEvent> saveTime = nativeMouseEvent -> {
            lastPressed = thisPressed;
            thisPressed = new Date().getTime();
        };
        Consumer<NativeMouseEvent> saveTimeAndHideStage = nativeMouseEvent -> {
            Platform.runLater(() -> stage.hide());
            saveTime.accept(nativeMouseEvent);
        };
        mouseListener.onPressed(saveTimeAndHideStage);

        Consumer<NativeMouseEvent> translate = (nativeMouseEvent) -> {
            Platform.setImplicitExit(false);
            Platform.runLater(() -> flash(nativeMouseEvent.getX(), nativeMouseEvent.getY()));
            translator.translate(lastSearch);
        };
        Consumer<NativeMouseEvent> addFilterAndTranslate = nativeMouseEvent -> {
            long thisReleased = new Date().getTime();
            Logger.getLogger(Dictionary.class.getSimpleName()).info("mouse pressed duration:" + (thisReleased - thisPressed));
            Logger.getLogger(Dictionary.class.getSimpleName()).info("double click duration:" + (thisReleased - lastPressed));
            boolean willHandle = thisReleased - thisPressed > 500 || thisReleased - lastPressed < 800;
            String thisSearch = SystemSelectionListener.getStringFlavor();
            if (willHandle && !thisSearch.equals(lastSearch)) {
                lastSearch = thisSearch;
                translate.accept(nativeMouseEvent);
            }
        };
        mouseListener.onReleased(addFilterAndTranslate);
    }


    private void flash(int x, int y) {
        stage.setX(x);
        stage.setY(y + 45);
        stage.show();
    }

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        launch(args);
    }

}
