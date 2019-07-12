package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComManagerComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusComManagerRecordOp extends CMISOperation {
	
	private final String modelId = "CusComManager";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
//			String condition="";
			
			KeyedCollection kColl = null;
			String phone = "";
			String cus_id_rel = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				phone = (String)kColl.getDataValue("com_relate_phone");//高管联系手机  Added by FCL 2015-01-27
				cus_id_rel = (String)kColl.getDataValue("cus_id_rel");//高管客户码
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			ComponentHelper cHelper = new ComponentHelper();
			CusComManager cusComManager = new CusComManager();
			CusComManager cusComManagerNew = new CusComManager();

			cusComManager = (CusComManager) cHelper.kcolTOdomain(cusComManager,kColl);
			cusComManagerNew = (CusComManager) cusComManager.clone();
			
			CusComManagerComponent cusComManagerComponent = (CusComManagerComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMMANAGER, context, connection);
			String strReturnMessage = cusComManagerComponent.checkExist(cusComManager);
			TableModelDAO dao = this.getTableModelDAO(context);
			//如果高管类别是法人代表、总经理、财务负责人、董事长，则只能唯一
//			String com_mrg_typ = (String) kColl.getDataValue("com_mrg_typ");
//			if(com_mrg_typ.equals("02") || com_mrg_typ.equals("03") || com_mrg_typ.equals("04") || com_mrg_typ.equals("07")){
//				strReturnMessage = cusComManagerComponent.checkExistByMrgType(cusComManagerNew);
//				if (strReturnMessage.equals("no")) {
//					// 不存在，可以新增
//					dao.update(kColl, connection);
//					flag = "修改成功";
//				} else if (strReturnMessage.equals("yes")) {
//					// 该资质证书编号+客户码已存在
//					flag = "该高管客户已经存在高管类别是法人代表、总经理、财务负责人、董事长，不能新增！";
//				}
//			}else{
//				dao.update(kColl, connection);
//				flag = "修改成功";
//			}
			
			dao.update(kColl, connection);
			//--------------20150127 Edited by FCL 高管手机号码有更新，同步更新到个人基础信息中
			CusIndivComponent cusIndivComponent = (CusIndivComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIV, context, connection);
			CusIndiv cindiv = cusIndivComponent.getCusIndiv(cus_id_rel);
			String ori_mobile = cindiv.getMobile();
			if(phone!=null&&!phone.equals(ori_mobile)){//高管手机号码有更新，同步更新到个人基础信息中
				System.out.println("--------高管手机号有更新["+cus_id_rel+"]------新手机号："+phone+"----旧号码:"+ori_mobile);
				int i = cusIndivComponent.updateMobileByCusId(cus_id_rel, phone);
				if(i>0){
					System.out.println("更新成功");
				}
			}
			//-----------------------END------------------------------------------------
			flag = "修改成功";
		}catch (EMPException ee) {
			flag = "修改失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
