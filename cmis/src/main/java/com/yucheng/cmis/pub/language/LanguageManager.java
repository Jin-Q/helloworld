/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.pub.language;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.Session;
import com.ecc.emp.session.SessionManager;
import com.yucheng.cmis.util.CommonUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class LanguageManager {
	private static final String LANGUAGE_FILE_ENCODING = "UTF-8";
	private static final String LANG_FILE_SUFFIX = ".lang";
	private static final String LANG_FILES_PATH = "languages";
	private static Map<String, Map<String, String>> languageMap = null;

	private static void loadLanguageFiles() {
		languageMap = new HashMap();
		String langFilesPath = CommonUtil.getClassesPath() + "languages";
		File path = new File(langFilesPath);
		if (!(path.isDirectory()))
			return;
		File[] listFiles = path.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".lang"));
			}
		});
		for (File file : listFiles) {
			String fileName = file.getName();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				Map map = new HashMap();
				String line = null;

				while ((line = br.readLine()) != null) {
					if (line.trim().equals(""))
						continue;
					if (line.trim().startsWith("#")) {
						continue;
					}
					int indexOf = line.indexOf("=");

					if (indexOf < 0) {
						continue;
					}
					String key = line.substring(0, indexOf).trim();

					if (key.length() == 0) {
						continue;
					}
					String value = line.substring(indexOf + 1);
					map.put(key, value);
				}

				String language = fileName.substring(0, fileName.lastIndexOf(".lang"));
				languageMap.put(language.toLowerCase(), map);
				EMPLog.log("Core", EMPLog.INFO, 0, "加载语言文件" + fileName + "成功");
			} catch (Exception e) {
				e.printStackTrace();
				EMPLog.log("Core", EMPLog.INFO, 0, "加载语言文件" + fileName + "出错,", e);
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (IOException e) {
						EMPLog.log("Core", EMPLog.INFO, 0, "加语言文件" + fileName + "出错,", e);
					}
			}
		}
	}

	public static String translation(PageContext pageContext, String text) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		return translation(request, text);
	}

	public static String translation(HttpServletRequest request, String text) {
		String language = getUserLanguage(request);
		return translation(language, text);
	}

	public static String translation(Context context, String text) {
		String language = "zh_CN";
		try {
			String userLang = (String) context.getDataValue("user_locale_language");
			if ((userLang != null) && (!("".equals(userLang))))
				language = userLang;
		} catch (Exception localException) {
		}
		return translation(language, text);
	}

	public static String translation(String language, String text) {
		if ((language == null) || (language.equals(""))) {
			return text;
		}

		if (languageMap == null) {
			loadLanguageFiles();
		}

		Map map = (Map) languageMap.get(language.toLowerCase());
		String retText = null;
		if (map != null) {
			retText = (String) map.get(text);
		}
		return ((retText == null) ? text : retText);
	}

	public static String getUserLanguage(PageContext pageContext) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		return getUserLanguage(request);
	}

	public static String getUserLanguage(HttpServletRequest request) {
		String language = null;

		Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
		if (context != null) {
			try {
				language = (String) context.getDataValue("user_locale_language");
			} catch (Exception localException) {
			}
		}
		if (language == null) {
			SessionManager sessionManager = (SessionManager) request.getAttribute(EMPConstance.ATTR_SESSION_MGR);
			Session empSession = sessionManager.getSession(request, null, false);
			if (empSession != null) {
				language = (String) empSession.getAttribute("user_locale_language");
			}

		}

		if (language == null) {
			language = (String) request.getSession().getAttribute("user_locale_language");
		}

		if (language == null) {
			Locale locale = request.getLocale();
			if (locale != null)
				language = locale.toString();
			else {
				language = "zh_CN";
			}
		}
		return language.toLowerCase();
	}
}