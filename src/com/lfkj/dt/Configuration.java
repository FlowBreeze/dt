package com.lfkj.dt;

import com.lfkj.util.Pair;
import com.lfkj.util.function.RepeatableConsumer;
import com.lfkj.util.system.Keys;
import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import java.io.*;

import static com.lfkj.dt.Constant.CONFIG_FILE_NAME;
import static com.lfkj.dt.Constant.VERSION;
import static com.lfkj.util.cast.LfkjCommonCast.pairOf;

@Config.Sources("file:" + CONFIG_FILE_NAME)
public interface Configuration extends Mutable, Accessible {
    String useSelection = "useSelection";

    @DefaultValue("true")
    boolean useSelection();

    String selectionTiggerByMouse = "selectionTiggerByMouse";

    @DefaultValue("true")
    boolean selectionTiggerByMouse();

    String doubleClickInterval = "doubleClickInterval";

    @DefaultValue("800")
    int doubleClickInterval();

    String drugSelectInterval = "drugSelectInterval";

    @DefaultValue("500")
    int drugSelectInterval();

    String selectionTiggerByKey = "selectionTiggerByKey";

    @DefaultValue("true")
    boolean selectionTiggerByKey();

    String selectionTiggerKey = "selectionTiggerKey";

    @Separator("\\+")
    @DefaultValue("alt + d")
    Keys[] selectionTiggerKey();

    String useClipBoard = "useClipBoard";

    @DefaultValue("false")
    boolean useClipBoard();

    String clipboardTiggerKey = "clipboardTiggerKey";

    @Separator("\\+")
    @DefaultValue("ctrl + c")
    Keys[] clipboardTiggerKey();

    String tabs = "tabs";

    @DefaultValue("BaiduTranslator.fxml, ICiBaTranslator.fxml")
    String[] tabs();

    String hideWhenMouseOutOfStage = "hideWhenMouseOutOfStage";

    @DefaultValue("true")
    boolean hideWhenMouseOutOfStage();

    String hideWhenFixedTime = "hideWhenFixedTime";

    @DefaultValue("-1")
    int hideWhenFixedTime();

    String hideWhenMouseClickOutside = "hideWhenMouseClickOutside";

    @DefaultValue("true")
    boolean hideWhenMouseClickOutside();

    String offsetX = "offsetX";

    @DefaultValue("0")
    int offsetX();

    String offsetY = "offsetY";

    @DefaultValue("20")
    int offsetY();

    String showPosition = "showPosition";

    @DefaultValue("nearMouse")
    ShowPosition showPosition();

    enum ShowPosition {
        nearMouse, fixed
    }

    @DefaultValue("3B8BEC6E03BE66DAB129AFFBC6C57380")
    String iCiBaKey();

    String iCiBaKey = "iCiBaKey";

    @DefaultValue("20181216000249121")
    long baiduAppId();

    String baiduAppId = "baiduAppId";

    @DefaultValue("i7roLjgbkCP3O4fq9OEr")
    String baiduPrivateKey();

    String baiduPrivateKey = "baiduPrivateKey";

    default void save() throws IOException {
        File file = new File(CONFIG_FILE_NAME);
        BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
        RepeatableConsumer<Pair<String, String>> write = s -> {
            try {
                out.write(s._1 + "=" + s._2 + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        out.write("# dt 默认获取通过操作系统用户选中的文本，在 linux 下运行良好、windows 下未测试\n");
        out.write("# 版本 " + VERSION + "\n");
        out.write("# 以下是用户配置信息，通过这些配置可以简易的更改 dt 的运行逻辑\n");
        out.write("\n# < 数据源和触发 >\n");
        out.write("# 当 useSelection 为 true 时,可通过 selectionTiggerByMouse 和 selectionTiggerByKey 和其中的子选项设置翻译程序的触发\n");
        out.write("# selectionTiggerByMouse 通过鼠标触发， selectionTiggerByKey 通过快捷键触发");
        out.write("# doubleClickInterval 设置双击间隔，当用户双击间隔小于设置时间时触发翻译（可能有100ms的误差）\n");
        out.write("# drugSelectInterval 设置拖拽间隔，当用户鼠标按下--抬起的间隔大于设置时间时触发翻译（可能有100ms的误差）\n");
        out.write("# 鼠标监听不会响应和上一次一样的翻译事件，避免双击打开应用也会弹出界面\n");
        out.write("# selectionTiggerKey clipboardTiggerKey 可以设置快捷键翻译，可以使用的按键有 [0-9a-zA-Z] 和 ctrl alt shift win\n");
        write.reAccept(pairOf(useSelection, getProperty(useSelection)))
                .reAccept(pairOf(selectionTiggerByMouse, getProperty(selectionTiggerByMouse)))
                .reAccept(pairOf(doubleClickInterval, getProperty(doubleClickInterval)))
                .reAccept(pairOf(drugSelectInterval, getProperty(drugSelectInterval)))
                .reAccept(pairOf(selectionTiggerByKey, getProperty(selectionTiggerByKey)))
                .reAccept(pairOf(selectionTiggerKey, getProperty(selectionTiggerKey)));
        out.write("# 当 useClipBoard 为 true 时,开启剪切板监听，剪贴板监听只支持快捷键触发翻译\n");
        write.reAccept(pairOf(useClipBoard, getProperty(useClipBoard)))
                .reAccept(pairOf(clipboardTiggerKey, getProperty(clipboardTiggerKey)));
        out.write("\n# < 界面展示 >\n");
        out.write("# tabs 中是 javafx 的 fxml 布局文件，可以通过更改切换布局样式，每个 fxml 对应一个翻译器 (translator),之间用逗号隔开\n");
        write.reAccept(pairOf(tabs, getProperty(tabs)))
                .reAccept(pairOf(hideWhenMouseOutOfStage, getProperty(hideWhenMouseOutOfStage)))
                .reAccept(pairOf(hideWhenMouseClickOutside, getProperty(hideWhenMouseClickOutside)))
                .reAccept(pairOf(hideWhenFixedTime, getProperty(hideWhenFixedTime)));
        out.write("# showPosition 有两个属性值，分别是 nearMouse(贴近鼠标) 和 fixed(固定) 为翻译窗口默认出现的位置\n");
        out.write("# offsetX 和 offsetY 对应窗口的偏移量，当 showPosition 为 nearMouse 时，从当前鼠标位置开始计算、当 showPosition 为 fixed 时，从屏幕左上角开始计算\n");
        write.reAccept(pairOf(showPosition, getProperty(showPosition)))
                .reAccept(pairOf(offsetX, getProperty(offsetX)))
                .reAccept(pairOf(offsetY, getProperty(offsetY)));
        out.write("\n# < 翻译接口 >\n");
        out.write("# 百度翻译api目前有免费额度，超出收费,默认的appid和私钥超出收费时可能会停用\n");
        out.write("# 请自行到下面的网址申请\n");
        out.write("# https://api.fanyi.baidu.com/api/trans/product/prodinfo\n");
        write.reAccept(pairOf(iCiBaKey, getProperty(iCiBaKey)))
                .reAccept(pairOf(baiduAppId, getProperty(baiduAppId)))
                .reAccept(pairOf(baiduPrivateKey, getProperty(baiduPrivateKey)));
        out.flush();
    }

    @Override
    default void store(OutputStream outputStream, String common) {
        throw new UnsupportedOperationException();
    }

    static Configuration loadOrDefaultConfiguration() {
        return ConfigFactory.create(Configuration.class);
    }
}
