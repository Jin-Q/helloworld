package com.yucheng.cmis.biz01line.lmt.op;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class ViewLmtAgrInfoOp extends CMISOperation {
	private final String modelIdDetail= "LmtIntbankDetail";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String ret = "";
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			String value_res = (String)context.getDataValue("agr_no");
			if(null==value_res && "".equals(value_res)){
				throw new EMPException("查询授信信息错误，授信协议/授信台账编号不能为空！");
			}
			String agr_type ="";
			Object obj = SqlClient.queryFirst("viewLmtAgrInfoOp", value_res, null, connection);
			if(null != obj){
				agr_type =  (String)obj;
			}
			
			String menuId = "";
			KeyedCollection kcoll = new KeyedCollection();
			if("COM".equalsIgnoreCase(agr_type) || "INDIV".equalsIgnoreCase(agr_type) || "JOINT".equalsIgnoreCase(agr_type)){
				kcoll = dao.queryDetail("LmtAgrDetails", value_res, connection);
				context.addDataField("limit_code", kcoll.getDataValue("limit_code"));
				/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置,1+N担保模式 begin**/
				String belg_line = "BL100";  //默认公司
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cusBase = service.getCusBaseByCusId(kcoll.getDataValue("cus_id").toString(),context,connection);
				if(null!=cusBase && null!=cusBase.getCusId()){
					belg_line = cusBase.getBelgLine();
				}
				context.addDataField("belg_line", belg_line);
				/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置,1+N担保模式 end**/
				ret = "comAgrInfo";
				menuId = "crd_ledger";
			}else if("COOP".equalsIgnoreCase(agr_type)){   //合作方
				kcoll = dao.queryDetail("LmtAgrJointCoop", value_res, connection);
				ret = "coopAgrInfo";
				menuId = "lmt_agr_coop";
			}else if("BIZARE".equalsIgnoreCase(agr_type)){   //圈商
				kcoll = dao.queryDetail("LmtAgrBizArea", value_res, connection);
				ret = "bizAreaAgrInfo";
				menuId = "argBizArea";
				
				//修改页面获取不到biz_area_type问题，start 
				String biz_area_type = (String)kcoll.getDataValue("biz_area_type");
				if(context.containsKey("biz_area_type")){
					context.setDataValue("biz_area_type", biz_area_type);
				}else{
					context.addDataField("biz_area_type", biz_area_type);
				}
				//修改页面获取不到biz_area_type问题，end
				
				//总户数直接查名单表     
				//客户状态为1：有效        2：无效
				String condSql = " WHERE AGR_NO = '" + value_res + "' AND CUS_STATUS='1'";
				IndexedCollection icTotCus = dao.queryList("LmtNameList", condSql, connection);
				//总户数
				kcoll.addDataField("totl_cus", icTotCus.size());
				if("0".equals(biz_area_type)){//一般
					KeyedCollection kCollComn = (KeyedCollection)dao.queryDetail("LmtAgrBizAreaComn", value_res, connection);
					this.putDataElement2Context(kCollComn, context);
				}else if("1".equals(biz_area_type)){//核心企业供应商类
					KeyedCollection kCollCore = (KeyedCollection)dao.queryDetail("LmtAgrBizAreaCore", value_res, connection);
					this.putDataElement2Context(kCollCore, context);
				}else if("2".equals(biz_area_type)){//超市类
					IndexedCollection iCollSupmk = dao.queryList("LmtAgrBizAreaSupmk", " where agr_no = '" + value_res + "'", connection);
					iCollSupmk.setName("LmtAgrBizAreaSupmkList");
					this.putDataElement2Context(iCollSupmk, context);
				}
			}else if("INDUS".equalsIgnoreCase(agr_type)){   //行业
				kcoll = dao.queryDetail("LmtIndusAgr", value_res, connection);
				ret = "indusAgrInfo";
				menuId = "indus_crd_agr";
			}else if("SAME".equalsIgnoreCase(agr_type)){   //同业
				IndexedCollection iCollSame = dao.queryList("LmtIntbankAcc"," where agr_no ='"+value_res+"'", connection);
				if(iCollSame.size()>0){
					kcoll = (KeyedCollection)iCollSame.get(0);
					String cus_id=(String)kcoll.getDataValue("cus_id");
					String conditionStr ="where cus_id='"+cus_id+"'"+"order by cus_id desc";
		            IndexedCollection iColl = dao.queryList(modelIdDetail,conditionStr,connection);
					iColl.setName(iColl.getName()+"List");
					String[] args=new String[] { "cus_id" };
				    String[] modelIds=new String[]{"CusSameOrg"};
				    String[] modelForeign=new String[]{"cus_id"};
				    String[] fieldName=new String[]{"same_org_cnname"};
					SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
					this.putDataElement2Context(iColl, context);
				}
				ret = "LmtIntbankAcc";
				menuId = "LmtIntbankAcc";
			}else{
				throw new EMPException("查询授信信息失败，根据授信协议/授信台账编号找不到对应的授信记录！");
			}
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			
			//授信协议没有登记机构、登记人、责任人、管理机构等
			if(!("COM".equalsIgnoreCase(agr_type) || "INDIV".equalsIgnoreCase(agr_type) || "JOINT".equalsIgnoreCase(agr_type) || "SAME".equalsIgnoreCase(agr_type))){
				SInfoUtils.addUSerName(kcoll, new String[] { "input_id","manager_id" });
				SInfoUtils.addSOrgName(kcoll, new String[] { "input_br_id","manager_br_id" });
				
				KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo((String)kcoll.getDataValue("agr_no"), "03", connection, context);
				String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
				//合作方
				if("COOP".equalsIgnoreCase(agr_type)){
					double bal_amt = Double.parseDouble((String)kcoll.getDataValue("lmt_totl_amt")) - Double.parseDouble(lmt_amt);
					kcoll.addDataField("lmt_bal_amt", bal_amt);
				}
				
				//行业
				if("INDUS".equalsIgnoreCase(agr_type)){
					double bal_amt = Double.parseDouble((String)kcoll.getDataValue("indus_amt")) - Double.parseDouble(lmt_amt);
					kcoll.addDataField("bal_amt", bal_amt);
					
					SInfoUtils.getPrdPopName(kcoll, "suit_prd", connection);  //翻译产品
				}
				
				//圈商
				if("BIZARE".equalsIgnoreCase(agr_type)){
					double bal_amt = Double.parseDouble((String)kcoll.getDataValue("lmt_totl_amt")) - Double.parseDouble(lmt_amt);
					kcoll.addDataField("already_used", bal_amt);
				}
				
				//翻译共享范围的机构信息
				if(null != kcoll.getDataValue("belg_org")){
					SystemTransUtils.containCommaORG2CN("belg_org",kcoll,context);
				}
			}else if("SAME".equalsIgnoreCase(agr_type)){
				//查询授信占用额度
				KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo((String)kcoll.getDataValue("agr_no"), "02", connection, context);
				BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollTemp.getDataValue("lmt_amt"));
				BigDecimal bal_amt = (BigDecimalUtil.replaceNull(kcoll.getDataValue("lmt_amt")).subtract(lmt_amt)).subtract(BigDecimalUtil.replaceNull(kcoll.getDataValue("froze_amt"))); 
				kcoll.addDataField("bal_amt", bal_amt);
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				KeyedCollection kColl_cus =service.getCusSameOrgKcoll(kcoll.getDataValue("cus_id").toString(), context, connection);
				String same_org_cnname = (String)kColl_cus.getDataValue("same_org_cnname");
				kcoll.addDataField("same_org_cnname", same_org_cnname);
				SInfoUtils.addUSerName(kcoll, new String[] { "input_id","manager_id" });
				SInfoUtils.addSOrgName(kcoll, new String[] { "input_br_id","manager_br_id" });
			}else{
				//查询授信占用额度
				KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo((String)kcoll.getDataValue("limit_code"), "01", connection, context);
				String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
				double bal_amt = Double.parseDouble((String)kcoll.getDataValue("crd_amt")) - Double.parseDouble(lmt_amt);
				kcoll.addDataField("bal_amt", bal_amt);
				
				SInfoUtils.getPrdPopName(kcoll, "prd_id", connection);  //翻译产品
				
				//翻译绿色信贷
				Map<String,String> map = new HashMap<String,String>();
				map.put("green_indus","STD_ZB_GREEN_INDUS");
				CMISTreeDicService service_tree = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				SInfoUtils.addPopName(kcoll, map, service_tree);
				
				if("05".equals(kcoll.getDataValue("sub_type"))){  //供应链授信时翻译核心企业客户码
					String[] args=new String[] {"core_corp_cus_id" };
					String[] modelIds=new String[]{"CusBase"};
					String[] modelForeign=new String[]{"cus_id"};
					String[] fieldName=new String[]{"cus_name"};
					//详细信息翻译时调用			
					SystemTransUtils.dealName(kcoll, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				}
				/**翻译额度名称**/
				String[] args=new String[] { "limit_name" };
				String[] modelIds=new String[]{"PrdBasicinfo"};
				String[] modelForeign=new String[]{"prdid"};
				String[] fieldName=new String[]{"prdname"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(kcoll, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			}
			//行业授信跟圈商授信没有客户码
			if(!"INDUS".equalsIgnoreCase(agr_type)){
				String[] args=new String[] { "cus_id" };
				String[] modelIds=new String[]{"CusBase"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"cus_name"};
				//详细信息翻译时调用
				SystemTransUtils.dealName(kcoll, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			}
			
			context.setDataValue("agr_no", kcoll.getDataValue("agr_no"));
			if(context.containsKey("type")){
				context.put("menuIdTab", menuId);
			}else{
				context.addDataField("menuIdTab", menuId);
			}
			
			if(context.containsKey("type")){
				context.setDataValue("type", "surp");  //控制页面显示按钮
			}else{
				context.addDataField("type", "surp");  //控制页面显示按钮
			}
			this.putDataElement2Context(kcoll, context);
			HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			String requestUrl = request.getServletPath();
			if(context.containsKey("menuIdTab")){
				request.setAttribute("menuIdTab", context.getDataValue("menuIdTab"));
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return ret;
	}

}
