package com.yucheng.cmis.biz01line.psp.msi.msiimple;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.psp.msi.PspServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;

public class PspServiceInterfaceImple extends CMISModualService implements PspServiceInterface {
	private static final Logger logger = Logger.getLogger(PspServiceInterfaceImple.class);

	/**
	 * 根据押品编号获得押品重估信息
	 * @param guaranty_no 押品编号
	 * @return res_value 返回押品重估信息列表
	 */
	public IndexedCollection getPspGuarantyValueReevalList(String guaranty_no,Connection connection ,Context context) throws EMPException{
		IndexedCollection res_value = null;
		logger.info("---------------调用贷后管理模块接口获得押品重估信息开始---------------");
		if(null==guaranty_no ){ //标志位为空时
			logger.error("调用贷后管理模块接口获得押品重估信息错误，押品编号[guaranty_no]传入值："+guaranty_no+"不能为空！");
			throw new EMPException("调用贷后管理模块接口获得押品重估信息错误，押品编号[guaranty_no]传入值："+guaranty_no+"不能为空！");
		}
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			res_value = dao.queryList("PspGuarantyValueReeval","where guaranty_no = '"+guaranty_no+"'", connection);
		}catch (Exception e) {
			logger.error("获得押品重估信息失败，错误描述："+e.getMessage());
			throw new EMPException("获得押品重估信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用贷后管理模块接口获得押品重估信息结束---------------");
		return res_value;
	}
}
