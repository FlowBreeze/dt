package com.lfkj.util.system;

import java.awt.*;

/**
 * {@link SystemText} 的多态重载之一
 * 负责获取用户框选的文本（Windows 下可能不支持）
 *
 * @see SystemText
 */
public class SystemSelection extends SystemText {
    public SystemSelection() {
        clipboard = Toolkit.getDefaultToolkit().getSystemSelection();
    }
}
