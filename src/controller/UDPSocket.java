package controller;
/*
 * Sistemas Distribuídos
 * Profº. Rodrigo Campiolo
 * Emerson Yudi Nakashima 1451600
 * Gustavo Correia Gonzalez 1551787
 * Implementação do socket UDP
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import model.MatrixSudoku;
import view.SudokuFrame;

public class UDPSocket {
	
	private final int serverPort;
	private DatagramSocket serverSocket;
	private HashMap<String, String> onlineMap;
	private SudokuFrame sudokuFrame;
	private MatrixSudoku matrixSudoku;
	
	public UDPSocket(String nick, SudokuFrame sudokuFrame, HashMap<String, String> onlineMap, MatrixSudoku matrixSudoku) throws InterruptedException {
		
		this.serverPort = 6799;
		this.onlineMap = onlineMap;
		this.sudokuFrame = sudokuFrame;
		this.matrixSudoku = matrixSudoku;
		
		Runnable serverRunnable = new Runnable() {
			public void run() {
				try {
					UDPServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread serverThread = new Thread(serverRunnable);
		serverThread.start();
	}
	
	private void UDPClient(String host, String message) throws IOException {
		
		DatagramSocket clientSocket = null;
		byte mClient[];

		clientSocket = new DatagramSocket();
		
		mClient = message.getBytes();
		
		InetAddress aHost = InetAddress.getByName(host);
		DatagramPacket request = new DatagramPacket(mClient, mClient.length, aHost, this.serverPort);
		
		clientSocket.send(request);
		
        clientSocket.close();
	}
	
	private void UDPServer() throws IOException {
		
		serverSocket = new DatagramSocket(this.serverPort);
		String nick = new String();
		String message = new String();
		String command = new String();
		
		while(true) {
			byte[] buffer = new byte[1000];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			serverSocket.receive(request);
			
			String[] chunks = new String(request.getData()).split(" ");
			command = chunks[0].trim();
			if(command.equals("JOINACK")) {			
				nick = chunks[1].trim();
				if(!onlineMap.containsKey(nick)) {
					onlineMap.put(nick, request.getAddress().getHostAddress());
					this.sudokuFrame.appendPlayer(nick);
				}
			} else if(command.equals("SUDOKU")) {
				nick = chunks[1].trim();
				message = chunks[2].trim();
				parseMatrix(message);
				sudokuFrame.insertMatrix(matrixSudoku.getMatrix());
			}
		}
	}

	public void sendMessage(String host, String message) throws IOException {
		UDPClient(host, message);
	}
	
	private void parseMatrix(String receivedMatrix) {
		int[][] matrix = new int[9][9];
		int p = 0;
		for(int l = 0; l < 9; l++) {
			for(int c = 0; c < 9; c++) {
				matrix[l][c] = Integer.parseInt(receivedMatrix.substring(p, p + 1));
				p++;
			}
		}
		this.matrixSudoku.setMatrix(matrix);
	}

	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

}
