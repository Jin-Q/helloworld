package com.yucheng.cmis.biz01line.psp.op.rectask;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class CreateRscTaskInfoSubExcelOp extends CMISOperation {
	private final String modelId = "RscTaskInfoSub";
	/**
     * 
     * <p>
     * <h2>简述</h2>
     * 		<ol>生成可供下载的Excel模版</ol>
     * <h2>功能描述</h2>
     * 		<ol>xml配置中，operation必须要有，所以有个空方法</ol>
     * </p>
     * @param context
     * @return
     * @throws EMPException
     */
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		return "0";
	}

}
