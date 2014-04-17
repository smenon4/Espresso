/*
 * Copyright 2012 Espresso Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.espresso.extension;

import static java.util.Arrays.asList;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Hanldes american style date format
 * 
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 * */
public class AmericanDateExtension implements DateExtension {

	private static final List<String> FORMATS = asList("d MMM y", "d-MMM-y",
			"d/MMM/y", "MMM d yy", "MMM-d-yy", "MMM/d/yy");
	public static final AmericanDateExtension AMERICAN_DATE_EXTENSION = new AmericanDateExtension();

	@SqlExtension
	public Date toDate(final String dateString) throws SQLException {
		try {
			return null == dateString ? null : DateFormat
					.toDateAmericanFormat(dateString);
		} catch (final Exception e) {
			return fallback(dateString);
		}
	}

	private static Date fallback(final String dateString) throws SQLException {
		for (final String format : FORMATS) {
			try {
				return new SimpleDateFormat(format).parse(dateString);
			} catch (ParseException e) {
			}
		}

		throw new SQLException();
	}
}
