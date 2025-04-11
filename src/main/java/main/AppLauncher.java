package main;

import gui.MensajeGUI;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AppLauncher {
    public static void main(String[] args) {
        // Preguntar al usuario qué interfaz desea utilizar
        String[] opciones = {"Interfaz Gráfica", "Consola"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Cómo desea ejecutar la aplicación?",
                "Sistema de Mensajes",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == 0) {
            // Iniciar interfaz gráfica
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                MensajeGUI gui = new MensajeGUI();
                gui.setVisible(true);
            });
        } else if (seleccion == 1) {
            // Iniciar interfaz de consola
            Main.main(args);
        } else {
            System.exit(0);
        }
    }
}