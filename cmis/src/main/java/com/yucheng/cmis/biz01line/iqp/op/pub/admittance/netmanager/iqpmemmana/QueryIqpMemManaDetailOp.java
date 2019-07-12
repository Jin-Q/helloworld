package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpmemmana;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpMemManaDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpMemMana";
	
	private final String net_agr_no_name = "net_agr_no";
	private final String mem_cus_id_name = "mem_cus_id";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = context.getDataValue("cus_id").toString();
			String mem_cus_id = context.getDataValue("mem_cus_id").toString();
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}						
			String net_agr_no = null;
			try {
				net_agr_no = (String)context.getDataValue(net_agr_no_name);
			} catch (Exception e) {}
			if(net_agr_no == null || net_agr_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+net_agr_no_name+"] cannot be null!");	
			
			String mem_cus_id_value = null;
			try {
				mem_cus_id_value = (String)context.getDataValue(mem_cus_id_name);
			} catch (Exception e) {}
			if(mem_cus_id_value == null || mem_cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+mem_cus_id_name+"] cannot be null!");
			
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("net_agr_no",net_agr_no);
			pkMap.put("mem_cus_id",mem_cus_id_value);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			kColl.addDataField("cus_id", cus_id);
			kColl.setDataValue("mem_cus_id", mem_cus_id);
			String[] args=new String[] { "mem_cus_id","cus_id"};
		    String[] modelIds=new String[]{"CusBase","CusBase"};
		    String[] modelForeign=new String[]{"cus_id","cus_id"};
		    String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			
			//得到授信接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			//通过授信接口查询客户所有供应链授信
			IndexedCollection lmt_iColl = lmtservice.searchLmtAgrDetailsList(mem_cus_id, "05",cus_id, connection);
			lmt_iColl.setName("LmtAgrDetailsList");
			
			//统计供应链下授信总额度
//			BigDecimal total = new BigDecimal("0");
//			if(null != lmt_iColl){
//				for (Iterator<KeyedCollection> iterator = lmt_iColl.iterator(); iterator.hasNext();) {
//					KeyedCollection lmt_kColl = (KeyedCollection) iterator.next();
//					total = total.add(new BigDecimal(lmt_kColl.getDataValue("crd_amt").toString()));
//				}
//			}
//			
//			context.addDataField("total", total);
			
			/**翻译额度名称**/
			args = new String[] { "limit_name" };
			modelIds = new String[]{"PrdBasicinfo"};
			modelForeign = new String[]{"prdid"};
			fieldName = new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(lmt_iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			
			this.putDataElement2Context(lmt_iColl, context);
			
			IndexedCollection kColl_BizType = setBizType();
			this.putDataElement2Context(kColl_BizType, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	private IndexedCollection setBizType() throws EMPException{
		IndexedCollection dicIcoll = new IndexedCollection("STD_BIZ_TYPE");
		try{
			KeyedCollection kTmp1 = new KeyedCollection();
			kTmp1.addDataField("enname", "0");
			kTmp1.addDataField("cnname","先票后货");
			dicIcoll.addDataElement(kTmp1);
			
			KeyedCollection kTmp2= new KeyedCollection();
			kTmp2.addDataField("enname", "1");
			kTmp2.addDataField("cnname","保兑仓");
			dicIcoll.addDataElement(kTmp2);
			
			KeyedCollection kTmp3 = new KeyedCollection();
			kTmp3.addDataField("enname", "2");
			kTmp3.addDataField("cnname","阶段性担保+货押");
			dicIcoll.addDataElement(kTmp3);
			
			KeyedCollection kTmp4 = new KeyedCollection();
			kTmp4.addDataField("enname", "3");
			kTmp4.addDataField("cnname","应收账款（池）质押融资");
			dicIcoll.addDataElement(kTmp4);
			
			KeyedCollection kTmp5 = new KeyedCollection();
			kTmp5.addDataField("enname", "4");
			kTmp5.addDataField("cnname","国内保理");
			dicIcoll.addDataElement(kTmp5);
			
			KeyedCollection kTmp6 = new KeyedCollection();
			kTmp6.addDataField("enname", "5");
			kTmp6.addDataField("cnname","存货质押");
			dicIcoll.addDataElement(kTmp6);
			
			KeyedCollection kTmp7 = new KeyedCollection();
			kTmp7.addDataField("enname", "6");
			kTmp7.addDataField("cnname","票据池融资");
			dicIcoll.addDataElement(kTmp7);
			
		}catch(EMPException e){
			throw new EMPException(""+e.getMessage());
		}
		
		return dicIcoll;
	}
}
