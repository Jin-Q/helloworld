package com.yucheng.cmis.pub.sequence.template;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceTemplate;

/**
 * 审批申请编号(业务流水号)模板
 * 18位： LS+机构号(5位)+业务属性(2位)+年份(2位)+顺序号(7位) 
 *	owner=法人机构号+业务属性
 */
public class SernoSequenceTemplate implements CMISSequenceTemplate {

	public String format(String owner, String sequence, Context context)
	throws EMPException {  

		String date = "" ;					//日期
		int length=7 ;
		StringBuffer sb=new StringBuffer("") ;
		String retuStr="" ;

		try {
			sb.append("LS") ;
			sb.append(owner) ;
			date = (String)context.getDataValue(PUBConstant.OPENDAY) ;
			date = (date.replaceAll("-", "")).substring(2,4) ;
			sb.append(date) ;
			if( sequence.length() > length ){
				sequence = sequence.substring(0, length) ;
			}
			for(int i = sequence.length(); i < length; i++){ 
				sequence = "0" + sequence; 
			}
			sb.append(sequence);
			retuStr=sb.toString();
			sb.delete(0, sb.length());
			sb.setLength(0);

		} catch(Exception e) {
			throw new EMPException(e);
		}

		return retuStr;
	}

	public String getAType() { 
		return "LS";
	}


}
