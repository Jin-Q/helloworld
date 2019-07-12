package com.yucheng.cmis.pub.sequence;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;

public interface CMISSequenceTemplate {

	public String format(String owner, String cur_sernum, Context context) throws EMPException;

	public String getAType();

}
