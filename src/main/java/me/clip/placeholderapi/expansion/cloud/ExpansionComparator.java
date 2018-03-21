/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
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
