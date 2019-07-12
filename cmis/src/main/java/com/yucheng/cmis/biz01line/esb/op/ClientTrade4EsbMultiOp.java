package com.yucheng.cmis.biz01line.esb.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * XD作为客户端请求ESB交易转发类，此类主要初始化数据，并且转发给不同交易
 * 传递的参数必须包括交易码以及交易场景，其余参数更具自身需求添加
 * 由于担保品出入库与核心交易比较特殊，需逐笔发送同时接收返回，故特殊处理
 * 担保品出入库核心记账成功后处理规则如下：
 * 1.出入库方式为入库则修改权证状态为3在库,同时修改押品信息表的状态为3押品生效
 * 2.出入库方式为取出则修改权证状态为6出库
 * 3.出入库方式为借出则修改权证状态为4借出
 * 4.出入库方式为注销出库则修改权证状态为7核销
 * @author Pansq
 * @version V1.0
 * 修改记录：
 * 版本号    修改人       修改日期      修改内容
 *
 */
public class ClientTrade4EsbMultiOp extends CMISOperation {
	private static final String AUTHMODEL = "PvpAuthorize";
	private static final String PVPSUBMODEL = "PvpAuthorizeSub";
	ClientTradeInterface clientTradeInterface;
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean result = false;//反馈信息标识
		try {
			connection = this.getConnection(context);
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection retKColl;
			String serviceCode = null;
			String senceCode = null;
			String tran_serno = null;
			if(context.containsKey("service_code")){
				serviceCode = (String)context.getDataValue("service_code");
			}
			if(context.containsKey("sence_code")){
				senceCode = (String)context.getDataValue("sence_code");
			}
			if(context.containsKey("tran_serno")){
				tran_serno = (String)context.getDataValue("tran_serno");
			}
			if(serviceCode == null || serviceCode.trim().length() == 0){
				throw new EMPException("交易码获取失败！");
			}
			if(senceCode == null || senceCode.trim().length() == 0){
				throw new EMPException("交易场景获取失败！");
			}
			if(tran_serno == null || tran_serno.trim().length() == 0){
				throw new EMPException("交易场景获取失败！");
			}
			//执行失败，则默认重复执行5次控制
			for(int i=0;i<5;i++){
			if(result){//执行成功则直接跳出循环
				break;
			}
			//通过交易码
			KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
			String authorize_no = (String)authKColl.getDataValue("authorize_no");
			IndexedCollection pvpIColl = dao.queryList(AUTHMODEL, " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
			for(int j=0;j<pvpIColl.size();j++){
				KeyedCollection kColl = (KeyedCollection)pvpIColl.get(j);
				String tranSerno = (String)kColl.getDataValue("tran_serno");//交易流水号
				String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);//当前营业日期，作为出入库时间更新
				String serno = (String)kColl.getDataValue("serno");
				/** 封装发送报文信息 */
				CompositeData reqCD= new CompositeData();
				ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
				/** 系统头 */
				reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKCNSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
				/** 应用头 */
				reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
						(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
				
				/** 封装报文体 */
				KeyedCollection reflectKColl = TagUtil.getReflectKColl(kColl);
				CompositeData bodyCD = new CompositeData();
				bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
				bodyCD.addField("PLEDGE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("PLEDGE_NO"), FieldType.FIELD_STRING, 52, 0));
				bodyCD.addField("TICKET_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("TICKET_TYPE"), FieldType.FIELD_STRING, 100, 0));
				bodyCD.addField("TICKET_NO", TagUtil.getEMPField(reflectKColl.getDataValue("TICKET_NO"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("IN_OUT_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("IN_OUT_TYPE"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("COMMISSION", TagUtil.getEMPField(reflectKColl.getDataValue("COMMISSION"), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("LOT_NUM", TagUtil.getEMPField(reflectKColl.getDataValue("LOT_NUM"), FieldType.FIELD_INT, 5, 0));
				reqCD.addStruct("BODY", bodyCD);
				/** 加载转发机制 */
				getTranExecute(serviceCode, senceCode, connection);
				/** 打印后台发送日志 */
				EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
				/** 执行发送操作 */
				CompositeData retCD = ESBClient.request(reqCD);
				/** 打印后台反馈日志 */
				EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
				/** 解析反馈报文头 */
				retKColl = esbInterfacesImple.getRespSysHeadCD(retCD);
				/** 解析反馈报文体 */
				KeyedCollection respBodyKColl = esbInterfacesImple.getRespBodyCD4KColl(retCD);
				this.putDataElement2Context(respBodyKColl, context);
				/** 判断报文发送成功与否 */
				if(TagUtil.haveSuccess(retKColl, context)){//成功
					result=true;
					dulReturnMsg(retKColl,"02", context, connection);
					this.setRetStatus("success", (String)retKColl.getDataValue("RET_MSG"), context);
					if("02002000012".equals(serviceCode)&&"01".equals(senceCode)){//担保品出入库授权交易专用处理
						//更新出入库明细表
						SqlClient.update("updateMortStorexwaDetail", tranSerno, openDay, null, connection);
						String warrant_no = (String)reflectKColl.getDataValue("TICKET_NO");//权证编号
						String warrant_type = (String)reflectKColl.getDataValue("TICKET_TYPE");//权证类型
						String in_out_type = (String)reflectKColl.getDataValue("IN_OUT_TYPE");//出入库类型
						Map param = new HashMap();
						param.put("warrant_no", warrant_no);
						param.put("warrant_type", warrant_type);
						Map paramValue = new HashMap();
						paramValue.put("date", openDay);
						if(in_out_type.equals("04")){//入库
							//更新权证状态为3在库
							paramValue.put("warrant_state", "3");
							SqlClient.update("updateCertiInfo", param, paramValue, null, connection);
							//更新押品信息状态为3生效
							SqlClient.update("updateGuarantyBaseInfo", param, "3", null, connection);
						}else if(in_out_type.equals("03")){//注销出库
							//更新权证状态为7核销
							paramValue.put("warrant_state", "7");
							SqlClient.update("updateCertiInfo", param, paramValue, null, connection);
						}else if(in_out_type.equals("02")){//借出
							//更新权证状态为4借出
							paramValue.put("warrant_state", "4");
							SqlClient.update("updateCertiInfo", param, paramValue, null, connection);
						}else if(in_out_type.equals("02")){//取出
							//更新权证状态为6出库
							paramValue.put("warrant_state", "6");
							SqlClient.update("updateCertiInfo", param, paramValue, null, connection);
						}
					}
					
				}else {//失败
					result=false;
					dulReturnMsg(retKColl,"01", context, connection);
					this.setRetStatus("failed", (String)retKColl.getDataValue("RET_MSG"), context);
				}
				//成功一笔事务提交一次
				connection.commit();
			}
		 }
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 设置交易的返回信息
	 * @param flag 返回成功标识
	 * @param retMsg 返回信息
	 * @param context 上下文
	 * @throws EMPException
	 */
	private void setRetStatus(String flag, String retMsg, Context context) throws EMPException {
		if(context.containsKey("flag")){
			context.setDataValue("flag", flag);
		}else {
			context.addDataField("flag", flag);
		}
		if(context.containsKey("retMsg")){
			context.setDataValue("retMsg", retMsg);
		}else {
			context.addDataField("retMsg", retMsg);
		}
	}
	
	/**
	 * 通过交易码查询需要转发的不同交易信息
	 * @param serviceCode 交易码
	 * @param senceCode 交易场景
	 */
	private void getTranExecute(String serviceCode, String senceCode, Connection connection) throws EMPException{
		KeyedCollection kc = new KeyedCollection();
		kc.addDataField("SERVICE_CODE", serviceCode);
		kc.addDataField("SERVICE_SCENE", senceCode);
		/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
		String impleClass;
		try {
			impleClass = (String)SqlClient.queryFirst("queryEsbConfig", kc, null, connection);
			if(impleClass == null || impleClass.trim().length() == 0){
				throw new EMPException("交易码【"+serviceCode+"】，交易场景【"+senceCode+"】未获取到配置的业务实现类");
			}
			clientTradeInterface = (ClientTradeInterface)Class.forName(impleClass.trim()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 针对授权信息更新本地授权信息更新操作实现方法
	 * @param retKColl 反馈状态
	 * @param context 上下文
	 * @param connection 数据库连接
	 */
	private void dulReturnMsg(KeyedCollection retKColl,String status, Context context, Connection connection) throws EMPException {
		String serno = null;
		if(context.containsKey("tran_serno")){//提交记录交易流水号
			serno = (String)context.getDataValue("tran_serno");
		}
		if(serno == null || serno.trim().length() == 0){
			throw new EMPException("更新授权台帐表，获取授权交易流水号失败！");
		}
		TableModelDAO dao = this.getTableModelDAO(context);
		KeyedCollection authKcoll = dao.queryDetail("PvpAuthorize", serno, connection);
		String authorize_no = (String)authKcoll.getDataValue("authorize_no");
		IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
		String sendNum = "";//发送次数
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection kc = (KeyedCollection)pvpIColl.get(i);
			/** 发送次数+1 */
			sendNum = (String)kc.getDataValue("send_times");
			if(sendNum == null || sendNum.trim().length() == 0){
				sendNum = "0";
			}
			int sendTimes = Integer.parseInt(sendNum);
			sendNum = String.valueOf(sendTimes+1);
			kc.setDataValue("status", status);
			kc.setDataValue("send_times", sendNum);
			kc.setDataValue("return_code", retKColl.getDataValue("RET_CODE"));
			kc.setDataValue("return_desc", retKColl.getDataValue("RET_MSG"));
			dao.update(kc, connection);
		}
	}
	
}
