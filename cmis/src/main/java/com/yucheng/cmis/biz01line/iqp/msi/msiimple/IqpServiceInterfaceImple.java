package com.yucheng.cmis.biz01line.iqp.msi.msiimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpServiceInterfaceImple extends CMISModualService implements IqpServiceInterface {
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);

	/**
	 * 根据授信台账编号/授信合作方协议编号查询授信占用。
	 * @param agr_no   授信协议编号
	 * @param lmt_type 授信类型  01：单一法人 02:同业客户 03:合作方
	 * @return iColl KeyedCollection数据集(filed:lmt_amt授信占用、is_grp是否集团融资模式、grp_lmt_amt集团授信占用)
	 */
	public KeyedCollection getAgrUsedInfoByArgNo(String agr_no,String lmt_type,Connection connection, Context context) throws EMPException {
		KeyedCollection kColl = null;
		logger.info("---------------根据授信台账编号/授信合作方协议编号查询授信占用   开始---------------");
		if(null==agr_no || "".equals(agr_no)){ //授信协议编号为空时
			logger.error("调用业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用，授信协议编号[agr_no]传入值："+agr_no+" 错误！");
			throw new EMPException("调用业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用，授信协议编号[agr_no]传入值："+agr_no+" 错误！");
		}
		if(null==lmt_type || "".equals(lmt_type)){ //授信类型为空时
			logger.error("调用业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用，授信类型[lmt_type]传入值："+lmt_type+" 错误！");
			throw new EMPException("调用业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用，授信类型[lmt_type]传入值："+lmt_type+" 错误！");
		}
		try{
			/**
			 * 思路：
			 * 1、通过业务和授信关系表（r_bus_lmt_info）和 业务和第三方授信关系表（r_bus_lmtcredit_info）查询使用授信
			 *    协议的业务合同编号。
			 * 2、需包含三部分：
			 *    非循环合同：占用=合同剩余金额+未审批通过的出账金额+台账余额
			 *    循环合同  ：占用=合同金额=合同剩余金额+台账金额
			 *    未生成合同：占用=申请金额敞口部分
			 *   （备注：借款合同中无循环标识，通过业务类型区分是否循环。）
			 * 3、计算余额时，要按照敞口比例来计算，即扣除保证金、视同保证金的部分。
			 * 4、返回中，如果是单一法人，且该法人属于集团融资模式，需要返回该集团下所有法人的总授信占用
			 */
			
			if("01".equals(lmt_type)){//单一法人额度
				kColl = this.getLmtAmtByArgNo(agr_no,lmt_type,connection,context);
			}else if("02".equals(lmt_type)){//同业额度
				kColl = this.getLmtAmtByArgNo(agr_no,lmt_type,connection,context);
			}else if("03".equals(lmt_type)){//合同方额度
				kColl = this.getLmtAmtByArgNo(agr_no,lmt_type,connection,context);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用错误，错误描述："+e.getMessage());
			throw new EMPException("业务模块接口根据授信台账编号/授信合作方协议编号查询授信占用错误，错误描述："+e.getMessage());
		}
		logger.info("---------------根据授信台账编号/授信合作方协议编号查询授信占用  结束---------------");
		return kColl;
	}
	
	/**
	 * 根据授信台账编号/授信合作方协议编号查询授信占用。
	 * (filed:lmt_amt授信占用、is_grp是否集团融资模式、grp_lmt_amt集团授信占用)
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getLmtAmtByArgNo(String agr_no,String lmt_type,Connection connection,Context context) throws Exception {
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		KeyedCollection kColl = new KeyedCollection();
		BigDecimal lmt_amt = new BigDecimal("0");//授信占用
		String is_grp = "2";//是否集团融资模式(1:是、2:否)，默认为否
		BigDecimal grp_lmt_amt = new BigDecimal("0");//集团授信占用
		IndexedCollection grpiColl = null;//集团客户信息列表
		String modelId = "";
		String grp_no = "";
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			
			if(lmt_type.equals("01")){//单一法人额度
				modelId = "RBusLmtInfo";
				String modelIdCredit = "RBusLmtcreditInfo";
				
				/**增加第一步，查询额度台账的额度类型，循环额度使用循环额度方式计算，一次性额度使用一次性额度方式计算*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				String aaa = "'"+agr_no+"'";
				IndexedCollection lmtIColl = lmtServiceInterface.queryLmtAgrDetailsByLimitCodeStr(aaa, null, dataSource);
				String limit_type = "";
				if(lmtIColl.size()>0){
					KeyedCollection lmtKColl = (KeyedCollection) lmtIColl.get(0);
					limit_type = (String) lmtKColl.getDataValue("limit_type");
				}
				
				/**第一步，计算未生成合同部分授信占用，即申请时占用*/
				String iqpCondition = " where agr_no = '"+agr_no+"' and cont_no is null ";
				IndexedCollection iqpIColl = dao.queryList(modelId, iqpCondition, connection);
				IndexedCollection iqpCreditIColl = dao.queryList(modelIdCredit, iqpCondition, connection);
				KeyedCollection iqpkc = null;
				KeyedCollection iqpCrekc = null;
				String serno = "";
				for(int i=0;i<iqpIColl.size();i++){
					iqpkc = (KeyedCollection) iqpIColl.get(i);
					serno = (String) iqpkc.getDataValue("serno");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySerno(serno));//查询该申请下的授信占用，并循环累加
				}
				for(int i=0;i<iqpCreditIColl.size();i++){
					iqpCrekc = (KeyedCollection) iqpCreditIColl.get(i);
					serno = (String) iqpCrekc.getDataValue("serno");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySerno(serno));//查询该申请下的授信占用，并循环累加
				}
				
				/**第二步，计算有合同部分授信占用*/
				String condition = " where agr_no = '"+agr_no+"' and cont_no is not null ";
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);//查询授信和业务关系
				IndexedCollection creIColl = dao.queryList(modelIdCredit, condition, connection);//查询授信和业务关系
				KeyedCollection kc = null;
				KeyedCollection crekc = null;
				String cont_no = "";
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					serno = (String) kc.getDataValue("serno");
					if(limit_type.equals("01")){
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（循环额度方式）
					}else{
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getOneLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（一次性额度方式）
					}
				}
				for(int i=0;i<creIColl.size();i++){
					crekc = (KeyedCollection) creIColl.get(i);
					cont_no = (String) crekc.getDataValue("cont_no");
					serno = (String) crekc.getDataValue("serno");
					if(limit_type.equals("01")){
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（循环额度方式）
					}else{
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getOneLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（一次性额度方式）
					}
				}
				/**第三步，判断是否集团融资模式**/
				if(!serno.equals("")){
					String cusId = "";
					KeyedCollection iqpkColl = dao.queryDetail("IqpLoanApp", serno, connection);
					cusId = (String) iqpkColl.getDataValue("cus_id");
					
					//调用客户模块接口，查询该客户所属集团信息，非集团客户则无集团信息                 
					
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					grpiColl = service.getGrpMemberByCusId(cusId,context,connection);
					if(grpiColl!=null){
						if(grpiColl.size()>0){
							is_grp = "1";//集团客户
						}else{
							is_grp = "2";//非集团客户
						}
					}else{
						is_grp = "2";//非集团客户
					}
				}else{
					is_grp = "2";//关系表中不存在关系
				}
				
				/**第四步，获取集团授信占用，累加所有集团下法人的授信占用*/
				KeyedCollection cuskColl = null;
				if(grpiColl!=null){
					for(int i=0;i<grpiColl.size();i++){
						cuskColl = (KeyedCollection) grpiColl.get(i);
						String cus_id = (String) cuskColl.getDataValue("cus_id");
						grp_lmt_amt = grp_lmt_amt.add(iqpLoanAppComponent.getLmtAmtByCusId(cus_id));
					}
					KeyedCollection kCollMem = (KeyedCollection)grpiColl.get(0);
					grp_no = (String)kCollMem.getDataValue("grp_no");//取得集团编号
				}
			}else if(lmt_type.equals("02")){//同业额度
				modelId = "RBusLmtInfo";
				String modelIdCredit = "RBusLmtcreditInfo";
				/**第一步，计算未生成合同部分授信占用，即申请时占用*/
				String iqpCondition = " where agr_no = '"+agr_no+"' and cont_no is null ";
				IndexedCollection iqpIColl = dao.queryList(modelId, iqpCondition, connection);
				IndexedCollection iqpICollCredit = dao.queryList(modelIdCredit, iqpCondition, connection);
				KeyedCollection iqpkc = null;
				String serno = "";
				//单一法人授信关系表
				for(int i=0;i<iqpIColl.size();i++){
					iqpkc = (KeyedCollection) iqpIColl.get(i);
					serno = (String) iqpkc.getDataValue("serno");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySernoForBank(serno));//查询该申请下的授信占用，并循环累加
				}
				for(int i=0;i<iqpIColl.size();i++){
					iqpkc = (KeyedCollection) iqpIColl.get(i);
					serno = (String) iqpkc.getDataValue("serno");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySerno(serno));//并循环累加
				}
				//第三方授信关系表
				for(int i=0;i<iqpICollCredit.size();i++){
					iqpkc = (KeyedCollection) iqpICollCredit.get(i);
					serno = (String) iqpkc.getDataValue("serno");
					String lmt_type_rel = (String) iqpkc.getDataValue("lmt_type");
					if(!"90".equals(lmt_type_rel)){
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySernoForBank(serno));//查询该申请下的授信占用，并循环累加
					}
				}
				for(int i=0;i<iqpICollCredit.size();i++){
					iqpkc = (KeyedCollection) iqpICollCredit.get(i);
					String lmt_type_rel = (String) iqpkc.getDataValue("lmt_type");
					serno = (String) iqpkc.getDataValue("serno");
					String agr_no_select = (String) iqpkc.getDataValue("agr_no");
					if("90".equals(lmt_type_rel)){//占用承兑行
						HashMap<String,String> map = new HashMap<String,String>();
						String openDay = (String)context.getDataValue("OPENDAY");
						map.put("agr_no_select", agr_no_select);
						map.put("serno", serno);
						map.put("openDay", openDay);
						IndexedCollection iCollAmt = SqlClient.queryList4IColl("queryDrftAmt4Iqp", map, connection);
						if(iCollAmt.size()>0){
				        	KeyedCollection kCollAmt = (KeyedCollection)iCollAmt.get(0);
				        	lmt_amt = lmt_amt.add(BigDecimalUtil.replaceNull(kCollAmt.getDataValue("lmt_amt")));
				        }
						
					}else{
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySerno(serno));//并循环累加
					} 
				}
				
				/**第二步，计算有合同部分授信占用*/
				String condition = " where agr_no = '"+agr_no+"' and cont_no is not null ";
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);//查询授信和业务关系
				IndexedCollection iCollCredit = dao.queryList(modelIdCredit, condition, connection);
				KeyedCollection kc = null;
				String cont_no = "";
				//单一法人授信关系表
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNoForBank(cont_no));//查询该合同下的授信占用，并循环累加
				}
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（循环额度方式）
				}
				//第三方授信关系表
				for(int i=0;i<iCollCredit.size();i++){
					kc = (KeyedCollection) iCollCredit.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					String lmt_type_rel = (String) kc.getDataValue("lmt_type");
					if(!"90".equals(lmt_type_rel)){
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNoForBank(cont_no));//查询该合同下的授信占用，并循环累加
					}
				}
				for(int i=0;i<iCollCredit.size();i++){
					kc = (KeyedCollection) iCollCredit.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					
					String lmt_type_rel = (String) kc.getDataValue("lmt_type");
					String agr_no_select = (String) kc.getDataValue("agr_no");
					if("90".equals(lmt_type_rel)){//占用承兑行
						HashMap<String,String> map = new HashMap<String,String>();
						String openDay = (String)context.getDataValue("OPENDAY");
						map.put("agr_no_select", agr_no_select);
						map.put("cont_no", cont_no);
						map.put("openDay", openDay);
						IndexedCollection iCollAmt = SqlClient.queryList4IColl("queryDrftAmt4Crt", map, connection);
				        if(iCollAmt.size()>0){
				        	KeyedCollection kCollAmt = (KeyedCollection)iCollAmt.get(0);
				        	lmt_amt = lmt_amt.add(BigDecimalUtil.replaceNull(kCollAmt.getDataValue("lmt_amt")));
				        }
					}else{
						KeyedCollection kCollCtrLoanCont = dao.queryDetail("CtrLoanCont", cont_no, connection);
						KeyedCollection kCollCtrRpddscntCont = dao.queryDetail("CtrRpddscntCont", cont_no, connection);
						if(kCollCtrLoanCont.containsKey("cont_status")){
							String cont_status = (String)kCollCtrLoanCont.getDataValue("cont_status");
							if(!"100".equals(cont_status) && !"200".equals(cont_status) && cont_status != null){
								continue;
							}
						}
						if(kCollCtrRpddscntCont.containsKey("cont_status")){
							String cont_status = (String)kCollCtrRpddscntCont.getDataValue("cont_status");
							if(!"100".equals(cont_status) && !"200".equals(cont_status) && cont_status != null){
								continue;
							}
						}
						lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));//并循环累加
					} 
				}
				
				/**第三步，判断是否集团融资模式*/
				is_grp = "2";//同业额度，无需判断集团融资模式
				
				/**第四步，获取集团授信占用，累加所有集团下法人的授信占用*/
				grp_lmt_amt = new BigDecimal("0");//同业额度无集团授信占用
			}else if(lmt_type.equals("03")){//合同方额度
				modelId = "RBusLmtcreditInfo";
				
				/**第一步，计算未生成合同部分授信占用，即申请时占用*/
				String iqpCondition = " where agr_no = '"+agr_no+"' and cont_no is null ";
				IndexedCollection iqpIColl = dao.queryList(modelId, iqpCondition, connection);
				KeyedCollection iqpkc = null;
				String serno = "";
				for(int i=0;i<iqpIColl.size();i++){
					iqpkc = (KeyedCollection) iqpIColl.get(i);
					serno = (String) iqpkc.getDataValue("serno");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtBySerno(serno));//查询该申请下的授信占用，并循环累加
				}
				
				/**第二步，计算有合同部分授信占用*/
				String condition = " where agr_no = '"+agr_no+"' and cont_no is not null ";
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);//查询授信和业务关系
				KeyedCollection kc = null;
				String cont_no = "";
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					cont_no = (String) kc.getDataValue("cont_no");
					lmt_amt = lmt_amt.add(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));//查询该合同下的授信占用，并循环累加（第三方授信，没有一次性额度，所以使用循环额度方式）
				}
				
				/**第三步，判断是否集团融资模式*/
				is_grp = "2";//合作方额度，无需判断集团融资模式
				
				/**第四步，获取集团授信占用，累加所有集团下法人的授信占用*/
				grp_lmt_amt = new BigDecimal("0");//合作方额度无集团授信占用
			}
			
			kColl.addDataField("lmt_amt",lmt_amt);//授信占用
			kColl.addDataField("is_grp",is_grp);//是否集团融资模式(1:是、2:否)
			kColl.addDataField("grp_lmt_amt",grp_lmt_amt);//集团授信占用
			kColl.addDataField("grp_no",grp_no);//集团编号
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据授信台账编号/授信合作方协议编号查询授信占用失败!错误信息："+e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 通过业务流水号查询是否集团下成员，如果是，判断是否超过集团占用总额
	 */
	public KeyedCollection getGrpInfo(String serno,Connection connection,Context context) throws Exception{
		KeyedCollection kColl  = new KeyedCollection();
		IndexedCollection grpiColl = new IndexedCollection();
		BigDecimal grp_lmt_amt = new BigDecimal("0");//集团授信占用
		String cusId = "";//客户码
		String is_grp = "";//是否集团下成员
		String grp_no = "";//集团编号
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				
			KeyedCollection iqpkColl = dao.queryDetail("IqpLoanApp", serno, connection);
			cusId = (String) iqpkColl.getDataValue("cus_id");
			//调用客户模块接口，查询该客户所属集团信息，非集团客户则无集团信息                 
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			grpiColl = service.getGrpMemberByCusId(cusId,context,connection);
			if(grpiColl!=null){
				if(grpiColl.size()>0){
					is_grp = "1";//集团客户
				}else{
					is_grp = "2";//非集团客户
				}
			}else{
				is_grp = "2";//非集团客户
			}
			
			/**第四步，获取集团授信占用，累加所有集团下法人的授信占用*/
			KeyedCollection cuskColl = null;
			if(grpiColl!=null){
				String flag = iqpLoanAppComponent.getIsLrisk(serno, null, connection, context);
				if(!"can".equals(flag)){
					for(int i=0;i<grpiColl.size();i++){
						cuskColl = (KeyedCollection) grpiColl.get(i);
						String cus_id = (String) cuskColl.getDataValue("cus_id");
						grp_lmt_amt = grp_lmt_amt.add(iqpLoanAppComponent.getLmtAmtByCusId4Grp(cus_id,context,connection));
					}
					KeyedCollection kCollMem = (KeyedCollection)grpiColl.get(0);
					grp_no = (String)kCollMem.getDataValue("grp_no");//取得集团编号
				}else{
					kColl.put("grp_lmt_amt", grp_lmt_amt);
					kColl.put("grp_no", grp_no);
					return kColl;
				}
			}
			kColl.put("grp_lmt_amt", grp_lmt_amt);
			kColl.put("grp_no", grp_no);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据客户码查询集团授信占用信息失败!错误信息："+e.getMessage());
		}
		return kColl;
	}
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param arg_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getHistoryContByLimitAccNo(String limitInd, String argNo, PageInfo pageInfo,String conditionStr, Context context, Connection connection) throws EMPException {
		IndexedCollection contIColl = new IndexedCollection();
		/**
		 * 存量合同表表只有贷款ctr_loan_cont主表和贴现ctr_disc_cont两张主表
		 * 所有存量业务都会存在于这两张表中，目前通过直接查询获得。
		 */
		try {
			if(limitInd == null || limitInd.trim().length() == 0){
				throw new EMPException("获取授信台帐类型失败！");
			}
			if(argNo == null || argNo.trim().length() == 0){
				throw new EMPException("获取授信台帐编号失败！");
			}
			if(pageInfo == null){
				throw new EMPException("获取分页信息失败！");
			}
			
			String cont_no_str ="";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = SqlClient.queryList4IColl("selectContNoFormRel", argNo, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cont_no = (String)kColl.getDataValue("cont_no");
				if(i==(iColl.size()-1)){
					cont_no_str += "'"+cont_no+"'"; 
				}else{
					cont_no_str += "'"+cont_no+"',"; 
				}
			}
			if("".equals(cont_no_str)){
				cont_no_str ="''";
			}
			String conndition = "where cont_no in("+cont_no_str+")";
			if(conditionStr!= null && !"".equals(conditionStr)){
				conndition = conndition + " and " +conditionStr.substring(8, conditionStr.length()-2);
			}
			conndition += " order by cont_end_date desc";
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("cont_no");
			list.add("cn_cont_no");
			list.add("prd_id");
			list.add("assure_main");
			list.add("cont_cur_type");
			list.add("cont_amt");
			list.add("cont_balance");
			list.add("cont_start_date");
			list.add("cont_end_date");
			list.add("cont_status");
			list.add("exchange_rate");
			list.add("security_rate");
			list.add("same_security_amt");
			list.add("manager_br_id");
			list.add("input_id");
			list.add("input_br_id");
			list.add("biz_type");
			list.add("security_cur_type");
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			list.add("security_exchange_rate");
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			contIColl = dao.queryList("CtrLoanCont", list, conndition, pageInfo, connection); 
				//dao.queryList("CtrLoanCont", conndition, connection);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(contIColl, new String[]{"input_id","input_br_id"});
			
			for(int i=0;i<contIColl.size();i++){
				KeyedCollection contKColl = (KeyedCollection)contIColl.get(i);
				String prd_id = (String) contKColl.getDataValue("prd_id");
				String serno = (String) contKColl.getDataValue("serno");

				/**计算敞口金额*/
				BigDecimal cont_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("cont_amt"));
				
				//获取实时汇率  start
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
//				String cur_type = (String) contKColl.getDataValue("cont_cur_type");
//				String security_cur_type = (String) contKColl.getDataValue("security_cur_type");//保证金币种
//				if(security_cur_type==null||security_cur_type.equals("")){
//					security_cur_type = "CNY";
//				}
//				KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
//				KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				//获取实时汇率  end
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
							//risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
						}
					}
					//合同金额*保证金比例*（1+溢装比例）*合同汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
				    if(risk_open_amt.compareTo(new BigDecimal(0))<0){
				    	risk_open_amt = new BigDecimal(0);
				    }
				}
				
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				contKColl.addDataField("risk_open_amt", risk_amt);
			} 
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return contIColl;
	}
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（查询待发起状态的业务申请）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param arg_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getIqpByLimitAccNo(String limitInd, String argNo, PageInfo pageInfo, Context context, Connection connection) throws EMPException {
		IndexedCollection iqpIColl = new IndexedCollection();
		try {
			if(limitInd == null || limitInd.trim().length() == 0){
				throw new EMPException("获取授信台帐类型失败！");
			}
			if(argNo == null || argNo.trim().length() == 0){
				throw new EMPException("获取授信台帐编号失败！");
			}
			if(pageInfo == null){
				throw new EMPException("获取分页信息失败！");
			}
			
			String serno_str ="";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = SqlClient.queryList4IColl("selectSernoNoFormRel", argNo, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String serno = (String)kColl.getDataValue("serno");
				if(i==(iColl.size()-1)){
					serno_str += "'"+serno+"'"; 
				}else{
					serno_str += "'"+serno+"',"; 
				}
			}
			if("".equals(serno_str)){
				serno_str ="''";
			}
			String conndition = "where serno in("+serno_str+") and approve_status<>'997'";
			iqpIColl = dao.queryList("IqpLoanApp", null, conndition, pageInfo, connection); 
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iqpIColl, new String[]{"input_id","input_br_id"});
			
			for(int i=0;i<iqpIColl.size();i++){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(i);
				
				String prd_id = (String) iqpKColl.getDataValue("prd_id");
				String serno = (String) iqpKColl.getDataValue("serno");
				
				/**计算敞口金额*/
				BigDecimal apply_amount = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("apply_amount"));
				//获取实时汇率  start
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
//				String cur_type = (String) iqpKColl.getDataValue("apply_cur_type");//申请币种
//				String security_cur_type = (String) iqpKColl.getDataValue("security_cur_type");//保证金币种
//				KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
//				KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//申请汇率
//				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				//获取实时汇率  end
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					//申请金额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = (apply_amount.multiply(security_rate)).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
					if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
						risk_open_amt = new BigDecimal(0);
					}
					//（申请金额-保证金金额）*汇率-视同保证金
					    //申请金额*（1+怡装比例）-申请金额*（1+怡装比例）*保证金比例*汇率
				}
				
				//risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				iqpKColl.put("risk_open_amt", risk_amt);
			} 
			
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iqpIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return iqpIColl;
	}
	
	/**
	 * 通过授信协议编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param arg_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getHistoryContByLimitArgNo(String argNo, String conditionStr, Context context, Connection connection) throws EMPException {
		IndexedCollection contIColl = new IndexedCollection();
		try {
			if(argNo == null || argNo.trim().length() == 0){
				throw new EMPException("获取授信台帐编号失败！");
			}
			String conndition = "";
			String cont_no_str ="";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = SqlClient.queryList4IColl("selectContNoFormRel", argNo, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cont_no = (String)kColl.getDataValue("cont_no");
				if(i==(iColl.size()-1)){
					cont_no_str += "'"+cont_no+"'"; 
				}else{
					cont_no_str += "'"+cont_no+"',"; 
				}
			}
			if("".equals(cont_no_str)){
				cont_no_str ="''";
			}
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
            
			String sql_select ="";
			if(conditionStr!= null && !"".equals(conditionStr)){
				conditionStr = conditionStr.substring(8, conditionStr.length()-2);
				sql_select = "select a.serno,a.cus_id as cus_id,a.cont_no,a.prd_id,a.cont_amt,a.cont_status,'loanapp' as tab from ctr_loan_cont a where a.cont_no in("+cont_no_str+") and a."+conditionStr+" "+
                              "union all select b.serno,b.toorg_name as cus_id,b.cont_no,b.prd_id,b.takeover_total_amt,b.cont_status,'assetstrsf' as tab from ctr_assetstrsf_cont b where b.cont_no in("+cont_no_str+") and b."+conditionStr+" "+
                             "union all select c.serno,c.toorg_name as cus_id,c.cont_no,c.prd_id,c.bill_total_amt,c.cont_status,'rpddscnt' as tab from ctr_rpddscnt_cont c where c.cont_no in("+cont_no_str+") and c."+conditionStr+" ";
			}else{
				sql_select = "select a.serno,a.cus_id as cus_id,a.cont_no,a.prd_id,a.cont_amt,a.cont_status,'loanapp' as tab from ctr_loan_cont a where a.cont_no in("+cont_no_str+") "+
                             "union all select b.serno,b.toorg_name as cus_id,b.cont_no,b.prd_id,b.takeover_total_amt,b.cont_status,'assetstrsf' as tab from ctr_assetstrsf_cont b where b.cont_no in("+cont_no_str+") "+
                             "union all select c.serno,c.toorg_name as cus_id,c.cont_no,c.prd_id,c.bill_total_amt,c.cont_status,'rpddscnt' as tab from ctr_rpddscnt_cont c where c.cont_no in("+cont_no_str+") ";
			}
			
			contIColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
//			String conndition = "where cont_no in (select cont_no from r_bus_lmt_info where agr_no = '"+argNo+"' union select cont_no from r_bus_lmtcredit_info where agr_no = '"+argNo+"') ";
//			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
//			List<String> list = new ArrayList<String>();
//			contIColl = dao.queryList("CtrLoanCont", list, conndition, pageInfo, connection); 
//			/** 组织机构、登记机构翻译 */
//			SInfoUtils.addUSerName(contIColl, new String[]{"input_id","input_br_id"});
//			
//			for(int i=0;i<contIColl.size();i++){
//				KeyedCollection contKColl = (KeyedCollection)contIColl.get(i);
//				/**计算敞口金额*/
//				BigDecimal cont_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("cont_amt"));
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("exchange_rate"));//汇率
//				BigDecimal security_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_rate")); //保证金比例
//				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("same_security_amt"));//视同保证金
//				
//				BigDecimal risk_open_amt = new BigDecimal(0);
//				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
//				contKColl.addDataField("risk_open_amt", risk_open_amt);
//			} 
			
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return contIColl;
	}
	
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询待发起业务信息）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param agr_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getIqpByLimitAgrNoForSame(String agr_no, PageInfo pageInfo, Context context, Connection connection) throws EMPException {
		IndexedCollection contIColl = new IndexedCollection();
		try {
			if(agr_no == null || agr_no.trim().length() == 0){
				throw new EMPException("获取授信台帐编号失败！");
			}
			if(pageInfo == null){
				throw new EMPException("获取分页信息失败！");
			}
			String serno_str ="";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = SqlClient.queryList4IColl("selectSernoNoFormRel", agr_no, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String serno = (String)kColl.getDataValue("serno");
				if(i==(iColl.size()-1)){
					serno_str += "'"+serno+"'"; 
				}else{
					serno_str += "'"+serno+"',"; 
				}
			}
			if("".equals(serno_str)){
				serno_str ="''";
			}
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String sql_select =SqlClient.joinQuerySql("selectIqpBySerno",serno_str,null);
			contIColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
			//详细信息翻译时调用			
			SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return contIColl;
	}
	
	/**
	 * 更新监管机构信用评级、评级日期
	  * @param overseeOrgId	监管机构编号
	 * @param cdtEval	信用评级
	 * @param evalTime 评级日期
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public String updateCdtEval(String overseeOrgId, String cdtEval, String evalTime, Connection connection) throws Exception {
		try {
			Map<String, String> insertMap = new HashMap<String, String>();
			insertMap.put("cdtEval", cdtEval);
			insertMap.put("evalTime",  evalTime);
			SqlClient.update("updateCdtEval4Oversee",overseeOrgId,insertMap, null, connection);
		} catch (Exception e) {
			throw new EMPException("评级信息回写监管机构表失败，错误描述："+e.getMessage());
		}
		return "1";
	}
	/**
	 * 取借据信息pop
	 * @param cusId	客户编号
	 * @param OrgId	机构号
	 * @param connection 数据库连接
	 * @return 所选借据信息kcoll
	 * @throws EMPException
	 */
	public String queryBillNoPop(String cusId, String OrgId,
			Connection connection) throws Exception {
		
		return null;
	}
	
	
	/**
	 * 新增一般担保合同后保存业务合同关系表
	 * @param kColl	 
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 是否保存成功
	 * @throws EMPException
	 */
	public int addGrtLoanRGur(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		kColl.setId("GrtLoanRGur"); 
		int count = 0;
		String conditionStr = null;
		String condition = null;
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);  
		DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
		/**判断是否是信用证修改，是否通过合同编号过滤
		 * 判断是否存在此业务合同关系，存在则修改，不存在在新增*/
		String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
		String isCreditChange = (String)kColl.getDataValue("isCreditChange");
		String serno = (String)kColl.getDataValue("serno");
		conditionStr = "where serno='"+serno+"' and guar_cont_no='"+guar_cont_no+"'";
		condition  = "where serno='"+serno+"'";
		
		IndexedCollection selectIColl = dao.queryList("GrtLoanRGur", conditionStr, connection); 
		if(selectIColl.size()>0){
			KeyedCollection modelKcoll = (KeyedCollection)selectIColl.get(0);
			modelKcoll.setDataValue("guar_amt", kColl.getDataValue("guar_amt"));
			modelKcoll.setDataValue("is_per_gur", kColl.getDataValue("is_per_gur"));
			modelKcoll.setDataValue("is_add_guar", kColl.getDataValue("is_add_guar"));   
			count = dao.update(modelKcoll, connection);
		}else{
			//String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//kColl.addDataField("pk_id",pk_id);//主键
			
			/**如果为信用证修改，新增的一般担保合同关联关系为新增*/
			if("is".equals(isCreditChange)){
				kColl.addDataField("corre_rel","2"); //关联关系(新增)
				kColl.setDataValue("cont_no", "");//合同编号置空
			}else{
				kColl.addDataField("corre_rel","1"); //关联关系默认值(普通)   
			}
			
	/**担保等级赋值，大小为已有等级+1   --Start--*/      
				IndexedCollection iColl = (IndexedCollection)dao.queryList("GrtLoanRGur",condition, connection);  
				String guar_cont_no_str ="";
				IndexedCollection ContiColl =null;
				int m = 0;
				int size = 10;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
				//把担保合同编号拼装成一个String 
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
					String guarContNo = (String)kCollGrt.getDataValue("guar_cont_no");
					guar_cont_no_str += "'"+guarContNo+"',"; 
				}
				if(guar_cont_no_str.length()>1){
					guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
					/**调用担保模块接口*/
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
					ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, datasource); 		
				}
				
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
					String guar_cont_no_Rel = (String)kCollGrt.getDataValue("guar_cont_no");
					for(int j=0;j<ContiColl.size();j++){
						KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
						String guarContNo = (String)GuarContkColl.getDataValue("guar_cont_no");
						if(guar_cont_no_Rel.equals(guarContNo)){ 
							String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
							//判断担保合同类型 
							if(guar_cont_type.equals("01")){
								m += 1; //如果是最高额担保，则记录
							} 
						}
					}
				}
				kColl.addDataField("guar_lvl", iColl.size()-m+1); 
			
			/**---------------担保等级默认赋值--end-------------------*/
			count = dao.insert(kColl, connection);
			
		}
		
		return count;
	}
	/**
	 * 修改查看担保合同查询业务担保合同关系表
	 * @param pk_id	业务合同关系表主键  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同kColl
	 * @throws EMPException
	 */
	public KeyedCollection selectGetLoanRGur(String pk_id, Context context,Connection connection) throws Exception {
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kColl = dao.queryDetail("GrtLoanRGur", pk_id, connection);
		return kColl;  
	}
	/**
	 * 修改删除担保合同查询业务担保合同关系表此担保合同条数
	 * @param guar_cont_no	担保合同编号  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 条数 
	 * @throws EMPException
	 */
	public int checkGetLoanRGurNum(String guar_cont_no, Context context,Connection connection) throws Exception {
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		String condition = "where guar_cont_no = '"+guar_cont_no+"'";
		IndexedCollection iColl = (IndexedCollection)dao.queryList("GrtLoanRGur", condition, connection);
		return iColl.size();  
	}
	/**
	 * 担保合同模块查询业务担保合同关联表中新增的担保变更
	 * @param pageInfo  翻页信息
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同iColl
	 * @throws EMPException
	 */
	public IndexedCollection getInsertGuarChange(KeyedCollection queryData,PageInfo pageInfo,String conditionStrT, Context context,Connection connection) throws Exception {
		logger.info("---------------担保合同模块查询业务担保合同关联表中新增的担保变更  开始---------------");
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		IndexedCollection iColl = null;
		String condition = null;
		String conditionStr = "";
		if(queryData != null){
			String cont_no = (String)queryData.getDataValue("cont_no");
			String guar_cont_no = (String)queryData.getDataValue("guar_cont_no");
			String guar_way = (String)queryData.getDataValue("guar_way");
			String guar_cont_state = (String)queryData.getDataValue("guar_cont_state");
			if(guar_cont_no!=null && !"".equals(guar_cont_no)){
				conditionStr = "grt.guar_cont_no ='"+guar_cont_no+"'";
			}
			if(guar_way!=null && !"".equals(guar_way) && "".equals(conditionStr)){
				conditionStr = "grt.guar_way ='"+guar_way+"'";
			}else if(guar_way!=null && !"".equals(guar_way) && !"".equals(conditionStr)){
				conditionStr += " and grt.guar_way ='"+guar_way+"'";
			}
			if(guar_cont_state!=null && !"".equals(guar_cont_state) && "".equals(conditionStr)){
				conditionStr = "grt.guar_cont_state ='"+guar_cont_state+"'";
			}else if(guar_cont_state!=null && !"".equals(guar_cont_state) && !"".equals(conditionStr)){
				conditionStr += " and grt.guar_cont_state ='"+guar_cont_state+"'";
			}
			if("".equals(conditionStr)){
				conditionStr = " 1=1 ";
			}
			if(cont_no!=null && !"".equals(cont_no)){
				condition = "select rel.cont_no,grt.* from grt_loan_r_gur rel, grt_guar_cont grt where "+conditionStr+" and rel.guar_cont_no = grt.guar_cont_no and grt.guar_cont_state = '00' and rel.corre_rel='1' and rel.cont_no is not null and rel.cont_no='"+cont_no+"' and rel.cont_no in(" +
				            " select grt.cont_no as cont_no from iqp_guar_change_app grt "+
                            "union all "+
                            "select credit.cont_no as cont_no from iqp_credit_change_app credit "+
                            "union all "+
                            "select guarant.cont_no as  cont_no  from iqp_guarant_change_app guarant )";
			}else{
				condition = "select rel.cont_no,grt.* from grt_loan_r_gur rel, grt_guar_cont grt where "+conditionStr+" and rel.guar_cont_no = grt.guar_cont_no and grt.guar_cont_state = '00' and rel.cont_no is not null and rel.corre_rel='2'  and rel.cont_no in(" +
				            " select grt.cont_no as cont_no from iqp_guar_change_app grt "+
                            "union all "+
                            "select credit.cont_no as cont_no from iqp_credit_change_app credit "+
                            "union all "+
                            "select guarant.cont_no as  cont_no  from iqp_guarant_change_app guarant )";
			}
		}else{
			condition = "select rel.cont_no,grt.* from grt_loan_r_gur rel, grt_guar_cont grt where rel.guar_cont_no = grt.guar_cont_no and grt.guar_cont_state = '00' and rel.cont_no is not null and rel.corre_rel='1' and rel.cont_no in(" +
					" select grt.cont_no as cont_no from iqp_guar_change_app grt "+
                    "union all "+
                    "select credit.cont_no as cont_no from iqp_credit_change_app credit "+
                    "union all "+
                    "select guarant.cont_no as  cont_no  from iqp_guarant_change_app guarant )";
		} 
		//added by yangzy 2015/04/14 需求：XD150325024，集中作业扫描岗权限改造 START 
		if(conditionStrT!=null&&!"".equals(conditionStrT)){
			conditionStrT = conditionStrT.replace("WHERE", " AND ");
			condition += conditionStrT;
		}
		//added by yangzy 2015/04/14 需求：XD150325024，集中作业扫描岗权限改造 END 
		try{
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			iColl= TableModelUtil.buildPageData(pageInfo,dataSource, condition);
		}catch(Exception e){
			logger.error("担保合同模块查询业务担保合同关联表中新增的担保变更，错误描述："+e.getMessage());
			throw new EMPException("担保合同模块查询业务担保合同关联表中新增的担保变更，错误描述："+e.getMessage());	
		}
		return iColl;
	}
	/**
	 * 根据查询条件返回台账信息
	 * @param queryData
	 * @param pageInfo（可传空）
	 * @param context
	 * @param connection
	 * @param acc_type（1-贷款台账，2-银承台账，3-票据台账，4-垫款台账，5-资产转让台账）
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getAccByQueryData(KeyedCollection queryData,PageInfo pageInfo, Context context,Connection connection,String acc_type) throws Exception {
		logger.info("---------------根据查询条件返回台账信息  开始---------------");
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		IndexedCollection iColl = null;
		String conditionStr = "";
		String modelId = "";
		String menuId = "";
		String cus_id = "";
		if(acc_type.equals("1")){
			modelId = "AccLoan";
			cus_id = (String)queryData.getDataValue("cus_id");
		}else if(acc_type.equals("2")){
			modelId = "AccAccp";
			cus_id = (String)queryData.getDataValue("daorg_cusid");
		}else if(acc_type.equals("3")){
			modelId = "AccDrft";
			cus_id = (String)queryData.getDataValue("discount_per");
		}else if(acc_type.equals("4")){
			modelId = "AccPad";
			cus_id = (String)queryData.getDataValue("cus_id");
		}else if(acc_type.equals("5")){
			modelId = "AccAssetstrsf";
			cus_id = (String)queryData.getDataValue("cus_id");
		}else{
			throw new EMPException("未知台账类型！");	
		}
		
		try{
			if(queryData != null){
				conditionStr = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false);
			}
			//************start
			if(context.containsKey("menuId")){
				menuId = (String) context.getDataValue("menuId");
				//通过客户码去合作方授信协议表(去除coop_type=010为联保)中查询授信协议编号,再从业务和第三方关系表中去查出业务合同编号
				if("expert_check_task_coop".equals(menuId)){
					conditionStr = "where cont_no in(select rel.cont_no from r_bus_lmtcredit_info rel where rel.agr_no in(select lmt.agr_no from Lmt_Agr_Joint_Coop lmt where lmt.cus_id='"+cus_id+"' and lmt.coop_type<>'010'))";
				}else if("".equals(menuId)){
					conditionStr = "where cont_no in(select iqpRel.Cont_No from grt_loan_r_gur iqpRel where iqpRel.Guar_Cont_No in(select grtRe.Guar_Cont_No from grt_guaranty_re grtRe where grtRe.Guaranty_Id in (select grt.guar_id from Grt_Guarantee grt,cus_base cus where grt.cus_id=cus.cus_id and cus.cus_type='A2' and grt.cus_id = '"+cus_id+"')))";
				}
			}
			
			//************end
			if(pageInfo==null){
				iColl = dao.queryList(modelId, null, conditionStr, connection);
			}else{
				iColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);
			}
			
		}catch(Exception e){ 
			logger.error("根据查询条件返回台账信息，错误描述："+e.getMessage());
			throw new EMPException("根据查询条件返回台账信息，错误描述："+e.getMessage());	
		}
		return iColl;
	}
	
	/**
	 * 通用查询借据信息pop
	 */
	public void queryAccLoanPop() {
	}
	/**
	 * 通过币种从汇率表中取得币种对应的汇率信息
	 * @param currType 币种
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return double
	 * @throws Exception
	 */
	public KeyedCollection getHLByCurrType(String currType, Context context,Connection connection) throws Exception {
		IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
		KeyedCollection ic = cmisComponent.getHLByCurrType(currType);
		return ic;
	}
	/**
	 * 通过担保合同编号获取其所关联的业务合同
	 * @param GuarContNo 担保合同编号
	 * @param type 1--担保合同编号，2--池编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 担保所关联合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getHistoryContByGuarContNo(String GuarContNo, String type,PageInfo pageInfo, Context context, Connection connection) throws EMPException {
		IndexedCollection contIColl = new IndexedCollection();
		IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
		try {
			if(GuarContNo == null || GuarContNo.trim().length() == 0){
				throw new EMPException("获取担保合同编号失败！");
			}
			String conndition = "";
			if(type== null || type.trim().length() == 0){
				throw new EMPException("获取参数类型失败！");
			}else{
				if("1".equals(type)){
					conndition = "where cont_no in(select a.cont_no from Grt_Loan_R_Gur a where a.guar_cont_no='"+GuarContNo+"')";
				}else if("2".equals(type)){
					conndition = "where cont_no in(select a.cont_no from Grt_Loan_R_Gur a where a.guar_cont_no in (select guar_cont_no from Grt_Guaranty_Re where guaranty_id = '"+GuarContNo+"'))";
				}
				//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start
				else if("3".equals(type)){//保理池关联合同
					conndition = "where cont_no in(select a.cont_no from Grt_Loan_R_Gur a where a.guar_cont_no in (select guar_cont_no from Grt_Guaranty_Re where guaranty_id = '"+GuarContNo+"'))";
					conndition +="   or cont_no in (select p1.cont_no                            "
								+"                    from ctr_loan_cont p1                      "
								+"                   where exists (select 1                      "
								+"                            from iqp_inter_fact p2             "
								+"                           where p1.serno = p2.serno           "
								+"                             and p2.po_no = '"+GuarContNo+"')) ";
				}
				//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end
			}
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("cont_no");
			list.add("cn_cont_no");
			list.add("prd_id");
			list.add("assure_main");
			list.add("cont_cur_type");
			list.add("cont_amt");
			list.add("cont_balance");
			list.add("cont_start_date");
			list.add("cont_end_date");
			list.add("cont_status");
			list.add("exchange_rate");
			list.add("security_rate");
			list.add("same_security_amt");
			list.add("manager_br_id");
			list.add("input_id");
			list.add("input_br_id");
			contIColl = dao.queryList("CtrLoanCont", list, conndition, pageInfo, connection); 
				//dao.queryList("CtrLoanCont", conndition, connection);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(contIColl, new String[]{"input_id","input_br_id"});
			
			for(int i=0;i<contIColl.size();i++){
				KeyedCollection contKColl = (KeyedCollection)contIColl.get(i);
				String cont_no = (String)contKColl.getDataValue("cont_no");
				BigDecimal lmt_amt = new BigDecimal(0);
				lmt_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);
				
//				/**计算敞口金额*/
//				BigDecimal cont_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("cont_amt"));
//				
//				//获取实时汇率  start
//				String cur_type = (String) contKColl.getDataValue("cont_cur_type");
//				KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//				//获取实时汇率  end
//				
//				BigDecimal security_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_rate")); //保证金比例
//				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("same_security_amt"));//视同保证金
//				
//				BigDecimal risk_open_amt = new BigDecimal(0);
//				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
//				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				contKColl.addDataField("risk_open_amt", lmt_amt);
			} 
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
		    //详细信息翻译时调用	
		    SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			throw new EMPException("通过担保合同编号获取其所关联的业务合同错误，错误描述："+e.getMessage());
		}
		return contIColl;
	}

	public IndexedCollection getAccByCondition(String condition,Context context,PageInfo pageInfo, Connection connection, String acc_type)
			throws Exception {
		logger.info("---------------根据查询条件返回台账信息  开始---------------");
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		IndexedCollection iColl = null;
		String modelId = "";
		if(acc_type.equals("1")){
			modelId = "AccLoan";
		}else if(acc_type.equals("2")){
			modelId = "AccAccp";
		}else if(acc_type.equals("3")){
			modelId = "AccDrft";
		}else if(acc_type.equals("4")){
			modelId = "AccPad";
		}else if(acc_type.equals("5")){
			modelId = "AccAssetstrsf";
		}else if(acc_type.equals("0")){
			modelId = "CusManager";
			iColl = dao.queryList(modelId, condition, connection);
			return iColl;
		}else if(acc_type.equals("6")){
			condition = condition.replaceFirst("acc_status", "status");
			modelId = "AccView";
		}else{
			throw new EMPException("未知台账类型！");	
		}
		
		try{
			iColl = dao.queryList(modelId, null, condition, pageInfo, connection);
		}catch(Exception e){ 
			e.printStackTrace();
			logger.error("根据查询条件返回台账信息，错误描述："+e.getMessage());
			throw new EMPException("根据查询条件返回台账信息，错误描述："+e.getMessage());	
		}
		return iColl;
	}

	public String delManageInfo(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		String bill_no = kColl.getDataValue("bill_no").toString(); //借据编号
		String cont_no = kColl.getDataValue("cont_no").toString(); //合同编号
		String rcv_person = kColl.getDataValue("rcv_person").toString(); //接收人员
		String rcv_org = kColl.getDataValue("rcv_org").toString(); //接收机构
		
		KeyedCollection tranc_kColl = new KeyedCollection();//传到dao中的值
		tranc_kColl.addDataField("bill_no", bill_no);
		tranc_kColl.addDataField("cont_no", cont_no);
		tranc_kColl.addDataField("manager_id", rcv_person);
		tranc_kColl.addDataField("manager_br_id", rcv_org);
		
		CatalogManaComponent cmisComponent = (CatalogManaComponent)CMISComponentFactory.
		getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
		cmisComponent.excuteSql("DelManageInfo", tranc_kColl);
		return null;
	}

	public KeyedCollection getAccByConditionKcoll(String condition,Context context, Connection connection, String acc_type)
			throws Exception {
		logger.info("---------------根据查询条件返回台账信息  开始---------------");
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kColl = null;
		String modelId = "";
		if(acc_type.equals("1")){
			modelId = "AccLoan";
		}else if(acc_type.equals("2")){
			modelId = "AccAccp";
		}else if(acc_type.equals("3")){
			modelId = "AccDrft";
		}else if(acc_type.equals("4")){
			modelId = "AccPad";
		}else if(acc_type.equals("5")){
			modelId = "AccAssetstrsf";
		}else{
			throw new EMPException("未知台账类型！");	
		}		
		try{
			kColl = dao.queryDetail(modelId, condition, connection);
		}catch(Exception e){ 
			logger.error("根据查询条件返回台账信息，错误描述："+e.getMessage());
			throw new EMPException("根据查询条件返回台账信息，错误描述："+e.getMessage());	
		}
		return kColl;
	}
	/**
	 * 根据serno统计业务申请阶段保证金的占用总额
	 * @param serno
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getBailInfo4IqpApp(String serno, Context context,Connection connection) throws Exception {
		logger.info("---------------根据serno统计业务申请阶段保证金的占用总额  开始---------------");
		BigDecimal amt = new BigDecimal(0.00);
		KeyedCollection returnKColl = new KeyedCollection();
		try {
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getBailInfo4IqpApp", serno,null,connection);
			amt = BigDecimalUtil.replaceNull(returnKColl.getDataValue("res"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("---------------根据serno统计业务申请阶段保证金的占用总额  结束---------------");
		return amt;
	}
	/**
	 * 根据serno统计合同之后保证金占用总额
	 * @param serno
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getBailInfo4cont(String serno, Context context,Connection connection) throws Exception {
        logger.info("---------------根据serno统计合同之后保证金占用总额  开始---------------");
        TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
        IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
        BigDecimal amt = new BigDecimal(0.00);
        BigDecimal cont_amt = new BigDecimal(0.00);
        BigDecimal security_rate = new BigDecimal(0.00);
        String condition = "where cont_no in(select b.cont_no from pub_bail_info b where b.bail_acct_no in(select a.bail_acct_no from pub_bail_info a where a.serno = '"+serno+"') and b.cont_no is not null) and cont_status='200'";
		IndexedCollection iColl = dao.queryList("CtrLoanCont", condition, connection);
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cont_no = (String)kColl.getDataValue("cont_no");
			security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));
			String cur_type = (String)kColl.getDataValue("cont_cur_type");
			String security_cur_type = (String) kColl.getDataValue("security_cur_type");//保证金币种

			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//获取实时汇率
			//KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
			//KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
			//if("failed".equals(kCollRate.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
			
			//循环贷款 贴现贷款 银票贷款
			if(prd_id.equals("100039")||prd_id.equals("100050") ){
				cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
			}else if( prd_id.equals("300021")||prd_id.equals("300020")){
				KeyedCollection drftkcoll = (KeyedCollection)SqlClient.queryFirst("queryDrftAmt4CtrLoan", cont_no, null, connection);
				cont_amt = BigDecimalUtil.replaceNull(drftkcoll.getDataValue("amt"));
				//cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
			}else if(prd_id.equals("200024")){//银承贷款,
				cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				String condtitionStr = "where cont_no='"+cont_no+"'";
				IndexedCollection iCollAcc = dao.queryList("AccView", condtitionStr, connection);
				if(iCollAcc.size()>0){
					BigDecimal loan_balance_all = cmisComponent.getAccpBalanceByContNo(cont_no);
					amt = amt.add(loan_balance_all.multiply(security_rate));
				}else{
					amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
				}
			}else{//其他类型业务,生成贷款台账,授信占用=合同余额+台账余额
				BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_balance"));
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}                       
					}
					//合同余额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = cont_balance.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					securityAmt = cmisComponent.carryCurrency(securityAmt);
					cont_balance = securityAmt.multiply(exchange_rate_security);
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no,context,connection);
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					loan_balance_all = cmisComponent.carryCurrency(loan_balance_all);
					loan_balance_all = loan_balance_all.multiply(exchange_rate_security);
					cont_balance = cont_balance.add(loan_balance_all);
				}else{
					cont_balance = cont_balance.multiply(security_rate).multiply(exchange_rate);
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no,context,connection);
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate);
					cont_balance = cont_balance.add(loan_balance_all);
				}
				amt = amt.add(cont_balance);
			}
		}
		logger.info("---------------根据serno统计合同之后保证金占用总额  结束---------------");
		return amt;
	}
	
	/**
	 * 根据cont_no统计合同之后保证金占用总额(去除本笔业务for信用证、保函修改)
	 * @param serno
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getBailInfo4Change(String cont_no, Context context,Connection connection) throws Exception {
		logger.info("---------------根据cont_no统计合同之后保证金占用总额  开始---------------");
		IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
		
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		BigDecimal amt = new BigDecimal(0.00);
		BigDecimal cont_amt = new BigDecimal(0.00);
		BigDecimal security_rate = new BigDecimal(0.00);
		String condition = "where cont_no in(select b.cont_no from pub_bail_info b where b.bail_acct_no in(select a.bail_acct_no from pub_bail_info a where a.cont_no = '"+cont_no+"') and cont_no<>'"+cont_no+"' and b.cont_no <>'')";
		IndexedCollection iColl = dao.queryList("CtrLoanCont", condition, connection);
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cont_no_select = (String)kColl.getDataValue("cont_no");
			String serno = (String)kColl.getDataValue("serno");
			String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");
			String security_cur_type = (String)kColl.getDataValue("security_cur_type");
			security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));
			
			//获取实时汇率
			KeyedCollection kCollRate = this.getHLByCurrType(cont_cur_type, context, connection);
			KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
			if("failed".equals(kCollRate.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			
			//循环贷款 
			if(prd_id.equals("100039")||prd_id.equals("100050")){
				cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
			}else if(prd_id.equals("300021")||prd_id.equals("300020")){//贴现贷款
				KeyedCollection drftkcoll = (KeyedCollection)SqlClient.queryFirst("queryDrftAmt4CtrLoan", cont_no_select, null, connection);
				cont_amt = BigDecimalUtil.replaceNull(drftkcoll.getDataValue("amt"));
				//cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
			}else if(prd_id.equals("200024")){//银承贷款
				String condtitionStr = "where cont_no='"+cont_no_select+"'";
				IndexedCollection iCollAcc = dao.queryList("AccView", condtitionStr, connection);
				if(iCollAcc.size()>0){
					BigDecimal loan_balance_all = cmisComponent.getAccpBalanceByContNo(cont_no_select);
					loan_balance_all = loan_balance_all.add(loan_balance_all);
					amt = amt.add(loan_balance_all.multiply(security_rate).multiply(exchange_rate));
				}else{
					amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate));
				}
			}else{//其他类型业务,生成贷款台账,授信占用=合同余额+台账余额
				BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_balance"));
				BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
				if("500027".equals(prd_id) || "500028".equals(prd_id) || "500026".equals(prd_id) || "500021".equals(prd_id) || "500020".equals(prd_id) ||
						"500032".equals(prd_id) || "500029".equals(prd_id) || "500031".equals(prd_id) || "500022".equals(prd_id) || "500025".equals(prd_id) || 
						"500024".equals(prd_id) || "500023".equals(prd_id) || "400020".equals(prd_id) || "700020".equals(prd_id) || "700021".equals(prd_id)){
					//判断是否为信用证业务
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					//合同余额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = cont_balance.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					securityAmt = cmisComponent.carryCurrency(securityAmt);
					cont_balance = securityAmt.multiply(exchange_rate_security);
					
					//贷款余额*保证金比例*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					//贷款余额
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no_select,context,connection);
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					loan_balance_all = cmisComponent.carryCurrency(loan_balance_all);
					loan_balance_all = loan_balance_all.multiply(exchange_rate_security);
					cont_balance = cont_balance.add(loan_balance_all);
				}else{
					cont_balance = cont_balance.multiply(security_rate).multiply(exchange_rate);
					
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no_select,context,connection);
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate);
					cont_balance = cont_balance.add(loan_balance_all);
				}
				amt = amt.add(cont_balance);
			}   
		}
		logger.info("---------------根据cont_no统计合同之后保证金占用总额  结束---------------");
		return amt;
	}
	/**
	 * 根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLoanBalanceByContNo(String cont_no,Context context,Connection connection) throws Exception {
		BigDecimal loan_balance_all = new BigDecimal("0");
		String cont_status = "";
		IndexedCollection iColl = null;
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
			cont_status = (String) kColl.getDataValue("cont_status");
			String prd_id = (String) kColl.getDataValue("prd_id");
			if(cont_status.equals("200")){//仅统计生效合同
				if("700020".equals(prd_id) || "700021".equals(prd_id) || "400020".equals(prd_id)){
					iColl = SqlClient.queryList4IColl("queryLoanBalanceByContNo4Spe", cont_no, connection);
				}else{
					iColl = SqlClient.queryList4IColl("queryLoanBalanceByContNo", cont_no, connection);
				}
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					loan_balance_all = new BigDecimal(kc.getDataValue("loan_balance")+"");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据合同号获取总贷款余额失败!");
		}
		return loan_balance_all;
	}

	/**
	 * 根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getWFPvpAmtByContNo(String cont_no,Context context,Connection connection) throws Exception {
		BigDecimal pvp_amt_all = new BigDecimal("0");
		String cont_status = "";
		IndexedCollection iColl = null;
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
			cont_status = (String) kColl.getDataValue("cont_status");
			if(cont_status.equals("200")){//仅统计生效合同
				iColl = SqlClient.queryList4IColl("queryWFPvpAmtByContNo", cont_no, connection);
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					pvp_amt_all = new BigDecimal(kc.getDataValue("pvp_amt")+"");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据合同编号获取该合同下所有未审批通过的出账金额失败!");
		}
		return pvp_amt_all;
	}

	/**
	 * 根据授信台账编号查询项下业务是否结清
	 * @param limit_code 授信台账编号 字符串 
	 * @param context
	 * @param connection
	 * @return true/false结果
	 * @throws Exception
	 */
	public Boolean checkIqpInfo4Lmt(String limit_code, Context context, Connection connection) throws Exception {
		Boolean res = true;
		String cont_no_str = "";
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		if(!limit_code.startsWith("'")){
			limit_code = "'"+limit_code+"'";
		}
		String condition = "where agr_no in("+limit_code+")";
		IndexedCollection iCollCredit = dao.queryList("RBusLmtInfo", condition, connection);
		if(iCollCredit.size()==0 || iCollCredit==null){
			res = true;
		}else{
			/**1.关系表中合同编号为空的，则业务未结清
			 * 2.组装合同编号字符串
			 */
			for(int i=0;i<iCollCredit.size();i++){
				KeyedCollection kCollCredit = (KeyedCollection)iCollCredit.get(i);
				String cont_no = (String)kCollCredit.getDataValue("cont_no");
				if(cont_no == null || "".equals(cont_no)){
					res = false;
					break;
				}else{
					if(i==(iCollCredit.size()-1)){
						cont_no_str += "'"+cont_no+"'";
					}else{
						cont_no_str += "'"+cont_no+"',";
					}
				}
			}
			//通过合同编号字符串判断业务是否已结清
			if(res != false){
				 //1.是否有在途出账 2.台账记录中是否有未结清的记录
				 String conditionPvp = "where cont_no in("+cont_no_str+") and approve_status in('000','111','991','992')";//待发起，审批中，重办，打回
		         String conditionAcc = "where cont_no in("+cont_no_str+") and status not in('8','9')";
				 IndexedCollection iCollPvp = dao.queryList("PvpLoanApp", conditionPvp, connection);
		         IndexedCollection iCollAcc = dao.queryList("AccView", conditionAcc, connection);
		         if((iCollPvp.size()==0 || iCollPvp == null) && (iCollAcc.size()==0 || iCollAcc != null)){
		        	 res = true;
		         }else{
		        	 res = false;
		         }
			}
		}
		return res;
	}

	/**
	 * 根据授信台账编号查询是否发生业务（判断是否可以一票否决等）
	 * @param limit_code 授信台账编号 字符串 
	 * @param context
	 * @param connection
	 * @return true/false结果
	 * @throws Exception
	 */
	public Boolean checkIqp4DisAgree(String limit_code, Context context, Connection connection) throws Exception {
		Boolean res = true;
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		if(!limit_code.startsWith("'")){
			limit_code = "'"+limit_code+"'";
		}
		String condition = "where agr_no in("+limit_code+")";
		IndexedCollection iColl = dao.queryList("RBusLmtInfo", condition, connection);
		if(iColl.size()>0 && iColl!=null){
			res = false;
		}else{
			res = true;
		}
		
		return res;
	}
	
	/**
	 * 根据条件更新业务表信息
	 * @param kColl (默认参数1.type其他按需扩展)
	 * @param context
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection updateIqpByCondition(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		Object type = kColl.getDataValue("type");
		if(type.equals("updateBadDbtBalance")){ //呆账认定申请流程后处理，更新呆账余额，并将其他三项余额清0
			String tableName = "AccLoan";
			String bill_no = kColl.getDataValue("bill_no").toString();
			KeyedCollection acc_kColl = dao.queryDetail(tableName, bill_no, connection);
			String loan_balance = acc_kColl.getDataValue("loan_balance").toString();	//取贷款余额
			
			acc_kColl.setDataValue("normal_balance", "0.00");
			acc_kColl.setDataValue("overdue_balance", "0.00");
			acc_kColl.setDataValue("slack_balance", "0.00");
			acc_kColl.setDataValue("bad_dbt_balance", loan_balance);
			dao.update(acc_kColl, connection);
		}
		
		return kColl;
	}
	/**
	 * 根据担保合同编号获取其与业务关联表信息
	 * @param guar_cont_no 担保合同编号
	 * @param connection
	 * @return IndexedCollection 业务关联表信息iColl
	 * @throws Exception
	 */
	public IndexedCollection getIqpGuarContReByGuarContNo(String guar_cont_no,Connection connection) throws Exception {
		logger.info("---------------根据担保合同编号获取其与业务关联表信息开始---------------");
		IndexedCollection iColl = null;
		try {
			iColl =SqlClient.queryList4IColl("getIqpGuarContReByGuarContNo",guar_cont_no,connection);
		}catch (Exception e){
			logger.error("根据担保合同编号获取其与业务关联表信息失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号获取其与业务关联表信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号获取其与业务关联表信息结束---------------");
		return iColl;
	}
	/**
	 * 通过客户码查询客户的存量台账
	 * @param cus_id 客户码 
	 * @param connection
	 * @param context
	 * @return IndexedCollection 返回的台账数据
	 * @throws Exception
	 */
	public IndexedCollection getAccViewByCusId(String cus_id,Connection connection, Context context) throws Exception {
		logger.info("---------------通过客户码查询客户的存量台账开始---------------");
		IndexedCollection iColl = null;
		try {
			String modelId = "AccView";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String condition = "where cus_id='"+cus_id+"'";
		    iColl = dao.queryList(modelId, condition, connection);
		}catch (Exception e){
			logger.error("通过客户码查询客户的存量台账失败，错误描述："+e.getMessage());
			throw new EMPException("通过客户码查询客户的存量台账失败，错误描述："+e.getMessage());
		}
		logger.info("---------------通过客户码查询客户的存量台账结束---------------");
		return iColl;
	}
	
	/**
	 * 通过融资性担保公司授信台帐编号获取授信台帐存量业务（查询待发起状态的业务申请）
	 * @param cus_id 融资性担保公司客户ID
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getIqp4LmtAgrFinGuar(String cus_id, PageInfo pageInfo, Context context, Connection connection) throws EMPException {
		IndexedCollection iqpIColl = new IndexedCollection();
		try {
			if(cus_id == null || cus_id.trim().length() == 0){
				throw new EMPException("获取融资性担保公司客户ID失败！");
			}
			if(pageInfo == null){
				throw new EMPException("获取分页信息失败！");
			}
			String serno_str ="select iqpRel.serno from grt_loan_r_gur iqpRel where iqpRel.Guar_Cont_No in (select grtRe.Guar_Cont_No from grt_guaranty_re grtRe where grtRe.Guaranty_Id in (select grt.guar_id from Grt_Guarantee grt, cus_base cus where grt.cus_id = cus.cus_id and cus.cus_type = 'A2' and grt.cus_id = '"+cus_id+"'))";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String conndition = "where serno in("+serno_str+") and approve_status<>'997'";
			iqpIColl = dao.queryList("IqpLoanApp", null, conndition, pageInfo, connection); 
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iqpIColl, new String[]{"input_id","input_br_id"});
			
			for(int i=0;i<iqpIColl.size();i++){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(i);
				
				String prd_id = (String) iqpKColl.getDataValue("prd_id");
				String serno = (String) iqpKColl.getDataValue("serno");
				
				/**计算敞口金额*/
				BigDecimal apply_amount = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("apply_amount"));
				//获取实时汇率  start
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
//				String cur_type = (String) iqpKColl.getDataValue("apply_cur_type");//申请币种
//				String security_cur_type = (String) iqpKColl.getDataValue("security_cur_type");//保证金币种
//				KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
//				KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//申请汇率
//				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				//获取实时汇率  end
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					//申请金额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = (apply_amount.multiply(security_rate)).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
					if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
						risk_open_amt = new BigDecimal(0);
					}
					//（申请金额-保证金金额）*汇率-视同保证金
					    //申请金额*（1+怡装比例）-申请金额*（1+怡装比例）*保证金比例*汇率
				}
				
				//risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				iqpKColl.put("risk_open_amt", risk_amt);
			}
			
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iqpIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return iqpIColl;
	}
	
	/**
	 * 通过融资性担保公司授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param cus_id 融资性担保公司客户ID
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	public IndexedCollection getHisCont4LmtAgrFinGuar(String cus_id, PageInfo pageInfo,String conditionStr, Context context, Connection connection) throws EMPException {
		IndexedCollection contIColl = new IndexedCollection();
		try {
			if(cus_id == null || cus_id.trim().length() == 0){
				throw new EMPException("获取授信台帐编号失败！");
			}
			if(pageInfo == null){
				throw new EMPException("获取分页信息失败！");
			}
			
			String cont_no_str ="select iqpRel.cont_no from grt_loan_r_gur iqpRel where iqpRel.Guar_Cont_No in (select grtRe.Guar_Cont_No from grt_guaranty_re grtRe where grtRe.Guaranty_Id in (select grt.guar_id from Grt_Guarantee grt, cus_base cus where grt.cus_id = cus.cus_id and cus.cus_type = 'A2' and grt.cus_id = '"+cus_id+"'))";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String conndition = "where cont_no in("+cont_no_str+")";
			if(conditionStr!= null && !"".equals(conditionStr)){
				conndition = conndition + " and " +conditionStr.substring(8, conditionStr.length()-2);
			}
			conndition += " order by cont_end_date desc"; 
			
			List<String> list = new ArrayList<String>();
			contIColl = dao.queryList("CtrLoanCont", list, conndition, pageInfo, connection); 
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(contIColl, new String[]{"input_id","input_br_id"});
			
			for(int i=0;i<contIColl.size();i++){
				KeyedCollection contKColl = (KeyedCollection)contIColl.get(i);
				
				String prd_id = (String) contKColl.getDataValue("prd_id");
				String serno = (String) contKColl.getDataValue("serno");

				/**计算敞口金额*/
				BigDecimal cont_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("cont_amt"));
				
				//获取实时汇率  start
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
//				String cur_type = (String) contKColl.getDataValue("cont_cur_type");
//				String security_cur_type = (String) contKColl.getDataValue("security_cur_type");//保证金币种
//				if(security_cur_type==null||security_cur_type.equals("")){
//					security_cur_type = "CNY";
//				}
//				KeyedCollection kCollRate = this.getHLByCurrType(cur_type, context, connection);
//				KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				//获取实时汇率  end
				BigDecimal security_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
							//risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
						}
					}
					//合同金额*保证金比例*（1+溢装比例）*合同汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
				    if(risk_open_amt.compareTo(new BigDecimal(0))<0){
				    	risk_open_amt = new BigDecimal(0);
				    }
				}
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				contKColl.addDataField("risk_open_amt", risk_amt);
			} 
			
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("获取授信占用明细错误，错误描述："+e.getMessage());
		} 
		return contIColl;
	}
	
	/**
	 * 通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计）
	 * @param cus_id 客户ID
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException 
	 */
	public KeyedCollection getCusBizBalanceByCusId(String cus_id,Connection connection) throws EMPException {
		logger.info("---------------通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计） 开始---------------");
		IndexedCollection iColl = null;
		KeyedCollection acckColl = new KeyedCollection();
		try {
			//贷款余额、贷款累计欠息
			iColl =SqlClient.queryList4IColl("selectLoanBalanceByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("loan_balance", kCollTmp.getDataValue("loan_bal")==null?"0":kCollTmp.getDataValue("loan_bal"));
				acckColl.put("owe_int", kCollTmp.getDataValue("owe_int")==null?"0":kCollTmp.getDataValue("owe_int"));
			}
			//银承票面金额
			iColl =SqlClient.queryList4IColl("selectDrftAmtByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("drft_amt", kCollTmp.getDataValue("drft_amt")==null?"0":kCollTmp.getDataValue("drft_amt"));
			}
			//保函金额
			iColl =SqlClient.queryList4IColl("selectGuarantAmtByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("guarant_amt", kCollTmp.getDataValue("guarant_amt")==null?"0":kCollTmp.getDataValue("guarant_amt"));
			}
			//贴现金额
			iColl =SqlClient.queryList4IColl("selectRpayAmtByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("rpay_amt", kCollTmp.getDataValue("rpay_amt")==null?"0":kCollTmp.getDataValue("rpay_amt"));
			}
			//贸易融资余额
			iColl =SqlClient.queryList4IColl("selectTfLoanBalanceByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("tf_loan_bal", kCollTmp.getDataValue("tf_loan_bal")==null?"0":kCollTmp.getDataValue("tf_loan_bal"));
			}
			//垫款余额
			iColl =SqlClient.queryList4IColl("selectPadBalanceByCusId",cus_id,connection);
			if(iColl.size()>0){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
				acckColl.put("pad_bal", kCollTmp.getDataValue("pad_bal")==null?"0":kCollTmp.getDataValue("pad_bal"));
			}
		}catch (Exception e){
			logger.error("通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计）失败，错误描述："+e.getMessage());
			throw new EMPException("通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计）失败，错误描述："+e.getMessage());
		}
		logger.info("---------------通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计） 结束---------------");
		return acckColl;
	}
	/**
	 * 通过担保合同编号查询担保合同占用金额（最高额担保）
	 * @param guar_cont_no_value 担保合同编号
	 * @param connection 数据库连接
	 * @param context context
	 * @throws EMPException --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
	 */
	public BigDecimal getAmtForGuarCont(String guar_cont_no_value,Context context,Connection connection) throws EMPException {
		logger.info("---------------通过担保合同编号查询担保合同占用金额（最高额担保） 开始---------------");
		BigDecimal used_amt = new BigDecimal(0.00);
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			
			//1.未生成合同部分
			String conditionStrIqp = "where guar_cont_no='"+guar_cont_no_value+"' and corre_rel ='1' and is_add_guar='2' and cont_no is null";
			//2.生成合同部分
			String conditionStrCont = "where guar_cont_no='"+guar_cont_no_value+"' and corre_rel not in('2','3','4','5','6') and is_add_guar='2' and serno not in (select serno from Grt_Loan_R_Gur where corre_rel = '1' and cont_no is null and guar_cont_no = '"+guar_cont_no_value+"')";
			//3.计算未生成合同部分
			IndexedCollection iCollIqp = dao.queryList("GrtLoanRGur", conditionStrIqp, connection);
			for(int i=0;i<iCollIqp.size();i++){
				KeyedCollection kCollIqp = (KeyedCollection)iCollIqp.get(i);
				String serno = (String)kCollIqp.getDataValue("serno");
				String pk_value = (String)kCollIqp.getDataValue("pk_id");
				String res = iqpLoanAppComponent.caculateGuarAmtSp(serno, null,pk_value);
			    BigDecimal guar_amt = new BigDecimal(0);
			    if("2".equals(res)){//占用自己的担保金额
			    	guar_amt = BigDecimalUtil.replaceNull(kCollIqp.getDataValue("guar_amt"));
			    }else{
			    	guar_amt = new BigDecimal(0);
			    }
				//BigDecimal riskAmt = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getLmtAmtBySerno(serno));
				used_amt = used_amt.add(guar_amt);
			}
			//4.计算合同未出账部分
			IndexedCollection iCollCont =  dao.queryList("GrtLoanRGur", conditionStrCont, connection);
			for(int i=0;i<iCollCont.size();i++){
				KeyedCollection kCollCont = (KeyedCollection)iCollCont.get(i);
				String pk_value = (String)kCollCont.getDataValue("pk_id");
				String cont_no = TagUtil.replaceNull4String(kCollCont.getDataValue("cont_no"));
				//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
				String serno = "";
				String condtitionSelectIsChange = "where corre_rel in ('2','4','3') and is_add_guar='2' and guar_cont_no = '"+guar_cont_no_value+"'";
			    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
			    if(iCollSelectIsChange.size() > 0 && (cont_no == null || "".equals(cont_no))){
			    	KeyedCollection kCollSelectIsChange = (KeyedCollection)iCollSelectIsChange.get(0);
			    	serno = (String)kCollSelectIsChange.getDataValue("serno");
			    	IndexedCollection iCollRes = SqlClient.queryList4IColl("selectIsChangeBySernoStr", serno, connection);
			    	if(iCollRes.size()>0){
			    		KeyedCollection kCollRes = (KeyedCollection)iCollRes.get(0);
			    		cont_no = (String)kCollRes.getDataValue("cont_no");
			    	}
			    }
			    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
			    
				IndexedCollection iCollAcc = dao.queryList("AccView", "where cont_no='"+cont_no+"'", connection);
				if(iCollAcc.size()<=0){
					String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_value);
					BigDecimal guar_amt = new BigDecimal(0);
					if("2".equals(res)){
						guar_amt = BigDecimalUtil.replaceNull(kCollCont.getDataValue("guar_amt"));
						BigDecimal riskAmt = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getOneLmtAmtByContNo(cont_no));
						if(riskAmt.compareTo(guar_amt)>=0){
							used_amt = used_amt.add(guar_amt);
						}else{
							used_amt = used_amt.add(riskAmt);
						}
					}else{
						guar_amt = new BigDecimal(0);
						used_amt = used_amt.add(guar_amt);
					}
				}else{
					IndexedCollection iColl4loan = new IndexedCollection();
					iColl4loan = SqlClient.queryList4IColl("selectAccByContNo", cont_no, connection);
					if(iColl4loan!=null&&iColl4loan.size()>0){
						    String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_value);
						    if("2".equals(res)){
						    	//for(int j=0;j<iColl4loan.size();j++){
							    BigDecimal loan_bal_ck = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);
//								KeyedCollection kColl4loan = (KeyedCollection)iColl4loan.get(j);
//								BigDecimal loan_balance = new BigDecimal(kColl4loan.getDataValue("loan_balance").toString());
//								
//								KeyedCollection kColl4cont = dao.queryDetail("CtrLoanCont", cont_no, connection);
//								String cont_status = (String) kColl4cont.getDataValue("cont_status");
//								String sernoIqp = (String)kColl4cont.getDataValue("serno");
//								String prd_id = (String) kColl4cont.getDataValue("prd_id");
//								BigDecimal cont_amt = BigDecimalUtil.replaceNull(kColl4cont.getDataValue("cont_amt"));
//								
//								BigDecimal security_rate = new BigDecimal(kColl4cont.getDataValue("security_rate").toString());
//								BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl4cont.getDataValue("cont_balance"));
//								//获取实时汇率  start
//								String cur_type = (String) kColl4cont.getDataValue("cont_cur_type");
//								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//								IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//								KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
//								if("failed".equals(kCollRate.getDataValue("flag"))){
//									throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//								}
//								BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//								//获取实时汇率  end
//								
//								BigDecimal loan_bal_ck = (loan_balance.add(cont_balance.multiply(exchange_rate))).multiply(new BigDecimal(1.00).subtract(security_rate));
//								loan_bal_ck = loan_bal_ck.add(iqpLoanAppComponent.getAccPadBalByContNo(cont_no));
//								loan_bal_ck = iqpLoanAppComponent.caculateLimitSpac(sernoIqp, loan_bal_ck);
								
								if(loan_bal_ck.compareTo(new BigDecimal(0.00))>0){
									String flag = "start";
									String conditionStr4co = "";
									//if(iCollSelectIsChange.size() > 0){
										//新增，续作状态的
									//	conditionStr4co = "where serno='"+serno+"' and corre_rel in ('2','4') and is_add_guar='2' order by guar_lvl desc";
									//}else{
										//正常状态
										conditionStr4co = "where cont_no='"+cont_no+"' and corre_rel ='1' and is_add_guar='2' order by guar_lvl desc";
									//}
									/**查询关联业务*/
									IndexedCollection iColl4co =  dao.queryList("GrtLoanRGur", conditionStr4co, connection);
									if(iColl4co!=null&&iColl4co.size()>0){
										for(int k=0;k<iColl4co.size();k++){
											if("start".equals(flag)){
												KeyedCollection kColl4co = (KeyedCollection)iColl4co.get(k);
												BigDecimal guar_amt = new BigDecimal(kColl4co.getDataValue("guar_amt").toString());
												String guar_cont_no4tmp = kColl4co.getDataValue("guar_cont_no").toString();
												if(guar_cont_no4tmp!=null&&guar_cont_no_value.equals(guar_cont_no4tmp)){
													if(loan_bal_ck.compareTo(guar_amt)>=0){
														used_amt = used_amt.add(guar_amt);
														flag = "end";
													}else{
														used_amt = used_amt.add(loan_bal_ck);
														flag = "end";
													}
												}else{
													if(loan_bal_ck.compareTo(guar_amt)>=0){
														loan_bal_ck = loan_bal_ck.subtract(guar_amt);
													}else{
														used_amt = used_amt.add(new BigDecimal(0.00));
														flag = "end";
													}
												}
											}
										}
									}
								}else{
									used_amt = used_amt.add(new BigDecimal(0.00));
								}
						//	}
						    }else{
						    	used_amt = used_amt.add(new BigDecimal(0));
							}
					}else{
						   used_amt = used_amt.add(new BigDecimal(kCollCont.getDataValue("guar_amt").toString()));
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("通过担保合同编号查询担保合同占用金额（最高额担保）失败，错误描述："+e.getMessage());
			throw new EMPException("通过担保合同编号查询担保合同占用金额（最高额担保）失败，错误描述："+e.getMessage());
		}
		logger.info("---------------通过担保合同编号查询担保合同占用金额（最高额担保） 结束---------------");
		return used_amt;
	}
	
	/**
	 * 根据cont_no统计本笔业务的保证金金额
	 * @param cont_no 合同编号
	 * @param drft_amt 票面金额（单张）
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getBailAmt(String cont_no,BigDecimal drft_amt, Context context,Connection connection) throws EMPException {
		logger.info("---------------根据cont_no统计本笔业务的保证金金额  开始---------------");
		IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
		
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		BigDecimal amt = new BigDecimal(0.00);
		BigDecimal cont_amt = new BigDecimal(0.00);
		BigDecimal security_rate = new BigDecimal(0.00);
		try{
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
			String prd_id = (String)kColl.getDataValue("prd_id");
			cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
			String serno = (String)kColl.getDataValue("serno");
			String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");
			//String security_cur_type = (String)kColl.getDataValue("security_cur_type");
			security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));
			
			IndexedCollection iCollBail = dao.queryList("PubBailInfo", "where cont_no='"+cont_no+"'", connection);
			String security_cur_type = "";
			if(iCollBail.size()>0){
				KeyedCollection kCollBail = (KeyedCollection)iCollBail.get(0);
				security_cur_type = (String)kCollBail.getDataValue("cur_type");
			}
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//获取实时汇率
//			KeyedCollection kCollRate = this.getHLByCurrType(cont_cur_type, context, connection);
//			KeyedCollection kCollRateSecurity = this.getHLByCurrType(security_cur_type, context, connection);
//			if("failed".equals(kCollRate.getDataValue("flag"))){
//				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//			}
//			if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
//				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//			}
//			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			//循环贷款 贴现贷款
			if(prd_id.equals("100039")||prd_id.equals("100050")){
				cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate)).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			}else if( prd_id.equals("300021")||prd_id.equals("300020")){
				cont_amt = drft_amt;
				amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate)).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			}else if(prd_id.equals("200024")){//银承贷款
				cont_amt = drft_amt;
				String condtitionStr = "where cont_no='"+cont_no+"'";
				IndexedCollection iCollAcc = dao.queryList("AccView", condtitionStr, connection);
				if(iCollAcc.size()>0){
					BigDecimal loan_balance_all = cmisComponent.getAccpBalanceByContNo(cont_no);
					loan_balance_all = loan_balance_all.add(loan_balance_all);
					amt = amt.add(loan_balance_all.multiply(security_rate).multiply(exchange_rate)).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
				}else{
					amt = amt.add(cont_amt.multiply(security_rate).multiply(exchange_rate)).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
				}
			}else{//其他类型业务,生成贷款台账,授信占用=合同余额+台账余额
				BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_balance"));
				BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
				if("500027".equals(prd_id) || "500028".equals(prd_id) || "500026".equals(prd_id) || "500021".equals(prd_id) || "500020".equals(prd_id) ||
						"500032".equals(prd_id) || "500029".equals(prd_id) || "500031".equals(prd_id) || "500022".equals(prd_id) || "500025".equals(prd_id) || 
						"500024".equals(prd_id) || "500023".equals(prd_id) || "400020".equals(prd_id) || "700020".equals(prd_id) || "700021".equals(prd_id)){
					//判断是否为信用证业务
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					//合同余额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = cont_balance.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					securityAmt = cmisComponent.carryCurrency(securityAmt);
					cont_balance = securityAmt;
					
					//贷款余额
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no,context,connection);
					//贷款余额*保证金比例*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					loan_balance_all = cmisComponent.carryCurrency(loan_balance_all);
					cont_balance = cont_balance.add(loan_balance_all);
					amt = amt.add(cont_balance);
				}else{
					cont_balance = cont_balance.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					//贷款余额
					BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no,context,connection);
					//贷款余额*保证金比例*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					loan_balance_all = loan_balance_all.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					cont_balance = cont_balance.add(loan_balance_all);
					amt = amt.add(cont_balance);
				}
				amt = amt.divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_EVEN);
			}   
		}catch (Exception e){
			e.printStackTrace();
			logger.error("根据cont_no统计本笔业务的保证金金额失败，错误描述："+e.getMessage());
			throw new EMPException("根据cont_no统计本笔业务的保证金金额失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据cont_no统计本笔业务的保证金金额  结束---------------");
		return amt;
	}
}

