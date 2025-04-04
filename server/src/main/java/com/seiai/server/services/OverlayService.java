package com.seiai.server.services;

import com.seiai.server.domain.TransparentOverlay;
import com.seiai.server.domain.Widget;
import com.seiai.server.repositories.WidgetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.Optional;

@Service
public class OverlayService implements CommandLineRunner {

    private final WidgetRepository widgetRepository;
    private final ApplicationEventPublisher eventPublisher;
    private TransparentOverlay overlay;

    public OverlayService(WidgetRepository widgetRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.widgetRepository = widgetRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run(String... args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            overlay = new TransparentOverlay();
            overlay.createAndShowGUI();
        });
    }

    @Transactional
    public void updateWidget(String ipAddress) {
        Widget widget = widgetRepository.findById(ipAddress)
                .orElse(Widget.builder().ipAddress(ipAddress).build());
        SwingUtilities.invokeLater(() -> overlay.updateText(widget));
    }

    public void toggleOverlay() {
        SwingUtilities.invokeLater(() -> overlay.toggleOverlay());
    }

    public void moveOverlayLeft() {
        SwingUtilities.invokeLater(() -> overlay.moveLeft());
    }

    public void moveOverlayCenter() {
        SwingUtilities.invokeLater(() -> overlay.centerFrame());
    }

    public void moveOverlayRight() {
        SwingUtilities.invokeLater(() -> overlay.moveRight());
    }
}