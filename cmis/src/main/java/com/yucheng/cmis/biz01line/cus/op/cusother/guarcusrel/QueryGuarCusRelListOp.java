package com.yucheng.cmis.biz01line.cus.op.cusother.guarcusrel;

import java.math.BigDecimal;
import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2014-12-25
*@description 需求编号：【XD141029074】增加授信企业关联担保提醒事项（关联担保客户）
*@version v1.0
*
 */
public class QueryGuarCusRelListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guar_cont_no ="";
		String guar_cont_state ="";//担保合同状态
		String cur_type ="";//币种
		String borrower_id ="";
		String guar_cus_id="";
		String borrowerName="";
		String borrowerIdList="";
		String guarCusName="";
		String guarCusIdList="";
		String condiStr ="";//模糊查询条件
		BigDecimal used_amt = new BigDecimal(0);//占用金额
		int size = 10;
		//模糊查询条件
		try{
			KeyedCollection queryData = (KeyedCollection)context.getDataElement("GuarCusRel");
			borrower_id = (String)queryData.getDataValue("borrower_id");
			guar_cus_id = (String)queryData.getDataValue("guar_cus_id");
			guar_cont_state = (String)queryData.getDataValue("guar_cont_state");
			borrowerName = (String) queryData.getDataValue("borrower_name");
			guarCusName = (String) queryData.getDataValue("guar_cus_name");
			if(borrower_id!=null && !"".equals(borrower_id)){
				condiStr +=" and t.cus_id like'%"+borrower_id+"%'";	
			}
			if(guar_cus_id!=null && !"".equals(guar_cus_id)){
				condiStr +=" and g2.cus_id like'%"+guar_cus_id+"%'";	
			}
			if(guar_cont_state!=null && !"".equals(guar_cont_state)){
				condiStr +=" and t.guar_cont_state ='"+guar_cont_state+"'";	
			}
			//借款人名称模糊查询
			if(borrowerName!=null && !"".equals(borrowerName)){
				String conSelect ="select c.cus_id from cus_base c where c.cus_name like '%"+borrowerName+"%'";
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
				IndexedCollection iCollSelect = new IndexedCollection();
				iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
				if(iCollSelect !=null && iCollSelect.size() > 0){
					for(int i=0;i<iCollSelect.size();i++){
						KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
						borrowerIdList += kColl.getDataValue("cus_id")+",";
					}
					condiStr += "and instr('"+ borrowerIdList+"', t.cus_id)>0";
				}
			}
			//保证人模糊查询
			if(guarCusName!=null && !"".equals(guarCusName)){
				String conSelect ="select c.cus_id from cus_base c where c.cus_name like '%"+guarCusName+"%'";
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
				IndexedCollection iCollSelect = new IndexedCollection();
				iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
				if(iCollSelect !=null && iCollSelect.size() > 0){
					for(int i=0;i<iCollSelect.size();i++){
						KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
						guarCusIdList += kColl.getDataValue("cus_id")+",";
					}
					condiStr += "and instr('"+ guarCusIdList+"', g2.cus_id)>0";
				}
			}
		}catch (Exception e) {}
		//列表查询SQL
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = new IndexedCollection();
			IndexedCollection iColl4GuarCusRel = new IndexedCollection();		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			//IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			  //.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conSelect = " select  t.guar_cont_no,                          "
							+"       t.cus_id,                                            "
							+"       t.guar_cont_state,                                   "
							+"       nvl(t.cur_type,'CNY') as cur_type,                   "
							+"       g2.cus_id as guar_cus_id                             "
							+"  from grt_guar_cont t, grt_guaranty_re g1, grt_guarantee g2"
							+" where t.guar_cont_no = g1.guar_cont_no                     "
							+"   and g1.guaranty_id = g2.guar_id                          "
							+"   and (exists (select 1                                    "
							+"                  from lmt_agr_details p1                   "
							+"                 where p1.cus_id = g2.cus_id                "
							+"                   and p1.lmt_status <> '30')               "
							+"				or                                            "
							+"       (exists (select 1                                    "
							+"                  from acc_view p2 where                    "
							+"                 not exists (select 1 from ctr_loan_cont p3 "
							+"					where p2.cont_no = p3.cont_no             "
							+"						and p3.security_rate <> '1')          "
							+"                    and p2.status in ('1', '6'))))          "
							+"   and t.guar_cont_state in ('00', '01')                    "
							+"   and t.cus_id is not null								  "
							+condiStr+" order by t.guar_cont_no desc                      ";
			iColl = TableModelUtil.buildPageData(pageInfo, dataSource, conSelect);
			if(iColl !=null && iColl.size() > 0){
				for(int i=0; i<iColl.size(); i++){
					KeyedCollection temp = (KeyedCollection) iColl.get(i);
					guar_cont_no = (String) temp.getDataValue("guar_cont_no");//该授信企业为他人担保的担保合同编号
					guar_cont_state = (String) temp.getDataValue("guar_cont_state");
					borrower_id = (String) temp.getDataValue("cus_id");
					guar_cus_id = (String) temp.getDataValue("guar_cus_id");
					cur_type = (String) temp.getDataValue("cur_type");
					KeyedCollection kColl = dao.queryDetail("GrtGuarCont", guar_cont_no, connection);
					try{
						/**modified by lisj 2015-1-9 修复前台数据显示不准确，影响查数功能 ，于2015-1-15上线 begin**/
						used_amt = BigDecimalUtil.replaceNull("0");
						used_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("guar_amt").toString());//直接取担保合同的保证金额
						/**modified by lisj 2015-1-9 修复前台数据显示不准确，影响查数功能，于2015-1-15上线 end**/
					}catch(Exception e){}					
				/**	if(kColl.containsKey("guar_cont_type")&&kColl.getDataValue("guar_cont_type")!=null
							&&"00".equals(kColl.getDataValue("guar_cont_type").toString())){
				//一般担保  1正常，5 已解除
				//查询关联表中此担保合同已已经引入的金额
				IndexedCollection FLRGIColl =  dao.queryList("GrtLoanRGur", "where guar_cont_no='"
								+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')", connection);
					for(int j=0;j<FLRGIColl.size();j++){
						KeyedCollection kCollTemp = (KeyedCollection)FLRGIColl.get(j);
						String is_per_gur = (String)kCollTemp.getDataValue("is_per_gur");
						if(is_per_gur != null && !"".equals(is_per_gur)){
							String pk_id = (String)kCollTemp.getDataValue("pk_id");
							String cont_no = (String)kCollTemp.getDataValue("cont_no");
							if(cont_no != null && !"".equals(cont_no)){
								String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
								if("2".equals(res)){
									used_amt = used_amt.add(new BigDecimal(kCollTemp.getDataValue("guar_amt").toString()));
								}else{
									used_amt = used_amt.add(new BigDecimal(0));
								}
							}else{
								String sernoSelect = (String)kCollTemp.getDataValue("serno");
								String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
								if("2".equals(res)){
									used_amt = used_amt.add(new BigDecimal(kCollTemp.getDataValue("guar_amt").toString()));
								}else{
									used_amt = used_amt.add(new BigDecimal(0));
								}
							}
						}
					}
				 }else{
					 //最高额担保
					 CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					 IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					 used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, connection));
				 } **/
					if(used_amt!=null && guar_cont_state !=null && 
						(guar_cont_state.equals("01") || (guar_cont_state.equals("00") && 
								(used_amt.compareTo(new BigDecimal(0.00)) >0)))){
						CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
						CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
						CusBase cb4Borrower = csi.getCusBaseByCusId(borrower_id,context,connection);
						CusBase cb4GuarCus = csi.getCusBaseByCusId(guar_cus_id,context,connection);
						String borrower_name = TagUtil.replaceNull4String(cb4Borrower.getCusName());//借款人名称
						String guar_cus_name = TagUtil.replaceNull4String(cb4GuarCus.getCusName());//担保人名称
						KeyedCollection kColl4GuarCusRel = new KeyedCollection();	
						kColl4GuarCusRel.put("guar_cont_no", guar_cont_no);
						kColl4GuarCusRel.put("borrower_id", borrower_id);
						kColl4GuarCusRel.put("borrower_name", borrower_name);
						kColl4GuarCusRel.put("cur_type", cur_type);
						kColl4GuarCusRel.put("guar_cus_id", guar_cus_id);
						kColl4GuarCusRel.put("guar_cus_name", guar_cus_name);
						kColl4GuarCusRel.put("guar_cont_state", guar_cont_state);
						kColl4GuarCusRel.put("used_amt", used_amt);
						iColl4GuarCusRel.add(kColl4GuarCusRel);
					}
				}
			}
			iColl4GuarCusRel.setName("GuarCusRelList");
			this.putDataElement2Context(iColl4GuarCusRel, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
