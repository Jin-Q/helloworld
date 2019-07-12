package com.yucheng.cmis.biz01line.cus.op.cuscogniz.evalorgcognizapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CheckCusOrgIsExistByNoOp extends CMISOperation {

	private final String modelIdMng = "CusOrgAppMng";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnInfo = "";
		try{
			connection = this.getConnection(context);

			String extr_eval_org = null;//组织机构代码
			try {
				extr_eval_org = (String)context.getDataValue("extr_eval_org");
			} catch (Exception e) {}
			if(extr_eval_org == null || extr_eval_org.length() == 0)
				throw new EMPJDBCException("评估机构准入申请组织机构代码[extr_eval_org]为空!");
				
			KeyedCollection kCollCusOrg = new KeyedCollection();
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where extr_eval_org='"+extr_eval_org+"'";
			IndexedCollection iColl = dao.queryList(modelIdMng, condition, connection);
			if(iColl.size()>0){//该评估机构已存在
				returnInfo = PUBConstant.EXISTS;			
				kCollCusOrg = (KeyedCollection)iColl.get(0);
				//地址
				String extr_eval_addr = (String)kCollCusOrg.getDataValue("extr_eval_addr");				
				if(extr_eval_addr!=null&&!"".equals(extr_eval_addr)){
					Map<String,String> map = new HashMap<String,String>();
					map.put("extr_eval_addr", "STD_GB_AREA_ALL");//实际经营地址
					//树形菜单服务
					CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
					SInfoUtils.addPopName(kCollCusOrg, map, service);
				}
			}else{
				returnInfo = PUBConstant.NOTEXISTS;
			}
			
			IndexedCollection iColl_check = dao.queryList("CusOrgApp", condition, connection);
			for(int i = 0 ; i < iColl_check.size() ; i++ ){					
				KeyedCollection kColl_check = new KeyedCollection();
				kColl_check = (KeyedCollection)iColl_check.get(i);
				String status = (String)kColl_check.getDataValue("approve_status");
				if(!status.equals("997")&&!status.equals("998")){
					returnInfo = PUBConstant.FAIL;
				}
			}
			
			kCollCusOrg.addDataField("flag", returnInfo);
			IndexedCollection iColl4Return=new IndexedCollection("cusOrgList"); 
			iColl4Return.addDataElement(kCollCusOrg);
			this.putDataElement2Context(iColl4Return, context);
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
