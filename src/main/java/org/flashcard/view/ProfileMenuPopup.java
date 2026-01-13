package org.flashcard.view;

import org.flashcard.application.dto.UserDTO;
import org.flashcard.controllers.UserController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProfileMenuPopup extends JWindow {

    private final UserController userController;
    private final Navbar navbar;
    private final JPanel container;

    public ProfileMenuPopup(UserController userController, Navbar navbar) {
        this.userController = userController;
        this.navbar = navbar;

        // Main container inside the popup
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(45, 45, 45));
        container.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        add(container);
    }

    /** Show the popup under the given component */
    public void showBelow(Component parent) {
        reloadList();

        Point p = parent.getLocationOnScreen();
        setLocation(p.x, p.y + parent.getHeight());

        pack();
        setVisible(true);
    }

    /** Rebuild the list each time the popup is opened */
    public void reloadList() {
        container.removeAll();

        List<UserDTO> users = userController.getAllUsers();

        for (UserDTO user : users) {
            container.add(createUserRow(user));
        }

        container.add(createSeparator());
        container.add(createCreateUserRow());

        container.revalidate();
        container.repaint();
    }

    /** Visual separator line */
    private Component createSeparator() {
        JPanel sep = new JPanel();
        sep.setPreferredSize(new Dimension(200, 1));
        sep.setBackground(new Color(100, 100, 100));
        return sep;
    }

    /** A row representing a user */
    private JPanel createUserRow(UserDTO user) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(55, 55, 55));
        row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        row.setPreferredSize(new Dimension(220, 36)); // <-- FIXED & STYLED

        JLabel name = new JLabel(user.getUsername());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton delete = new JButton("🗑");
        delete.setForeground(Color.RED);
        delete.setFocusable(false);
        delete.setOpaque(false);
        delete.setBorder(null);
        delete.setContentAreaFilled(false);
        delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Delete user " + user.getUsername() + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {

                Integer currentId = userController.getCurrentUserId();
                boolean deletedActiveUser = (currentId != null && currentId.equals(user.getId()));

                // delete user
                userController.deleteUser(user.getId());

                // reload menu UI
                reloadList();

                // If active user deleted → switch to first available user
                List<UserDTO> remaining = userController.getAllUsers();
                if (deletedActiveUser) {
                    if (!remaining.isEmpty()) {
                        userController.loginByUserId(remaining.get(0).getId());
                    } else {
                        // no users left → logout completely
                        //userController.logout();
                    }
                }

                navbar.onUserChanged();
            }
        });

        // Hover effect
        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                row.setBackground(new Color(70, 70, 70));
            }
            @Override public void mouseExited(MouseEvent e) {
                row.setBackground(new Color(55, 55, 55));
            }
            @Override public void mouseClicked(MouseEvent e) {
                userController.loginByUserId(user.getId());
                navbar.onUserChanged();
                setVisible(false);
            }
        });

        row.add(name, BorderLayout.WEST);
        row.add(delete, BorderLayout.EAST);

        return row;
    }

    /** Row for creating a new user */
    private JPanel createCreateUserRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(55, 55, 55));
        row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        row.setPreferredSize(new Dimension(220, 36));

        JLabel label = new JLabel("➕ Create User");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                row.setBackground(new Color(70, 70, 70));
            }
            @Override public void mouseExited(MouseEvent e) {
                row.setBackground(new Color(55, 55, 55));
            }
            public void mouseClicked(MouseEvent e) {
                while (true) {
                    String name = JOptionPane.showInputDialog(null, "Enter new username (at least 3 characters):");
                    if (name == null) return; // user cancelled

                    name = name.trim();
                    if (name.length() >= 3) {
                        UserDTO newUser = userController.createUser(name);
                        userController.loginByUserId(newUser.getId());
                        navbar.onUserChanged();

                        reloadList();
                        setVisible(false);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Username must be at least 3 characters long.", "Invalid Username", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        row.add(label, BorderLayout.WEST);
        return row;
    }
}
