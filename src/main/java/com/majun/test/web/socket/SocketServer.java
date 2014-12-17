package com.majun.test.web.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	private ServerSocket ss;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public SocketServer() throws IOException {
		try {
			ss = new ServerSocket(10000);
			while (true) {
				socket = ss.accept();
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				String line = in.readLine();
				out.println("you input is :" + line);
				out.close();
				in.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			ss.close();
		}
	}

	public static void main(String[] args) throws IOException {
		new SocketServer();
	}

}
