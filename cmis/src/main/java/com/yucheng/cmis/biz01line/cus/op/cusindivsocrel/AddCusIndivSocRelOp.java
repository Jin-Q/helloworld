package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivSocRelComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusIndivSocRelOp extends CMISOperation {

    // operation TableModel
    private final String modelId = "CusIndivSocRel";

    /**
     * bussiness logic operation
     */
    @Override
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        String flag = null;
        try {
            connection = this.getConnection(context);

            KeyedCollection kColl = null;
            try {
                kColl = (KeyedCollection) context.getDataElement(modelId);
            } catch (Exception e) {
            }
            if (kColl == null || kColl.size() == 0) {
                throw new EMPJDBCException("The values to insert[" + modelId+ "] cannot be empty!");
            }        	
			ComponentHelper cHelper = new ComponentHelper();
			CusIndivSocRel cusIndivSocRel = new CusIndivSocRel();
			CusIndivSocRel newcusIndivSocRel = new CusIndivSocRel();
			cusIndivSocRel = (CusIndivSocRel) cHelper.kcolTOdomain(cusIndivSocRel, kColl);
			String indivCusRel = cusIndivSocRel.getIndivCusRel();
			System.out.println("获得的客户关系类型是【"+indivCusRel+"】");

			newcusIndivSocRel = (CusIndivSocRel) cHelper.kcolTOdomain(newcusIndivSocRel, kColl);
			
			CusIndivSocRelComponent cusIndivSocRelComponent = (CusIndivSocRelComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVSOCREL,context,connection);
			
			String strReturnMessage = cusIndivSocRelComponent.checkExist(cusIndivSocRel);
			//校验配偶信息
			if(indivCusRel.equals("1")){
				if(strReturnMessage.equals("no")){
					//不存在，可以新增
					cusIndivSocRelComponent.giveValueToCusIndivSocRel(newcusIndivSocRel);
					flag = "新增成功";
				}else if(strReturnMessage.equals("yes")){
					//证件类型+证件号码+客户码已存在
					flag = "该客户下的同一关联客户已存在，不能新增！";
				}
			}else if(indivCusRel.equals("2")){//关系为父母则校验不能大于两个
//				String message = cusIndivSocRelComponent.checkParents(newcusIndivSocRel.getCusId());
//				if(message.equals("n")){//数量没有大于2可以新增
					if(strReturnMessage.equals("no")){
						//不存在，可以新增
						cusIndivSocRelComponent.giveValueToCusIndivSocRel(newcusIndivSocRel);
						flag = "新增成功";
					}else if(strReturnMessage.equals("yes")){
						//证件类型+证件号码+客户码已存在
						flag = "该客户下的同一关联客户已存在，不能新增！";
					}
//				}else {
//					flag = "该客户父母数已经存在两个不能再新增！";
//				}
			}else{
				if(strReturnMessage.equals("no")){
					//不存在，可以新增
					cusIndivSocRelComponent.giveValueToCusIndivSocRel(newcusIndivSocRel);
					flag = "新增成功";
				}else if(strReturnMessage.equals("yes")){
					//证件类型+证件号码+客户码已存在
					flag = "该客户下的同一关联客户已存在，不能新增！";
				}
			}
        } catch (EMPException ee) {
            flag = "新增失败";
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", ee);
            throw ee;
        } catch (Exception e) {
            flag = "新增失败";
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", e);
            throw new EMPException(e);
        } finally {

            if (connection != null) {
                this.releaseConnection(context, connection);
            }
        }
        context.addDataField("flag", flag);
        return "0";
    }
}
