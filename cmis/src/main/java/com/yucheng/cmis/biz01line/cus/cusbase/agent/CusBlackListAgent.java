package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.dao.CusBlkListDao;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBlkList;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;

public class CusBlackListAgent extends CMISAgent {

	 /**
	    * 查询指定条件下的客户是否是不易贷款户
	    * @param certType   证件类型
	    * @param certCode   证件号码
	     * @param conn      连接
	    * @return
	 * @throws Exception 
	    */
	    public  int  getCusBlkList(String certType,String certCode) throws Exception{
	    	int blkFlag = 1;
	    	Connection conn=this.getConnection();
	    	CusBlkListDao dao = new CusBlkListDao();
	    	blkFlag = dao.getCusBlkList(certType, certCode, conn);
	    	return blkFlag;
	    }
		/**
		 * 通过condition 查询不宜客户情况
		 * @param certType
		 * @param certCode
		 * @return
		 * @throws EMPException
		 */
		public List<CMISDomain> getCusBlkListByCondition(String condition)throws EMPException{
			return this.findListByCondition(CusBlkList.class, condition);
		}
}
