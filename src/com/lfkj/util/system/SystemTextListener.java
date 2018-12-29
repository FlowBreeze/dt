package com.lfkj.util.system;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.function.Consumer;

public abstract class SystemTextListener {

    Clipboard clipboard;

    public void addSelectionListener(Consumer<String> callBack) {
        clipboard.addFlavorListener(e -> {
            String data = null;
            try {
                data = (String) clipboard.getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(data);
            callBack.accept(data);
        });
    }

    public String getStringFlavor() {
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
