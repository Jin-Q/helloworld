package com.yucheng.cmis.biz01line.esb.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.component.ESBComponent;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
/**
 * <p>授权信息统一处理接口，转发不同业务对应的接口交易</p>
 * <p>处理逻辑：</p>
 * <p>1.更新授权表状态</p>
 * <p>2.将授发送信息存放于授权</p>
 */
public class SendEsbTradeOp extends CMISOperation {

	private static final String AUTHMODEL = "PvpAuthorize";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String tran_serno = "";//交易流水号
		boolean result = true;//反馈信息标识
		String tran_id= "";//交易ID
		String sence_id = "";//场景ID
		String returnMsg = "";//反馈信息
		try {
			connection = this.getConnection(context);
			tran_serno = (String)context.getDataValue("tran_serno");
		
			if(tran_serno == null || tran_serno.trim().length() == 0){
				context.setDataValue("RET_MSG", "获取发送授权流水号失败！");
				throw new EMPException("获取发送授权流水号失败！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
			tran_id = (String)authKColl.getDataValue("tran_id");
			
			if(tran_id == null || tran_id.trim().length() == 0){
				context.setDataValue("RET_MSG", "获取交易码失败，请检查生成授权信息交易码是否存在！");
				throw new EMPException("获取交易码失败，请检查生成授权信息交易码是否存在！");
			}
			
			sence_id = tran_id.substring(tran_id.length()-2,tran_id.length());
			tran_id = tran_id.substring(0, tran_id.length()-2);
			
			KeyedCollection returnKColl = null;
			ESBComponent esbComponent = (ESBComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(TradeConstance.ESBCOMPONENT, context, connection);
			if(tran_id.equals(TradeConstance.SERVICE_CODE_DKFFSQ)&&sence_id.equals(TradeConstance.SERVICE_SCENE_DKFFSQ)){//贷款发放授权
				this.dulSendTimes(authKColl, dao, connection);
				returnKColl = esbComponent.sendEsb4Dkffsq(authKColl,dao,context,connection);
				if(!this.haveSuccess(returnKColl, context)){
					result = false;
					returnMsg = (String)returnKColl.getDataValue("RET_MSG");
				}
				this.dulReturnMsg(authKColl, returnKColl,dao,connection);
			}else if(tran_id.equals(TradeConstance.SERVICE_CODE_YCFFSQ)&&sence_id.equals(TradeConstance.SERVICE_SCENE_YCFFSQ)){//银承出账授权
				/** 银承授权一次性发送所有的票据，根据授权编码 */
				String authorize_no = (String)authKColl.getDataValue("authorize_no");
				IndexedCollection pvpIColl = dao.queryList(AUTHMODEL, " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
				if(pvpIColl != null && pvpIColl.size() > 0){
					returnKColl = esbComponent.sendEsb4Ycffsq(pvpIColl,tran_id,TradeConstance.SERVICE_SCENE_YCFFSQ,authorize_no,dao,context,connection);
					//this.dulReturnMsg(authKColl, returnKColl,dao,connection);
				}
			}else if(tran_id.equals(TradeConstance.SERVICE_CODE_BHFFSQ)&& sence_id.equals(TradeConstance.SERVICE_SCENE_BHFFSQ)){//保函出账授权
				returnKColl = esbComponent.sendEsb4Bhffsq(authKColl,dao,context,connection);
			}else if(tran_id.equals(TradeConstance.SERVICE_CODE_DKCNSQ)&& sence_id.equals(TradeConstance.SERVICE_SCENE_DKCNSQ)){//贷款承诺授权
			    returnKColl = esbComponent.sendEsb4Dkcnsq(authKColl,dao,context,connection);
		    }else if(tran_id.equals(TradeConstance.SERVICE_CODE_TXFFSQ)&& sence_id.equals(TradeConstance.SERVICE_SCENE_TXFFSQ)){//贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权
		    	/** 贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权一次性发送所有的票据，根据授权编码 */
		    	String authorize_no = (String)authKColl.getDataValue("authorize_no");
				IndexedCollection pvpIColl = dao.queryList(AUTHMODEL, " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
				
				if(pvpIColl != null && pvpIColl.size() > 0){
					returnKColl = esbComponent.sendEsb4Txffsq(pvpIColl,tran_id,TradeConstance.SERVICE_SCENE_YCFFSQ,authorize_no,dao,context,connection);
					//this.dulReturnMsg(authKColl, returnKColl,dao,connection);
				}
				if(!this.haveSuccess(returnKColl, context)){
					result = false;
					returnMsg = (String)returnKColl.getDataValue("RET_MSG");
				}
				this.dulReturnMsg(authKColl, returnKColl,dao,connection);
			}
			if(result){
				context.addDataField("flag", "success");
			}else {
				context.addDataField("flag", "failed");
			}
			context.addDataField("retMsg", returnMsg); 
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 解析报文反馈信息是否正确，true表示交易成功，false表示失败
	 * @param returnKColl  反馈信息
	 * @param context 上下文
	 * @return boolean
	 * @throws EMPException
	 */
	private boolean haveSuccess(KeyedCollection returnKColl, Context context) throws EMPException{
		boolean flag  = true;
		if(returnKColl != null && returnKColl.size() > 0){
			String retStatus = (String)returnKColl.getDataValue("RET_STATUS");
			String retCode = (String)returnKColl.getDataValue("RET_CODE");
			/** 更新授权表状态 */
			if(retStatus.equals(TradeConstance.RETSTATUS1)&&retCode.equals(TradeConstance.RETCODE1)){
			}else {
				flag = false;
			}
		}else {
			flag = false;
		}
		return flag;
	}
	/**
	 * 更新反馈信息
	 * @param kColl 授权表单
	 * @param returnKColl 返回表单
	 * @param dao 
	 * @param connection
	 * @throws EMPException
	 */
	private void dulReturnMsg(KeyedCollection kColl, KeyedCollection returnKColl,TableModelDAO dao, Connection connection) throws EMPException{
		kColl.setDataValue("return_code", (String)returnKColl.getDataValue("RET_CODE"));
		kColl.setDataValue("return_desc", (String)returnKColl.getDataValue("RET_MSG"));
		//dao.update(kColl, connection);
	}
	/**
	 * 更新发送次数
	 * @param kColl 授权表单
	 * @param dao 
	 * @param connection
	 * @throws EMPException
	 */
	private void dulSendTimes(KeyedCollection kColl, TableModelDAO dao, Connection connection) throws EMPException{
		String sendNum = "";//发送次数
		/** 发送次数+1 */
		sendNum = (String)kColl.getDataValue("send_times");
		if(sendNum == null || sendNum.trim().length() == 0){
			sendNum = "0";
		}
		int sendTimes = Integer.parseInt(sendNum);
		sendNum = String.valueOf(sendTimes+1);
		kColl.setDataValue("send_times", sendNum);
		dao.update(kColl, connection);
	}
}
