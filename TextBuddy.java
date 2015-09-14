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

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextBuddy {
	
	private String fileName_;
	private File file_;
	private ArrayList<String> listOfLines_;
	private boolean isRunning_;
	
	public TextBuddy(String fileName) {
		fileName_ = fileName;
		file_ = new File(fileName);
		listOfLines_ = new ArrayList<String>();
		isRunning_ = true;
	}
	
	private void setFile(File newFile) {
		file_ = newFile;	
	}
	
	/** 
	 * Adds the input line of text into the .txt file.
	 * 
	 * @param text	Line of text to be added 
	 */
	private void add(String text) {
		listOfLines_.add(text);
		FileWriter fw;
		
		try {
			fw = new FileWriter(file_);
			fw.write(text);
			fw.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("added to " + fileName_
							+ ": \"" + text + "\"");
	}
	
	/** 
	 * Delete a specific line from the .txt file.
	 * 
	 * @param lineNumber 	Number of the line to be removed.
	 */
	private void delete(int lineNumber) {
				
		try {
			
			String lineToDelete = listOfLines_.get(lineNumber - 1);
			listOfLines_.remove(lineNumber - 1);
			File tempFile = new File(file_.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file_));
			FileWriter fw = new FileWriter(tempFile);

			String currLine = null;

			// read from original file and write to temp file
			// unless line matches line to be removed.
			while ((currLine = br.readLine()) != null) {

				if (!currLine.trim().equals(lineToDelete)) {
					fw.write(currLine);
				}
			}
			
			fw.close();
			br.close();

			//Replace original with the new file.

			file_.delete();
			setFile(tempFile);
			
			
			System.out.println("deleted from " + fileName_
								+ ": \"" + lineToDelete + "\"");
		}
		
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		catch (InputMismatchException ex) {
			System.out.println("Invalid line number!");
			ex.printStackTrace();
		}
		
		catch (IndexOutOfBoundsException ex) {
			System.out.println("No such line in file!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Clear the txt file of its contents entirely.
	 */	
	private void clear() {
		
		// Create new file
		File tempFile = new File(file_.getAbsolutePath() + ".tmp");
		
		// Delete original file and rename new file
		
		tempFile.renameTo(file_);
		file_.delete();
		setFile(tempFile);
		
		listOfLines_.clear();
		
		System.out.println("all content deleted from " + fileName_);
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
				System.out.println(index + ". " + listOfLines_.get(i));
			}
		} else {
			System.out.println(fileName_ + " is empty");
		}
	}
	
	/**
	 * Reads in and execute respective commands until exit command is called.
	 */
	public void run() {
		
		System.out.println("Welcome to TextBuddy. " + fileName_ + " is ready for use");
		Scanner sc = new Scanner(System.in);
		
        while (isRunning_) {
        	
        	System.out.print("command: ");
        	String command = sc.next();
        	
        	// executes the respective functions according to the inputed command
        	if (command.equals("add")) {
        		String addParam = sc.nextLine();
					add(addParam.trim()); // trim is used to remove white space in front of the text to be added				
        	} else if (command.equals("delete")) {
        		delete(sc.nextInt());
        	} else if (command.equals("clear")) {
        		clear();
        	} else if (command.equals("display")) {
        		display();
        	} else if (command.equals("exit")) {
        		isRunning_ = false;
        	} else {
        		System.out.println("Invalid Command!");
        	}
        }   	
	}
	
	public static void main(String[] args) {
		
		try {
		TextBuddy tb = new TextBuddy(args[0]);
		tb.run();
		} 
		
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("No .txt file inputed.");
			e.printStackTrace();
		}
		
	}
	
		
}
