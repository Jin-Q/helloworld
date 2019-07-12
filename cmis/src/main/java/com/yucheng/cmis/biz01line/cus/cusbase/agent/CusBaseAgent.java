package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.cusbase.dao.CusBaseDao;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
 

public class CusBaseAgent extends CMISAgent {

	/**
	 * 根据客户的证件信息查询该客户是否已经存在
	 * @param certCode 证件号码
	 * @param certType 证件类型
	 * @return
	 * @throws SQLException 
	 */
	public CusBase findCusBaseByComCert(String certCode, String certType)throws Exception {
		CusBase cusBase=null;
//		CusBaseDao cusDao = new CusBaseDao();
		CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		cusBase = cusDao.findCusBaseByCert(certCode, certType, this.getConnection());
		return cusBase;
	}
	
	/**
	 * 根据客户的证件信息查询该客户关联信息
	 * @param certCode 证件号码
	 * @param certType 证件类型
	 * @return
	 * @throws SQLException 
	 */
	public KeyedCollection findCusBaseByComCert1(String certCode, String certType)throws Exception {
		KeyedCollection cusBase=null;

		CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		cusBase = cusDao.findCusBaseByCert1(certCode, certType, this.getConnection());
		return cusBase;
	}
	
	/**
	 * 向表Cus_Base中新增一条记录
	 * @param cusBase
	 * @return
	 */
	public String insertCusBase(CusBase cusBase)throws AgentException{
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		// 新增客户基表信息
		int intMessage = this.insertCMISDomain(cusBase, PUBConstant.CUSBASE);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// "添加客户为："+cusBase.getCusName()+"的基本信息失败！";
		}
		return strMessage;
	}
	
	/**
	 * 新增对公客户信息
	 * @param cusCom
	 * @return
	 * @throws AgentException
	 */
	public String insertCusCom(CusCom cusCom)throws AgentException{
		String strMessage = CMISMessage.DEFEAT; // 错误信息
		// 新增对公客户基本信息
		int intMessage = this.insertCMISDomain(cusCom, PUBConstant.CUSCOM);
		if (1 == intMessage) {
			strMessage = CMISMessage.SUCCESS;// "添加客户为："+cusCom.getCusName()+"的基本信息失败！";
		}
		return strMessage;
	}
	/**
	 * 新增对私客户信息
	 * @param cusCom
	 * @return
	 * @throws AgentException
	 */
	public String insertCusIndiv(CusIndiv cusIndiv)throws AgentException{
		String strMessage = CMISMessage.DEFEAT; // 错误信息
		// 新增对私客户基本信息
		int intMessage = this.insertCMISDomain(cusIndiv, PUBConstant.CUSINDIV);
		if (1 == intMessage) {
			strMessage = CMISMessage.SUCCESS;// "添加客户为："+cusCom.getCusName()+"的基本信息失败！";
		}
		return strMessage;
	}
	
	/**
	 * 修改客户cusId的主管客户经理为custMgr，用于托管和移交操作。
	 * @param cusId  客户编号
	 * @param cusType  客户类型
	 * @param custMgr  主管客户经理
	 * @param conn  连接
	 * @return
	 */
	public String updateCusByCusId(String cusId,String receiverBrId,String cusType,String custMgr, String mainCusMgr)throws Exception{
		
		String str=CMISMessage.ADDDEFEAT;
//		CusBaseDao cusDao = new CusBaseDao();
		CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		str = cusDao.updateCusByCusId(cusId, receiverBrId, cusType, custMgr, mainCusMgr, this.getConnection());
		return str;
	}
	
    /**
     * 根据客户码获得CusBase的DOMAIN
     * */
	public CusBase getCusBaseDomainByCusId(String cusId) throws EMPException {
		CusBase cb = new CusBase();
		cb = (CusBase) this.findCMISDomainByKeyword(cb, PUBConstant.CUSBASE, cusId);
		return cb;
	}

	//根据客户id名获取客户需要属性并放入相应iColl中
	public void  getICollCusById(IndexedCollection iColl,Map<String,String> baseColMap, Map<String,String> colMap, String cusIdName) throws Exception{
		  String retMsg="";
		  CusBase cusBase;
		  String cusId="";
	      try{
	    	  if(iColl==null) {
		    	  retMsg= "<<<<<<< 结果集为空！";
		    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
		    	  return ;
		      }
//		      CusBaseDao cusDao = new CusBaseDao();
		      CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		      for(int i=0;i<iColl.size();i++){
		    	  KeyedCollection kColl=(KeyedCollection)iColl.get(i);
		    	  CusIndiv cusIndiv=new CusIndiv();
		    	  CusCom cusCom=new CusCom();
		    	  cusId = kColl.getDataValue(cusIdName).toString();
		    	  cusBase=cusDao.findCusBaseById(cusId,this.getConnection());
			      if(cusBase==null||cusBase.getCusId()==null||cusBase.getCusId().equals("")) {
			    	  retMsg="<<<<<< [客户码="+cusId+"]证件号码对应的客户信息不存在!"; 			    	  
			    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
			    	  continue;
			      }
			      ComponentHelper componetHelper = new ComponentHelper();
			      String belgLine=cusBase.getBelgLine();
			      //取base表属性
			      if(baseColMap!=null&&baseColMap.size()>0){
			    	  KeyedCollection kCollBase = componetHelper.domain2kcol(cusBase, "CusBase");
			    	  kColl2kColl(kColl,kCollBase,baseColMap);
			      }
			      //取明细表属性
			      if(colMap!=null&&colMap.size()>0){
			    	  if(belgLine.equals("BL300")){
			    		  cusIndiv=(CusIndiv) this.findCMISDomainByKeyword(cusIndiv, "CusIndiv", cusId);
			    		  KeyedCollection _kColl = componetHelper.domain2kcol(cusIndiv, "CusIndiv");
			    		  kColl2kColl(kColl,_kColl,colMap);
			    	  }else {
			    		  cusCom=(CusCom) this.findCMISDomainByKeyword(cusCom, "CusCom", cusId);
			    		  KeyedCollection _kColl = componetHelper.domain2kcol(cusCom, "CusCom");
			    		  kColl2kColl(kColl,_kColl,colMap);
			    	  }
			      }
		      }
	      }catch(Exception e){
	    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,e.toString());
	    	  throw e;
	      }
		 
		return ;
	  }
	
	public void  getKCollCusById(KeyedCollection kColl,Map<String,String> baseColMap, Map<String,String> colMap,String cusIdName){
		  String retMsg="";
		  CusBase cusBase;
		  String cusId="";
	      try{
	    	  if(kColl==null||kColl.size()<=0) {
		    	  retMsg= "<<<<<< 结果集为空或要转换的字段为空！";
		    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
		    	  return ;
		      }
//		      CusBaseDao cusDao = new CusBaseDao();
	    	  CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		      cusId = kColl.getDataValue(cusIdName).toString();
	    	  CusIndiv cusIndiv=new CusIndiv();
	    	  CusCom cusCom=new CusCom();
	    	 
	    	  cusBase=cusDao.findCusBaseById(cusId, this.getConnection());
		      if(cusBase==null||cusBase.getCusId()==null||cusBase.getCusId().equals("")) {
		    	  retMsg="<<<<<<<<<<<[客户码="+ cusId+"]客户码对应的客户信息不存在!";
		    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
		    	  return ;
		      }
		      ComponentHelper componetHelper = new ComponentHelper();
		      String belgLine=cusBase.getBelgLine();
		      //取base表属性
		      if(baseColMap!=null&&baseColMap.size()>0){
		    	  KeyedCollection kCollBase = componetHelper.domain2kcol(cusBase, "CusBase");
		    	  kColl2kColl(kColl,kCollBase,baseColMap);
		      }
		      //取明细表属性
		      if(colMap!=null&&colMap.size()>0){
		    	  if(belgLine.equals("BL300")){
		    		  cusIndiv=(CusIndiv) this.findCMISDomainByKeyword(cusIndiv, "CusIndiv", cusId);
		    		  KeyedCollection _kColl = componetHelper.domain2kcol(cusIndiv, "CusIndiv");
		    		  kColl2kColl(kColl,_kColl,colMap);
		    	  }else {
		    		  cusCom=(CusCom) this.findCMISDomainByKeyword(cusCom, "CusCom", cusId);
		    		  KeyedCollection _kColl = componetHelper.domain2kcol(cusCom, "CusCom");
		    		  kColl2kColl(kColl,_kColl,colMap);
		    	  }
		      }
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,e.toString());
	      }
		return ;
	  }
	
	 public void  getICollCus(IndexedCollection iColl,Map<String,String> colMap, Map<String,String> pkMap) throws Exception{
		  String retMsg="";
		  CusBase cusBase;
	      String certTypName="cert_type";
	      String certCodName="cert_code";
	      try{
	    	  if(iColl==null||iColl.size()<=0||colMap.size()<=0) {
		    	  retMsg= "<<<<<<< 结果集为空或要转换的字段为空！";
		    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
		    	  return ;
		      }
		      certTypName=(String)pkMap.get("cert_type");
		      certCodName=(String)pkMap.get("cert_code");
//		      CusBaseDao cusDao = new CusBaseDao();
		      CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		      for(int i=0;i<iColl.size();i++){
		    	  KeyedCollection kColl=(KeyedCollection)iColl.get(i);
		    	  String certType=kColl.containsKey(certTypName)?(String) kColl.getDataValue(certTypName):"";
		    	  String certCode=kColl.containsKey(certCodName)?(String) kColl.getDataValue(certCodName):"";
		    	  String modelId="CusIndiv";
		    	  String cus_id="";
		    	  CusIndiv cusIndiv=new CusIndiv();
		    	  CusCom cusCom=new CusCom();
		    	 
		    	  if(certType.startsWith("2"))modelId="CusCom";
		    	  cusBase=cusDao.findCusBaseByCert(certCode,certType,this.getConnection());
			      if(cusBase==null||cusBase.getCusId()==null||cusBase.getCusId().equals("")) {
			    	  retMsg="<<<<<< [证件类型="+ certType+"证件号码="+certCode+"]证件号码对应的客户信息不存在!"; 			    	  
			    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
			    	  continue;
			      }
			      ComponentHelper componetHelper = new ComponentHelper();
			      cus_id=cusBase.getCusId();
			      if(modelId.equals("CusIndiv")){
			    	  cusIndiv=(CusIndiv) this.findCMISDomainByKeyword(cusIndiv, modelId, cus_id);
				      KeyedCollection _kColl = componetHelper.domain2kcol(cusIndiv, "CusIndiv");
				      kColl2kColl(kColl,_kColl,colMap);
			      }else if(modelId.equals("CusCom")){
			    	  cusCom=(CusCom) this.findCMISDomainByKeyword(cusCom, modelId, cus_id);
				      KeyedCollection _kColl = componetHelper.domain2kcol(cusCom, "cusCom");
				      kColl2kColl(kColl,_kColl,colMap);
			      }
		      }
	      }catch(Exception e){
	    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,e.toString());
	    	  throw e;
	      }
		 
		return ;
	}

	
	 public void  getKCollCus(KeyedCollection kColl,Map<String,String> colMap, Map<String,String> pkMap){
		  String retMsg="";
		  CusBase cusBase;
	      String certTypName="cert_type";
	      String certCodName="cert_code";
	      try{
	    	  if(kColl==null||kColl.size()<=0||colMap.size()<=0) {
		    	  retMsg= "<<<<<< 结果集为空或要转换的字段为空！";
		    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
		    	  return ;
		      }
		      certTypName=(String)pkMap.get("cert_type");
		      certCodName=(String)pkMap.get("cert_code");
//		      CusBaseDao cusDao = new CusBaseDao();
		      CusBaseDao cusDao = (CusBaseDao)this.getDaoInstance("CusBase");
		      String certType=kColl.containsKey(certTypName)?(String) kColl.getDataValue(certTypName):"";
	    	  String certCode=kColl.containsKey(certCodName)?(String) kColl.getDataValue(certCodName):"";
		    	  String modelId="CusIndiv";
		    	  String cus_id="";
		    	  CusIndiv cusIndiv=new CusIndiv();
		    	  CusCom cusCom=new CusCom();
		    	 
		    	  if(certType.startsWith("2"))modelId="CusCom";
		    	  cusBase=cusDao.findCusBaseByCert(certCode,certType,this.getConnection());
			      if(cusBase==null||cusBase.getCusId()==null||cusBase.getCusId().equals("")) {
			    	  retMsg="<<<<<<<<<<<[证件类型="+ certType+"证件号码="+certCode+"]证件号码对应的客户信息不存在!";
			    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,retMsg);
			    	  return ;
			      }
			      ComponentHelper componetHelper = new ComponentHelper();
			      cus_id=cusBase.getCusId();
			      if(modelId.equals("CusIndiv")){
			    	  cusIndiv=(CusIndiv) this.findCMISDomainByKeyword(cusIndiv, modelId, cus_id);
				      KeyedCollection _kColl = componetHelper.domain2kcol(cusIndiv, "CusIndiv");
				      kColl2kColl(kColl,_kColl,colMap);
			      }else if(modelId.equals("CusCom")){
			    	  cusCom=(CusCom) this.findCMISDomainByKeyword(cusCom, modelId, cus_id);
				      KeyedCollection _kColl = componetHelper.domain2kcol(cusCom, "cusCom");
				      kColl2kColl(kColl,_kColl,colMap);
			      }
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,e.toString());
	      }
		return ;
	  }
	 
	public void kColl2kColl(KeyedCollection kColl,KeyedCollection _kColl,Map<String,String> map){
		 Iterator<Entry<String, String>> it = map.entrySet().iterator();
	     Map.Entry<String, String>  entry = null;
	     if (it != null) {
			while (it.hasNext()) {
				entry = (Map.Entry<String, String>) it.next();	
			   try{
				    String key=(String)entry.getKey();
					String keyValue = "";
					if(_kColl.containsKey((String)entry.getValue())){
						keyValue=(String) _kColl.getDataValue((String)entry.getValue());
						if(kColl.containsKey(key)){
							kColl.setDataValue(key,keyValue) ;
						}else{
							kColl.addDataField(key, keyValue);
						}
					}else{
						kColl.addDataField(key, keyValue);
					}
			   }catch(Exception e){
				   e.printStackTrace();
				   EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,e.toString());
				   continue;
			   }
			}
		}
	}
		
		
	/**
	 * 更新客户基本信息
	 * @param CusBase 客户基本信息
	 * @return 暂定返回null 表示成功 其他表示异常
	 * @throws EMPException
	 */
	public String update(CusBase cusBase) throws EMPException {
		String strMessage = CMISMessage.MODIFYDEFEAT; // 错误信息
		// 更新信息
		int count = this.modifyCMISDomain(cusBase, PUBConstant.CUSBASE);// 1成功
		// 如果失败，给标志信息赋值
		if (1 == count) {
			strMessage = CMISMessage.SUCCESS; // 成功
		}
		return strMessage;
	}
	
	public boolean delCusTrusteeRecord(String consignor_id, String trustee_id) throws Exception {
//		CusBaseDao dao = new CusBaseDao();
		CusBaseDao dao = (CusBaseDao)this.getDaoInstance("CusBase");
		return dao.delCusTrusteeRecord(consignor_id,trustee_id, this.getConnection());
	}
	
	public void delSubmitRecord(String tableName, String serno) throws SQLException, ComponentException {
		CusBaseDao dao = (CusBaseDao)this.getDaoInstance("CusBase");
		dao.delSubmitRecord(tableName, serno);
	}
	
	/**
	 * 获取该客户经理下所有客集合
	 * @param custMgr
	 * @return
	 * @throws ComponentException
	 */
	public List<CMISDomain> findCusListByCustMgr(String custMgr) throws ComponentException{
		List<CMISDomain> cusBaseList = new ArrayList<CMISDomain>();
		Connection connection=this.getConnection();
		CusBase cusBase = new CusBase();
		String modelId = PUBConstant.CUSBASE;
		IndexedCollection icol;
		TableModelDAO dao = this.getTableModelDAO();
		StringBuffer conditionStr = new StringBuffer("");
		conditionStr.append(" where  cust_mgr='"+custMgr+"'");
		/**客户移交申请增加临时客户    modefied by zhaoxp 2015-02-10 start*/
		conditionStr.append(" and cus_status in ('00','20','04')");
		/**客户移交申请增加临时客户    modefied by zhaoxp 2015-02-10 end*/
	
		try{
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("cus_name");
			list.add("cus_type");
			list.add("cert_type");
			list.add("cert_code");
			list.add("cust_mgr");
			list.add("main_br_id");
			list.add("belg_line");
			icol = dao.queryList(modelId,list, conditionStr.toString(),connection);
			ComponentHelper cHelper = new ComponentHelper();
			cusBaseList = cHelper.icol2domainlist(cusBase, icol);			
		}
		catch(EMPJDBCException e){
			throw new ComponentException(e);
		}
		catch(CMISException e2){
			throw new ComponentException(e2);
		}	
		return cusBaseList;
	}
	
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model
	 * @param conditionFields
	 * @return
	 * @throws AgentException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws AgentException {
		int count = 0;
		try {
			CusBaseDao cusBaseDao = (CusBaseDao)this.getDaoInstance("CusBase");
			count = cusBaseDao.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new AgentException("根据合作方流水号删除关联信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
}
