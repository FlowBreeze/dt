package com.lfkj.dt;

import com.lfkj.dt.controller.EventController;
import com.lfkj.dt.view.TranslateView;
import com.lfkj.dt.view.translator.Translator;
import com.lfkj.util.Pair;
import com.lfkj.util.fx.Shape;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.logging.LogManager;

import static com.lfkj.dt.Constant.CONF;
import static com.lfkj.dt.Constant.FXML_DIR;
import static com.lfkj.util.Context.nullOrThen;
import static com.lfkj.util.cast.LfkjCommonCast.pairOf;
import static java.util.Objects.nonNull;

/**
 * 应用主类
 * 负责使用加载程序的展示界面、
 * 与配置文件对象 {@link Constant#CONF} 交互、
 * 加载 {@link com.lfkj.dt.controller.Controller}
 * 并将其嵌入到对应的事件源进行事件处理
 *
 * @see Configuration
 * @see TranslatableApp
 * @see com.lfkj.dt.controller.Controller
 * @see #start(Stage) 主要方法
 */
public class Dictionary extends Application implements TranslatableApp {
    private Stage stage;
    /**
     * 组合翻译的标签页面，通过配置文件加载所需要使用的 {@link Translator}
     */
    private TranslateView translateView;
    private NativeEventBuilder nativeEventBuilder;
    private EventController controller;

    /**
     * 应用的入口、调用 {@link #start(Stage)} 方法
     */
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Dictionary.launch(args);
    }

    /**
     * 负责初始化界面 加载 {@link TranslateView}；
     * 加载 {@link com.lfkj.dt.controller.Controller} 对象
     * 并将其绑定给 {@link TranslateView} 和 {@link NativeEventBuilder}
     * 以实现对事件的响应
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        CONF.save();
// TODO: add tray
//        SystemTray tray = SystemTray.getSystemTray();
//        BufferedImage image = ImageIO.read(this.getClass()
//                .getResourceAsStream("/qrcode_develong_1.jpg"));
//        TrayIcon trayIcon = new TrayIcon(image, "自动备份工具");
//        trayIcon.setToolTip("自动备份工具");
//        tray.add(trayIcon);

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setX(Double.MAX_VALUE); // to move it out of sight
        primaryStage.setY(Double.MAX_VALUE);
        primaryStage.show();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FXML_DIR + "TranslateView.fxml"));
        Parent parent = fxmlLoader.load();


        this.stage = new Stage();
        translateView = fxmlLoader.getController();
        translateView.initThemes(CONF.tabs());
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(primaryStage);
        stage.setAlwaysOnTop(true);
        controller = new EventController();
        controller.init(this, CONF);
        translateView.bindEvent(controller, stage);

        nativeEventBuilder = new NativeEventBuilder(CONF, controller);
        nativeEventBuilder.addEvent();

    }

    @Override
    public void stop() {
        nativeEventBuilder.removeEvent(true);
        controller.unInit();
    }

    @Override
    public void displayTranslateView(@Nullable Pair<Integer, Integer> leftTop, @Nullable Pair<Integer, Integer> offset, @Nullable String search) {
        Platform.runLater(() -> {
            if (nonNull(leftTop)) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double width = stage.getWidth(), height = stage.getHeight();
                int offsetX = (nonNull(offset)) ? offset._1 : 0, offsetY = (nonNull(offset)) ? offset._2 : 0;
                int cushionLength = 30;
                if (width + leftTop._1 + cushionLength < screenBounds.getMaxX()) {
                    stage.setX(leftTop._1 + offsetX);
                } else
                    stage.setX(leftTop._1 - offsetX - width);
                if (height + leftTop._2 + cushionLength < screenBounds.getMaxY()) {
                    stage.setY(leftTop._2 + offsetY);
                } else
                    stage.setY(leftTop._2 - offsetY - height);
            }
            stage.show();
        });
        nullOrThen(search, translateView::translate);
    }

    @Override
    public void hide() {
        Platform.runLater(() -> stage.hide());
    }

    @Override
    public Shape.Rectangle getDisplayRect() {
        return Shape.rectangle(pairOf(stage.getX(), stage.getY()), stage.getWidth(), stage.getHeight());
    }

    @Override
    public void addMouseExitListener(Consumer<MouseEvent> listener) {
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, listener::accept);
    }

    @Override
    public boolean isShowing() {
        return stage.isShowing();
    }

    @Override
    public void enableEventListener(boolean isEnable) {
        if (isEnable)
            nativeEventBuilder.addEvent();
        else
            nativeEventBuilder.removeEvent(false);
    }

}
