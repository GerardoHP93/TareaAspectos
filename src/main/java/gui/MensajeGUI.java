package gui;

import models.Mensaje;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import services.MensajeService;
import config.ProjectConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MensajeGUI extends JFrame {
    private final MensajeService mensajeService;
    private final AnnotationConfigApplicationContext context;

    private JTextField txtAutor;
    private JTextArea txtMensaje;
    private JTextArea txtResultado;
    private JButton btnEnviar;

    public MensajeGUI() {
        // Inicializar el contexto de Spring
        context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);
        mensajeService = context.getBean(MensajeService.class);

        // Configurar la ventana
        setTitle("Sistema de Mensajes");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Añadir componentes
        crearComponentes();

        // Agregar un listener para cerrar el contexto de Spring cuando se cierre la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (context != null) {
                    context.close();
                }
                super.windowClosing(e);
            }
        });
    }

    private void crearComponentes() {
        // Panel principal con layout de BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior para datos del autor
        JPanel panelAutor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblAutor = new JLabel("Tu nombre:");
        txtAutor = new JTextField(20);
        panelAutor.add(lblAutor);
        panelAutor.add(txtAutor);

        // Panel central para el mensaje
        JPanel panelMensaje = new JPanel(new BorderLayout(5, 5));
        JLabel lblMensaje = new JLabel("Escribe tu mensaje:");
        txtMensaje = new JTextArea(5, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);

        panelMensaje.add(lblMensaje, BorderLayout.NORTH);
        panelMensaje.add(scrollMensaje, BorderLayout.CENTER);

        // Panel para el botón de enviar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnEnviar = new JButton("Enviar Mensaje");
        btnEnviar.addActionListener(e -> procesarMensaje());
        panelBoton.add(btnEnviar);

        // Panel para mostrar el resultado
        JPanel panelResultado = new JPanel(new BorderLayout(5, 5));
        JLabel lblResultado = new JLabel("Mensaje procesado:");
        txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollResultado = new JScrollPane(txtResultado);

        panelResultado.add(lblResultado, BorderLayout.NORTH);
        panelResultado.add(scrollResultado, BorderLayout.CENTER);

        // Añadir todo al panel principal
        panelPrincipal.add(panelAutor, BorderLayout.NORTH);

        // Panel central que contiene mensaje y resultado
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.add(panelMensaje);
        panelCentral.add(panelResultado);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        // Añadir el panel principal a la ventana
        add(panelPrincipal);
    }

    private void procesarMensaje() {
        // Validar entrada
        String autor = txtAutor.getText().trim();
        String contenido = txtMensaje.getText().trim();

        if (autor.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese su nombre.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE);
            txtAutor.requestFocus();
            return;
        }

        if (contenido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, escriba un mensaje.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE);
            txtMensaje.requestFocus();
            return;
        }

        // Crear y procesar el mensaje
        Mensaje mensaje = new Mensaje();
        mensaje.setAutor(autor);
        mensaje.setContenido(contenido);

        Mensaje mensajeProcesado = mensajeService.procesarMensaje(mensaje);

        // Mostrar resultado en el área de texto
        StringBuilder resultado = new StringBuilder();
        resultado.append("Autor: ").append(mensajeProcesado.getAutor()).append("\n\n");
        resultado.append("Mensaje: ").append(mensajeProcesado.getContenido()).append("\n\n");

        if (mensajeProcesado.isCensurado()) {
            resultado.append("⚠️ El mensaje ha sido censurado debido a contenido inapropiado.\n");
        } else {
            resultado.append("✓ El mensaje ha pasado la revisión de contenido sin problemas.\n");
        }

        txtResultado.setText(resultado.toString());

        // Opcionalmente, limpiar el campo de mensaje para el siguiente
        txtMensaje.setText("");
        txtMensaje.requestFocus();
    }

    public static void main(String[] args) {
        // Configurar el look and feel para que se vea mejor
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            MensajeGUI gui = new MensajeGUI();
            gui.setVisible(true);
        });
    }
}