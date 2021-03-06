package spell;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SpellChecker implements ISpellCorrector{

	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions.
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	
	public Trie myTrie = new Trie();
	//public Trie otherTrie = new Trie();
	public SpellChecker() {
		
	}
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner sc = new Scanner(new File(dictionaryFileName));

		while (sc.hasNext()) {
			String currentWord = sc.next();
			currentWord = currentWord.toLowerCase();
			
			myTrie.add(currentWord);
			//otherTrie.add(currentWord);
		}
		
		//otherTrie.rootNode = null;
		sc.close();
	}

	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion or null if there is no similar word in the dictionary
	 */
	public String suggestSimilarWord(String inputWord) {
		String s = myTrie.checkWord(inputWord);  
		//System.out.println(myTrie.toString());
		//System.out.println(myTrie.equals(otherTrie));
		return s;
		
	}
	/*My worst fear is that it doesn't return the correct word. 
	 * 
	 * 
	 */

}
