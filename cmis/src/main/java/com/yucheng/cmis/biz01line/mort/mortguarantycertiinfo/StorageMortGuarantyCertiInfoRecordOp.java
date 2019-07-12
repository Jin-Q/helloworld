package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class StorageMortGuarantyCertiInfoRecordOp extends CMISOperation {
	

	private final String modelId = "MortStorExwaInfo";//出入库申请
	private final String modelId1 = "MortStorExwaDetail";//出入库明细
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("stor")&&"storage".equals(context.getDataValue("stor"))){//入库操作
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
	            if(context.containsKey("serno")){
	            	serno=(String) context.getDataValue("serno");
	            	String guaranty_no1 = (String) context.getDataValue("guarantyNoStr");
	            	String[]guarantyNoStr = guaranty_no1.split(",");
	            	for(int i=0;i<guarantyNoStr.length;i++){
	            		guaranty_no =guarantyNoStr[i];
	            		kCol.setDataValue("guaranty_no",guaranty_no);
	            		//更新押品状态
	    			    //int cou=dao.update(kCol, connection);
	            	}
	            }else{
	            	//押品编号
					guaranty_no = (String) context.getDataValue("guaranty_no");
					kCol.setDataValue("guaranty_no",guaranty_no);
					//更新押品状态
				    //int cou=dao.update(kCol, connection);
	            	serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
	            	//新增入库申请主表信息
					KeyedCollection kCollInfo = new KeyedCollection(modelId);
					kCollInfo.addDataField("serno", serno);
					kCollInfo.addDataField("stor_exwa_mode","04");
					kCollInfo.addDataField("manager_id", context.getDataValue("currentUserId"));
					kCollInfo.addDataField("manager_br_id", context.getDataValue("organNo"));
					kCollInfo.addDataField("input_id", context.getDataValue("currentUserId"));
					kCollInfo.addDataField("input_br_id", context.getDataValue("organNo"));
					kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
					kCollInfo.addDataField("approve_status", "997");
					dao.insert(kCollInfo, connection);
	            }
				//入库申请明细
				String warrant_type1 = (String) context.getDataValue("warrantTypeNoStr");
				String warrant_no1 = (String) context.getDataValue("warrantNoStr");
				
				//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
				warrant_no1 = URLDecoder.decode(warrant_no1,"UTF-8");
				
				String guaranty_no1 = (String) context.getDataValue("guarantyNoStr");
				String warrant_state1 = (String) context.getDataValue("warrantStateStr");
				String[]warrantTypeStr = warrant_type1.split(",");
				String[]warrantNoStr = warrant_no1.split(",");
				String[]guarantyNoStr = guaranty_no1.split(",");
				String[]warrantStateStr = warrant_state1.split(",");
				KeyedCollection kCollDetail = new KeyedCollection(modelId1);
				kCollDetail.addDataField("serno",serno);
				kCollDetail.addDataField("guaranty_no","");
				kCollDetail.addDataField("warrant_no","");
				kCollDetail.addDataField("warrant_type","");
				kCollDetail.addDataField("warrant_state","");
				kCollDetail.addDataField("ori_warrant_state","");
				//权证信息kColl
				kColl.addDataField("warrant_no", "");
				kColl.addDataField("warrant_type","");
				kColl.addDataField("warrant_state", "");
				kColl.addDataField("guaranty_no","");
				for(int i=0;i<warrantTypeStr.length;i++){
					guaranty_no=guarantyNoStr[i];
					warrant_no=warrantNoStr[i];
					warrant_type=warrantTypeStr[i];
					warrant_state = warrantStateStr[i];
					if("1".equals(warrant_state)||"6".equals(warrant_state)||"7".equals(warrant_state)){//登记，出库，核销时，改为入库记账中
						kCollDetail.setDataValue("guaranty_no",guaranty_no);
						kCollDetail.setDataValue("warrant_no",warrant_no);
						kCollDetail.setDataValue("warrant_type",warrant_type);
						kCollDetail.setDataValue("warrant_state","2");
						kCollDetail.setDataValue("ori_warrant_state",warrant_state);
						int count1=dao.insert(kCollDetail, connection);
						kColl.setDataValue("warrant_no", warrant_no);
						kColl.setDataValue("warrant_type", warrant_type);
						kColl.setDataValue("warrant_state", "2");
						kColl.setDataValue("guaranty_no",guaranty_no);
					}else if("4".equals(warrant_state)){//借出时，改为借出归还记账中
						kCollDetail.setDataValue("guaranty_no",guaranty_no);
						kCollDetail.setDataValue("warrant_no",warrant_no);
						kCollDetail.setDataValue("warrant_type",warrant_type);
						kCollDetail.setDataValue("warrant_state","5");
						kCollDetail.setDataValue("ori_warrant_state",warrant_state);
						int count1=dao.insert(kCollDetail, connection);
						kColl.setDataValue("warrant_no", warrant_no);
						kColl.setDataValue("warrant_type", warrant_type);
						kColl.setDataValue("warrant_state", "5");
						kColl.setDataValue("guaranty_no",guaranty_no);
					}
					//更新权证状态
					int count=dao.update(kColl, connection);
				}
				if("4".equals(warrant_state)){
					KeyedCollection kCollInfo = new KeyedCollection(modelId);
					kCollInfo.put("serno", serno);
					kCollInfo.addDataField("stor_exwa_mode","05");
					dao.update(kCollInfo, connection);
				}
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
					context.addDataField("msg", "授权成功！");
					context.addDataField("serno", serno);
				}catch(Exception e){
					DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
					try {
						Connection conn = dataSource.getConnection();
						conn.rollback();
					} catch (SQLException e1) {}
					e.printStackTrace();
					context.addDataField("flag", "fail");
					context.addDataField("msg", e.getMessage());
					context.addDataField("serno", serno);
				}
			}else if (context.containsKey("stor")&&"exwa".equals(context.getDataValue("stor"))){//出库操作
				//押品编号
				String guaranty_no = "";
				//权证编号
				String warrant_no = "";
				//权证类型
				String warrant_type = "";
				//权证状态
				String warrant_state = "";
	           
	            String serno = "";
	            if(context.containsKey("serno")){
	            	serno=(String) context.getDataValue("serno");
	            }else{
	            	KeyedCollection appInfoKc = new KeyedCollection(modelId);
	            	serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
	            	/**此处申请数据都赋默认值使数据保持完整，因为下面直接跳转到修改页面**/
		            appInfoKc.put("serno",serno);
		            appInfoKc.put("stor_exwa_mode", "01");//暂时设置为“取出还贷”，后面可以修改
		            appInfoKc.put("manager_id", context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));//责任人、责任机构默认设置为当前登录人
		            appInfoKc.put("manager_br_id", context.getDataValue(CMISConstance.ATTR_ORGID));
		            appInfoKc.put("input_id", context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
		            appInfoKc.put("input_br_id", context.getDataValue(CMISConstance.ATTR_ORGID));
		            appInfoKc.put("input_date", context.getDataValue("OPENDAY"));
		            appInfoKc.put("approve_status", "000");//默认设置为“待发起”状态 
	            	//新增出入库申请主表信息
					dao.insert(appInfoKc, connection);
	            }
	           
				String warrant_type1 = (String) context.getDataValue("warrantTypeNoStr");
				String warrant_no1 = (String) context.getDataValue("warrantNoStr");
				
				//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
				warrant_no1 = URLDecoder.decode(warrant_no1,"UTF-8");
				
				String guaranty_no1 = (String) context.getDataValue("guarantyNoStr");
				String warrant_state1 = (String) context.getDataValue("warrantStateStr");
				String[]warrantTypeStr = warrant_type1.split(",");
				String[]warrantNoStr = warrant_no1.split(",");
				String[]guarantyNoStr = guaranty_no1.split(",");
				String[]warrantStateStr = warrant_state1.split(",");
				//出库申请明细
				int count1=0;
				for(int i=0;i<warrantTypeStr.length;i++){
					KeyedCollection kColl = new KeyedCollection(modelId1);
					KeyedCollection kCollParam = new KeyedCollection();
					guaranty_no=guarantyNoStr[i];
					warrant_no=warrantNoStr[i];
					warrant_type=warrantTypeStr[i];
					warrant_state = warrantStateStr[i];
					kColl.put("serno",serno);
					kColl.put("guaranty_no",guaranty_no);
					kColl.put("warrant_no",warrant_no);
					kColl.put("warrant_type",warrant_type);
					kColl.put("warrant_state",warrant_state);
					kColl.put("ori_warrant_state",warrant_state);
					count1+=dao.insert(kColl, connection);
					/***发起出库申请后将权证状态改为“出库记账中” add by tangzf 2014.04.25********/
					kCollParam.put("warrant_no", warrant_no);
					kCollParam.put("warrant_type",warrant_type);
					SqlClient.update("updateMortCertiStatus", kCollParam, "9", null, connection);
				}
				if(count1<1){
					throw new EMPException("Update Failed! Record Count: " + count1);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "授权成功！");
				context.put("serno", serno);
			}
		}catch (EMPException ee) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
			ee.printStackTrace();
			context.addDataField("flag", "fail");
			context.addDataField("msg", ee.getMessage());
			context.addDataField("serno", "");
			//throw ee;
		} catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			context.addDataField("flag", "fail");
			context.addDataField("msg", e.getMessage());
			context.addDataField("serno", "");
			//throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}


	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
