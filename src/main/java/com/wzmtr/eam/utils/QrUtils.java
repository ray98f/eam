package com.wzmtr.eam.utils;

import cn.hutool.extra.qrcode.QrConfig;

import java.awt.*;

/**
 * 二维码工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
public class QrUtils {

    public static QrConfig initQrConfig() {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(2);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.BLACK);
        // 设置背景色（灰色）
        config.setBackColor(Color.WHITE);
        return config;
    }
}
