
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SpellChecker implements iSpellCorrector{

	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions.
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	public Trie myTrie = new Trie();
	public Trie othertrie = new Trie();

	SpellChecker() {
		
	}
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner sc = new Scanner(new File(dictionaryFileName));

		/*while (sc.hasNext()) {
			String currentWord = sc.next();
			currentWord = currentWord.toLowerCase();
			
			myTrie.add(currentWord);
		}*/
		//myTrie.print();
		/*for (int i = 0; i < 100; ++i) {
			String currentWord = sc.next();
			currentWord = currentWord.toLowerCase();
			
			myTrie.add(currentWord);
			othertrie.add(currentWord);
		}*/
		myTrie.add("of");
		myTrie.add("of");
		myTrie.add("of");
		myTrie.add("of");
		othertrie.add("of"); //hmmmm

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
		System.out.println(myTrie.equals(othertrie));
		return s;
		
	}

}
