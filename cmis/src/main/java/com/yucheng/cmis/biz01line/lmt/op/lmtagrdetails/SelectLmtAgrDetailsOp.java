package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class SelectLmtAgrDetailsOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String res_value = "signPage";
		try{
			connection = this.getConnection(context);

			String condition = "";
			String conditionStr = "";
			if(context.containsKey("agr_no") && null!=context.getDataValue("agr_no") && !"".equals(context.getDataValue("agr_no"))){
				condition = " AND AGR_NO='"+context.getDataValue("agr_no")+"' ";
			}
			//额度编码（台账编码），适用于单一法人授信
			if(context.containsKey("limit_code")  && null!=context.getDataValue("limit_code") && !"".equals(context.getDataValue("limit_code"))){
				conditionStr = " AND LIMIT_CODE='"+context.getDataValue("limit_code")+"' ";
			}
			//同业单户授信协议号
			if(context.containsKey("sign_agr_no")  && null!=context.getDataValue("sign_agr_no") && !"".equals(context.getDataValue("sign_agr_no"))){
				conditionStr = " AND AGR_NO='"+context.getDataValue("sign_agr_no")+"' ";
			}
			//合作方客户码、商贴额度客户码
			if(context.containsKey("search_cus_id") && null!=context.getDataValue("search_cus_id") && !"".equals(context.getDataValue("search_cus_id"))){
				condition = " AND CUS_ID='"+context.getDataValue("search_cus_id")+"' ";
			}
			//分项类别，查询条件共用sub_type，后期代码根据具体的情况替换
			if(context.containsKey("sub_type") && null!=context.getDataValue("sub_type") && !"".equals(context.getDataValue("sub_type"))){
				condition = " AND SUB_TYPE='"+context.getDataValue("sub_type")+"' ";
			}
			//圈商名称，适用圈商
			if(context.containsKey("biz_area_name") && null!=context.getDataValue("biz_area_name") && !"".equals(context.getDataValue("biz_area_name"))){
				condition = " AND BIZ_AREA_NAME LIKE '%"+context.getDataValue("biz_area_name")+"%' ";
			}
			//同业客户号
			if(context.containsKey("same_cus_id")  && null!=context.getDataValue("same_cus_id") && !"".equals(context.getDataValue("same_cus_id"))){
				conditionStr = " AND CUS_ID in (select cus_id from cus_same_org where same_org_no = '"+context.getDataValue("same_cus_id")+"')";
			}
			//同业客户名称
			if(context.containsKey("same_cus_id_displayname")  && null!=context.getDataValue("same_cus_id_displayname") && !"".equals(context.getDataValue("same_cus_id_displayname"))){
				conditionStr = " AND CUS_ID in (select cus_id from cus_same_org where same_org_cnname like '%"+context.getDataValue("same_cus_id_displayname")+"%')";
			}
			
			int size = 10;
			
			//获取并校验客户码是否传入
			String cus_id = "";
			if(context.containsKey("cus_id") && null!=context.getDataValue("cus_id") && !"".equals(context.getDataValue("cus_id"))){
				cus_id = context.getDataValue("cus_id").toString();
			}else{
				throw new EMPException("查询额度的客户码[cus_id]不能为空。");
			}
			//获取并校验额度类型是否传入
			String lmt_type = "";
			if(context.containsKey("lmt_type") && null!=context.getDataValue("lmt_type") && !"".equals(context.getDataValue("lmt_type"))){
				lmt_type = context.getDataValue("lmt_type").toString();
			}else{
				throw new EMPException("查询额度的授信类型[lmt_type]（01-单一法人   02-同业客户   03-合作方）不能为空。");
			}
			
			//增加同业客户处理，同业客户要取到总行行号  start 2014-05-09
			TableModelDAO dao = this.getTableModelDAO(context);
			if(lmt_type.equals("02")){
				String condistr = " where same_org_no = '"+cus_id+"'";
				KeyedCollection orgkc = dao.queryFirst("CusSameOrg", null, condistr, connection);
				String head_org_no = (String) orgkc.getDataValue("head_org_no");
				if(head_org_no!=null&&!head_org_no.equals("")){
					cus_id = head_org_no;
				}
			}
			//增加同业客户处理，同业客户要取到总行行号  end
			
			String guar_type = "";
			
			//获取并校验产品编号是否传入
			if(!context.containsKey("prd_id") || null==context.getDataValue("prd_id") || "".equals(context.getDataValue("prd_id"))){
				throw new EMPException("查询额度的产品编号[prd_id]不能为空。");
			}else{
				//获取并校验担保方式是否传入
				if(!"02".equals(context.getDataValue("lmt_type")) && !"04".equals(context.getDataValue("lmt_type")) && !"300021".equals(context.getDataValue("prd_id"))){   //非同业额度时校验担保方式
					if(context.containsKey("guar_type") && null!=context.getDataValue("guar_type") && !"".equals(context.getDataValue("guar_type"))){
						guar_type = context.getDataValue("guar_type").toString();
					}else{
						throw new EMPException("查询额度的担保方式[guar_type]不能为空。");
					}
				}
			}
			String prd_id = (String)context.getDataValue("prd_id");//产品编号
			
			String rpddscnt_type = "";//转贴现方式
			if(context.containsKey("rpddscnt_type")){
				rpddscnt_type = (String)context.getDataValue("rpddscnt_type");
			}
			
			if(!context.containsKey("outstnd_amt")  || null==context.getDataValue("outstnd_amt") || "".equals(context.getDataValue("outstnd_amt"))){
				throw new EMPException("本次占用金额[outstnd_amt]不能为空。");
			}
			
			String limit_type = "";
			if("01".equals(lmt_type)){
				if((!context.containsKey("limit_type") || "".equals(context.containsKey("limit_type")))){
					throw new EMPException("单一法人授信查询额度类型[limit_type]不能为空。");
				}else if(!("01".equals(context.getDataValue("limit_type")) || "02".equals(context.getDataValue("limit_type")))){
					throw new EMPException("单一法人授信查询额度类型[limit_type]传入值["+context.getDataValue("limit_type")+"]错误，正确传值：01-循环额度 02-一次性额度。");
				}else{
					limit_type = (String)context.getDataValue("limit_type");
					
					//为单一法人授信选择时，判断集团融资模式，如果为整体授信，分配额度则查询集团总额度
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					String finance_type= service.getGrpFinanceType(cus_id, context, connection);  //查询客户所属集团融资模式
					
					if("01".equals(finance_type)){   //集团融资模式为：整体授信、分配额度，需查询集团整体授信额度
						ArrayList<String> show_list = new ArrayList<String>();
						show_list.add("grp_agr_no");  //集团授信编号
						show_list.add("grp_no");  //集团编号
						show_list.add("crd_totl_amt");   //集团授信总额
						KeyedCollection grp_kcoll = this.getTableModelDAO(context).queryFirst("LmtAgrGrp", show_list, "WHERE GRP_AGR_NO =(SELECT GRP_AGR_NO FROM LMT_AGR_INFO WHERE CUS_ID='"+cus_id+"')", connection);
						
						if(null==grp_kcoll.getDataValue("grp_agr_no") || "".equals(grp_kcoll.getDataValue("grp_agr_no"))){
							throw new EMPException("该客户所属集团融资模式为：整体授信，分配额度，未找到有效集团授信信息，请先做集团授信申请。");
						}
						String grp_amt = (String)grp_kcoll.getDataValue("crd_totl_amt");
						context.addDataField("grp_amt", grp_amt);  //将集团授信总额放到上下文
						context.addDataField("finance_type", finance_type);  //将集团融资模式放到上下文
					}
				}
			}
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			IndexedCollection iColl = new IndexedCollection();
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			String selectType = "";
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			if("03".equals(lmt_type) || "04".equals(lmt_type)){  //第三方授信 || 同业业务，选择第三方授信
				
				if(context.containsKey("selectType")){  //包含查询类型属性，说明非TAB页，需要查询数据
					selectType = context.getDataValue("selectType").toString();
					
					//根据查询类型，判断跳转页面
					if("".equals(selectType)){
						Map<String,String> inMap=new HashMap<String,String>();
						inMap.put("IN_PRDID", prd_id);
						inMap.put("IN_RPDDSCNT_TYPE", rpddscnt_type);//转贴现方式
						Map<String,String> outMap=new HashMap<String,String>();
						outMap = shuffleService.fireTargetRule("IQPCOMRULE", "SELECTTHIRDLMT", inMap);//规则：业务申请-业务申请-选择第三方额度信息
						context.put("OUT_COOP", outMap.get("OUT_COOP"));
						context.put("OUT_BIZAREA", outMap.get("OUT_BIZAREA"));
						context.put("OUT_BUSDRFT", outMap.get("OUT_BUSDRFT"));
						context.put("OUT_SAMEORG", outMap.get("OUT_SAMEORG"));
						context.put("OUT_INDUS", outMap.get("OUT_INDUS"));
						res_value = "thirdpartyPage";   //第三方授信TAB页面
						return res_value;
					}
					else if("1".equals(selectType)){  //合作方
						condition = condition.replaceAll("SUB_TYPE", "COOP_TYPE");  //将查询条件中的分项类别替换成合作方类别
						res_value = "coopPage"; 
					}
					else if("2".equals(selectType)){  //圈商
						condition = condition.replaceAll("SUB_TYPE", "BIZ_AREA_TYPE");  //将查询条件中的分项类别替换成圈商类别
						res_value = "bizAreaPage";
					}
					else if("3".equals(selectType)){   //行业
						condition = condition.replaceAll("SUB_TYPE", "INDUS_TYPE");  //将查询条件中的分项类别替换成行业类别
						res_value = "indusPage";
					}
					else if("4".equals(selectType)){   //商票贴现
						condition += conditionStr;
						
						res_value = "intBankPage";
					}
					else if("5".equals(selectType)){   //同业
						condition += conditionStr;
						
						res_value = "samePage";
					}
					
					iColl = lmtComponent.queryThirdPartyLmtInfo(cus_id, selectType, guar_type, condition, this.getDataSource(context),pageInfo);
				}else{
					Map<String,String> inMap=new HashMap<String,String>();
					inMap.put("IN_PRDID", prd_id);
					inMap.put("IN_RPDDSCNT_TYPE", rpddscnt_type);//转贴现方式
					Map<String,String> outMap=new HashMap<String,String>();
					outMap = shuffleService.fireTargetRule("IQPCOMRULE", "SELECTTHIRDLMT", inMap);//规则：业务申请-业务申请-选择第三方额度信息
					context.put("OUT_COOP", outMap.get("OUT_COOP"));
					context.put("OUT_BIZAREA", outMap.get("OUT_BIZAREA"));
					context.put("OUT_BUSDRFT", outMap.get("OUT_BUSDRFT"));
					context.put("OUT_SAMEORG", outMap.get("OUT_SAMEORG"));
					context.put("OUT_INDUS", outMap.get("OUT_INDUS"));
					res_value = "thirdpartyPage";   //第三方授信TAB页面
					return res_value;
				}
			}else{
				res_value = "signPage";
				String limit_ind = "";
				if(context.containsKey("limit_ind")){
					limit_ind = context.getDataValue("limit_ind").toString();
				}
				iColl = lmtComponent.queryLmtAgrDetails(cus_id,limit_ind, lmt_type, guar_type, limit_type, conditionStr, this.getDataSource(context),pageInfo);
			}
			
			iColl.setName("LmtAgrDetailsList");
			
			if(!"02".equals(lmt_type) && !"5".equals(selectType)){
				/**modified by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin**/
				String[] args=new String[] { "cus_id","core_corp_cus_id" };
				String[] modelIds=new String[]{"CusBase","CusBase"};
				String[] modelForeign=new String[]{"cus_id","cus_id"};
				String[] fieldName=new String[]{"cus_name","cus_name"};
				//详细信息翻译时调用
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				/**modified by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）end**/
			}else if(("03".equals(lmt_type) && "5".equals(selectType))||("04".equals(lmt_type) && "5".equals(selectType)) || ("02".equals(lmt_type))){   //同业客户翻译
				String[] args=new String[] { "cus_id" };
				String[] modelIds=new String[]{"CusSameOrg"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"same_org_cnname"};
				//详细信息翻译时调用
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			}
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return res_value;
	}

}
