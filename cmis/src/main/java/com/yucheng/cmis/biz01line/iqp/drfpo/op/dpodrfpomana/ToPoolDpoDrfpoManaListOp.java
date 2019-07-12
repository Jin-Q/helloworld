package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class ToPoolDpoDrfpoManaListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no = "";
		try{
			connection = this.getConnection(context);
			drfpo_no = (String) context.getDataValue("drfpo_no");
			//中文转码
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
		//	PageInfo pageInfo = TableModelUtil.
			//构建组件类
			DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			/**查询已在池状态的汇票信息*/
			IndexedCollection iCollIn = dpoComponent.getPorderListByDrfpoNo(drfpo_no,"'01'");
			/**查询待入池状态的汇票信息*/
			IndexedCollection iColl = dpoComponent.getPorderListByDrfpoNo(drfpo_no, "'00','03','04'");
			iColl.setName("IqpDrfpoManaList");
			iCollIn.setName("IqpDrfpoManaInPoList");
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(iCollIn, context);
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
