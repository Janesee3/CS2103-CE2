/**
 * TextBuddy++
 * 
 * This class is used to write, read or delete lines of text in a specified
 * .txt file. There is no limit to the number of lines that can be added.
 * The command format is given by the example interaction below:
 
	c:>java  TextBuddy mytextfile.txt
	Welcome to TextBuddy. mytextfile.txt is ready for use
	command: add little brown fox
	added to mytextfile.txt: “little brown fox”
	command: display
	1. little brown fox
	command: add jumped over the moon
	added to mytextfile.txt: “jumped over the moon”
	command: display
	1. little brown fox
	2. jumped over the moon
	command: delete 2
	deleted from mytextfile.txt: “jumped over the moon”
	command: display
	1. little brown fox
	command: clear
	all content deleted from mytextfile.txt
	command: display
	mytextfile.txt is empty
	command: exit
			 
* @author See Loo Jane
*/

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextBuddy {
	
	/* Private Class Attributes */
	
	//private String fileName_;
	private File file_;
	private ArrayList<String> listOfLines_;
	private boolean isRunning_;
	
	/* Static Utility */
	
	private static Scanner sc;
	
	/* Static String Constants */
	
	// Command Feedback Messages
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MESSAGE_ENTER_COMMAND = "command: "; 
	private static final String MESSAGE_ADDED = "added to %s: \"%s\"\n";
	private static final String MESSAGE_DELETED = "deleted from %s: \"%s\"\n";
	private static final String MESSAGE_CLEAR = "all content deleted from %s\n";
	private static final String MESSAGE_DISPLAY_LINE = "%d. %s\n";
	private static final String MESSAGE_DISPLAY_FAIL = "%s is empty\n";
	
	
	// Command Identifiers
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_EXIT = "exit";
	
	
	// Error/Exception Messages
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command!";
	private static final String MESSAGE_INVALID_LINE_NUMBER = "Invalid line number!";
	private static final String MESSAGE_LINE_NOT_FOUND = "No such line in file!";
	private static final String MESSAGE_NO_FILE_INPUT = "No .txt file inputed.";
	private static final String MESSAGE_IOEXCEPTION = "IO exception encountered.";
	private static final String MESSAGE_FILE_NOT_FOUND = "File is missing!";
	
	
	/* Constructor */
		
	public TextBuddy(String fileName) {
		//fileName_ = fileName;
		file_ = initialiseFile(fileName);
		listOfLines_ = new ArrayList<String>();
		isRunning_ = true;
		sc = new Scanner(System.in);
	}
	
	/* Ultily Methods */
	
	/**
	 * Initialise a new file instance and create an actual file
	 * given the input pathname
	 * 
	 * @param pathName	pathname of the file to be created
	 */
	private File initialiseFile(String pathName) {
		File file = new File(pathName);
		
		try {
			file.createNewFile();
		} 
		
		catch (IOException e) {
			System.out.println(MESSAGE_IOEXCEPTION);
		}
		
		return file;
	}
	
	/* Command Methods */

	/** 
	 * Adds the input line of text into the .txt file.
	 * 
	 * @param text	Line of text to be added 
	 */
	private void add(String text) {
		listOfLines_.add(text);
		
		try {
			PrintWriter pw = new PrintWriter(file_);
			for (int i = 0; i < listOfLines_.size(); i++) {
				pw.println(listOfLines_.get(i));
			}
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(MESSAGE_IOEXCEPTION);
		}
			
		System.out.printf(MESSAGE_ADDED, file_.getName(), text);
	}
	
	/** 
	 * Delete a specific line from the .txt file.
	 * 
	 * @param lineNumber 	Number of the line to be removed.
	 */
	private void delete(int lineNumber) {
		
		try {			
			// delete line from internal list first
			String lineToDelete = listOfLines_.get(lineNumber - 1);
			listOfLines_.remove(lineNumber - 1);	

			// Overwrite file entirely by rewriting the updated list of lines into the file

			PrintWriter pw = new PrintWriter(file_);
			
			for (int i = 0; i < listOfLines_.size(); i++) {
				pw.println(listOfLines_.get(i));
			}
			pw.close();
			System.out.printf(MESSAGE_DELETED, file_.getName(), lineToDelete);
		
		}
		
		catch (FileNotFoundException ex) {
			System.out.println(MESSAGE_FILE_NOT_FOUND);
		}
		
		catch (InputMismatchException ex) {
			System.out.println(MESSAGE_INVALID_LINE_NUMBER);
		}
		
		catch (IndexOutOfBoundsException ex) {
			System.out.println(MESSAGE_LINE_NOT_FOUND);
		}
	}
	
	/**
	 * Clear the txt file of its contents entirely.
	 */	
	private void clear() {
		
		// clear internal list first
		listOfLines_.clear();
		
		// Use PrintWriter object to initiate a rewrite on the file, 
		// effectively clearing it of its original content
		try {
			PrintWriter pw = new PrintWriter(file_);
		} catch (FileNotFoundException e) {
			System.out.println(MESSAGE_IOEXCEPTION);
		}

		System.out.printf(MESSAGE_CLEAR, file_.getName());
	}
	
	/**
	 * Prints out the contents in the txt file, in which every line is numbered.
	 * If file is empty, user will be notified.
	 */
	private void display() {
		int noOfLines = listOfLines_.size();
		
		if (noOfLines > 0) {
			for (int i = 0; i < noOfLines; i++) {
				int index = i + 1;
				System.out.printf(MESSAGE_DISPLAY_LINE, index, listOfLines_.get(i));
			}
		} else {
			System.out.printf(MESSAGE_DISPLAY_FAIL, file_.getName());
		}
	}
	
	
	/* Execution Methods */
	
	/**
	 * Reads in and execute respective commands until exit command is called.
	 */
	public void run() {
		
		System.out.printf(MESSAGE_WELCOME, file_.getName());
				
        while (isRunning_) {      	
        	System.out.printf(MESSAGE_ENTER_COMMAND);
        	String command = sc.next();
        	executeCommand(command);
        }
	}
        	
        	
    /**
     * executes the respective functions according to the input command
     * 
     *    @param command 	command input by user into the console
     */
	private void executeCommand(String command) {
		
		if (command.equals(COMMAND_ADD)) {
    		String addParam = sc.nextLine();
			add(addParam.trim()); // trim is used to remove white space in front of the text to be added				
    	} else if (command.equals(COMMAND_DELETE)) {
    		delete(sc.nextInt());
    	} else if (command.equals(COMMAND_CLEAR)) {
    		clear();
    	} else if (command.equals(COMMAND_DISPLAY)) {
    		display();
    	} else if (command.equals(COMMAND_EXIT)) {
    		isRunning_ = false;
    	} else {
    		System.out.println(MESSAGE_INVALID_COMMAND);
    	}
    }   	
		

	public static void main(String[] args) {
		
		try {
		TextBuddy tb = new TextBuddy(args[0]);
		tb.run();
		} 
		
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(MESSAGE_NO_FILE_INPUT);
		}
		
	}	
}
