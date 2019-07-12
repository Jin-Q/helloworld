package com.yucheng.cmis.biz01line.arp.component;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class ArpTradeComponent extends CMISComponent {
	
	/*** 呆账核销授权交易begin：贷款核销(02001000003)、贷款核销授权(02) ***/
	public void DbtWriteoffAuthorize(String serno) throws ComponentException{
		try {
			if(serno == null || serno == ""){
				throw new EMPException("呆账核销授权交易处理，获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			KeyedCollection kColl_app = dao.queryDetail("ArpDbtWriteoffApp", serno, this.getConnection());	//取一条申请记录
			IndexedCollection iColl_detail = dao.queryList("ArpDbtWriteoffDetail","where serno ='"+serno+"'", this.getConnection());	//取申请下的多条明细记录
			
			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name" };
			String[] resultName = new String[] { "cus_name" };	//翻译客户名称
			SystemTransUtils.dealPointName(kColl_app, args, SystemTransUtils.ADD, this.getContext(), modelIds, modelForeign, fieldName,resultName);			

			String tran_id = TradeConstance.SERVICE_CODE_DKHXSQ+TradeConstance.SERVICE_SCENE_DKZQSQ;			//交易码
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());			//授权编号
			
			for(int i=0;i<iColl_detail.size();i++){	//实际处理时核心是逐笔核销，并通知信贷的。所以每笔明细都要生成授权，以便处理之后的通知。
				
				KeyedCollection authorizeKColl = new KeyedCollection("PvpAuthorize");
				/*** 考虑到可能出现的外币情况，交易金额就不传了，核算直接做全额核销 ***/
				authorizeKColl.addDataField("serno",serno);
				authorizeKColl.addDataField("authorize_no",authSerno);	//授权编号
				authorizeKColl.addDataField("tran_id",tran_id);	//交易码
				authorizeKColl.addDataField("cus_id",kColl_app.getDataValue("cus_id"));  //客户编码
				authorizeKColl.addDataField("cus_name",kColl_app.getDataValue("cus_name"));  //客户名称
				authorizeKColl.addDataField("manager_br_id",kColl_app.getDataValue("manager_br_id"));  //管理机构
				authorizeKColl.addDataField("status", "02");	//流程结束时会直接发出授权，所以状态为已授权；状态：02-已授权
				authorizeKColl.addDataField("send_times","0");  //发送次数
				authorizeKColl.addDataField("tran_date",this.getContext().getDataValue(CMISConstance.OPENDAY));  //交易日期
				authorizeKColl.addDataField("fldvalue01","GEN_GL_NO@"+authSerno);	//授权编号
				
				KeyedCollection kColl_detail = (KeyedCollection) iColl_detail.get(i);
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
				authorizeKColl.addDataField("tran_serno",tranSerno);  //交易流水号
				authorizeKColl.addDataField("cont_no", kColl_detail.getDataValue("cont_no"));	//合同编号
				authorizeKColl.addDataField("bill_no", kColl_detail.getDataValue("bill_no"));	//借据编号
				authorizeKColl.addDataField("fldvalue02","DUEBILL_NO@"+kColl_detail.getDataValue("bill_no"));	//发给核算的借据编号

				dao.insert(authorizeKColl, this.getConnection());	//插授权
			}

		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("呆账核销授权交易处理失败！");
		}
	}
	/*** 呆账核销授权交易end ***/
	
}