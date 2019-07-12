package com.yucheng.cmis.pub.ide.tagGenerator;

public class Decide {
	
	static Object decideNull(String s) {
		if (s == null || s.trim().length() == 0)
			return "";
		else
			return s;
	}
 
	static Object decideNull(String attr, String s) {
		if (s == null || s.trim().length() == 0 || attr == null || attr.trim().length() == 0)
			return "";
		else
			return attr + "=\"" + s + "\" ";
	}
	
}
