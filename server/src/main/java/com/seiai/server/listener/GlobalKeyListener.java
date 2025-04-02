package com.seiai.server.listener;

import com.seiai.server.services.ScreenshotService;
import com.seiai.server.util.ScreenshotUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.event.KeyEvent;

@Component
public class GlobalKeyListener {

    private final ScreenshotService screenshotService;

    public GlobalKeyListener(ScreenshotService screenshotService) {
        this.screenshotService = screenshotService;
    }

    @PostConstruct
    public void init() {
        System.setProperty("java.awt.headless", "false");

        try {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(event -> {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            if (event.isControlDown() &&
                                    event.isAltDown() &&
                                    event.getKeyCode() == KeyEvent.VK_Q) {
                                try {
                                    System.out.println("KEy pRessed");
                                    MultipartFile screenshot = ScreenshotUtil.captureScreenshot();
                                    screenshotService.uploadScreenshot(screenshot);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        }
                        return false;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}