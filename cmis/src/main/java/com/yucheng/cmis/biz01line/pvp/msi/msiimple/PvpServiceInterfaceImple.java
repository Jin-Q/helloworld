package com.yucheng.cmis.biz01line.pvp.msi.msiimple;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.biz01line.pvp.msi.PvpServiceInterface;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;

public class PvpServiceInterfaceImple extends CMISModualService implements PvpServiceInterface {

	/**
	 * 通过合同编号生成出账借据号
	 * @param contNo 合同编号
	 * @param connection 数据库连接
	 * @return 借据编号
	 * @throws Exception 
	 */
	public String getBillNoByContNo(String contNo, Context context, Connection connection) throws Exception {
		String billNo = "";
		PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
		billNo = cmisComponent.getBillNoByContNo(contNo);
		return billNo;
	}

}
