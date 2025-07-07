package org.globaltester.logging.format;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.Message;
import org.globaltester.logging.tags.LogTag;

/**
 * Formats the {@link Message}. Used to format or manipulate log messages.
 */
public class LogFormat implements LogFormatService
{
	public static final String LOG_LEVEL_UNKNOWN = "UNKNOWN";
	private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault());

	@Override
	public String format(Message msg)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append('[').append(getLogLevel(msg)).append(" - ").append(getTimestamp(msg)).append("] ").append(msg.getMessageContent());
		return sb.toString();
	}

	/**
	 * Extracts the first additional data string for a tag with the given ID.
	 *
	 * @param msg
	 *            the message containing tags
	 * @param id
	 *            the tag ID to search for
	 * @return the first additional data string if available and the ID matches, else null
	 */
	public static String extractTag(Message msg, String id)
	{
		List<LogTag> tags = msg.getLogTags();
		if (tags == null || tags.isEmpty()) {
			return null;
		}
		for (LogTag curTag : tags) {
			if (id.equals(curTag.getId())) {
				String[] additionalData = curTag.getAdditionalData();
				if (additionalData != null && additionalData.length > 0) {
					return additionalData[0];
				}
			}
		}
		return null;
	}

	/**
	 * Returns the formatted timestamp from the message, or an empty string if unavailable.
	 *
	 * @param msg
	 *            the message
	 * @return formatted timestamp or empty string
	 */
	public static String getTimestamp(Message msg)
	{
		String tagValue = extractTag(msg, BasicLogger.TIMESTAMP_TAG_ID);
		if (tagValue != null) {
			try {
				long millis = Long.parseLong(tagValue);
				return getTimestampFromLong(millis);
			}
			catch (NumberFormatException e) {
				// Return empty string if parsing fails
				return "";
			}
		}
		return "";
	}

	/**
	 * Returns the formatted timestamp for the given epoch milliseconds.
	 *
	 * @param timestampInLong
	 *            epoch milliseconds
	 * @return formatted timestamp
	 */
	public static String getTimestampFromLong(long timestampInLong)
	{
		return ISO_DATE_FORMATTER.format(Instant.ofEpochMilli(timestampInLong));
	}

	/**
	 * Returns the current timestamp in formatted string.
	 *
	 * @return formatted current timestamp
	 */
	public static String getCurrentTimestamp()
	{
		return getTimestampFromLong(Instant.now().toEpochMilli());
	}

	/**
	 * Returns the log level from the message, or LOG_LEVEL_UNKNOWN if not found.
	 *
	 * @param msg
	 *            the message
	 * @return log level string
	 */
	public static String getLogLevel(Message msg)
	{
		String tagValue = extractTag(msg, BasicLogger.LOG_LEVEL_TAG_ID);
		return tagValue != null ? tagValue : LOG_LEVEL_UNKNOWN;
	}
}
