package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivSocRelComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
/**
 * 2013-6-29  16:29 lilx
 * 个人客户  信息TAB中的 配偶信息 维护
 * */
public class ManagerSpouseInfoOp extends CMISOperation {
									
	private final String modelId = "CusIndivSocRel";

	public String doExecute(Context context) throws EMPException {
		/*
		 * 是否有配偶的条件 CusIndivSocRel
		 * indiv_cus_rel  = 1 
		 * 关联客户客户码  cus_id_rel = cusid
		 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cusid = (String) context.getDataValue("cus_id");
			CusIndivSocRelComponent socCus = (CusIndivSocRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(modelId, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection ic = dao.queryList(modelId, " where cus_id = '" + cusid + "' and indiv_cus_rel='1'", connection);
			if( ic.size() == 1 ){
				CusIndivSocRel my = socCus.getSpouseCusIndivSocRel(cusid);
				CusIndivSocRel spouse = socCus.getSpouseCusIndivSocRel(my.getCusIdRel());
				CusBase relBase = new CusBase();
				CusIndiv relIndiv = new CusIndiv();
				CusBaseComponent baseComp = (CusBaseComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
				relBase = baseComp.getCusBase(my.getCusIdRel());
				CusIndivComponent comp = (CusIndivComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusIndiv", context, connection);
				relIndiv = comp.getCusIndiv(my.getCusIdRel());
				if(spouse !=null ){
					context.addDataField("weatherSpouse", 1);
					//证件类型
					context.addDataField("indiv_rel_cert_typ", relBase.getCertType());
					//证件号码
					context.addDataField("indiv_rl_cert_code", relBase.getCertCode());
					//配偶客户码
					context.addDataField("cus_id_rel", my.getCusIdRel());
					//姓名
					context.addDataField("indiv_rel_cus_name", relBase.getCusName());
					//性别
					context.addDataField("indiv_sex", relIndiv.getIndivSex());
					//年收入
					context.addDataField("indiv_rl_y_incm", relIndiv.getIndivAnnIncm());
					//工作单位
					context.addDataField("indiv_com_name", relIndiv.getIndivComName());
				}
			}else
				context.addDataField("weatherSpouse", 2);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
