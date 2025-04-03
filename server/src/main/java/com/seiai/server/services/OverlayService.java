package com.seiai.server.services;

import com.seiai.server.domain.TransparentOverlay;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class OverlayService implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Start the GUI in the EDT
        SwingUtilities.invokeLater(() -> {
            TransparentOverlay overlay = new TransparentOverlay();
            overlay.createAndShowGUI();
        });
    }
}