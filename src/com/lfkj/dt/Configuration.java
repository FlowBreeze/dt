package com.lfkj.dt;

import java.util.Map;

public class Configuration {

    public boolean useSystemSelection;
    // if useSystemSelection true these selection is available
//    {
    public boolean mouseTigger;
    public int doubleClickInterval;
    public int drugSelectInterval;
    public boolean notReapetLast;
    //    }
    public Map<Class<? extends Translator>, String> translatersUsed;
    public boolean hideWhenMouseOutOfStage;
    public int hideWhenFixedTime;
    public int hideWhenMouseClick;

    public ShowPosition showPosition;

    enum ShowPosition {
        nearMouse, fixed
    }

    public int offsetX;
    public int offsetY;

    private Configuration() {

    }

    public static Configuration loadOrDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.useSystemSelection = true;
        return configuration;
    }

    public void saveConfiguration() {

    }
}
