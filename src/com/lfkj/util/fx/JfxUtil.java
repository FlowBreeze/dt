package com.lfkj.util.fx;

import javafx.scene.Node;
import javafx.stage.Stage;

public class JfxUtil {
    /**
     * 绑定 stage 当 TabPane 拖动时对 stage 进行位置调整
     */
    public static void bindDruggedListener(Node node, Stage stage) {
        final double[] sceneXY = new double[2];
        node.setOnMousePressed(event -> {
            sceneXY[0] = event.getSceneX();
            sceneXY[1] = event.getSceneY();
        });
        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - sceneXY[0]);
            if (event.getScreenY() - sceneXY[1] < 0) {
                stage.setY(0);
            } else {
                stage.setY(event.getScreenY() - sceneXY[1]);
            }
        });
    }

}
