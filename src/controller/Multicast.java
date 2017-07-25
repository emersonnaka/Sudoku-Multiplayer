package controller;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import model.MatrixSudoku;
import view.SudokuFrame;

public class Multicast {
	
	private final MulticastSocket mSocket;
	private final String nickname;
	private final String host;
	private final int port;
	private final InetAddress group;
	private HashMap<String, String> onlineMap;
	private final SudokuFrame sudokuFrame;
	private MatrixSudoku matrixSudoku;
	private final UDPSocket udpChat;
	
	public Multicast(String nickname) throws IOException, InterruptedException {
		
		this.nickname = nickname;
		this.host = "225.1.2.3";
		this.port = 8001;
		this.group = InetAddress.getByName(this.host);
		mSocket = new MulticastSocket(this.port);
		mSocket.joinGroup(this.group);
		this.sudokuFrame = new SudokuFrame(this);
		this.onlineMap = new HashMap<>();
		this.matrixSudoku = MatrixSudoku.getMatrixSudoku();
		udpChat = new UDPSocket(this.nickname, sudokuFrame, onlineMap, matrixSudoku);
		
		Runnable serverRun = new Runnable() {
			@Override
			public void run() {
				try {
					serverMulticast();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		Thread serverThread = new Thread(serverRun);
		serverThread.start();
		
		sendMessage("JOIN " + nickname);
	}
	
	private void serverMulticast() throws IOException, InterruptedException {
		String command = new String();
		String msg = new String();
		String nick = new String();
		String value;
		String[] chunks;
		boolean locked;
		int l, c;
		
		while(true) {
			byte[] msgByte = new byte[1000];
			DatagramPacket msgDataIn = new DatagramPacket(msgByte, msgByte.length);
			mSocket.receive(msgDataIn);
			msg = new String(msgDataIn.getData(), 0, msgDataIn.getLength());
			System.out.println(msg);
			chunks = msg.split(" ");
			command = chunks[0];
			nick = chunks[1];
			
			if(command.equals("JOIN")) {
				String hostReceived = msgDataIn.getAddress().getHostAddress();
				if(!onlineMap.containsKey(nick)) {
					onlineMap.put(nick, hostReceived);
					if(nick.equals(this.nickname))
						this.sudokuFrame.appendMsg("Você entrou no jogo!\n");
					else {
						this.sudokuFrame.appendPlayer(nick);
						this.sudokuFrame.appendMsg(nick + " entrou no jogo!\n");
					}
				}
				udpChat.sendMessage(hostReceived, "JOINACK " + this.nickname);
				udpChat.sendMessage(hostReceived, "SUDOKU " + this.nickname + " " + matrixSudoku.toString());
			} else if(command.equals("MSG")) {
				msg = msg.substring(msg.indexOf(" "), msg.length()).trim();
				this.sudokuFrame.appendMsg(msg + "\n");
			} else if(command.equals("LOCKED")) {
				nick = chunks[1].trim();
				if(!nick.equals(this.nickname)) {
					l = Integer.parseInt(chunks[2].trim());
					c = Integer.parseInt(chunks[3].trim());
					locked = Boolean.parseBoolean(chunks[4].trim());
					this.sudokuFrame.setEditableCell(l, c, locked);;
				}
			} else if(command.equals("UPDATE")) {
				nick = chunks[1].trim();
				l = Integer.parseInt(chunks[2].trim());
				c = Integer.parseInt(chunks[3].trim());
				value = chunks[4].trim();
				
				int insert = Integer.parseInt(value);
				if(insert > 0 && insert < 10) {
					this.sudokuFrame.setTextCell(l, c, value);
					this.sudokuFrame.appendMsg(nick + ": matriz[" + l + "][" + c + "] = " + value + "\n");
				} else
					this.sudokuFrame.setTextCell(l, c, "");
				
				this.matrixSudoku.insertNumber(l, c, Integer.parseInt(value));
				
				if(!this.matrixSudoku.checkFinalGame()){
					if(!value.equals("0")){
						boolean checkSudoku = this.matrixSudoku.checkSudoku(l, c);
						
						sendMessage("VERIFY " + l + " " + c + " " + checkSudoku);
					}
				} else {
					TimeUnit.MICROSECONDS.sleep(1);
					sendMessage("FINISH " + this.nickname);
				}
				
			} else if(command.equals("VERIFY")){ 
				l = Integer.parseInt(chunks[1].trim());
				c = Integer.parseInt(chunks[2].trim());
				locked = Boolean.parseBoolean(chunks[3].trim());
				if(!locked){
					this.sudokuFrame.setForegroundCell(Color.RED, l, c);
				} else {
					this.sudokuFrame.setForegroundCell(Color.BLACK, l, c);
				}
			} else if(command.equals("LEAVE")) {
				msg = msg.split(" ")[1].trim();
				this.sudokuFrame.appendMsg(msg + " saiu do jogo!\n");
				this.sudokuFrame.removePlayer(msg);
			} else if(command.equals("FINISH")) {
				JOptionPane.showMessageDialog(this.sudokuFrame, "YOU WIN");
				System.exit(0);
			}
		}
	}

	private void sendMessage(String message) throws IOException {
		byte[] msgByte = message.getBytes();
		DatagramPacket msgDataOut = new DatagramPacket(msgByte,  msgByte.length, this.group, this.port);
		mSocket.send(msgDataOut);
	}
	
	public void sendMessageFrame(String message) throws IOException {
		sendMessage(message);
	}
	
	public String getNickname() {
		return nickname;
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		String nick = new String();
		while(nick.trim().isEmpty())
			nick = JOptionPane.showInputDialog("Enter your nickname:");
		new Multicast(nick);
	}
}
