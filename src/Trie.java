import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Your trie class should implement the ITrie interface
 */
public class Trie implements ITrie {

	
	public Node rootNode = new Node();
	public int wordCount = 0;
	public int nodeCount = 1;
	public int modCounter = 0;
	public StringBuilder wordList = new StringBuilder();

	public void add(String word) {
		rootNode.addToTrie( word);
		
	}
	
	//NODE CLASS
	public class Node implements ITrie.INode{
		public int count = 0;
		public Node[] nodeArray = new Node[26];
		Node() {
			
		}
		
		public boolean addToTrie(String currentWord) {
			char c = currentWord.charAt(0);
			if (nodeArray[c - 'a'] == null) {
				nodeArray[c - 'a'] = new Node();
				++nodeCount;
			}
			if (currentWord.length() > 1) {
				nodeArray[c - 'a'].addToTrie(currentWord.substring(1));
			}
			else {
				++nodeArray[c - 'a'].count;
				if (nodeArray[c - 'a'].count == 1) {
					++wordCount;
				}
			}
			
			return true;			
		}
		@Override
		public int getValue() {
			return count;
		}
		public void print() {
	
			//System.out.println("Children:");
			for (int i = 0; i < nodeArray.length; ++i) {
				if (nodeArray[i] != null) {
					//System.out.println(toChar(i) + " ");
				}
			}
			for (int i = 0; i < nodeArray.length; ++i) {
				if (nodeArray[i] != null) {
					//System.out.println("Parent: " + toChar(i));
					nodeArray[i].print();
				}
			}
			
		}
		public boolean isInTrie(String word) { 
			if (word.equals("")) {
				return false;
			}
			char c = word.charAt(0);
			if (nodeArray[c - 'a'] == null) {
				return false;
			}
			else {
				if (word.length() > 1) {
					return nodeArray[c - 'a'].isInTrie(word.substring(1));
				}
				else {
					if (nodeArray[c - 'a'].getValue() > 0) {
					return true;
					}
					else {
						return false;
					}
				}
			}
		}
		public int getWordCount(String word) {
			if (word.equals("")) {
				return 0;
			}
			char c = word.charAt(0);
			if (nodeArray[c - 'a'] == null) {
				return 0;
			}
			else {
				if (word.length() > 1) {
					return nodeArray[c - 'a'].getWordCount(word.substring(1));
				}
				else {
					return nodeArray[c - 'a'].getValue();
				}
			}
		}
		public void toString(StringBuilder str) {
			for (int i = 0; i < nodeArray.length; ++i) {
				StringBuilder currentWord = new StringBuilder(str); 
				if (nodeArray[i] != null) { 
					currentWord.append(toChar(i)); //add character to string 
					if (nodeArray[i].count < 1) { //if it's not the end of a word then recurse
						nodeArray[i].toString(currentWord);
					}
					else {
						wordList.append(currentWord); //add string to the word list, and then add a new line
						wordList.append("\n");
						boolean isEndOfBranch = true;
						for (int j = 0; j < nodeArray.length; ++j) { //if there are any more nodes, keep recursing. 
							if (nodeArray[i].nodeArray[j] != null) {
								isEndOfBranch = false;
							}
						}
						if (!isEndOfBranch) {
							nodeArray[i].toString(currentWord);
						}
					}
				}
			}
			return;
		}
		public boolean compare(Node compareNode) { 
			if (count != compareNode.count) { //if the counts are different
				return false;
			}
			boolean isEndOfPath = true;
			for (int i = 0; i < nodeArray.length; ++i) {
				if (nodeArray[i] != null) {
					isEndOfPath = false;
					if (compareNode.nodeArray[i] == null) { //if current has a node and compare doesn't
						return false;
					}
				}
				if (compareNode.nodeArray[i] != null) { //if compare has a node and current doesn't
					isEndOfPath = false;
					if (nodeArray[i] == null) {
						return false;
					}
					else {//if they both have a node in that spot
						System.out.println("testing if " + nodeArray[i].count + " and " + compareNode.nodeArray[i].count + " are different");
						if (nodeArray[i].count == compareNode.nodeArray[i].count) {
							System.out.println(nodeArray[i].count + " and " + compareNode.nodeArray[i].count + " are the same");
							nodeArray[i].compare(compareNode.nodeArray[i]);
						}
						else {
							return false;
						}
					}
				}
			}
			if (isEndOfPath) {
				return true;
			}
			
			return true;
		}
		/*Look at current node and compare node
		 * if counts are different, return false
		 * iterate through the arrays
		 * 		if there's a node in one and not the other, return false
		 * 		if there's a node in both, call one with the comparenode one as a parameter
		 * 
		 * 
		 * 
		 */
	}
	
	public void checkWordCount(String word) {
		//System.out.println(rootNode.getWordCount(word) + " instances of " + word);
	}

	public void print() { 
		//rootNode.print();
		System.out.println("words: " + wordCount);
		System.out.println("Nodes: " + nodeCount);
	}
	
	char toChar(int arrayElement) {
		char c = (char) (arrayElement + 'a');
		return c;
	}
	
	public int getWordCount() {
		return wordCount;
		
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	/**
	 * The toString specification is as follows:
	 * For each word, in alphabetical order:
	 * <word>\n
	 */
	
	public String checkWord(String inWord) { //this function is horribly overlarge and I shan't fix it
		inWord = inWord.toLowerCase();
		String returnWord = new String();
		if (rootNode.isInTrie(inWord)) {
			returnWord = inWord;
		}
		else {
			//IMPLEMENT SPELL CHECKING HERE
			ArrayList<String> modifications = new ArrayList<String>(); //make an array of all the possible modifications to the input word
			modifications.addAll(deletionMod(inWord));
			modifications.addAll(insertionMod(inWord));
			modifications.addAll(transpositionMod(inWord));
			modifications.addAll(alterationMod(inWord));
			
			
			Set<String> validModWords = new HashSet<String>(); //check if the modifications make a word that is found in the trie. If so, add to a valid word list. 
			for (int i = 0; i < modifications.size(); ++i) { 
				if (rootNode.isInTrie(modifications.get(i))) {
					validModWords.add(modifications.get(i));
				} 
			}
			
			//System.out.println("Valid modifications for " + inWord + ": " + validModWords);
			
			if (validModWords.size() > 0) { //if there are any valid words, find the best one
				returnWord = getBestAlternative(validModWords);
			}
			else {
				ArrayList<String> secondDistModifications = new ArrayList<String>(); //if there aren't, then implement a second edit distance
				for (int i = 0; i < modifications.size(); ++i) { //apply every modification to each modified word in the array
					secondDistModifications.addAll(deletionMod(modifications.get(i))); 
					secondDistModifications.addAll(insertionMod(modifications.get(i)));
					secondDistModifications.addAll(transpositionMod(modifications.get(i)));
					secondDistModifications.addAll(alterationMod(modifications.get(i)));
					
				}
				for (int i = 0; i < secondDistModifications.size(); ++i) { //check if the second distance modifications make a word found in the trie. If so, add to valid word list
					if (rootNode.isInTrie(secondDistModifications.get(i))) {
						validModWords.add(secondDistModifications.get(i));
					}
				}
				//System.out.println("Valid second distance modifications for " + inWord + ": " + validModWords);
				
				if (validModWords.size() > 0) { //if there are any valid words, find the best one
					returnWord = getBestAlternative(validModWords);
				}
				else {
					returnWord = null;
				}
			}
			
		}
		return returnWord;
	}
	
	public void test(String inWord) {
		ArrayList<String> modifications = new ArrayList<String>(); //make an array of all the possible modifications to the input word
		modifications.addAll(deletionMod(inWord));
		modifications.addAll(insertionMod(inWord));
		modifications.addAll(transpositionMod(inWord));
		modifications.addAll(alterationMod(inWord));
		Set<String> validModWords = new HashSet<String>(); //check if the modifications make a word that is found in the trie. If so, add to a valid word list. 
		for (int i = 0; i < modifications.size(); ++i) { 
			if (rootNode.isInTrie(modifications.get(i))) {
				validModWords.add(modifications.get(i));
			} 
		}
		
		//System.out.println("Valid first distance modifications for " + inWord + ": " + validModWords);
		ArrayList<String> secondDistModifications = new ArrayList<String>(); //if there aren't, then implement a second edit distance
		for (int i = 0; i < modifications.size(); ++i) { //apply every modification to each modified word in the array
			secondDistModifications.addAll(deletionMod(modifications.get(i))); 
			secondDistModifications.addAll(insertionMod(modifications.get(i)));
			secondDistModifications.addAll(transpositionMod(modifications.get(i)));
			secondDistModifications.addAll(alterationMod(modifications.get(i)));
			
		}
		for (int i = 0; i < secondDistModifications.size(); ++i) { //check if the second distance modifications make a word found in the trie. If so, add to valid word list
			if (rootNode.isInTrie(secondDistModifications.get(i))) {
				validModWords.add(secondDistModifications.get(i));
			}
		}
		//System.out.println("Valid second distance modifications for " + inWord + ": " + validModWords);
		
		
	}
	
	public String getBestAlternative(Set<String> validModWords) {
		String bestWord = new String();
		Iterator<String> iter = validModWords.iterator(); 
		//for each word in the array: if the current word has a greater count than the current best word, then make the current word the best word.
		while(iter.hasNext()) {
			String current = new String(iter.next());
			if (rootNode.getWordCount(current) > rootNode.getWordCount(bestWord)) {
				bestWord = new String(current);
			}
			else if (rootNode.getWordCount(current) == rootNode.getWordCount(bestWord)) { //if the two words have the same frequency
				if (current.charAt(0) < bestWord.charAt(0)) { //chooses the first alphabetically
					bestWord = new String(current);
				}
			}
		}
		return bestWord;
	}
	
	public ArrayList<String> deletionMod(String inWord) {
		ArrayList<String> returnList = new ArrayList<String>();
		StringBuilder word = new StringBuilder(inWord);
		for (int i = 0; i < word.length(); ++i) {
			StringBuilder tempWord = new StringBuilder(word);
			tempWord.deleteCharAt(i);
			returnList.add(tempWord.toString());
			++modCounter;
			
		}
		//System.out.println("Deletions: " + returnList);
		return returnList;
		
	}
	
	public ArrayList<String> insertionMod(String inWord) { //HEREIN LIES OUR DIFFICULTY
		ArrayList<String> returnList = new ArrayList<String>();
		StringBuilder word = new StringBuilder(inWord);
		
		for (int i = 0; i < word.length() + 1; ++i) {
			for (int j = 0; j < 26; ++j) {
				StringBuilder tempWord = new StringBuilder(word);
				tempWord.insert(i,  toChar(j));
				returnList.add(tempWord.toString());
				++modCounter;
			}
		}
		//System.out.println("Insertions: " + returnList);
		return returnList;
	}
	
	public ArrayList<String> transpositionMod(String inWord) {
		ArrayList<String> returnList = new ArrayList<String>();
		StringBuilder word = new StringBuilder(inWord);
		
		for (int i = 0; i < word.length() - 1; ++i) {
			StringBuilder tempWord = new StringBuilder(word);
			char tempChar = word.charAt(i + 1);
			tempWord.setCharAt(i + 1, tempWord.charAt(i));
			tempWord.setCharAt(i, tempChar);
			returnList.add(tempWord.toString());
			++modCounter;
		}
		//System.out.println("Transposition: " + returnList);
		return returnList;
	}
	
	public ArrayList<String> alterationMod(String inWord) {
		ArrayList<String> returnList = new ArrayList<String>();
		StringBuilder word = new StringBuilder(inWord);
		for (int i = 0; i < word.length(); ++i) {		
			for (int j = 0; j < 26; ++j) {
				StringBuilder tempWord = new StringBuilder(word);
				tempWord.setCharAt(i, toChar(j));
				returnList.add(tempWord.toString());
				++modCounter;

			}
			
		}
		//System.out.println("Alterations: " + returnList);

		return returnList;
	}
	
	@Override
 	public String toString() {
		StringBuilder str = new StringBuilder();
		rootNode.toString(str);
		String s = wordList.toString();
		wordList.setLength(0);
		return s;
	}
	
	@Override
	public int hashCode() { 
		return 73 + wordCount * 61 + nodeCount * 89 ;
	}
	
	@Override
	public boolean equals(Object o) {
		Trie compareTrie = (Trie) o;
		return rootNode.compare(compareTrie.rootNode);
		
	}
	
	/*How to write a recursive compare function? 
	 * 
	 * 
	 * 
	 */

	/**
	 * Your trie node class should implement the ITrie.INode interface
	 */
	public interface INode {
	
		/**
		 * Returns the freq uency count for the word represented by the node
		 * 
		 * @return The frequency count for the word represented by the node
		 */
		public int getValue();
	}

	@Override
	public ITrie.INode find(String word) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
