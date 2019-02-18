package com.lfkj.util.system;

import java.awt.*;

/**
 * {@link SystemText} 的多态重载之一
 * 负责与剪切板交互
 *
 * @see SystemText
 */
public class SystemClipboard extends SystemText {
    public SystemClipboard() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}
