package GedcomParse;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ListLargeAgeDifferencesTest {

	@Test
	void test() 
	{
		ListLargeAgeDifferences test = new ListLargeAgeDifferences();
		String test1 = test.birthDateComparison(30, 15);
		assertEquals("Wife Too Old!", test1);
		
		String test2 = test.birthDateComparison(15, 30);
		assertEquals("Hus Too Old!", test2);
		
		String test3 = test.birthDateComparison(15, 28);
		assertEquals("", test3);
		
		String test4 = test.birthDateComparison(25, 22);
		assertEquals("", test4);
		
		String test5 = test.birthDateComparison(15, 50);
		assertEquals("Hus Too Old!", test5);
	}

}