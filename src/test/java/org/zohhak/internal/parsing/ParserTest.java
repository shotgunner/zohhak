package org.zohhak.internal.parsing;

import static org.fest.assertions.api.Assertions.assertThat;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.zohhak.TestWith;
import org.zohhak.internal.parsing.Parser;
import org.zohhak.runners.MintakaRunner;

@RunWith(MintakaRunner.class)
public class ParserTest {

	private static final String QUOTE = "'";
	private static final String COMMA = ",";
	Parser parser = new Parser(); 
	
	@Test
	public void testSplitOneElement() {
		String[] splited = parser.split(" a ", COMMA, QUOTE);
		assertThat(splited).containsSequence("a").hasSize(1);
	}
	
	@Test
	public void testSplitMoreElements() {
		String[] splited = parser.split(" a , b ", COMMA, QUOTE);
		assertThat(splited).containsSequence("a", "b").hasSize(2);
	}
	
	@Test
	public void testOneQuoted() {
		String[] splited = parser.split(" ' a ' ", COMMA, QUOTE);
		assertThat(splited).containsSequence(" a ").hasSize(1);
	}
	
	@Test
	public void testTwoQuoted() {
		String[] splited = parser.split(" ' a ', ' b' ", COMMA, QUOTE);
		assertThat(splited).containsSequence(" a ", " b").hasSize(2);
	}
	
	@TestWith({
		"aba,	b",
		"ba,	b",
		"aabaa,	aba",
		"b,		b",
		"a,		",
		" , ",
		","
	})
	public void removeEdgesTest(String input, String trimmed) {
		String withoutEdges = parser.removeEdges(input, "a");
		assertThat(withoutEdges).isEqualTo(trimmed);
	}
	
}