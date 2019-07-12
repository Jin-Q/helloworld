package com.yucheng.cmis.biz01line.mort.cargo.mortbaildeliv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class SaveCargoPledgeOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortDelivList";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		String guaranty_no = "";
		try{
			connection = this.getConnection(context);
			
			IndexedCollection iColl = null;//保存多个提货清单
			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				iColl = (IndexedCollection)context.getDataElement("MortCargoPledgeNewList");
				guaranty_no = (String) context.getDataValue("guaranty_no");
				
			} catch (Exception e) {}
			if(iColl.size() == 0){
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			}else{
				 for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection) iColl.get(i);
					if(kc.getDataValue("deliv_qnt")!=null&&!"0".equals(kc.getDataValue("deliv_qnt"))){
						kc.put("storage_qnt",kc.getDataValue("qnt"));//库存数量
						kc.put("storage_value",kc.getDataValue("identy_total"));//库存总价值
						kc.put("guaranty_no",guaranty_no);//押品编号
						kc.put("input_id", context.getDataValue(PUBConstant.currentUserId));//登记人
						kc.put("input_br_id", context.getDataValue(PUBConstant.organNo));//登记机构
						kc.put("reg_date", context.getDataValue(PUBConstant.OPENDAY));//登记日期
						kc.put("op_type","2");//操作类型
						kc.put("cargo_status", "05");//保证金换货 状态默认为“出库待记账”  2014-05-29 唐顺岩 
						kc.setName(modelId);
						//KeyedCollection res = mortRe.queryMortDelivList(guaranty_no, kc.getDataValue("cargo_id").toString().trim(),"2");
						
						if(kc.getDataValue("serno")!=null&&!"".equals(kc.getDataValue("serno"))){
							dao.update(kc, connection);
						}else{
							serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
							kc.put("serno",serno);//业务编号
							dao.insert(kc,connection);
						}
					}
				}
				context.put("flag","success");
			}
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
