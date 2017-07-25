package view;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author DevMedia http://www.devmedia.com.br/como-alterar-o-componente-jtextfield-para-aceitar-apenas-numeros/26152
 * Adaptado por:
 *   Emerson Yudi Nakashima
 *   Gustavo Correia Gonzalez
 */
public class IntJTextField extends JTextField {
        /**
	 * 
	 */
	private static final long serialVersionUID = -7043386105706770279L;
	private int maxCharacters = 1, line, column;
    
    public IntJTextField() {
        super();
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);}});
    }
  
    private void jTextFieldKeyTyped(KeyEvent evt) {
        String characters="123456789";
        if(!characters.contains(evt.getKeyChar()+"")){
            evt.consume();
        }
        if((getText().length() >= getMaxCharacters()) && (getMaxCharacters() != -1)){
            evt.consume();
            setText(getText().substring(0,getMaxCharacters()));
        }
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }
    
    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
