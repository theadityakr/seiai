package com.seiai.server.domain;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TransparentOverlay {

    private JFrame frame;
    private JTextArea explanationText;
    private JTextArea codeText;
    private JTextArea extraText;
    private final User32 user32 = User32.INSTANCE;

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextArea createSectionText(int size, String fontName) {
        JTextArea textArea = new JTextArea("");
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font(fontName, Font.PLAIN, size));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return textArea;
    }

    private void styleCloseButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
    }

    public void centerFrame() {
        if (frame == null) return;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    public void moveLeft() {
        if (frame == null) return;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int leftCenterX = screenSize.width / 4;
        int x = leftCenterX - frame.getWidth() / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        frame.setLocation(x, y);
    }

    public void moveRight() {
        if (frame == null) return;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int rightCenterX = (screenSize.width * 3) / 4;
        int x = rightCenterX - frame.getWidth() / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        frame.setLocation(x, y);
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

    private static JPanel getJPanel() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Semi-transparent background
                g2d.setColor(new Color(30, 30, 30, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Border
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);

                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setOpaque(false);
        return mainPanel;
    }

    public void updateText(Widget widget) {
        explanationText.setText(widget.getExplanation());
        codeText.setText(widget.getCode());
        extraText.setText(widget.getExtra());
        frame.revalidate();
        frame.repaint();
    }

    public void hideOverlay() {
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    public void showOverlay() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
        }
    }

    public void toggleOverlay() {
        if (frame != null) {
            if (frame.isVisible()) {
                hideOverlay();
            } else {
                showOverlay();
            }
        }
    }

    public void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Transparent Overlay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setAlwaysOnTop(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 600, 900, 20, 20));

        JPanel mainPanel = getJPanel();

        // 1. Explanation Section
        JLabel explanationTitle = createSectionLabel("Explanation");
        explanationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(explanationTitle);
        explanationText = createSectionText(16, "Plain");
        mainPanel.add(explanationText);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. Code Block Section
        JLabel codeTitle = createSectionLabel("Code");
        codeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(codeTitle);
        codeText = createSectionText(14, "Monospaced");
        codeText.setBackground(new Color(50, 50, 50, 150));
        mainPanel.add(codeText);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. Extra Block Section
        JLabel extraTitle = createSectionLabel("Extra");
        extraTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(extraTitle);
        extraText = createSectionText(14, "Plain");
        extraText.setBackground(new Color(50, 50, 50, 150));
        mainPanel.add(extraText);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        frame.setContentPane(mainPanel);
        frame.setSize(600, 900);
        centerFrame();
        frame.setVisible(true);
        applyWindowsSettings();
    }

}