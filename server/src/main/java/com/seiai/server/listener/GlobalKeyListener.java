package com.seiai.server.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.seiai.server.services.OverlayService;
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
    private final OverlayService overlayService;

    public GlobalKeyListener(ScreenshotService screenshotService, OverlayService overlayService) {
        this.screenshotService = screenshotService;
        this.overlayService = overlayService;
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
        boolean ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
        boolean alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
        int alphabet = e.getKeyCode();

        if (ctrl && alt && (alphabet == NativeKeyEvent.VC_Q ) ) {
            try {
                MultipartFile screenshot = ScreenshotUtil.captureScreenshot();
                screenshotService.uploadScreenshot(screenshot);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (ctrl && alt && (alphabet == NativeKeyEvent.VC_W ) ) {
            try {
                overlayService.toggleOverlay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (ctrl && alt && (alphabet == NativeKeyEvent.VC_A ) ) {
            try {
                overlayService.moveOverlayLeft();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (ctrl && alt && (alphabet == NativeKeyEvent.VC_S ) ) {
            try {
                overlayService.moveOverlayCenter();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (ctrl && alt && (alphabet == NativeKeyEvent.VC_D ) ) {
            try {
                overlayService.moveOverlayRight();
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