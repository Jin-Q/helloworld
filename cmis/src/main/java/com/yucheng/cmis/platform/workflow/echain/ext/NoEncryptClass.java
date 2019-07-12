package com.yucheng.cmis.platform.workflow.echain.ext;

import com.ecc.echain.ext.EncryptIF;

/**
 * 泉州银行不加密流程意见内容
 * @author yucheng 2014-07-16
 */
public class NoEncryptClass implements EncryptIF {

	public NoEncryptClass(){
		
	}
	public String convertToCipher(String input) {
		return input;
	}

	public String convertToPlainness(String input) {
		return input;
	}

}
