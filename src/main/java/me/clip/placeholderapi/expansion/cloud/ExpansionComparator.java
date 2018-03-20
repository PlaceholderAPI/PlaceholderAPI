package me.clip.placeholderapi.expansion.cloud;

import java.util.Comparator;

public class ExpansionComparator implements Comparator<CloudExpansion> {

	private static final int AUTHOR = 0;
	private static final int LAST = 1;
	private static final int RATING = 2;
	private static final int TO_UPDATE = 3;
	private int compare_mode = LAST;

	public ExpansionComparator() { }

	public ExpansionComparator(int compare_mode) {
		    this.compare_mode = compare_mode;
	}

	@Override
	public int compare(CloudExpansion o1, CloudExpansion o2) {
		switch (compare_mode) {
		    case LAST:
		        return (int) (o2.getLastUpdate() - o1.getLastUpdate());
		    default:
		        return o1.getAuthor().compareTo(o2.getAuthor());
		}
	}
}
