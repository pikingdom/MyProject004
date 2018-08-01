package com.nd.hilauncherdev.kitset;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.nd.hilauncherdev.kitset.pinyin.MultiPinYin1;
import com.nd.hilauncherdev.kitset.pinyin.MultiPinYin2;
import com.nd.hilauncherdev.kitset.pinyin.MultiPinYin3;
import com.nd.hilauncherdev.kitset.pinyin.MultiPinYin4;
import com.nd.hilauncherdev.kitset.pinyin.MultiPinYin5;
import com.nd.hilauncherdev.kitset.pinyin.SimplePinYin;
import com.nd.hilauncherdev.kitset.util.StringUtil;

/**
 * 汉字转拼音，包括多音字的处理
 */
public class PinyinHelper {
	public static String[] pinyinArray = null;
	public static String wordBreaker = " ";
	public static char commaBreaker = ',';
	// ox4e00 ,十进制为19968
	private static int chUnicodePart1Begin = 19968;
	// 0x5e67,24167
	private static int chUnicodePart2Begin = 24167;
	// 0x6ECF,28367
	private static int chUnicodePart3Begin = 28367;
	// 0x7F37,32567
	private static int chUnicodePart4Begin = 32567;
	// 0x8F9F,36767
	private static int chUnicodePart5Begin = 36767;
	// 0x9FA5,40869
	private static int chUnicodePart5End = 40869;

	private PinyinHelper() {
	};

	/**
	 * 汉字转成拼音，如果是数字则返回数字，其他字符直接忽略
	 * 
	 * @param chinese
	 * @return 对应的拼音或数字
	 */
	public static String convertChineseToPinyin(final String chinese) {
		if (StringUtil.isEmpty(chinese))
			return "";
		
		String[] multiPinYin1 = MultiPinYin1.getMultiPinYin1();
		String[] multiPinYin2 = MultiPinYin2.getMultiPinYin2();
		String[] multiPinYin3 = MultiPinYin3.getMultiPinYin3();
		String[] multiPinYin4 = MultiPinYin4.getMultiPinYin4();
		String[] multiPinYin5 = MultiPinYin5.getMultiPinYin5();
		
		
		StringBuilder result = new StringBuilder();
		int len = chinese.length();
		for (int i = 0; i < len; i++) {
			char eachChar = chinese.charAt(i);
			int unicodeValue = chinese.charAt(i);
			if (unicodeValue >= chUnicodePart1Begin && unicodeValue < chUnicodePart2Begin) {
				int offset = unicodeValue - chUnicodePart1Begin;
				result.append(multiPinYin1[offset]).append(wordBreaker);
				continue;
			} else if (unicodeValue >= chUnicodePart2Begin && unicodeValue < chUnicodePart3Begin) {
				int offset = Integer.valueOf(unicodeValue - chUnicodePart2Begin);
				result.append(multiPinYin2[offset]).append(wordBreaker);
				continue;
			} else if (unicodeValue >= chUnicodePart3Begin && unicodeValue < chUnicodePart4Begin) {
				int offset = unicodeValue - chUnicodePart3Begin;
				result.append(multiPinYin3[offset]).append(wordBreaker);
				continue;
			} else if (unicodeValue >= chUnicodePart4Begin && unicodeValue < chUnicodePart5Begin) {
				int offset = unicodeValue - chUnicodePart4Begin;
				result.append(multiPinYin4[offset]).append(wordBreaker);
				continue;
			} else if (unicodeValue >= chUnicodePart5Begin && unicodeValue <= chUnicodePart5End) {
				int offset = unicodeValue - chUnicodePart5Begin;
				result.append(multiPinYin5[offset]).append(wordBreaker);
				continue;
			} else if (isAlphaPhebicNumeric(eachChar)) {
				result.append(eachChar);
				while (i < len - 1) {
					i++;
					char nextChar = chinese.charAt(i);
					if (isAlphaPhebicNumeric(nextChar))
						result.append(nextChar);
					else {
						result.append(wordBreaker);
						i--;
						break;
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * 中文的首字母
	 * 
	 * @param chinese
	 * @return 首字母
	 */
	public static String convertChineseOnlyFirstSpell(final String chinese) {
		String result = "";
		String[][] strss = convertChineseToPinyinArray(chinese);
		if (strss == null)
			return result;
		
		for (String[] strs : strss) {
			result += strs[0].charAt(0);
		}
		return result;
	}

	// TODO 需要重构
	public static String[][] convertChineseToPinyinArray(final String chinese) {
		if (StringUtil.isEmpty(chinese))
			return null;
		
		List<String[]> pinyinList = new ArrayList<String[]>();
		int len = chinese.length();
		for (int i = 0; i < len; i++) {
			char eachChar = chinese.charAt(i);
			int unicodeValue = chinese.charAt(i);
			if (unicodeValue >= chUnicodePart1Begin && unicodeValue < chUnicodePart2Begin) {
				int offset = unicodeValue - chUnicodePart1Begin;
				String[] resultPinyinArray = convertMultiPinStringToArray(MultiPinYin1.getMultiPinYin1()[offset]);
				pinyinList.add(resultPinyinArray);
				continue;
			} else if (unicodeValue >= chUnicodePart2Begin && unicodeValue < chUnicodePart3Begin) {
				int offset = Integer.valueOf(unicodeValue - chUnicodePart2Begin);
				String[] resultPinyinArray = convertMultiPinStringToArray(MultiPinYin2.getMultiPinYin2()[offset]);
				pinyinList.add(resultPinyinArray);
				continue;
			} else if (unicodeValue >= chUnicodePart3Begin && unicodeValue < chUnicodePart4Begin) {
				int offset = unicodeValue - chUnicodePart3Begin;
				String[] resultPinyinArray = convertMultiPinStringToArray(MultiPinYin3.getMultiPinYin3()[offset]);
				pinyinList.add(resultPinyinArray);
				continue;
			} else if (unicodeValue >= chUnicodePart4Begin && unicodeValue < chUnicodePart5Begin) {
				int offset = unicodeValue - chUnicodePart4Begin;
				String[] resultPinyinArray = convertMultiPinStringToArray(MultiPinYin4.getMultiPinYin4()[offset]);
				pinyinList.add(resultPinyinArray);
				continue;
			} else if (unicodeValue >= chUnicodePart5Begin && unicodeValue <= chUnicodePart5End) {
				int offset = unicodeValue - chUnicodePart5Begin;
				String[] resultPinyinArray = convertMultiPinStringToArray(MultiPinYin5.getMultiPinYin5()[offset]);
				pinyinList.add(resultPinyinArray);
				continue;
			}

			if (isAlphaPhebicNumeric(eachChar)) {
				StringBuilder result = new StringBuilder();
				result.append(eachChar);
				while (i < len - 1) {
					i++;
					char nextChar = chinese.charAt(i);
					if (isAlphaPhebicNumeric(nextChar))
						result.append(nextChar);
					else {
						i--;
						break;
					}
				}
				if (result.length() > 0) {
					String[] resultPinyinArray = convertMultiPinStringToArray(result.toString());
					pinyinList.add(resultPinyinArray);
				}
			}
		}
		return pinyinList.toArray(new String[pinyinList.size()][]);
	}

	/**
	 * 分割多音字拼音，如 ding,zheng
	 * 
	 * @param multiPinyin
	 * @return 多音字拼音
	 */
	private static String[] convertMultiPinStringToArray(final String multiPinyin) {
		List<String> matchList = new ArrayList<String>();
		int len = multiPinyin.length();
		int i = 0, start = 0;
		while (i < len) {
			if (multiPinyin.charAt(i) == commaBreaker) {
				String subStr = multiPinyin.substring(start, i);
				if (subStr.length() > 0)
					matchList.add(subStr);
				start = ++i;
				continue;
			}
			i++;
		}
		if (start < len) {
			String subStr = multiPinyin.substring(start, i);
			if (subStr.length() > 0)
				matchList.add(subStr);
		}
		return matchList.toArray(new String[matchList.size()]);
	}

	public static boolean isAlphaPhebicNumeric(char cs) {
		if (cs >= '0' && cs <= '9')
			return true;
		if (cs >= 'a' && cs <= 'z')
			return true;
		if (cs >= 'A' && cs <= 'Z')
			return true;
		return false;
	}

	/**
	 * 获取中文的全拼，如果是多音字，则取常用拼音
	 * @param chinese
	 * @return 中文的全拼
	 */
	public static String convertChineseToFullPinyin(String chinese) {
		String originalPinyin = convertChineseToPinyin(chinese);
		StringBuffer fullPinyin = new StringBuffer("");
		if (originalPinyin.lastIndexOf(commaBreaker) == -1) { // 不存在多音字
			fullPinyin.append(originalPinyin.replace(wordBreaker, ""));
		} else {
			String[] pys = originalPinyin.split(wordBreaker);
			for (String py : pys) {
				if (py.lastIndexOf(commaBreaker) == -1) {
					fullPinyin.append(py);
				} else {
					fullPinyin.append(py.split(String.valueOf(commaBreaker))[0]);
				}
			}
		}
		return fullPinyin.toString().trim();
	}

	/**
	 * 获取多音字首字母
	 * @param pinyin
	 * @return 首字母
	 */
	public static String convertMutilPinyinToFirstPinyin(String pinyin) {
		String originalPinyin = pinyin;
		StringBuffer firstPinyin = new StringBuffer("");
		String[] pys = originalPinyin.split(wordBreaker);
		for (String py : pys) {
			if (!"".equals(py.trim())) {
				char ch = py.charAt(0);
				if (ch >= '0' && ch <= '9') {
					firstPinyin.append(py);
				} else {
					firstPinyin.append(py.charAt(0));
				}
			}
		}
		return firstPinyin.toString().trim();
	}

	/**
	 * 获取多音字全拼
	 * @param pinyin
	 * @return 全拼
	 */
	public static String convertMutilPinyinToFullPinyin(String pinyin) {
		String originalPinyin = pinyin;
		StringBuffer fullPinyin = new StringBuffer("");
		if (originalPinyin.lastIndexOf(commaBreaker) == -1) { // 不存在多音字
			fullPinyin.append(originalPinyin.replace(wordBreaker, ""));
		} else {
			String[] pys = originalPinyin.split(wordBreaker);
			for (String py : pys) {
				if (py.lastIndexOf(commaBreaker) == -1) {
					fullPinyin.append(py);
				} else {
					fullPinyin.append(py.split(String.valueOf(commaBreaker))[0]);
				}
			}
		}
		return fullPinyin.toString().trim();
	}

	/**
	 * 获取多音字全拼,以空格分隔
	 * @param pinyin
	 * @return 全拼
	 */
	public static String convertMutilPinyinToFullPinyinWithBreaker(String pinyin) {
		String originalPinyin = pinyin;
		StringBuffer fullPinyin = new StringBuffer("");
		if (originalPinyin.lastIndexOf(commaBreaker) == -1) { // 不存在多音字
			fullPinyin.append(originalPinyin);
		} else {
			String[] pys = originalPinyin.split(wordBreaker);
			for (String py : pys) {
				if (py.lastIndexOf(commaBreaker) == -1) {
					fullPinyin.append(wordBreaker).append(py);
				} else {
					fullPinyin.append(wordBreaker).append(py.split(String.valueOf(commaBreaker))[0]);
				}
			}
		}
		return fullPinyin.toString().trim();
	}

	/**
	 * 单个中文的第一个拼音
	 * @param chinese
	 * @return 第一个拼音
	 */
	public static String getFirstPinyin(String chinese) {
		String pinyin = convertChineseToPinyin(chinese);
		return pinyin.split(String.valueOf(commaBreaker))[0].trim();
	}
	
	
	
	/**
	 * 获得拼音搜索匹配正则的表达式
     * @param target 搜索字符串, 未处理大小写问题, 请在调用方法前target.toLowerCase()
     * @param wildChar 追加的通配符
     * @return 拼音搜索正则表达式
     */
	public static String getPinyinSearchRegExp(String target, String wildChar) {		
    	String exp = "";
    	String[] pinyinArray = SimplePinYin.get();
		
		for (int i = 0, start = 0, length = target.length(); i < length; i++) {
			String s = target.substring(start, i + 1);
			
			if (!binaryMatch(pinyinArray, s)) { 
				exp += target.substring(start, i) + wildChar;
				start = i;
			} 
			
			if (i == length - 1)
				exp += target.substring(start, i + 1) + wildChar;		
		}
		return exp;
	}
    
	public static String[] getPyRegExpStrArr(String target) {
		List<String> list = new ArrayList<String>();
		
    	String[] pinyinArray = SimplePinYin.get();
		
		for (int i = 0, start = 0, length = target.length(); i < length; i++) {
			String s = target.substring(start, i + 1);
			
			if (!binaryMatch(pinyinArray, s)) { 
				String tmp = target.substring(start, i);
				if (tmp != null && tmp.length() > 0)
					list.add(tmp); // 非空时添加
				
				start = i;
			} 
			
			if (i == length - 1)
				list.add(target.substring(start, i + 1));		
		}
		
		return list.toArray(new String[]{});
	}
	
	
    public static boolean isPinyin(String str) {
		Pattern pattern = Pattern.compile("[ a-zA-Z]*");
        return pattern.matcher(str).matches();
	}
	
	public static boolean containChinese(String str) {        
	    Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");          
	    return pattern.matcher(str).find();
	}
    
    
    /**
     * 2分法查找拼音表
	 * @param a 拼音数组
	 * @param key 源
     */
	public static boolean binarySearch(String[] a, String key) {
		int low = 0;
		int high = a.length - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			String midVal = a[mid];

			if (midVal.compareToIgnoreCase(key) < 0)
				low = mid + 1;
			else if (midVal.compareToIgnoreCase(key) > 0)
				high = mid - 1;
			else
				return true; // key found
		}
		return false; // key not found.
	}
	
	/**
	 * 2分法匹配拼音表
	 * @param a 拼音数组
	 * @param key 源
	 * @return 存在匹配返回true, 否则false;
	 */
	public static boolean binaryMatch(String[] a, String key) {
		
		int low = 0;
		int high = a.length - 1;
		
		while (low <= high) {
			int mid = (low + high) >>> 1;
			String midVal = a[mid];
			
			try {
				if (midVal.matches(key + "[a-zA-Z]*"))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (midVal.compareTo(key) < 0) 
				low = mid + 1;
			else 
				high = mid - 1;
		}
		return false;
	}
}
