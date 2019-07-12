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

public class UpdateCusIndivSocRelRecordOp extends CMISOperation {

    private final String modelId = "CusIndivSocRel";

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
                throw new EMPJDBCException("The values to update[" + modelId+ "] cannot be empty!");
            }
            
       	   kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
		   kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));
		 
		   CusIndivSocRelComponent cusIndivSocRelComponent = (CusIndivSocRelComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVSOCREL,context,connection);
		   ComponentHelper cHelper = new ComponentHelper();
			CusIndivSocRel newcusIndivSocRel = new CusIndivSocRel();
		   newcusIndivSocRel = (CusIndivSocRel) cHelper.kcolTOdomain(newcusIndivSocRel, kColl);
		   
		   cusIndivSocRelComponent.giveValueToCusIndivSocRel(newcusIndivSocRel);
            flag = "修改成功";
        } catch (EMPException ee) {
            flag = "修改失败";
            context.addDataField("flag", flag);
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "修改失败!", ee);
            ee.printStackTrace();
        } catch (Exception e) {
            flag = "修改失败";
            context.addDataField("flag", flag);
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "修改失败!", e);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                this.releaseConnection(context, connection);
            }
        }
        context.addDataField("flag", flag);
        return "0";
    }
}
