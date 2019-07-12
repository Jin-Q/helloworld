package com.yucheng.cmis.biz01line.mort.component;


import java.util.HashMap;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class MortFlowComponent extends CMISComponent {
	
	/**
	 * 出入库申请流程审批通过
	 * @param serno 出入库申请流水号
	 * @throws ComponentException
	 */
	private static final String MORTSTOREXWAINFOMODEL = "MortStorExwaInfo";//出入库申请主表
	private static final String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
	private static final String MORTSTOREXWADETAILMODEL = "MortStorExwaDetail";//出入库申请明细表
	private static final String MORTGUARANTYBASEINFOMODEL = "MortGuarantyBaseInfo";//押品信息主表
	private static final String MORTGUARANTYCERTIINFOMODEL = "MortGuarantyCertiInfo";//权证信息主表
	public void doWfAgreeForMort(String serno)throws ComponentException {
		
		try {
			/**
			 * 权证出入库申请审批通过执行操作：
			 * 1.生成授权信息,注：授权表中交易码由：【交易码+交易场景】组成
			 */
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			/** 1.通过业务流水号查询出生成质押出入库授权所需要的字段*/
			KeyedCollection mortstorexwainfokColl = dao.queryDetail(MORTSTOREXWAINFOMODEL, serno, this.getConnection());
			
			String stor_exwa_mode = (String)mortstorexwainfokColl.getDataValue("stor_exwa_mode");//出入库方式
			String exwa_type = (String)mortstorexwainfokColl.getDataValue("exwa_type");//出入库类型
			//为了方便核算，此处转换出入库方式为I: 入库 O: 出库
			if("04".equals(stor_exwa_mode)){//入库
				stor_exwa_mode = "01";
			}else if("05".equals(stor_exwa_mode)){
				stor_exwa_mode = "04";
			}else if(!"02".equals(stor_exwa_mode)){
				stor_exwa_mode = "02";
			}else{
				stor_exwa_mode = "03";//临时借出
			}
			//if(!"03".equals(stor_exwa_mode)){
				authorizeKColl.put("serno","");  //业务编号
				authorizeKColl.put("tran_serno","");  //交易流水号
				authorizeKColl.put("authorize_no","");  //授权编号
				authorizeKColl.put("tran_id","");  //交易码
				authorizeKColl.put("fldvalue01","");  //授权编号
				authorizeKColl.put("fldvalue02","");  //抵押质押品编号
				authorizeKColl.put("fldvalue03","");  //权证类型
				authorizeKColl.put("fldvalue04","");  //权证编号
				authorizeKColl.put("fldvalue05","IN_OUT_TYPE@"+stor_exwa_mode);  //出入库方式
				//authorizeKColl.put("fldvalue06","COMMISSION@0");  //手续费
				authorizeKColl.put("fldvalue07","LOT_NUM@1");  //份数 
		    	KeyedCollection mortstorexwadetailkColl= new KeyedCollection(MORTSTOREXWADETAILMODEL);
				/** 2.通过业务流水号查询出生成质押出入库从表信息，需循环插入授权表。*/
				IndexedCollection mortstorexwadetailiColl = dao.queryList(MORTSTOREXWADETAILMODEL,"where serno ='"+serno+"'", this.getConnection());
				
				if(null==mortstorexwadetailiColl||mortstorexwadetailiColl.size()==0){
					throw new EMPException("根据业务流水号获取出入库权证(出入池)条数为零！");
				}else{
					//押品编号
					String guaranty_no="";
					//权证编号
					String warrant_no="";
					//权证类型
					String warrant_type="";
					//交易流水号
					String tranSerno = "";
					//交易码
					String tran_id = TradeConstance.SERVICE_CODE_ZYGL + TradeConstance.SERVICE_SCENE_ZYCRKSQ;
					//授权编号
					String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
					//客户码
					String cus_id="";
					//核心客户号
					String hx_cus_id = "";
					//核心流水号
					String hx_serno = "";
					//客户名称
					String cus_name="";
					authorizeKColl.put("serno",serno);
					authorizeKColl.put("authorize_no",authSerno);//授权编号
					authorizeKColl.put("tran_id",tran_id);//交易码
					authorizeKColl.put("fldvalue01","GEN_GL_NO@"+authSerno);//授权编号
					//调用客户模块接口
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					String guaranty_type = "";
					String keep_org = "";
					for(int i=0;i<mortstorexwadetailiColl.size();i++){
						mortstorexwadetailkColl = (KeyedCollection) mortstorexwadetailiColl.get(i);
						tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
						guaranty_no = (String) mortstorexwadetailkColl.getDataValue("guaranty_no");//押品编号
						warrant_no = (String) mortstorexwadetailkColl.getDataValue("warrant_no");//权证编号
						warrant_type = (String) mortstorexwadetailkColl.getDataValue("warrant_type");//权证类型
						if(exwa_type!=null&&"03".equals(exwa_type)){
							//票据池主表
							KeyedCollection iqpDrfpoManakColl = dao.queryDetail("IqpDrfpoMana",guaranty_no, this.getConnection());
							//保管机构
							keep_org = (String)iqpDrfpoManakColl.getDataValue("manager_br_id");
							//客户码
							cus_id = (String) iqpDrfpoManakColl.getDataValue("cus_id");
							guaranty_type = "DZY002000";
						}else if(exwa_type!=null&&("01".equals(exwa_type)||"02".equals(exwa_type))){
							//票据池主表
							KeyedCollection iqpDrfpoManakColl = dao.queryDetail("IqpActrecpoMana",guaranty_no, this.getConnection());
							//保管机构
							keep_org = (String)iqpDrfpoManakColl.getDataValue("manager_br_id");
							//客户码
							cus_id = (String) iqpDrfpoManakColl.getDataValue("cus_id");
							guaranty_type = "DZY002000";
						}else{
							//押品信息主表
							KeyedCollection mortGuarantyBaseInfokColl = dao.queryDetail(MORTGUARANTYBASEINFOMODEL,guaranty_no, this.getConnection());
							//查询权证信息表
							HashMap map = new HashMap();
							map.put("warrant_type", warrant_type);
							map.put("warrant_no", warrant_no);
							KeyedCollection mortGuarantyCertiInfokColl = dao.queryDetail(MORTGUARANTYCERTIINFOMODEL, map, this.getConnection());
							//核算押品类别（DZY001000：抵押 DZY002000：质押）
							guaranty_type = (String) mortGuarantyBaseInfokColl.getDataValue("guaranty_cls");
							if(guaranty_type.equals("1")){
								guaranty_type = "DZY001000";
							}else{
								guaranty_type = "DZY002000";
							}
							//保管机构
							keep_org = (String)mortGuarantyCertiInfokColl.getDataValue("keep_org_no");
							hx_serno = (String) mortGuarantyCertiInfokColl.getDataValue("hx_serno");
							//客户码
							cus_id = (String) mortGuarantyBaseInfokColl.getDataValue("cus_id");
							KeyedCollection cusBaseKcoll = dao.queryDetail("CusBase", cus_id, this.getConnection());
							hx_cus_id = (String) cusBaseKcoll.getDataValue("hx_cus_id");
						}
						//查询押品价值信息表
						IndexedCollection mortGuarantyEvalValueIcoll = dao.queryList("MortGuarantyEvalValue","where guaranty_no ='"+guaranty_no+"'", this.getConnection());
						String amt = "";
						if(null==mortGuarantyEvalValueIcoll||mortGuarantyEvalValueIcoll.size()==0){
							throw new EMPException("根据押品编号获取出入库押品价值信息为空！");
						}else{
							KeyedCollection mortGuarantyEvalValueKcoll = (KeyedCollection) mortGuarantyEvalValueIcoll.get(0);
							amt = (String) mortGuarantyEvalValueKcoll.getDataValue("wrr_amt");
						}
						
						CusBase cus = service.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
						//客户名称
						cus_name = cus.getCusName();
						authorizeKColl.put("tran_serno",tranSerno);  //交易流水号
						authorizeKColl.put("fldvalue02","PLEDGE_NO@"+guaranty_no);//押品编号----核心流水号
						authorizeKColl.put("fldvalue03","TICKET_TYPE@"+warrant_type);//权证类型
						authorizeKColl.put("fldvalue04","TICKET_NO@"+warrant_no);//权证编号
						authorizeKColl.put("fldvalue08","CLIENT_NO@"+hx_cus_id);  //客户码
						authorizeKColl.put("fldvalue09","CLIENT_NAME@"+cus_name);  //客户名称
						authorizeKColl.put("fldvalue10","BRANCH_ID@"+keep_org);  //保管机构
						authorizeKColl.put("fldvalue11","BANK_ID@");  //银行号
						authorizeKColl.put("fldvalue12","PREDGE_AMT@"+amt);  //抵押品价值
						authorizeKColl.put("fldvalue13","PLEDGE_TYPE@"+guaranty_type);  //抵押品类型
						authorizeKColl.put("fldvalue14","CCY@"+"CNY");  //币种
						authorizeKColl.put("fldvalue15","HX_SERNO@"+hx_serno);  //核心流水号
						/** 抽取发往核算系统的授权信息--end-- */
						dao.insert(authorizeKColl, this.getConnection());
					}
				}
			//}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！"+e.getMessage());
		}
	
	}
}
