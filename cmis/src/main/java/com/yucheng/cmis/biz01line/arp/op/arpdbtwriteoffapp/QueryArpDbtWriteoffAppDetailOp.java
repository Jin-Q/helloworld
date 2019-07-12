package com.yucheng.cmis.biz01line.arp.op.arpdbtwriteoffapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpDbtWriteoffAppDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpDbtWriteoffApp";
	private final String serno_name = "serno";	
	private boolean updateCheck = true;	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				if(context.containsKey("restrictUsed")&&(context.getDataValue("restrictUsed").toString()).equals("false")){
					
				}else{
					RecordRestrict recordRestrict = this.getRecordRestrict(context);
					recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
				}
			}

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			/*** 初步处理，从cus_base取需要字段，并得到条线 ***/
			String[] args = new String[] { "cus_id","cus_id","cus_id"};
			String[] modelIds = new String[] { "CusBase","CusBase","CusBase"};
			String[] modelForeign = new String[] { "cus_id","cus_id","cus_id"};
			String[] fieldName = new String[] { "belg_line","cus_name","cust_mgr" };
			String[] resultName = new String[] { "belg_line","cus_name","cust_mgr" };
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			/*** 取条线并判断客户类型，再进一步处理客户信息 ***/
			String belg_line = kColl.getDataValue("belg_line").toString();
			Map<String,String> map = new HashMap<String, String>();
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			if(belg_line.equals("BL300")){	//个人客户处理
				args=new String[]{"cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusIndiv","CusIndiv","CusIndiv"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id"};
				fieldName=new String[]{"mobile","indiv_rsd_addr","street3"};
				resultName=new String[]{"mobile","indiv_rsd_addr","street3"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				
				map.put("indiv_rsd_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}else{	//对公客户处理
				args=new String[]{"cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusCom","CusCom","CusCom"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id"};
				fieldName=new String[]{"legal_phone","acu_addr","street"};
				resultName=new String[]{"legal_phone","acu_addr","street"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				
				map.put("acu_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}
			
			/*** 统计明细表合计项begin ***/
			KeyedCollection kColl_trans = new KeyedCollection("TransValue");
			kColl_trans.addDataField("serno", serno_value);
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			kColl_trans = cmisComponent.delReturnSql("SumDbtWriteoffDetail", kColl_trans);
			
			if(kColl_trans != null){
				for(int i = 0; i < kColl_trans.size() ; i++){
					kColl.addDataElement(kColl_trans.getDataElement(i));
				}
			}
			/*** 统计明细表合计项end ***/
			
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id" , "cust_mgr"});
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			context.put("manager_id", (String)kColl.getDataValue("manager_id"));
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			this.putDataElement2Context(kColl, context);
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
}