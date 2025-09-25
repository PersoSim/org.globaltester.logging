package org.globaltester.logging.format;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.Message;

/**
 * Formats log messages for file output.
 */
public class GtFileLogFormatter implements LogFormatService
{
	public static final String DATE_FORMAT_GT_STRING = "yyyy-MM-dd' 'HH:mm:ss,SSS";
	public static final String DATE_FORMAT_GT_ISO_STRING = "yyyy-MM-dd'T'HH:mm:ss";


	protected static String getTimestamp(Message msg)
	{
		return LogFormat.getTimestamp(msg);
	}

	@Override
	public String format(Message msg)
	{
		String timestamp = getTimestamp(msg);
		String logLevel = LogFormat.getLogLevel(msg);
		String messageContent = msg.getMessageContent();
		String stackTrace = LogFormat.extractTag(msg, BasicLogger.EXCEPTION_STACK_TAG_ID);

		StringBuilder sb = new StringBuilder(256);
		sb.append(timestamp).append(" - ").append(padRight(logLevel, 5)).append(" - ").append(messageContent);

		if (stackTrace != null) {
			sb.append('\n').append(stackTrace);
		}
		return sb.toString();
	}

	/**
	 * Helper method for fast padding
	 * Pads a string to the right with spaces up to the specified length.
	 * If the string is longer or equal, returns it unchanged.
	 */
	protected static String padRight(String s, int n)
	{
		int len = s.length();
		if (len >= n) {
			return s;
		}
		StringBuilder sb = new StringBuilder(n);
		sb.append(s);
		for (int i = len; i < n; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
}

