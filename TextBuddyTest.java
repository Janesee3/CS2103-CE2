import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TextBuddyTest {
	
	
	@Test
	public void testSort() throws IOException {
			
		/* sorting multiple lines (not initially in alphabetical order) in text file */
		/* 
		 * 1. c             1. a
		 * 2. a      to     2. b
		 * 3. b             3. c    
		 */
		TextBuddy tb1 = new TextBuddy("testSort1.txt");
		
		tb1.executeCommand("add c");
		tb1.executeCommand("add a");
		tb1.executeCommand("add b");
		tb1.executeCommand("sort");
		String expected1 = "1. a\n" + "2. b\n" + "3. c\n";
		assertEquals(expected1, tb1.executeCommand("display"));
	
		/* sort text file with only a single line */
		TextBuddy tb2 = new TextBuddy("testSort2.txt");
		
		tb2.executeCommand("add a");
		tb2.executeCommand("sort");
		String expected2 = "1. a\n";
		assertEquals(expected2, tb2.executeCommand("display"));
		
		/* sort empty text file */
		TextBuddy tb3 = new TextBuddy("testSort3.txt");
		
		tb3.executeCommand("sort");
		String expected3 = "testSort3.txt is empty\n";
		assertEquals(expected3, tb3.executeCommand("display"));
		
		/* test feedback message */
		String expected4 = "testSort3.txt has been sorted\n";
		assertEquals(expected4, tb3.executeCommand("sort"));
	}
}
	
