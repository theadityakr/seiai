package com.seiai.server.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ScreenshotUtil {

    public static byte[] captureScreenshot() throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenCapture = new Robot().createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "png", baos);
        return baos.toByteArray();
    }
}