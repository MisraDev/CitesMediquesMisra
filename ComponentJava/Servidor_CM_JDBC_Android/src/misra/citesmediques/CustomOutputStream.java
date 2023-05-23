/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author misra
 */
class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }
}
