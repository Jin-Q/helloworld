package com.yucheng.cmis.biz01line.esb.pub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateSno {

	private static int i = 0;
	private static int maxSeq = 99;


	public synchronized static String createSnoByTime() {
		String seqNo = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS",
				Locale.ENGLISH);
		seqNo = sdf.format(new Date());
		if (i >= maxSeq) {
			i = 0;
		} else {
			i++;
		}
		seqNo += I2Seq(i);
		return seqNo;
	}

	private static String I2Seq(int i) {
		String sn = String.valueOf(i);
		int iLen = sn.length();
		int mLen = String.valueOf(maxSeq).length();
		for (int j = 0; j < mLen - iLen; j++) {
			sn = "0" + sn;
		}
		return sn;
	}

	public static void main(String[] args) {
		// CreateSno c = new CreateSno();
		for (int m = 0; m < 1001; m++)
			System.err.println(CreateSno.createSnoByTime());
	}
}
