package com.yucheng.cmis.biz01line.prd.prdtools.tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 通过标签实现tab页签动态挂接，达到控制操作权限的继承控制，以及配置控制
 * @author Pansq
 */
public class EMPActButton extends EMPExtTagSupport {
	private String op;
	private String label;
	private String mouseoutCss = "";
	private String mouseoverCss = "";
	private String mousedownCss = "";
	private String mouseupCss = "";
	private String locked;

	public EMPActButton() {
		op = null;
		label = null;
	}

	public int doStartTag() throws JspException {
		DataSource dataSource = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String menuId = null;
		try {
			/** 获取资源 */
			Context context = (Context)this.pageContext.getRequest().getAttribute(EMPConstance.ATTR_CONTEXT);
			TableModelLoader modelLoader = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			if(context.containsKey("menuIdTab")){
				menuId = (String)context.getDataValue("menuIdTab");
			}
			if(menuId == null || menuId.trim().length() == 0){
				menuId = (String)context.getParent().getDataValue("menuId");//主资源ID
			}
			String EMP_SID = (String)context.getParent().getDataValue("EMP_SID");
			String subButtonId = "";
			if(context.containsKey("subMenuId")){
				subButtonId =(String)context.getDataValue("subMenuId");//从资源ID
			}else {
				if(context.getParent().containsKey("subMenuId")){
					subButtonId =(String)context.getParent().getDataValue("subMenuId");//从资源ID
				}
			}
			String op = "";
			if(context.containsKey("op")){
				op = (String)context.getDataValue("op");//操作权限
			}else {
				if(context.getParent().containsKey("op")){
					op =(String)context.getParent().getDataValue("op");//操作权限
				}
			}
			dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = ConnectionManager.getConnection(dataSource);
			/** 获取从资源tab页签的资源ID，需要通过主资源以及访问路径url定位  */
			String sql = "select pkid,main_id,main_act_id,sub_id,sub_act_id,rule from prd_sub_tab_action where main_id=? and main_act_id=? and sub_id=?";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, menuId);
			pstmt.setString(2, op);
			pstmt.setString(3, subButtonId);
			rs = pstmt.executeQuery();
				while(rs.next()){
					String sub_act_id = rs.getString("sub_act_id");
					String rule = rs.getString("rule");
					String reResult = "true";
					if(rule == null || rule == ""){
						reResult = "";
					}else {
						/** 通过规则判断,判断之前取得session级别参数,如有需要可自行增加 */
						String userId= "";
						String organId = "";
						String dutyId = "";
						String roleId = "";
						if(context.getParent().containsKey(CMISConstance.ATTR_CURRENTUSERID)){
							userId = (String)context.getParent().getDataValue(CMISConstance.ATTR_CURRENTUSERID);
						}
						if(context.getParent().containsKey(CMISConstance.ATTR_ORGID)){
							organId = (String)context.getParent().getDataValue(CMISConstance.ATTR_ORGID);
						}
						if(context.getParent().containsKey(CMISConstance.ATTR_DUTYNO_LIST)){
							dutyId = (String)context.getParent().getDataValue(CMISConstance.ATTR_DUTYNO_LIST);
						}
						if(context.getParent().containsKey(CMISConstance.ATTR_ROLENO_LIST)){
							roleId = (String)context.getParent().getDataValue(CMISConstance.ATTR_ROLENO_LIST);
						}
						/** 1.调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色 */
	    				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    				ShuffleServiceInterface shuffleService = null;
	    				try {
	    					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
	    				} catch (Exception e) {
	    					EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
	    					throw new EMPException(e);
	    				}
	    				/**2.规则输入参数封装，此处只提供业务主键，所有数据通过主键查询*/
	    				Map<String, String> returnMap = new HashMap<String, String>();
						Map inputValueMap=new HashMap();
			    		inputValueMap.put("IN_登录人员ID", userId);
			    		inputValueMap.put("IN_登录人员机构ID", organId);
			    		inputValueMap.put("IN_登录人员岗位ID", dutyId);
			    		inputValueMap.put("IN_登录人员角色ID", roleId);
			    		/**3.执行规则检查，获得规则检查结果  */
			    		returnMap=shuffleService.fireTargetRule(rule.split("\\$")[0],rule.split("\\$")[1], inputValueMap);	
			    		reResult = returnMap.get("OUT_结果");
					}
					/** 判断规则返回true\false,确认显示的按钮信息 */
					if(reResult == "" || "true".equals(reResult)){
						if(sub_act_id.equals(this.op)){
							/** ------------------生成页面按钮代码START------------------ */
							try {
								StringBuffer sb = new StringBuffer();
								if (id == null || id.length() == 0){
									id = op;
								}
								sb.append("<button id=\"button_").append(id).append("\"");
								/*if (op != null && !"".equals(op)) {
									sb.append(" style=\"display:none\"");
									sb.append(" op='" + op + "'");
								}*/
								String btn_mouseout = this.mouseoutCss != null
										&& !this.mouseoutCss.trim().equals("") ? this.mouseoutCss
										: "btn_mouseout";
								String btn_mouseover = this.mouseoverCss != null
										&& !this.mouseoverCss.trim().equals("") ? this.mouseoverCss
										: "btn_mouseover";
								String btn_mousedown = this.mousedownCss != null
										&& !this.mousedownCss.trim().equals("") ? this.mousedownCss
										: "btn_mousedown";
								String btn_mouseup = this.mouseupCss != null
										&& !this.mouseupCss.trim().equals("") ? this.mouseupCss
										: "btn_mouseup";

								sb.append(" class=\"" + btn_mouseout + "\"").append(
										" onmouseover=\"this.className='" + btn_mouseover + "'\"")
										.append(
												" onmouseout=\"this.className='" + btn_mouseout
														+ "'\"").append(
												" onmousedown=\"this.className='" + btn_mousedown
														+ "'\"").append(
												" onmouseup=\"this.className='" + btn_mouseup
														+ "'\"").append(
												" onclick=\"" + getButtonClickFunc() + "\">"
														+ getLabel() + "</button>");

								outputContent(sb.toString());
								System.out.println(sb.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
							/** ------------------生成页面按钮代码END------------------ */
						}
					}else {
						/** 按钮隐藏*/
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				if(connection != null){
					ConnectionManager.releaseConnection(dataSource, connection);
					//connection.close();
					connection = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return 1;
	}

	protected String getButtonClickFunc() {
		StringBuffer sb = new StringBuffer();

		if (getLocked() != null && getLocked().equals("true")) {
			sb.append("lockScreen();");
		}
		sb.append("do" + id.substring(0, 1).toUpperCase() + id.substring(1))
				.append("(this)");
		return sb.toString();
	}

	public int doEndTag() throws JspException {
		return 0;
	}

	public String getLabel() {
		return getResourceValue(label);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getMouseoutCss() {
		return mouseoutCss;
	}

	public void setMouseoutCss(String mouseoutCss) {
		this.mouseoutCss = mouseoutCss;
	}

	public String getMouseoverCss() {
		return mouseoverCss;
	}

	public void setMouseoverCss(String mouseoverCss) {
		this.mouseoverCss = mouseoverCss;
	}

	public String getMousedownCss() {
		return mousedownCss;
	}

	public void setMousedownCss(String mousedownCss) {
		this.mousedownCss = mousedownCss;
	}

	public String getMouseupCss() {
		return mouseupCss;
	}

	public void setMouseupCss(String mouseupCss) {
		this.mouseupCss = mouseupCss;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}
}
