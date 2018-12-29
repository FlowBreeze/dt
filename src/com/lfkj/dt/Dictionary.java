package com.lfkj.dt;

import com.lfkj.dt.translator.TranslatorComposite;
import com.lfkj.util.system.*;
import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;

import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.lfkj.dt.Constant.CONF;
import static com.lfkj.dt.Constant.FXML_DIR;
import static java.util.Objects.nonNull;

public class Dictionary extends Application {
    private Stage stage;
    private TranslatorComposite translator;

    @Override
    public void start(Stage primaryStage) throws Exception {

        CONF.save();

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setX(Double.MAX_VALUE); // to move it out of sight
        primaryStage.setY(Double.MAX_VALUE);
        primaryStage.show();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FXML_DIR + "TranslatorComposite.fxml"));
        Parent parent = fxmlLoader.load();
        this.stage = new Stage();
        translator = fxmlLoader.getController();
        translator.initThemes(CONF.tabs());
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(primaryStage);
        stage.setAlwaysOnTop(true);

        if (CONF.useSelection()) {
            SystemSelectionListener selectionListener = new SystemSelectionListener();
            if (CONF.selectionTiggerByMouse()) {
                addMouseListener(selectionListener);
            }
            if (CONF.selectionTiggerByKey()) {
                addKeyListener(selectionListener, CONF.selectionTiggerKey());
            }
        }
        if (CONF.useClipBoard()) {
            SystemClipboardListener systemClipboardListener = new SystemClipboardListener();
            addKeyListener(systemClipboardListener, CONF.clipboardTiggerKey());
        }
        parent.addEventHandler(MouseEvent.MOUSE_EXITED, (event -> stage.hide()));
    }

    private void addKeyListener(@Nullable SystemTextListener textListener, Keys... keys) throws NativeHookException {
        KeyListenerFacade keyListener = new KeyListenerFacade(keys);
        keyListener.onAllKeyPressed(() -> {
            Platform.setImplicitExit(false);
            Platform.runLater(stage::show);
            if (nonNull(textListener)) {
                String thisSearch = textListener.getStringFlavor();
                if (!thisSearch.equals(lastSearch))
                    translator.translate(thisSearch);
            }
        });
        keyListener.addToGlobalScreen();
    }

    private static String lastSearch;
    private static long lastPressed;
    private static long thisPressed;

    private void addMouseListener(SystemTextListener textListener) throws NativeHookException {
        MouseListenerFacade mouseListener = new MouseListenerFacade();

        Consumer<NativeMouseEvent> saveTime = nativeMouseEvent -> {
            lastPressed = thisPressed;
            thisPressed = new Date().getTime();
        };
        if (CONF.hideWhenMouseClickOutside()) {
            Consumer<NativeMouseEvent> saveTimeAndHideStage = nativeMouseEvent -> {
                int x = nativeMouseEvent.getX(), y = nativeMouseEvent.getY();
                double stageX = stage.getX(), stageY = stage.getY();
                if (x < stageX || x > stageX + stage.getHeight())
                    if (y < stageY || y > stageY + stage.getWidth())
                        Platform.runLater(() -> stage.hide());
                {
                    saveTime.accept(nativeMouseEvent);
                }
            };
            mouseListener.onPressed(saveTimeAndHideStage);
        } else {
            mouseListener.onPressed(saveTime);
        }

        Consumer<NativeMouseEvent> translate = (nativeMouseEvent) -> {
            Platform.setImplicitExit(false);
            Platform.runLater(() -> flash(nativeMouseEvent.getX(), nativeMouseEvent.getY()));
            translator.translate(lastSearch);
        };

        Consumer<NativeMouseEvent> addFilterAndTranslate = nativeMouseEvent -> {
            final long thisReleased = new Date().getTime();
            Logger.getLogger(Dictionary.class.getSimpleName()).info("mouse pressed duration:" + (thisReleased - thisPressed));
            Logger.getLogger(Dictionary.class.getSimpleName()).info("double click duration:" + (thisReleased - lastPressed));
            final boolean clickIntervalOK = thisReleased - thisPressed > CONF.drugSelectInterval()
                    || thisReleased - lastPressed < CONF.doubleClickInterval();
            final String thisSearch = textListener.getStringFlavor();
            final boolean notRepeat = !thisSearch.equals(lastSearch);
            if (clickIntervalOK && notRepeat) {
                lastSearch = thisSearch;
                {
                    translate.accept(nativeMouseEvent);
                }
            }
        };
        mouseListener.onReleased(addFilterAndTranslate);

        mouseListener.addToGlobalScreen();
    }

    private void flash(int x, int y) {
        switch (CONF.showPosition()) {
            case fixed:
                stage.setX(CONF.offsetX());
                stage.setY(CONF.offsetY());
                break;
            case nearMouse:
                stage.setX(x + CONF.offsetX());
                stage.setY(y + CONF.offsetY());
                break;
        }
        stage.show();
    }

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        launch(args);
    }

}
