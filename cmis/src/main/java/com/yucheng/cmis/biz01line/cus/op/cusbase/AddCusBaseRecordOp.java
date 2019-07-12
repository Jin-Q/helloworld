package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBlkListComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/**
 * 
 *@Classname AddCusBaseRecordOp.java
 *@Version 1.0
 *@Copyright yuchengtech
 *@Author liuxin
 *@Description： 用于客户开户处理
 *@Lastmodified
 *@Author
 * 
 */
public class AddCusBaseRecordOp extends CMISOperation {

	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		/**
		 * 摘要: 本类实现客户开户的功能，具体如下：
		 * 1. 正式客户开户 a) 检查是否在当前CUS_BASE里存在重复数据  b)插入CUS_BASE表  c)插入CUS_INDIV/CUS_COM
		 * 2. 非正式客户开户（担保客户） a) 插入CUS_BASE表 b) 插入CUS_INDIV/CUS_COM
		 */
		String flag = "";
		// 主办机构
		String mainBrId = "";
		String mainBrName = "";
		// 客户经理
		String custMgr = "";
		String custMgrName = "";
		// 开户类型 01 正式 02 临时
		String openType = "";
		String cusId = "";
		String cusName = "";
		String certType = "";
		String certCode = "";
		try {
			connection = this.getConnection(context);

			//客户基表信息
			KeyedCollection kCollBase = (KeyedCollection) context.getDataElement(PUBConstant.CUSBASE);
			if (kCollBase == null) {
				throw new ComponentException("数据异常：未获取到页面数据");
			}
			//获得相应属性值
			cusId = kCollBase.getDataValue("cus_id").toString();
			cusId = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);
			kCollBase.put("cus_id", cusId);
			certType = kCollBase.getDataValue("cert_type").toString();
			certCode = kCollBase.getDataValue("cert_code").toString();
			
			// 构建业务处理类
			ComponentHelper cHelper = new ComponentHelper();
			CusBase cusBase = new CusBase();
			cusBase = (CusBase) cHelper.kcolTOdomain(cusBase, kCollBase);
			CusBaseComponent cusBaseComponent = (CusBaseComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
			//查询是否黑名单用户
			CusBlkListComponent cbl = (CusBlkListComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance("CusBlkListComponent", context, connection);
			int cblFlag = cbl.getCusBlkList(certType, certCode);
			if (flag.equals("") && cblFlag != 0) {
				flag = "Black";
			}
			//获取开户类型，如果为空则默认开正式户
			try {
				openType = (String) kCollBase.getDataValue("openType");
			} catch (Exception e) {
				openType = (String) context.getDataValue("openType");
			}
			if (openType == null||openType.equals("null")) {
				openType = "01";
			}

			CusBase queryCusBase = cusBaseComponent.getCusBaseByCert(certCode,certType);
			if(queryCusBase.getCusId()==null||"".equals(queryCusBase.getCusId())){
				//不存在客户信息，可以新增
				try {
					KeyedCollection kCollComOrInd = null;
					if("20".equals(certType)||"a".equals(certType)||"b".equals(certType)||"X".equals(certType)){
						kCollComOrInd = (KeyedCollection) context.getDataElement(PUBConstant.CUSCOM);
					}else {
						kCollComOrInd = (KeyedCollection) context.getDataElement(PUBConstant.CUSINDIV);
					}
					//明细表中加入客户id
					kCollComOrInd.put("cus_id", cusId);
					//插入客户信息基表与明细表
					this.addCusBaseRecord(context, connection, cusBase);
					this.addCusComOrCusIndivRecord(cusBase, kCollComOrInd, context, connection);
					//正式客户开户
					if("01".equals(openType)){
						flag = "newFormal";
					}else{
						flag = "newTemp";
					}
					// 开户成功,插入CUS_BASE和CUS_INDIV/CUS_COM
				} catch (Exception e) {
					e.printStackTrace();
					// 如果在取核心客户号时,发生任何异常,则把异常信息放到页面上去
					flag = e.getMessage();
				}
			}else{
				// 如果根据证件类型证、件号码能查出数据，则说明已开户
				cusId = queryCusBase.getCusId();
				certType = queryCusBase.getCertType();
				cusName = queryCusBase.getCusName();
				mainBrId = queryCusBase.getMainBrId();
				custMgr = queryCusBase.getCustMgr();
//				mainBrName = (String) kCollTemp.getDataValue("mainBrName");
//				custMgrName = (String) kCollTemp.getDataValue("custMgrName");
//				flag = (String) kCollTemp.getDataValue("flag");
				String cus_status = queryCusBase.getCusStatus();
				if("00".equals(cus_status)||"20".equals(cus_status)){
					flag = "updateFormal";
				}else if("04".equals(cus_status)){
					flag = "updateTemp";
				}
			}
		} catch (CMISException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			flag = e.toString();
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw new EMPException(e);
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("cus_id", cusId);
		context.addDataField("cusName", cusName);
		context.addDataField("mainBrId", mainBrId);
		context.addDataField("custMgr", custMgr);
		context.addDataField("mainBrName", mainBrName);
		context.addDataField("custMgrName", custMgrName);
		context.addDataField("flag", flag);

		return "0";
	}

	/**
	 * 插入CUS_BASE
	 */
	private KeyedCollection addCusBaseRecord(Context context,Connection connection, CusBase cusBase) throws Exception {
		CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context, connection);
		cusBaseComponent.addCusBase(cusBase);
		return null;
	}

	/**
	 * 插入CUS_COM或者CUS_INDIV
	 * */
	private KeyedCollection addCusComOrCusIndivRecord(CusBase cusBase,KeyedCollection kcRep, Context context,Connection connection) throws Exception {
		CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context, connection);
		KeyedCollection kCollTemp = new KeyedCollection();
		String flag = "";
		String flagInfo = "";
		String certType = cusBase.getCertType();
		String cusId = cusBase.getCusId();
		String cusIdTmp = "";
		
		//根据当前的数据去CUS_COM或CUS_INDIV表查一下
		if("20".equals(certType)||"a".equals(certType)||"b".equals(certType)||"X".equals(certType)){
			CusCom cusCom = cusBaseComponent.getCusCom(cusId);
			cusIdTmp = cusCom.getCusId();
		} else {
			CusIndiv cusIndiv = cusBaseComponent.getCusIndiv(cusId);
			cusIdTmp = cusIndiv.getCusId();
		}
		//客户明细表里不存在该客户信息
		if (cusIdTmp==null || cusIdTmp=="" || "".equals(cusIdTmp)) {
			flagInfo = cusBaseComponent.addCus(cusBase, kcRep);
			if (flagInfo.equals(CMISMessage.DEFEAT)) {
				flag = "error";
			} else {
				flag = "new";
			}
		} else {
			//能进这个里面来的，肯定是数据异常，原因：CUS_BASE没有数据，但是CUS_COM/CUS_INDIV有数据
			flag = "error";
		}

		kCollTemp.addDataField("flag", flag);
		return kCollTemp;
	}

}
