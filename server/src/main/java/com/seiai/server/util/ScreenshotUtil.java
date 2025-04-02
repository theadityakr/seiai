package com.seiai.server.util;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;

public class ScreenshotUtil {

    public static MultipartFile captureScreenshot() throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenCapture = new Robot().createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return new MultipartFile() {
            @Override
            public String getName() {
                return "screenshot.png";
            }

            @Override
            public String getOriginalFilename() {
                return "screenshot.png";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return imageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return imageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return imageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(), imageBytes);
            }
        };
    }
}