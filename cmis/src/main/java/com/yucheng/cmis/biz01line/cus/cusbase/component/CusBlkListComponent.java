package com.yucheng.cmis.biz01line.cus.cusbase.component;

import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBlackListAgent;
import com.yucheng.cmis.pub.CMISComponent;

public class CusBlkListComponent extends CMISComponent {
	
 
   /**
    * 查询指定条件下的客户是否是不易贷款户
    * @param certType   证件类型
    * @param certCode   证件号码
    * @return
 * @throws Exception 
    */
    public  int  getCusBlkList(String certType,String certCode) throws Exception{
    	
    	int blkFlag = 1;
    	
    	CusBlackListAgent cusBlkListAgent = (CusBlackListAgent) this.getAgentInstance("CusBlkListAgent");
    	blkFlag = cusBlkListAgent.getCusBlkList(certType,certCode);
    	return blkFlag;
    }
}
