package com.yucheng.cmis.biz01line.lmt.agent.intbank;

import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.lmt.dao.intbank.LmtIntbankDao;
import com.yucheng.cmis.pub.CMISAgent;

   public class LmtIntbankLmtAgent extends CMISAgent {
	  public void insertcus2batchlist(Map<String,String> map) throws EMPException{
		  try{
			  LmtIntbankDao intbankdao = (LmtIntbankDao)this.getDaoInstance("LmtIntbank");
				intbankdao.insertcus2batchlist(map);
		  }catch(Exception e){
			  throw new EMPException(e);
		  }
				
	   }
	  public IndexedCollection QueryCusInfoList(String batchNo)throws EMPException{
		  try{
			  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
			  return intbankDao.QueryCusInfoList(batchNo);
		  }catch(Exception e){
			  throw new EMPException(e);
		  }		  
	  }
	//将单笔授信插入台账
	  public KeyedCollection insert2kColl(KeyedCollection kColl) throws EMPException{
		  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  return intbankDao.insert2kColl(kColl);
	  }
	  public KeyedCollection insert2kColl4Agr(KeyedCollection kColl) throws EMPException{
		  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  return intbankDao.insert2kColl4Agr(kColl);
	  }
	  //将批量授信包里的单户授信逐一插入台账中
	  public KeyedCollection insert2kColl4Acc(KeyedCollection kColl) throws EMPException{
		  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  return intbankDao.insert2kColl4Acc(kColl);
	  }
	  //获取剩余额度
	  public double getOddAmt(KeyedCollection kColl) throws EMPException{
		  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  return intbankDao.getOddAmt(kColl);
	  }
	  public void DeleteLmtSub(String serno) throws EMPException{
		  LmtIntbankDao intbankDao = (LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		   intbankDao.DeleteLmtSub(serno);
	  }
	  public void updateSub(IndexedCollection iColl) throws EMPException{
		  LmtIntbankDao intbankDao=(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  intbankDao.updateSub(iColl);
	  }
	//根据批量客户编号修改批量包的状态
	  public void updateStatus(String batch_cus_no) throws EMPException{
		  LmtIntbankDao intbankDao =(LmtIntbankDao)this.getDaoInstance("LmtIntbank");
		  intbankDao.updateStatus(batch_cus_no);
	  }
	

}
