package com.seiai.server.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.seiai.server.services.ScreenshotService;
import com.seiai.server.util.ScreenshotUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Component
public class GlobalKeyListener implements NativeKeyListener {

    private final ScreenshotService screenshotService;

    public GlobalKeyListener(ScreenshotService screenshotService) {
        this.screenshotService = screenshotService;
    }

    @PostConstruct
    public void init() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException e) {
            System.err.println("Failed to register native hook: " + e.getMessage());
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // Check for Ctrl+Alt+Q
        boolean ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
        boolean alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
        boolean isQ = e.getKeyCode() == NativeKeyEvent.VC_Q;

        if (ctrl && alt && isQ) {
            try {
                MultipartFile screenshot = ScreenshotUtil.captureScreenshot();
                screenshotService.uploadScreenshot(screenshot);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }
    }
}