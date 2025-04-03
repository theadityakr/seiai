package com.seiai.server.domain;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TransparentOverlay {

    private JFrame frame;
    private User32 user32 = User32.INSTANCE;

    public void createAndShowGUI() {
        // Create undecorated frame
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setAlwaysOnTop(true);

        // Make transparent
        frame.setOpacity(0.7f); // Adjust transparency level

        // Set size and position (example: full screen)
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = ge.getMaximumWindowBounds();
        frame.setSize(bounds.width, bounds.height);
        frame.setLocation(bounds.x, bounds.y);

        // Add your GUI components here
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Custom painting here
                g.setColor(Color.RED);
                g.drawString("Your Overlay Content", 50, 50);
            }
        };
        panel.setOpaque(false);
        frame.setContentPane(panel);

        frame.setVisible(true);

        // Apply Windows-specific settings
        applyWindowsSettings();
    }

    private void applyWindowsSettings() {
        // Get native window handle
        WinDef.HWND hwnd = new WinDef.HWND(Native.getWindowPointer(frame));

        // Get current extended style
        int extendedStyle = user32.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);

        // Set the extended styles using their actual values
        extendedStyle |= 0x00000080;   // WS_EX_TOOLWINDOW
        extendedStyle |= 0x08000000;   // WS_EX_NOACTIVATE
        extendedStyle |= 0x00080000;   // WS_EX_LAYERED
        extendedStyle |= 0x00000020;   // WS_EX_TRANSPARENT

        // Apply the new extended style
        user32.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, extendedStyle);

        // Force redraw
        user32.SetWindowPos(hwnd, new WinDef.HWND(new Pointer(-1L)), 0, 0, 0, 0,
                WinUser.SWP_NOMOVE | WinUser.SWP_NOSIZE | WinUser.SWP_FRAMECHANGED);
    }
}