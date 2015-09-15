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

import java.io.File;
import java.io.FileNotFoundException;

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
	
	private static Scanner sc = new Scanner(System.in);
	
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
	private static final String MESSAGE_INVALID_LINE_NUMBER = "Invalid line number!";
	private static final String MESSAGE_LINE_NOT_FOUND = "No such line in file!";
	private static final String MESSAGE_NO_FILE_INPUT = "No .txt file inputed.";
	private static final String MESSAGE_IOEXCEPTION = "IO exception encountered.";
	private static final String MESSAGE_SORTED = null;
	
	public static void main(String[] args) throws IOException {
		
		try {
			TextBuddy tb = new TextBuddy(args[0]);
			showToUser(String.format(MESSAGE_WELCOME, args[0]));
			while (true) {
				showToUser(MESSAGE_ENTER_COMMAND);
				String command = sc.next();
				String feedback = tb.executeCommand(command);
				showToUser(feedback);
			}
		}

		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(MESSAGE_NO_FILE_INPUT);
		}	
	}

	/* Constructor */
		
	public TextBuddy(String fileName) {
		file_ = initialiseFile(fileName);
		listOfLines_ = new ArrayList<String>();
		sc = new Scanner(System.in);
	}
	
	/* Utility Methods */
	
	public static void showToUser(String text) {
		System.out.print(text);
	}
	
	/**
	 * Initialise a new file instance and create an actual file
	 * given the input pathname
	 * 
	 * @param pathName	pathname of the file to be created
	 * @return File object created with the input pathName
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
	 * @return feedback message that line is added
	 * @throws IOException 
	 */
	private String add(String text) throws IOException {
		listOfLines_.add(text);
		
			PrintWriter pw = new PrintWriter(file_);
			for (int i = 0; i < listOfLines_.size(); i++) {
				pw.println(listOfLines_.get(i));
			}
			pw.close();
			
		return String.format(MESSAGE_ADDED, file_.getName(), text);
	}
	
	/** 
	 * Delete a specific line from the .txt file.
	 * 
	 * @param lineNumber 	Number of the line to be removed.
	 * @return feedback message that line is deleted.
	 * @throws InputMismatchException
	 * @throws FileNotFoundException 
	 */
	private String delete(int lineNumber) throws InputMismatchException, FileNotFoundException {
			
			// delete line from internal list first
			if (lineNumber >= listOfLines_.size()) {
				throw new IndexOutOfBoundsException(MESSAGE_LINE_NOT_FOUND);
			}
			
			String lineToDelete = listOfLines_.get(lineNumber - 1);
			listOfLines_.remove(lineNumber - 1);	
			
			// Overwrite file entirely by rewriting the updated list of lines into the file

			PrintWriter pw = new PrintWriter(file_);
			
			for (int i = 0; i < listOfLines_.size(); i++) {
				pw.println(listOfLines_.get(i));
			}
			pw.close();			
			return String.format(MESSAGE_DELETED, file_.getName(), lineToDelete);
	}
	
	/**
	 * Clear the txt file of its contents entirely.
	 * 
	 * @return feedback message that text file is cleared.
	 * @throws IOException 
	 */	
	private String clear() throws IOException {
		
		// clear internal list first
		listOfLines_.clear();
		
		// Use PrintWriter object to initiate a rewrite on the file, 
		// effectively clearing it of its original content
		PrintWriter pw = new PrintWriter(file_);
		return String.format(MESSAGE_CLEAR, file_.getName());
	}
	
	/**
	 * Prints out the contents in the txt file, in which every line is numbered.
	 * If file is empty, user will be notified.
	 * 
	 * @return indexed form of lines in the text file 
	 */
	private String display() {
		int noOfLines = listOfLines_.size();
		StringBuilder sb = new StringBuilder();
		
		if (noOfLines > 0) {
			for (int i = 0; i < noOfLines; i++) {
				int index = i + 1;
				sb.append(String.format(MESSAGE_DISPLAY_LINE, index, listOfLines_.get(i)));
			}
		} else {
			sb.append(String.format(MESSAGE_DISPLAY_FAIL, file_.getName()));
		}
		
		return sb.toString();
	}
	
	/**
	 * Sorts the text file in alphabetical order according to the first word of every line
	 * 
	 * @return feedback message that lines are sorted
	 */
	public String sort() {
		return "";
		//return String.format(MESSAGE_SORTED, file_.getName());
	}
	
	/**
	 * Searches the text file for lines that contains the input word, and return those lines.
	 * 
	 * @param word
	 * @return lines in text file that contain the input word
	 */
	public String search(String word) {
		return "";
	}
	
	       	
    /**
     * executes the respective functions according to the input command
     * 
     * @param command 	command input by user into the console
     * @throws IOException 
     */
	String executeCommand(String command) throws IOException {
		
		String feedback = null;

		if (command.equals(COMMAND_ADD)) {
			String addParam = sc.nextLine();
			feedback = add(addParam.trim()); // trim is used to remove white space in front of the text to be added				
		} else if (command.equals(COMMAND_DELETE)) {
			try {
				feedback = delete(sc.nextInt());
			}
			catch (InputMismatchException e) {
				showToUser(MESSAGE_INVALID_LINE_NUMBER);
			}
		} else if (command.equals(COMMAND_CLEAR)) {
			feedback = clear();
		} else if (command.equals(COMMAND_DISPLAY)) {
			feedback = display();
		} else if (command.equals(COMMAND_EXIT)) {
			System.exit(0);
		} else {
			throw new Error("Unrecognized command type");
		}
		return feedback;
    }   		
}
