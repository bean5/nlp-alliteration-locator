package alliterationLocator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import NGramSet.Alliteration;
import NGramSet.AlliterationImpl;

public class AlliterationFinder<T1 extends Alliteration> implements InterestingPhraseFinder
{
	protected boolean	isTesting			= false;
	private boolean	USESTOPWORDS		= true;
	protected boolean	matchCase			= true;
	protected int		totalRightMatches	= 0;

	public List<List<String>> findAlliterations(String file, int min)
	{
		HashSet<Alliteration> NGramsWithMatches = new HashSet<Alliteration>();

		char[] chars1 = file.toCharArray();

		List<String> words = scanForWords(chars1);

		if (isTesting)
		{
			int maxSub = 1000;
			words = words.subList(0, (maxSub > words.size()) ? words.size() : maxSub);
		}

		AlliterationImpl.setUseStopWords(USESTOPWORDS);
		AlliterationImpl.setMinSize(min);

		return findAlliterations(words, min);

		/*
		 * organizeMatches(NGramsWithMatches); mergeRepeats(NGramsWithMatches,
		 * min); rankResults(NGramsWithMatches, min);
		 */
	}

	private void organizeMatches(HashSet<Alliteration> nGramsWithMatches)
	{
		Set<Alliteration> bst = new TreeSet<Alliteration>(nGramsWithMatches);
		// nGramsWithMatches = new LinkedList<Alliteration>(bst.toArray());
	}

	private void rankResults(HashSet<Alliteration> foundNGrams, int min)
	{
	}

	private void mergeRepeats(HashSet<Alliteration> foundNGrams, int min)
	{
	}

	protected List<List<String>> findAlliterations(List<String> words, int min)
	{
		List<List<String>> alliterations = new LinkedList<List<String>>();
		List<String> alliteration = new ArrayList<String>(min);

		String first = null;
		for (String word : words)
		{
			if (alliteration.size() == 0)
			{
				alliteration.add(word);
				first = word;
				continue;
			}

			if (word.toCharArray()[0] == first.toCharArray()[0])
			{
				alliteration.add(word);
			}
			else
			{
				if (alliteration.size() >= min)
				{
					alliterations.add(alliteration);

					System.out.println("Alliteration Found: " + alliteration.toString());
				}
				alliteration = new ArrayList<String>(min);
			}
		}

		return alliterations;
	}

	private ArrayList<Alliteration> getAllNGramsOfSize(List<String> words)
	{
		String processedWord = null;
		int size = 1;

		ArrayList<Alliteration> sets = new ArrayList<Alliteration>(words.size());
		final int documentSize = words.size();

		AlliterationImpl current = new AlliterationImpl(size);

		current.setDocument(words);

		for (int i = 0; i < size && i < documentSize; i++)
		{
			processedWord = current.processWord(words.get(i));
		}
		sets.add(current);

		AlliterationImpl prev = current;
		for (int i = size; i < documentSize; i++)
		{
			current = new AlliterationImpl((Alliteration) prev);

			processedWord = current.processWord(words.get(i));
			current.popFirstWord();

			sets.add(current);
			prev = current;

			if (processedWord == null) continue;

			List<String> relevantWords = current.getModifiedWordList();
		}
		// if(map != null) System.out.println("Map size: " +
		// map.entrySet().size());
		return sets;
	}

	private List<String> scanForWords(char[] chars)
	{
		List<String> words = new ArrayList<String>(chars.length / 4);// assume
																							// that the
																							// average
																							// word
																							// length
																							// is 4
		StringBuilder str = new StringBuilder();
		str.setLength(30);

		int total = 0;
		int length = 0;
		int max = chars.length;

		for (int i = 0; i < max; i++)
		{
			char currChar = chars[i];
			// TODO fix this because it is wrong.
			char nextChar = chars[i];

			if (characterEvaluater.isAlphaOrDashFollowedByAlpha(currChar, nextChar))
			{
				str.setCharAt(length, currChar);
				// System.out.print(currChar);
				length++;
			}
			else if (length > 0)
			{
				words.add(str.substring(0, length));

				total += length;
				length = 0;
			}
		}

		System.out.println("Total length: " + total);
		System.out.println("Predicted length: " + chars.length / 8);
		System.out.println("Average length: " + total / words.size());

		assert (chars.length == 0 || words.size() > 0);

		return words;
	}

	public void setMatchCase(boolean matchCase)
	{
		this.matchCase = matchCase;
	}

	public String toString()
	{
		return new String("");
	}

	public void setUseStopWords(boolean useStopWords)
	{
		USESTOPWORDS = useStopWords;
	}
}
