package com.lfkj.dt;

import com.lfkj.dt.translator.TranslatorComposite;
import com.lfkj.util.Pair;
import com.lfkj.util.fx.Shape;
import com.lfkj.util.system.*;
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
import org.jnativehook.NativeHookException;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.LogManager;

import static com.lfkj.dt.Configuration.HideStrategy.clickOut;
import static com.lfkj.dt.Configuration.HideStrategy.moveOrClickOut;
import static com.lfkj.dt.Constant.CONF;
import static com.lfkj.dt.Constant.FXML_DIR;
import static com.lfkj.util.cast.LfkjCommonCast.pairOf;
import static java.util.Objects.nonNull;

/**
 * 应用的入口、调用 {@link #start(Stage)} 方法
 * 与配置文件对象 {@link Constant#CONF} 交互，通过配置文件的参数更改程序表现形式；
 *
 * @see #start(Stage)
 * @see Configuration
 */
public class Dictionary extends Application {
    private Stage stage;
    /**
     * 组合翻译的标签页面，通过配置文件加载所需要使用的 {@link com.lfkj.dt.translator.Translator}
     */
    private TranslatorComposite translator;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Dictionary.launch(args);
    }

    /**
     * 负责初始化界面；
     * 加载翻译组合器 {@link TranslatorComposite}
     * 注册鼠标、键盘监听，加载配置；
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        CONF.save();
        SystemTray tray = SystemTray.getSystemTray();
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
        fxmlLoader.setLocation(getClass().getResource(FXML_DIR + "TranslatorComposite.fxml"));
        Parent parent = fxmlLoader.load();

        this.stage = new Stage();
        translator = fxmlLoader.getController();
        translator.initThemes(CONF.tabs());
        translator.bindDruggedListener(stage);
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(primaryStage);
        stage.setAlwaysOnTop(true);

        SystemSelection systemSelection = new SystemSelection();
        new MouseCallBackHandler(CONF).initMouseListener(systemSelection::getStringFlavor, this::displayTranslateView);
        if (CONF.useSelection() && CONF.selectionTiggerByKey()) {
            addKeyListener(systemSelection, CONF.selectionTiggerKey());
        }
        if (CONF.useClipBoard()) {
            addKeyListener(new SystemClipboard(), CONF.clipboardTiggerKey());
        }

    }

    /**
     * 添加 {@link Dictionary} 对键盘事件的监听
     *
     * @param systemText 需要进行操作的文本源
     * @param keys       触发操作所需要的所有按键（全部按下时触发）
     */
    private void addKeyListener(SystemText systemText, Keys... keys) throws NativeHookException {
        KeyListenerFacade keyListener = new KeyListenerFacade(keys);
        keyListener.onAllKeyPressed(() -> {
            if (stage.isShowing())
                hide();
            else {
                Point location = MouseInfo.getPointerInfo().getLocation();
                displayTranslateView(resolvePosition(location.getX(), location.getY()), systemText.getStringFlavor());
            }
        });
        keyListener.addToGlobalScreen();
    }

    private static String lastSearch;


    /**
     * @param position 展示位置（左上角） 为空时为上一次隐藏的位置
     * @param search   为空时不进行搜索
     */
    private void displayTranslateView(@Nullable Pair<Double, Double> position, @Nullable String search) {
        Platform.runLater(() -> {
            if (nonNull(position)) {
                stage.setX(position._1);
                stage.setY(position._2);
            }
            stage.show();
        });
        if (nonNull(search) && !search.equals(lastSearch)) {
            translator.translate(search);
            lastSearch = search;
        }
    }

    private void hide() {
        Platform.runLater(() -> stage.hide());
    }

    /**
     * 根据配置文件解析显示位置
     * 为 {@link Configuration.ShowPosition#nearMouse} 时，接触到屏幕边缘会反弹
     */
    private Pair<Double, Double> resolvePosition(double x, double y) {
        switch (CONF.showPosition()) {
            case fixed:
                return pairOf((double) CONF.offsetX(), (double) CONF.offsetY());
            case nearMouse:
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double width = stage.getWidth();
                double height = stage.getHeight();
                double cushionLength = 30;
                if (width + x + cushionLength < screenBounds.getMaxX())
                    x += CONF.offsetX();
                else
                    x -= CONF.offsetX() + width;
                if (height + y + cushionLength < screenBounds.getMaxY())
                    y += CONF.offsetY();
                else
                    y -= CONF.offsetY() + height;
                return pairOf(x, y);
        }
        throw new UnsupportedOperationException("异常的配置信息 showPosition");
    }

    /**
     * 需要使用外部类提供的 {@link #stage} 对象、初始化时需要提供 {@link Configuration} 对象
     * 通过 {@link #initMouseListener(Supplier, BiConsumer)} 对 {@link Dictionary} 提供服务
     */
    class MouseCallBackHandler {

        private final Configuration conf;

        MouseCallBackHandler(Configuration conf) {
            this.conf = conf;
        }

        /**
         * 初始化鼠标监听,唯一一个供 {@link Dictionary} 调用的方法
         * 根据 {@link Constant#CONF} 构建所有鼠标按下、抬起事件
         * 运用本类其他内部方法抽象调用过程
         * 目前来说鼠标使用的翻译源只会是 {@link SystemSelection}
         *
         * @param textSupplier     获取的翻译文本的函数
         * @param viewAndTranslate 展示的翻译结果的回调方法
         *                         参数{@link Pair} 是计算后的展示位置
         *                         参数 {@link String} 是需要搜索的结果
         */
        private void initMouseListener(Supplier<String> textSupplier, BiConsumer<Pair<Double, Double>, String> viewAndTranslate) throws NativeHookException {
            // 为 null 时表明鼠标不能触发翻译
            final ClickTimeChecker clickTimeChecker = (conf.useSelection() && conf.selectionTiggerByMouse()) ? new ClickTimeChecker(conf.drugSelectInterval(), conf.doubleClickInterval()) : null;

            MouseListenerFacade mouseListener = new MouseListenerFacade();
            // 为 null 时表明不需要监控鼠标移出一定范围（后隐藏）
            // 在翻译界面出现后生效 触发隐藏后移除
            SquareMouseMovedListener squareMouseMovedListener = (conf.hideStrategy() == moveOrClickOut) ? new SquareMouseMovedListener() : null;
            // 在进行双击操作时 抬起(onReleased) 会比 按下(onPressed) 先处理，而在单击时正常,所以尽量不要在 onPressed 中添加过多事件
            mouseListener.onPressed(event -> {
                if (nonNull(clickTimeChecker)) {
                    clickTimeChecker.saveCurrentClick();
                }
            });
            // 当进行隐藏时 短时间内响应翻译时间将不再隐藏
            final boolean[] reViewByClick = {false};
            mouseListener.onReleased(event -> {
                final String thisSearch = textSupplier.get();
                if (!thisSearch.equals(lastSearch) && nonNull(clickTimeChecker) && clickTimeChecker.check()) {
                    reViewByClick[0] = true;
                    viewAndTranslate.accept(resolvePosition(event.getX(), event.getY()), thisSearch);
                    if (nonNull(squareMouseMovedListener)) {
                        squareMouseMovedListener.setNewSquare(event.getX() - 50, event.getY() - 25, event.getX() + 50, event.getY() + 25);
                        squareMouseMovedListener.setDisable(false);
                    }
                } else if ((CONF.hideStrategy() == clickOut || CONF.hideStrategy() == moveOrClickOut) && stage.isShowing()) {
                    try {
                        Thread.sleep(conf.doubleClickInterval());
                    } catch (InterruptedException e) {
                        throw new RuntimeException("无法有效识别双击操作，双击取词可能会导致翻译界面闪现，请使用拖拽取词", e);
                    }
                    if (reViewByClick[0])
                        return;
                    hideIfMouseOut(event.getX(), event.getY());
                    reViewByClick[0] = false;
                }
            });

            if (nonNull(squareMouseMovedListener)) {
                squareMouseMovedListener.onMovedTouchSquare((event) -> {
                    hideIfMouseOut(event.getX(), event.getY());
                    squareMouseMovedListener.setDisable(true);
                });
                stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, (event -> hide()));
                squareMouseMovedListener.addToGlobalScreen();
            }
            mouseListener.addToGlobalScreen();

        }


        /**
         * 加载当前 {@link #stage} 的矩形范围，若传入的x,y 不在此范围，则隐藏翻译页面
         */
        private void hideIfMouseOut(int x, int y) {
            Shape.Rectangle square = Shape.rectangle(pairOf(stage.getX(), stage.getY()), stage.getWidth(), stage.getHeight());
            if (square.isOutside(x, y)) {
                hide();
            }
        }


        class ClickTimeChecker {
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
                        || Duration.between(lastPressed, thisReleased).toMillis() < doubleClickInterval;
            }
        }

    }

}
