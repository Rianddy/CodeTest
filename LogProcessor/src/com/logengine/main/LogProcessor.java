package com.logengine.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Question: Implement a log processor in Java/Scala which does the following. *
 * The process takes the path to a text log file and remote host:port as the
 * input. Each line in the log file is an "event". - e.g. ./log_processor
 * /var/tmp/logs/test.log host1.example.com:6780 * When the process is started
 * it reads the log file and sends each event to a TCP/IP port on a remote host
 * or localhost. * If the process is restarted, it skips all the events that was
 * already sent and only processes the remaining events. * While the process is
 * running, if a new event is added to the log file that event is processed
 * automatically (i.e. no process restart is required). If there are unstated
 * requirements, make your own assumptions. But, clearly state your assumptions.
 * In fact, clearly state any and all assumptions you make. Also, write unit
 * tests for your implementation. Assume that the file can grow up to 10 GB.
 * Bonus points for handling log rotation.
 * 
 * 
 * 
 * Solution: I implemented a log processor based on just one log file. When
 * insert an "event" into the log file, the log processor will judge the size.
 * And it archives the content that was already read into a zip file. Then it
 * saves the content that has not been read yet. This kind of mechanism is a
 * simple log rotation.
 * 
 * @author rianddy
 *
 */
public class LogProcessor {
	private String logFilePath;
	private String remoteAddress;

	// Singleton design
	private static LogProcessor logProcessor = null;

	// Max bound for log rotation
	private final double MAX_SIZE = 1024 * 1024 * 1024;

	// Record current position for reading event
	private long position = 0;

	// Store current size of the log file
	private double logFileSize;

	// Store records of all log files
	private HashMap<String, Long> logRowMap;

	// Used to create an identifier with logFilePath and remoteAddress
	private final String split = "##";
	private String identifier;

	// Output parameters are used to log positions of the content that was
	// already read and new archive file for log rotation
	private final String outputPath = "";
	private final String logRowName = outputPath + "row.txt";
	private final String archiveFileName = outputPath + "archive.zip";

	public static LogProcessor getInstance(String logFilePath,
			String remoteAddress) {
		if (logProcessor == null) {
			logProcessor = new LogProcessor(logFilePath, remoteAddress);
		}
		return logProcessor;
	}

	private LogProcessor(String logFilePath, String remoteAddress) {
		this.logFilePath = logFilePath;
		this.remoteAddress = remoteAddress;
		File curLogFile = new File(logFilePath);
		this.logFileSize = curLogFile.length();
		identifier = logFilePath + split + remoteAddress;

		// Generate the record file of positions.
		try {
			File file = new File(logRowName);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				lookupLogRowFile(identifier);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		readEventAndSendEvent();
	}

	/**
	 * Read each line's "event"
	 */
	private void readEventAndSendEvent() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(logFilePath)));
			reader.skip(position);
			String curEvent = reader.readLine();
			while (curEvent != null) {
				// Assume each line end with \n and record position
				position += curEvent.length() + 1;
				logRowMap.put(identifier, position);
				writeLogRowFile();

				sendEventToAddress(curEvent);
				curEvent = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send "event" over java.net
	 * 
	 * @param event
	 */
	private void sendEventToAddress(String event) {
		String[] addressparts = remoteAddress.split(":");
		try {
			InetAddress host = InetAddress.getByName(addressparts[0]);
			int serverPort = Integer.parseInt(addressparts[1]);

			Socket socket = new Socket(host, serverPort);
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(),
					true);
			BufferedReader fromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			toServer.println(event);

			// TODO: do something about received message from server

			toServer.close();
			fromServer.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add "event" to current log file. If the size will be larger than MAX
	 * limitation. It will call log rotation.
	 * 
	 * @param newEvent
	 * @return
	 */
	public boolean addEvent(String newEvent) {
		try {
			double curEventSize = newEvent.getBytes().length;
			double totalSize = logFileSize + curEventSize;

			BufferedWriter output;
			if (totalSize <= MAX_SIZE) {
				logFileSize = totalSize;
				output = new BufferedWriter(new FileWriter(logFilePath, true));
			} else {
				if (logRotation())
					output = new BufferedWriter(new FileWriter(logFilePath,
							false));
				else
					return false;
			}
			output.append(newEvent);
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * For this processor to achieve simple log rotation based on size. Achieve
	 * the content that was already been read and save the content that has not
	 * been read yet.
	 * 
	 * @return
	 */
	private boolean logRotation() {
		try {
			if (position == 0)
				return false;
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(logFilePath)));
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					archiveFileName));
			out.putNextEntry(new ZipEntry(identifier));
			int count = 0;
			String curLine = in.readLine();
			while (curLine != null || count < position) {
				out.write(curLine.getBytes());
				curLine = in.readLine();
				count++;
			}
			out.close();

			BufferedWriter output = new BufferedWriter(new FileWriter(
					logFilePath, false));
			while (curLine != null) {
				output.write(curLine);
				curLine = in.readLine();
			}

			logRowMap.put(identifier, (long) 0);
			writeLogRowFile();
			logFileSize = 0;
			position = 0;

			output.close();
			in.close();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Load the records for positions
	 * 
	 * @param identifier
	 */
	private void lookupLogRowFile(String identifier) {
		try {
			BufferedReader numOfRowReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(logRowName)));
			String curLine = numOfRowReader.readLine();
			logRowMap = new HashMap<String, Long>();
			while (curLine != null) {
				String[] mapElement = curLine.split(" ");
				if (curLine == identifier) {
					position = Long.parseLong(mapElement[1]);
				}
				logRowMap.put(mapElement[0], Long.parseLong(mapElement[1]));
				curLine = numOfRowReader.readLine();
			}
			numOfRowReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rewrite the record file for positions
	 * 
	 */
	private void writeLogRowFile() {
		try {
			BufferedWriter numOfRowWritter = new BufferedWriter(new FileWriter(
					logRowName, false));
			for (String key : logRowMap.keySet()) {
				numOfRowWritter.write(key + " " + logRowMap.get(key));
				numOfRowWritter.newLine();
			}
			numOfRowWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
