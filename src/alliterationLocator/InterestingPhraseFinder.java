package alliterationLocator;

import java.util.List;

public interface InterestingPhraseFinder
{
	public List<List<String>> findAlliterations(String string1, int min);

	public String toString();

	public int hashCode();
}
