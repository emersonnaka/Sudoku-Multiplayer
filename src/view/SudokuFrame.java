/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.GridLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.DefaultCaret;

import controller.Multicast;

/**
 *
 * @author Emerson Yudi Nakashima
 * @author Gustavo Correia Gonzalez
 */
public class SudokuFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2456990262302355919L;
	private DefaultListModel<String> model;
	private final Multicast multicast;

    /**
     * Creates new form Sudoku
     */
    public SudokuFrame(final Multicast multicast) {
    	model = new DefaultListModel<String>();
    	this.multicast = multicast;
    	
    	setTitle("Sudoku Multiplayer");
        initComponents();
        this.matrixSudokuTextField = new IntJTextField[9][9];
        buildMatrixSudoku();
        
        this.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		try {
					multicast.sendMessageFrame("LEAVE " + multicast.getNickname());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	}
		});
        
        this.setVisible(true);
    }
    
    private void buildMatrixSudoku() {
    	
    	for(int l = 0; l < 9; l++) {
    		for(int c = 0; c < 9; c++) {
    			final IntJTextField cell = new IntJTextField();
    			this.matrixSudokuTextField[l][c] = new IntJTextField();
    			buildCell(cell,l, c);
    			this.matrixSudokuTextField[l][c] = cell;
    			this.sudokuPanel.add(this.matrixSudokuTextField[l][c]);
    		}
    	}
    }
    
    private void buildCell(final IntJTextField cell, int l, int c) {
    	cell.setFont(new Font("sansserif", Font.BOLD, 30));
		cell.setHorizontalAlignment(IntJTextField.CENTER);
		cell.setCaretColor(Color.LIGHT_GRAY);
		cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cell.setLine(l);
		cell.setColumn(c);
		cell.setForeground(Color.BLACK);
		cell.setAutoscrolls(false);
		addListenersCell(cell);
    }
    
    private void addListenersCell(final IntJTextField cell)  {
    	final int l = cell.getLine();
    	final int c = cell.getColumn();
    	addFocusListenerCell(cell, l, c);
    	addKeyListener(cell, l, c);
    }
    
    private void addFocusListenerCell(final IntJTextField cell, final int l, final int c) {
    	cell.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				paintLineColumn(l, c, Color.LIGHT_GRAY);
				cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
				try {
					if(cell.isEditable()) 
						multicast.sendMessageFrame("LOCKED " + multicast.getNickname() + " " + String.valueOf(l) + " " + String.valueOf(c) + " false");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				paintLineColumn(l, c, Color.WHITE);
				cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				try {
					multicast.sendMessageFrame("LOCKED " + multicast.getNickname() + " " + String.valueOf(l) + " " + String.valueOf(c) + " true");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
    }
    
    private void addKeyListener(final IntJTextField cell, final int l, final int c) {
    	cell.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int valueKeyInput = Character.getNumericValue(e.getKeyChar());
				int contains;
				if(!cell.getText().equals(""))
					contains = Integer.parseInt(cell.getText().trim());
				else
					contains = 0;
				if(valueKeyInput >= 0 && valueKeyInput < 10) {
					if(valueKeyInput != contains){
						if(valueKeyInput != 0){
							cell.setText(String.valueOf(valueKeyInput));
						} else {
							cell.setText("");
						}
						
						try {
							multicast.sendMessageFrame("UPDATE " + multicast.getNickname() + " " + String.valueOf(l) + " " + String.valueOf(c) + " " + String.valueOf(valueKeyInput));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
    }
    
    private void paintLineColumn(int l, int c, Color color) {
    	for(int scrollLine = 0, scrollColumn = 0; scrollLine < 9; scrollLine++, scrollColumn++) {
    		this.matrixSudokuTextField[scrollLine][c].setBackground(color);
    		this.matrixSudokuTextField[l][scrollColumn].setBackground(color);
    	}
    	
    	if(l < 3) {
            if(c < 3)
                paintMatrix(0, 0, color);
            else if(c >= 3 && c < 6)
                paintMatrix(0, 3, color);
            else
                paintMatrix(0, 6, color);
        } else if(l >= 3 && l < 6) {
            if(c < 3)
                paintMatrix(3, 0, color);
            else if(c >= 3 && c < 6)
                paintMatrix(3, 3, color);
            else
                paintMatrix(3, 6, color);
        } else {
            if(c < 3)
                paintMatrix(6, 0, color);
            else if(c >= 3 && c < 6)
                paintMatrix(6, 3, color);
            else
                paintMatrix(6, 6, color);
        }
    }

    private void paintMatrix(int l, int c, Color color) {
    	for(int scrollLine = l; scrollLine < (l + 3); scrollLine++) {
            for(int scrollColumn = c; scrollColumn < (c + 3); scrollColumn++) {
                this.matrixSudokuTextField[scrollLine][scrollColumn].setBackground(color);
            }
        }
    }
    
    public void appendPlayer(String nick) {
    	model.addElement(nick);
    }
    
    public void removePlayer(String nick) {
    	model.removeElement(nick);
    }
    
    public boolean verifyPlayer(String nick) {
    	return model.contains(nick);
    }
    
    public void appendMsg(String msg) {
    	chatTextArea.append(msg);
    }
    
    public String getValueCell(int l, int c) {
    	return matrixSudokuTextField[l][c].getText().trim();
    }
    
    public void setEditableCell(int l, int c, boolean locked) {
    	matrixSudokuTextField[l][c].setEnabled(locked);
    }
    
    public void setTextCell(int l, int c, String value) {
    	if(!value.equals("0"))
    		matrixSudokuTextField[l][c].setText(value);
    	else
    		matrixSudokuTextField[l][c].setText("");
    }
    
    public void insertMatrix(int[][] matrixSudoku){
    	for (int i = 0; i < matrixSudoku.length; i++) {
			for (int j = 0; j < matrixSudoku.length; j++) {
				if(matrixSudoku[i][j] != 0) {
					this.matrixSudokuTextField[i][j].setEnabled(false);
					this.matrixSudokuTextField[i][j].setText(String.valueOf(matrixSudoku[i][j]));
					this.matrixSudokuTextField[i][j].setForeground(Color.BLACK);
				} else
					this.matrixSudokuTextField[i][j].setText("");
			}
		}
    }
    
    public void setForegroundCell(Color color, int l, int c){
    	this.matrixSudokuTextField[l][c].setForeground(color);
    }

	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        chatPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        listPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        playersList = new javax.swing.JList<>(this.model);
        sudokuPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 700));

        chatPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        msgTextArea.setColumns(20);
        msgTextArea.setRows(3);
        msgTextArea.setLineWrap(true);
        jScrollPane1.setViewportView(msgTextArea);

        sendButton.setText("Send");
        sendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = msgTextArea.getText().trim();
				if(!msg.isEmpty()) {
					msg = "MSG " + multicast.getNickname() + ": " + msg;
					try {
						multicast.sendMessageFrame(msg);
						msgTextArea.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

        chatTextArea.setEditable(false);
        chatTextArea.setColumns(20);
        chatTextArea.setRows(5);
        DefaultCaret caret = (DefaultCaret)chatTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jScrollPane2.setViewportView(chatTextArea);

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        listPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        playersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(playersList);

        javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
        );
        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        );

        sudokuPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(listPanel, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(sudokuPanel, GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(listPanel, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        				.addComponent(sudokuPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(chatPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        getContentPane().setLayout(layout);
        sudokuPanel.setLayout(new GridLayout(9, 9, 1, 1));

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JPanel chatPanel;
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel listPanel;
    private javax.swing.JTextArea msgTextArea;
    private javax.swing.JList<String> playersList;
    private javax.swing.JButton sendButton;
    private javax.swing.JPanel sudokuPanel;
    private IntJTextField matrixSudokuTextField[][];
    // End of variables declaration                   
}

