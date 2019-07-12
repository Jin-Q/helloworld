package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class ReturnMortGuarantyCertiInfoRecordOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			/*//权证编号
			String warrant_no = "";
			//权证类型
			String warrant_type = "";
			//押品编号
			String guaranty_no = "";
			try {
				warrant_no = (String)context.getDataValue("warrant_no");
				warrant_no = java.net.URLDecoder.decode(warrant_no, "UTF-8");
				
				warrant_type = (String)context.getDataValue("warrant_type");
				guaranty_no = (String)context.getDataValue("guaranty_no");
			} catch (Exception e) {}
			if(warrant_no == null || warrant_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+warrant_no+"] cannot be null!");
			HashMap map = new HashMap();
			map.put("warrant_type", warrant_type);
			map.put("warrant_no", warrant_no);
			KeyedCollection mortGuarantyCertiInfokColl = dao.queryDetail("MortGuarantyCertiInfo", map, connection);
			if(mortGuarantyCertiInfokColl!=null&&mortGuarantyCertiInfokColl.getDataValue("warrant_no")!=null&&!"".equals(mortGuarantyCertiInfokColl.getDataValue("warrant_no"))){
				mortGuarantyCertiInfokColl.put("warrant_state", "3");//更新状态为在库
				dao.update(mortGuarantyCertiInfokColl, connection);
			}
			KeyedCollection mortstorexwadetailkColl= new KeyedCollection("MortStorExwaDetail");
			*//** 通过业务流水号查询出生成质押出入库从表信息**//*
			IndexedCollection mortstorexwadetailiColl = dao.queryList("MortStorExwaDetail","where guaranty_no ='"+guaranty_no+"' and warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and warrant_state = '4' ", connection);
			if(mortstorexwadetailiColl!=null&&mortstorexwadetailiColl.size()>0){
				for(int i=0;i<mortstorexwadetailiColl.size();i++){
					mortstorexwadetailkColl = (KeyedCollection) mortstorexwadetailiColl.get(i);
					mortstorexwadetailkColl.put("warrant_state", "3");//更新状态为在库
					dao.update(mortstorexwadetailkColl, connection);
				}
			}*/
			//归还入库操作
			//押品编号
			String guaranty_no ="";
			//权证编号
			String warrant_no = "";
			//权证类型
			String warrant_type = "";
			//原权证状态
			String warrant_state = "";
			//权证信息kColl
			
			KeyedCollection kColl = new KeyedCollection("MortGuarantyCertiInfo");
			//押品信息kColl
			KeyedCollection kCol = new KeyedCollection("MortGuarantyBaseInfo");
			
			//押品基本信息kColl
			kCol.addDataField("guaranty_no","");
			kCol.addDataField("guaranty_info_status","3");
			String serno = "";
			
        	//押品编号
			guaranty_no = (String) context.getDataValue("guaranty_no");
			kCol.setDataValue("guaranty_no",guaranty_no);
			//更新押品状态
		    //int cou=dao.update(kCol, connection);
        	serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
        	//新增入库申请主表信息
			KeyedCollection kCollInfo = new KeyedCollection("MortStorExwaInfo");
			kCollInfo.addDataField("serno", serno);
			kCollInfo.addDataField("stor_exwa_mode","04");
			kCollInfo.addDataField("manager_id", context.getDataValue("currentUserId"));
			kCollInfo.addDataField("manager_br_id", context.getDataValue("organNo"));
			kCollInfo.addDataField("input_id", context.getDataValue("currentUserId"));
			kCollInfo.addDataField("input_br_id", context.getDataValue("organNo"));
			kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
			kCollInfo.addDataField("approve_status", "997");
			dao.insert(kCollInfo, connection);
			//入库申请明细
			warrant_type = (String) context.getDataValue("warrant_type");
			warrant_no = (String) context.getDataValue("warrant_no");
			
			//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
			warrant_no = URLDecoder.decode(warrant_no,"UTF-8");
			
			KeyedCollection kCollDetail = new KeyedCollection("MortStorExwaDetail");
			kCollDetail.addDataField("serno",serno);
			kCollDetail.addDataField("guaranty_no",guaranty_no);
			kCollDetail.addDataField("warrant_no",warrant_no);
			kCollDetail.addDataField("warrant_type",warrant_type);
			kCollDetail.addDataField("warrant_state","5");
			kCollDetail.addDataField("ori_warrant_state","4");
			int count1=dao.insert(kCollDetail, connection);
			kColl.addDataField("warrant_no", warrant_no);
			kColl.addDataField("warrant_type", warrant_type);
			kColl.addDataField("warrant_state", "5");
			kColl.addDataField("guaranty_no",guaranty_no);
			int count=dao.update(kColl, connection);

			MortFlowComponent MortFlowComponent = (MortFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("MortFlowComponent",context,connection );
			//审批通过的同时生成授权
			MortFlowComponent.doWfAgreeForMort(serno);
			/**调用ESB接口，发送报文*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			try{
				serviceRel.tradeDZYWQZCRKYM(serno, context, connection);
				context.addDataField("flag", "success");
				context.addDataField("serno", serno);
			}catch(Exception e){
				context.addDataField("flag", "抵质押物权证出/入库授权发送失败!");
				e.printStackTrace();
				throw new EMPException("抵质押物权证出/入库授权发送失败!");
			}
		}catch (EMPException ee) {
			context.put("flag", "error");
			throw ee;
		} catch(Exception e){
			context.put("flag", "error");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
