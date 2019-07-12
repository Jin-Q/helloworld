package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * 客户信息公用pop，其中查询条件可根据传入的cusType的不同进行自定义
 * 
 * @author QZCB
 *
 */
public class QueryAllCusListOp extends CMISOperation {

        private final String modelId = "CusBase";

        public String doExecute(Context context) throws EMPException {
                
                Connection connection = null;
                String cusQueryCondition = "";
//                OrganizationServiceInterface orgMsi = null;
                try{
                    connection = this.getConnection(context);
                    
                    KeyedCollection queryData = null;
//                    String orgId = "";
//                    String currId = "";
                    try {
//                        currId = (String) context.getDataValue("currentUserId");
//                        orgId= (String) context.getDataValue("organNo");
                        queryData = (KeyedCollection)context.getDataElement(this.modelId);
                    } catch (Exception e) {}
                    
                    try {
                    	//2014-08-28 正式户状态过滤生效 by yangzy start
                    	//2014-07-09上线特殊处理  去掉正式户状态过滤  by zhaozq start
                    	if(context.containsKey("cusTypCondition")){
                    		cusQueryCondition = (String) context.getDataValue("cusTypCondition");
                    	}
                    	String fg = "";
                    	if(context.containsKey("flag")){
                			fg = (String) context.getDataValue("flag");
                			if("1".equals(fg)){
                				cusQueryCondition = " BELG_LINE in ('BL100','BL200','BL300') and cus_status='20' and cus_id in (select cus_id from acc_loan a where five_class > '20') ";
                        	}
                    	}
                    	
                    	//if(cusQueryCondition.indexOf("and cus_status='20'")!=-1){
                    	//	cusQueryCondition = cusQueryCondition.replace("and cus_status='20'", "");
                    	//}
                    	//if(cusQueryCondition.indexOf("cus_status='20' and")!=-1){
                    	//	cusQueryCondition = cusQueryCondition.replace("cus_status='20' and", "");
                    	//}
                    	//if(cusQueryCondition.indexOf("cus_status='20'")!=-1){
                    	//	cusQueryCondition = cusQueryCondition.replace("cus_status='20'", "");
                    	//}
                    	//2014-07-09上线特殊处理  去掉正式户状态过滤  by zhaozq end
                    	//2014-08-28 正式户状态过滤生效 by yangzy end
                    	/* modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 begin*/
                    	String opt="";
                    	if(context.containsKey("opt")){
                    		opt=TagUtil.replaceNull4String(context.getDataValue("opt"));
                    	}
                    	if("team".equals(opt)){
                    		String cusMgr=(String) context.getDataValue("currentUserId");
                    		String organNo=(String) context.getDataValue("organNo");
                    		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
                    		IndexedCollection sorgList=dao.queryList("SOrg", " where  s_org.organno='"+organNo+"' ", connection);//丰泽分行和鲤城区分行筛选
                    		if(sorgList!=null&&sorgList.size()>0){
                    			KeyedCollection kColl=dao.queryFirst("STeam", null," where status='1' and team_no in (SELECT team_no FROM s_team_user  WHERE  mem_no='"+cusMgr+"' ) ", connection);
                        		if(kColl!=null&&!kColl.isEmpty()&&kColl.getDataValue("team_no")!=null&&!"".equals((String)kColl.getDataValue("team_no"))){
                        			cusQueryCondition = (String) context.getDataValue("cusTypCondition2");//客户经理属于团队
                        			String team_no=(String) kColl.getDataValue("team_no");//团队编号唯一
                        			IndexedCollection iColl=dao.queryList("STeamOrg"," where team_no='"+team_no+"' ", connection);
                        			cusQueryCondition+=" and BELG_LINE='BL100' and main_br_id in ";
                        			String mainBrIdStr="(";
                            		if(iColl!=null&&iColl.size()>0){
                            			for(int i=0;i<iColl.size();i++){
                            				KeyedCollection kcoll=(KeyedCollection) iColl.get(i);
                            				String team_org_id=(String) kcoll.getDataValue("team_org_id");
                            				mainBrIdStr+="'"+team_org_id+"',";
                            			}
                            		}else{
                            			mainBrIdStr+="'',";
                            		}
                            		mainBrIdStr=mainBrIdStr.substring(0, mainBrIdStr.length()-1);
                            		mainBrIdStr+=")";
                            		cusQueryCondition+=mainBrIdStr;
                        		}else{
                        			cusQueryCondition += " and 1=1";
                        		}
                    		}
                    	}
                    	/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 end*/
                    	
                    } catch (Exception e) {
                    	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "没有配置查询条件", null);}
                    
                    String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
                    
                    if("".equals(conditionStr)||conditionStr==null){
                    	conditionStr = " where 1=1 " ;
                    }
                    conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
                    if(cusQueryCondition!=null&&!"".equals(cusQueryCondition)){
                    	conditionStr = conditionStr + " and " + cusQueryCondition;
                    }
//                    String main_br_id = (String)context.getDataValue("organNo");
//                    String currentUserId = (String)context.getDataValue("currentUserId");
//                    
//                    orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
//                    List<SRole> roleList = orgMsi.getRolesByUserId(currentUserId, connection);
//                    String rolenos = "", rolenames = "";
//                    String roles = "";
//                    if(roleList!=null && roleList.size()>0) {
//            		for(SRole role : roleList) {
//            			rolenos += role.getRoleno() + ",";
//            			rolenames += role.getRolename() + ",";
//            			roles += ("'" + role.getRoleno() + "',");
//            		}
//            		roles = roles.substring(0, roles.length()-1);
//            		rolenos = rolenos.substring(0, rolenos.length()-1);
//            		rolenames = rolenames.substring(0, rolenames.length()-1);
//            		}
//                    conditionStr = conditionStr + " and (main_br_id in (select organno from s_org where locate like '%"+main_br_id+"%') or ('1010' in ("+roles+")) or ('1020' in ("+roles+")) or ('1021' in ("+roles+")) or ('1022' in ("+roles+"))) ";
        /*            StringBuffer sb = new StringBuffer("");
                    if("".equals(cusType)||cusType==null||"All".equals(cusType)){//默认查全部
                    	
                    }else if("Com".equals(cusType)){
                    	sb.append(" and belg_line in ('BL100','BL200') ");	//用所属条线区分客户对公还是个人
                    }else if("Ind".equals(cusType)){
                    	sb.append(" and belg_line='BL300' ");
                    }else if(cusType.equals("CusCogniz")){//客户认定
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_id not in (select cus_id from Cus_Cogniz_Apply) ");
    					sb.append(" and cus_status = '20'");
    				}else if(cusType.equals("CusScale")){//条线认定
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_id not in (select cus_id from Cus_Scale_Apply) ");
    					sb.append(" and cus_status = '20'");
    					sb.append(" and cus_id in (select cus_id from cus_com )");
    				}else if(cusType.equals("EvalOrgCogniz")){//评估机构认定
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_id not in (select cus_id from CUS_ORG_APP) ");
    					sb.append(" and cus_status = '20'");
    					sb.append(" and cus_id in (select cus_id from cus_com )");
    				}else if(cusType.equals("CusGrpInfo")){//关联（集团）客户认定
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_status = '20'");
    					sb.append(" and cus_id in (select cus_id from cus_com )");
    				}else if(cusType.equals("GoverFinTer")){//政府融资平台信息
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_id not in (select cus_id from Cus_gover_fin_ter) ");
    					sb.append(" and cus_status = '20'");
    					sb.append(" and cus_id in (select cus_id from cus_com )");
    				}else if(cusType.equals("FinGuar")){//融资性担保公司
    					sb.append(" and cus_type='A2'");
    				}else if(cusType.equals("NotFinCom")){//非融资性担保公司且为对公客户
    					sb.append(" and cus_type <>'A2' and belg_line in ('BL100','BL200') ");
    				}else if(cusType.equals("LmtIndusList")){//行业授信名单
    					String serno = (String) context.getDataValue("serno");
    					sb.append(" and cust_mgr='"+currId+"'");
    					sb.append(" and main_br_id='"+orgId+"'");
    					sb.append(" and cus_id not in (select cus_id from lmt_indus_List where serno = '"+serno+"') ");
    					sb.append(" and cus_status = '20'");
    					sb.append(" and cus_id in (select cus_id from cus_com )");
    				}else if(cusType.equals("bizArea")){//圈商客户新增名单+
    					String baapp_flag = (String)context.getDataValue("baapp_flag");
    					String serno = (String)context.getDataValue("serno");
    					if("0".equals(baapp_flag )){
    						//入圈   在圈内的客户(任何状态)过滤掉
//    						sb.append(" and cus_id not in (select cus_id from lmt_name_list " +
//        							"where serno in (select serno from lmt_app_join_back where biz_area_no in " +
//        							"(select biz_area_no from lmt_app_join_back where serno = '" + serno + "')))");
    						sb.append(" and belg_line='BL300'");
    					}else{  //退圈   有效客户才能退圈
    						sb.append(" and cus_id in (select cus_id from lmt_name_list " +
    							"where serno in (select serno from lmt_app_join_back where biz_area_no in " +
    							"(select biz_area_no from lmt_app_join_back where serno = '" + serno + "')) and cus_state='2')");
    					}
    					//sb.append(" and belg_line='BL300'");
    				}else if(cusType.equals("Soc")){//配偶信息
    					String sex = (String) context.getDataValue("indiv_sex");
    					sb.append(" and indiv_sex = '"+ sex+"' order by cus_state desc, cus_id desc");
    				}else if(cusType.equals("LmtIndiv")){
    					String lmt_serno = (String) context.getDataValue("lmt_serno");
    					sb.append(" and cus_id in ((select cus_id from lmt_app_indiv where serno='"+lmt_serno+"')union(select same_debtor_id from lmt_app_indiv where serno='"+lmt_serno+"')union(select cus_id_rel from cus_indiv_soc_rel where indiv_cus_rel='1' and cus_id in (select cus_id from lmt_app_indiv where serno='"+lmt_serno+"')))");
    				}else if("IqpNet".equals(cusType)){
    					sb.append("and belg_line='BL200'");
    				}else if("IqpCommo".equals(cusType)){//条线不属于个人的客户
    					sb.append(" and belg_line <>'BL300'");
    				}
                    conditionStr = conditionStr + sb.toString();
                    */
                    System.out.println(conditionStr);
                    int size = 15;
            
                    PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            
                    TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
            
                    List<String> list = new ArrayList<String>();
                    list.add("cus_id");
                    list.add("cus_name");
                    list.add("cert_type");
                    list.add("cert_code");
                    list.add("cust_mgr");
                    list.add("main_br_id");
//                    list.add("cus_addr");
                    list.add("loan_card_id");
                    list.add("cus_type");
//                    list.add("guar_cls");
//                    list.add("guar_bail_multiple");
                    list.add("belg_line");
                    list.add("guar_crd_grade");
                    list.add("cus_crd_grade");   // 2013-11-19  唐顺岩添加  信用等级
                    list.add("cus_country");
                    IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
                    iColl.setName(iColl.getName()+"List");
                    SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
                    SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
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
                return "0";
        }

}
