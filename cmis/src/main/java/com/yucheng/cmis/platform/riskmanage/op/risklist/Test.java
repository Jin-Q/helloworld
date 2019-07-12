package com.yucheng.cmis.platform.riskmanage.op.risklist;

import com.ecc.emp.core.Context;
import com.hankcs.hanlp.HanLP;
import javax.swing.JOptionPane;

public class Test implements UrlLinkInterface {

	public String getUrl(Context context, String className, String serno) {
		return "getPrdPvRiskItemUpdatePage.do?item_id=FFFA27870112FC47C68091DEEA15999E";
	}

	public static void main(String[] args) {
		System.out.println(HanLP
				.convertToSimplifiedChinese("「聖誕節反抗四季度房價客流集散地以後等妳當上皇后，就能買士多啤梨慶祝了」"));
		System.out.println(HanLP
				.convertToTraditionalChinese("“以后等你当上皇后，就能买草莓庆祝了”"));

		String QJstr = "wch()";

		String QJstr1 = "ｈｅｌｌｏ（）";

		String result = ToSBC(QJstr);

		String result1 = ToDBC(QJstr1);

		System.out.println(QJstr + "转成全角：" + result);

		System.out.println(QJstr1 + "转成半角：" + result1);
		
		/****/
		double lilv;
		double benjin;
		int nian;
		double yuehuankuan;
		double zonge;
		String textstr;

		L1: for (;;) {// 该循环用来处理异常，用户选择关闭或者取消，则程序结束

			lilv = DoubleScanf("利率");
			if (lilv == -1)
				break;
			benjin = DoubleScanf("贷款总额");
			if (benjin == -1)
				continue L1;
			nian = (int) DoubleScanf("贷款年限");
			if (nian == -1)
				continue L1;

			zonge = lilv * nian * 0.01 * benjin + benjin;
			yuehuankuan = zonge / nian / 12;
			textstr = benjin + "元\n贷" + nian + "年\n利率" + lilv + "%\n还款总额为"
					+ zonge + "元\n平均每月还款" + yuehuankuan + "元";
			JOptionPane.showMessageDialog(null, textstr, "贷款计算器",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		}
		
		/****/
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	
	public static double DoubleScanf(String StrChars) {
		// 定义一个方法DoubleScanf，用来弹出对话框并且读取一个字符串，判断并转化为double型，返回该double型数值
		double doubleNum = 0;
		head: for (;;) {
			String doubleStr = JOptionPane.showInputDialog(null, "请输入"
					+ StrChars);// 定义一个字符串变量doubleStr，用于获取输入框的输入
			if (doubleStr == null) {
				return -1;
			} else if (doubleStr.equals("")) {
				JOptionPane.showMessageDialog(null, StrChars + "不能为空", "警告",
						JOptionPane.INFORMATION_MESSAGE);
				continue head;
			}// 获取一个非空的输入

			char doubleChars[] = doubleStr.toCharArray();// 定义一个字符数组变量doubleChars[]，用于将输入的字符串转化为数组进行判断
			if (doubleChars.length > 30) {
				JOptionPane.showMessageDialog(null, "数据太长，请少于30位", "警告",
						JOptionPane.INFORMATION_MESSAGE);
				continue head;
			}// 保证输入不多于30个字符

			boolean point = false;
			for (int i = 0; i < doubleChars.length; i++) {
				if (doubleChars[i] <= 57 && doubleChars[i] >= 48) {
				} else if (doubleChars[i] == 46 && i != 0) {
					if (point == false)
						point = true;
					else {
						JOptionPane.showMessageDialog(null, "非法输入", "警告",
								JOptionPane.INFORMATION_MESSAGE);
						continue head;
					}
				} else {
					JOptionPane.showMessageDialog(null, "非法输入", "警告",
							JOptionPane.INFORMATION_MESSAGE);
					continue head;
				}// 保证输入字符为数字，至多有一个小数点
			}// 判断输入是否合法

			doubleNum = Double.parseDouble(doubleStr);
			break;// 定义一个double变量doubleNum，用于存储输入的数据并返回
		}
		JOptionPane.showMessageDialog(null, StrChars + "为" + doubleNum, "警告",
				JOptionPane.INFORMATION_MESSAGE);
		return doubleNum;
	}
}
