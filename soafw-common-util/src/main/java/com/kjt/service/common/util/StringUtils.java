package com.kjt.service.common.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字符串通用工具.
 */
public final class StringUtils {
	private StringUtils() {
	}

	/**
	 * 空字符串常量.
	 */
	public final static String EMPTY = "";

	/**
	 * 测试是否为空字符串.
	 *
	 * @param s
	 * @return true 字符串为空或者null. 否则返回false.
	 */
	public static boolean isEmpty(String s) {
		return (s == null || s.length() <= 0);
	}

	/**
	 * 测试两个字符串是否相等.
	 *
	 * @param s1
	 * @param s2
	 * @return true 两个字符串相等或者都为null. 否则返回false.
	 */
	public static boolean equals(String s1, String s2) {
		return s1 == null ? s2 == null : s1.equals(s2);
	}

	public static String splitAndFilterString(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}

	public static String replaceHtmlAndJs(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input
				.replaceAll("[\n\r\t]", "")
				.replaceAll(
						"<(?!p|/p|span|/span|font|/font|br|strong|/strong|em|/em|b|/b|U|/U)(.*?)>",
						"").replaceAll("<p>\\s*</p>", "");
		return str;
	}

	/**
	 * getStrCount
	 * 
	 * @param originStr
	 * @param targetStr
	 * @return
	 */
	public static int getStrCount(String originStr, String targetStr) {
		int index = 0;

		int count = 0;
		while (true) {
			index = originStr.indexOf(targetStr, index);

			if (index >= 0) {
				count++;
				index++;
			} else {
				break;
			}
		}

		return count;
	}

	/**
	 * getIndexOf
	 * 
	 * @param originStr
	 * @param targetStr
	 * @param count
	 * @return index
	 */
	public static int getIndexOf(String originStr, String targetStr, int count) {
		int index = 0;
		int i = 0;

		while (true) {
			index = originStr.indexOf(targetStr, index);

			if (index >= 0) {
				index++;
				i++;

				if (i == count)
					return index;
			} else {
				break;
			}
		}

		return 0;
	}

	public static String idsStrFormat(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			ids = ids.trim().replaceAll("(\\s*,\\s*)+$", "");
			if (RegexUtils.isMatch(ids, "^\\d+\\s*(,\\s*\\d+\\s*)+")) {
				return ids;
			}
		}
		return null;
	}

	public static String array2str(int[] ids) {
		StringBuffer sb = new StringBuffer();
		int size = ids == null ? 0 : ids.length;
		for (int i = 0; i < size; i++) {
			if (i > 0)
				sb.append(",");
			sb.append(ids[i]);
		}
		return sb.toString();
	}

	public static Integer[] str2Array(String ids) {
		return str2Array(ids, ",");
	}

	public static Integer[] str2Array(String ids, String split) {
		if (!StringUtils.isEmpty(ids)) {
			ids = ids.trim().replaceAll("(\\s*,\\s*)+$", "");
			if (RegexUtils.isMatch(ids, "^\\d+\\s*(,\\s*\\d+\\s*)*")) {
				if (split == null) {
					split = ",";
				}
				String[] _ids = ids.split(split);
				int size = _ids == null ? 0 : _ids.length;
				Integer[] ids_ = new Integer[size];
				for (int i = 0; i < size; i++) {
					ids_[i] = Integer.valueOf(_ids[i].trim());
				}
				return ids_;
			}
		}
		return null;
	}

	public static Integer[] strToArray(String ids) {
		return strToArray(ids, ",");
	}

	public static Integer[] strToArray(String ids, String split) {
		if (!StringUtils.isEmpty(ids)) {
			ids = ids.trim().replaceAll("(\\s*,\\s*)+$", "");
			if (RegexUtils.isMatch(ids, "^\\d+\\s*(,\\s*\\d+\\s*)+")) {
				if (split == null) {
					split = ",";
				}
				String[] _ids = ids.split(split);
				int size = _ids == null ? 0 : _ids.length;
				Integer[] ids_ = new Integer[size];
				for (int i = 0; i < size; i++) {
					ids_[i] = Integer.valueOf(_ids[i].trim());
				}
				return ids_;
			}
		}
		return new Integer[] { Integer.valueOf(ids) };
	}

	public static String list2Str(List<Integer> ids, String pre, String conj) {
		StringBuffer sb = new StringBuffer();
		boolean hasPre = !StringUtils.isEmpty(pre);
		if (hasPre)
			sb.append(pre);
		int size = ids == null ? 0 : ids.size();
		if (size == 0 && !hasPre)
			return null;
		else if (hasPre)
			return sb.toString();
		if (StringUtils.isEmpty(conj))
			conj = ",";
		for (int i = 0; i < size; i++) {
			if (i > 0)
				sb.append(conj);
			sb.append(ids.get(i));
		}
		return sb.toString();
	}

	public static Set<Integer> str2Set(String ids) {
		Set<Integer> list = null;
		if (!StringUtils.isEmpty(ids)) {
			ids = ids.trim().replaceAll("(\\s*,\\s*)+$", "");
			if (RegexUtils.isMatch(ids, "^\\d+\\s*(,\\s*\\d+\\s*)*")) {
				String[] _ids = ids.split(",");
				int size = _ids == null ? 0 : _ids.length;
				Integer[] ids_ = new Integer[size];
				list = new HashSet<Integer>();
				for (int i = 0; i < size; i++) {
					String tmp = _ids[i].trim();
					if (!StringUtils.isEmpty(tmp)) {
						list.add(Integer.valueOf(tmp));
					}
				}
				return list;
			}
		}
		return null;
	}

	public static Set<Long> str2LongSet(String ids) {
		Set<Long> list = null;
		if (!StringUtils.isEmpty(ids)) {
			ids = ids.trim().replaceAll("(\\s*,\\s*)+$", "");
			if (RegexUtils.isMatch(ids, "^\\d+\\s*(,\\s*\\d+\\s*)*")) {
				String[] _ids = ids.split(",");
				int size = _ids == null ? 0 : _ids.length;
				Long[] ids_ = new Long[size];
				list = new HashSet<Long>();
				for (int i = 0; i < size; i++) {
					String tmp = _ids[i].trim();
					if (!StringUtils.isEmpty(tmp)) {
						list.add(Long.valueOf(tmp));
					}
				}
				return list;
			}
		}
		return null;
	}

	public static char ascii2Char(int ASCII) {
		return (char) ASCII;
	}

	public static int char2ASCII(char c) {
		return (int) c;
	}

	public static String chinese2pinyin(String chinese, Map<String, String> db,
			boolean ishead) {
		if (StringUtils.isEmpty(chinese))
			return chinese;
		String str = chinese.trim();
		int slen = str.length();
		if (slen < 1)
			return str;
		char[] chars = chinese.toCharArray();
		int size = db == null ? 0 : db.size();
		if (size == 0) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < slen; i++) {
			if (char2ASCII(chars[i]) > 0x80) {
				String pinyin = db.get(String.valueOf(chars[i]));
				if (ishead) {
					sb.append(pinyin.substring(0, 1).toUpperCase());
				} else {
					sb.append(pinyin.toUpperCase());
				}
			} else if (RegexUtils.isMatch(String.valueOf(chars[i]), "[a-z0-9]")) {
				sb.append(String.valueOf(chars[i]));
			}
		}
		return sb.toString();
	}

	public static String getDomain(String url) {
		String url_ = RegexUtils.match(url, "^http\\:\\/\\/([^/]+).+?$");
		int idx = url_.indexOf(":");
		if (idx != -1) {
			url_ = url_.substring(0, idx);
		}
		return url_;
	}

	public static String htmlspecialchars(String data) {
		if (data == null || data.length() == 0) {
			return data;
		}
		/**
		 * '&' (ampersand) becomes '&amp;'
		 */
		data = data.replaceAll("&", "&amp;");
		/**
		 * '"' (double quote) becomes '&quot;' when ENT_NOQUOTES is not set.
		 */
		data = data.replaceAll("\"", "&quot;");
		/**
		 * ''' (single quote) becomes '&#039;' only when ENT_QUOTES is set.
		 */
		data = data.replaceAll("'", "&#039;");
/**
		 * '<' (less than) becomes '&lt;' 
		 */
		data = data.replaceAll("<", "&lt;");
		/**
		 * '>' (greater than) becomes '&gt;'
		 */
		data = data.replaceAll(">", "&gt;");

		return data;
	}

	// 去除 指定的前缀字符 ,
	public static String ltrim(String str, char c) {

		String result = str;
		if (str == null) {
			return null;
		} else {
			int size = str.length();
			for (int i = 0; i < size; i++) {
				char temp = str.charAt(i);
				if (temp == c) {
					result = str.substring(i + 1);
				} else {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 10进制整数 转为2进制字符串
	 * 
	 * @param param
	 *            10进制整数
	 * @param len
	 *            2进制字符串位数
	 * @return
	 */
	public static String int2BStr(int param, int len) {
		String binaryEqu = Integer.toBinaryString(param);
		if (binaryEqu.length() < len) {
			int length = binaryEqu.length();
			for (int i = 0; i < (len - length); i++) {
				binaryEqu = "0" + binaryEqu;
			}
		}
		return binaryEqu;
	}

	public static void main(String[] args) {
		// String last5NewCommIds = "684";
		// Integer[] aa = StringUtils.str2Array(last5NewCommIds);
	}
}
