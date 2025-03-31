package com.seiai.server.listener;

import com.seiai.server.util.ScreenshotUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Component
public class GlobalKeyListener {

    private final ApiService apiService;

    public GlobalKeyListener(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostConstruct
    public void init() {
        System.setProperty("java.awt.headless", "false");

        try {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(event -> {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            if (event.isControlDown() &&
                                    event.isShiftDown() &&
                                    event.getKeyCode() == KeyEvent.VK_S) {
                                try {
                                    byte[] screenshot = ScreenshotUtil.captureScreenshot();
                                    apiService.uploadScreenshot(screenshot);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true; // consume the event
                            }
                        }
                        return false;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}