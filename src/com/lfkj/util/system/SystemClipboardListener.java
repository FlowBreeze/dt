package com.lfkj.util.system;

import java.awt.*;

public class SystemClipboardListener extends SystemTextListener {
    public SystemClipboardListener() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}
