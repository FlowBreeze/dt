package com.lfkj.util.system;

import java.awt.*;

public class SystemSelectionListener extends SystemTextListener {
    public SystemSelectionListener() {
        clipboard = Toolkit.getDefaultToolkit().getSystemSelection();
    }
}
