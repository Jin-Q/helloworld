package com.yucheng.cmis.platform.permission.resource.op;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.permission.PermissionContents;
import com.yucheng.cmis.platform.permission.XMLFileUtil;
import com.yucheng.cmis.platform.permission.domain.CMISResource;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.ResourceUtils;

public class ConfigResourceOp extends CMISOperation {
	private String dic;
    private String op;
    @Override
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String str = "";
            if("resourceTree".equals(op)){
            	str = this.queryResourceTree(context);
            }else if ("roleTree".equals(op)) {
            	str = this.queryRoleJsonByNodeId(context);
            }else if ("dutyTree".equals(op)){
            	str = this.queryDutyJsonByNodeId(context);
            }else if("actionTree".equals(op)){
            	str = this.queryActionJsonByNodeId(context);
            } else if("rightTree".equals(op)){
            	str = this.queryRightJsonByNodeId(context);
            } else if("grantRight".equals(op)){
            	str = this.grantRight(context);
            } else if("queryBizLineCfg".equals(op)){
            	str = this.queryBizLineCfg(context);
            }
            try {
                context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
            } catch (DuplicatedDataNameException e) {
                context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
            }
        } catch (EMPException e) {
            e.printStackTrace();
            throw new AsynException(e.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new AsynException(e1.getMessage());
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return "0";
    }

    private String queryRoleJsonByNodeId(Context context) {
        Connection con = null;
        String bizLine = null;
        String json = "";
        try {
            con = this.getConnection(context);
            try {
            	bizLine = (String) context.getDataValue("bizLine");
            } catch (Exception e) {
            	EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "context中没有属性bizLine！");
            }
            
            IndexedCollection dicIcol = null;
    		//查找对应的 条线字典
    		try{
    			dicIcol = (IndexedCollection)context.getDataElement("dictColl."+dic);
    		}catch(ObjectNotFoundException e){
    			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "获取对应字典出错！"+dic);
    		}
    		
    		
    		/*
    		 * 查找参数判断是不是需要   增加条线展示
    		 */
    		String lineShow = null;
    		try{
    			lineShow = (String)context.getDataValue("lineShow");
    		}catch(ObjectNotFoundException e){
    			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "未传递条线展示值");
    		}
    		
            //System.err.println("queryRoleJsonByNodeId>>>" + bizLine);
            TableModelDAO dao = this.getTableModelDAO(context);
            String condi = " where memo='"+bizLine+"' or memo='BL_DEFAULT'  order by  memo asc ,roleno asc";
            if(bizLine == null){
            	condi = " where memo='BL_DEFAULT'";
            } else if(bizLine.trim().equals("BL_ALL")){
            	condi = " order by  memo asc ,roleno asc";
            }
            IndexedCollection rolelist = dao.queryList("SRole", condi,con);

            json = "[{id:'root',text:'角色列表',leaf:false,expanded:true,children:[";
            for (int i = 0; i < rolelist.size(); i++) {
                KeyedCollection kcoll = (KeyedCollection) rolelist.get(i);
                String roleId = (String) kcoll.getDataValue("roleno");
                String rolename = (String) kcoll.getDataValue("rolename");
                String busiLine = (String)kcoll.getDataValue("memo");

                json += "{id:'" + roleId + "',text:'" + rolename + ("true".equals(lineShow)?"【<font color=\"red\">"+(XMLFileUtil.DEFAULT_BUSINESSLINE_ID.equals(busiLine)?"缺省业务条线":transferBusiLineDic(busiLine,dicIcol))+"</font>】":"")+"',checked:false,leaf:true,iconCls:'user',qtip:'"+roleId+"'}";
                if (i != rolelist.size() - 1) {
                    json += ",";
                }
            }
            json += "]}]";
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.releaseConnection(context, con);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return json;
    }
    
    private String queryDutyJsonByNodeId(Context context) {
        Connection con = null;
        String bizLine = null;
        String json = "";
        try {
            con = this.getConnection(context);
            try {
            	bizLine = (String) context.getDataValue("bizLine");
            } catch (Exception e) {
            	EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "context中没有属性bizLine！");
            }
            
            IndexedCollection dicIcol = null;
    		//查找对应的 条线字典
    		try{
    			dicIcol = (IndexedCollection)context.getDataElement("dictColl."+dic);
    		}catch(ObjectNotFoundException e){
    			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "获取对应字典出错！"+dic);
    		}
    		
    		
    		/*
    		 * 查找参数判断是不是需要   增加条线展示
    		 */
    		String lineShow = null;
    		try{
    			lineShow = (String)context.getDataValue("lineShow");
    		}catch(ObjectNotFoundException e){
    			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "未传递条线展示值");
    		}
    		
            //System.err.println("queryRoleJsonByNodeId>>>" + bizLine);
            TableModelDAO dao = this.getTableModelDAO(context);
            String condi = " where memo='"+bizLine+"' or memo='BL_DEFAULT'  order by  memo asc ,dutyno asc";
            if(bizLine == null){
            	condi = " where memo='BL_DEFAULT'";
            } else if(bizLine.trim().equals("BL_ALL")){
            	condi = " order by  memo asc ,dutyno asc";
            }
            IndexedCollection rolelist = dao.queryList("SDuty", condi,con);

            json = "[{id:'root',text:'岗位列表',leaf:false,expanded:true,children:[";
            for (int i = 0; i < rolelist.size(); i++) {
                KeyedCollection kcoll = (KeyedCollection) rolelist.get(i);
                String dutyNO = (String) kcoll.getDataValue("dutyno");
                String dutyName = (String) kcoll.getDataValue("dutyname");
                String busiLine = (String)kcoll.getDataValue("memo");

                json += "{id:'" + dutyNO + "',text:'" + dutyName + ("true".equals(lineShow)?"【<font color=\"red\">"+(XMLFileUtil.DEFAULT_BUSINESSLINE_ID.equals(busiLine)?"缺省业务条线":transferBusiLineDic(busiLine,dicIcol))+"</font>】":"")+"',checked:false,leaf:true,iconCls:'user',qtip:'"+dutyNO+"'}";
                if (i != rolelist.size() - 1) {
                    json += ",";
                }
            }
            json += "]}]";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.releaseConnection(context, con);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return json;
    }
    
    private String queryActionJsonByNodeId(Context context) {
        Connection con = null;
        String resId = "root";
        String json = "";
        try {
            con = this.getConnection(context);
            try {
            	resId = (String) context.getDataValue("resId");
            } catch (Exception e) {
                // TODO: handle exception
            }
            TableModelDAO dao = this.getTableModelDAO(context);
            IndexedCollection actionList = dao.queryList("s_resourceaction", "where resourceid='" + resId + "'", con);
      
            json = "[{id:'root',text:'可选操作',leaf:false,expanded:true,children:[";
            for (int i = 0; i < actionList.size(); i++) {
                KeyedCollection kcoll = (KeyedCollection) actionList.get(i);
                String actionId = (String) kcoll.getDataValue("actid");
                String descr = (String) kcoll.getDataValue("descr") + " " +actionId;
                
                //json += "{id:'" + actionId + "',text:'" + descr + "',leaf:true}"; //无checkbox
	            boolean checked = false;
                json += "{id:'" + actionId + "',text:'" + descr + "',checked:" + checked + ",leaf:true,iconCls:'oper'}";
 
                if (i != actionList.size() - 1) {
                    json += ",";
                }

            }
            json += "]}]";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.releaseConnection(context, con);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return json;
    }

    private String queryRightJsonByNodeId(Context context) {
        Connection con = null;
        String resId = "root";
        String roleId = "";
        String json = "";
        try {
            con = this.getConnection(context);
            try {
            	resId = (String) context.getDataValue("resId");
            	roleId = (String) context.getDataValue("roleId");
//System.err.println("queryRightJsonByNodeId: " + roleId);
            	
            	roleId = roleId.replaceAll(",", "','");
            	roleId = "'" + roleId + "'";
            } catch (Exception e) {
            	System.err.println(e.getMessage());
            }
            String condition = "";
            if(roleId != null && !roleId.trim().equals("")){
            	condition = "where resourceid='" + resId + "' and roleno in ("+roleId+")";
            }else{
            	condition = "where resourceid='" + resId + "'";
            }
            TableModelDAO dao = this.getTableModelDAO(context);
            IndexedCollection actionList = dao.queryList("s_resourceaction", "where resourceid='" + resId + "'", con);
      
            IndexedCollection rightList = dao.queryList("SRoleright", condition, con);
            
            json = "[{id:'root',text:'已分配权限',leaf:false,expanded:true,children:[";
            for (int i = 0; i < actionList.size(); i++) {
                KeyedCollection kcoll = (KeyedCollection) actionList.get(i);
                String actionId = (String) kcoll.getDataValue("actid");
                String descr = (String) kcoll.getDataValue("descr") + " " + actionId;
                
                if(rightList != null && rightList.size() > 0){
                	boolean isRight = false;
                	for(int n=0; n < rightList.size(); n++){
                		KeyedCollection kcolRight = (KeyedCollection)rightList.get(n);
                		String _aid = (String)kcolRight.getDataValue("actid");
                		if(_aid != null && _aid.trim().equals(actionId.trim())){
                			isRight = true;
                			break;
                		}
                	}
                	if(isRight){
                	   json += "{id:'" + actionId + "',text:'" + descr + "',leaf:true,iconCls:'oper'},"; //无checkbox
                	}
                }
            }
            if(json.endsWith(",")){
            	json = json.substring(0,json.length() - 1);//去掉最后一个逗号
            }
            json += "]}]";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.releaseConnection(context, con);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return json;
    }
    /**
     * 为角色分配权限
     * @param context
     * @return
     */
    private String grantRight(Context context) throws Exception{
        Connection con = null;
        String resId = "root";
        String roleId = "";
        String actionId = "";
        String json = "";
        String optype = null;
    	try {
    		resId = (String) context.getDataValue("resId"); //当前菜单
			roleId = (String) context.getDataValue("roleId");//当前角色
			actionId = (String) context.getDataValue("actionId");//待分配的操作
			
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			throw new Exception("参数不全，无法执行权限分配"+e.getMessage());
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			throw new Exception("参数非法，无法执行权限分配"+e.getMessage());
		}
		
		if(resId == null || resId.trim().equals("") || resId.trim().equals("--")){
			throw new Exception("参数'菜单编号'不全，无法执行权限分配");
		}
		
		if(actionId == null || actionId.trim().equals("")){
			throw new Exception("参数'操作编号'不全，无法执行权限分配");
		}
		
		if(roleId == null || roleId.trim().equals("")){
			throw new Exception("参数'角色编号'不全，无法执行权限分配");
		}
		try{
		   optype = (String) context.getDataValue("optype");//分配操作类型: grant、revoke
		}catch (ObjectNotFoundException e) {
			optype = null;
		}
		
		con = this.getConnection(context);
		try{
		if(roleId != null && !roleId.trim().equals("")){
			String[] roleAry = roleId.split(",");
			String[] actionAry = actionId.split(",");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int n=0; n<roleAry.length; n++){
				
               for(int i=0; i<actionAry.length; i++){
               
	               //删除角色在当前资源下的与所选中的操作ID一至的操作，避免重
	               KeyedCollection kcoll = new KeyedCollection("SRoleright");
	               kcoll.setAppend(true);
	               kcoll.addDataField("resourceid", resId);
	               kcoll.addDataField("roleno", roleAry[n]);
	               kcoll.addDataField("actid", actionAry[i]);
	               dao.deleteByPks("SRoleright", kcoll, con);
	               
	               //当操作类型是grant时将选中的操作插入表中
	               if(optype == null || optype.trim().equals("grant")){
	                   kcoll.addDataField("state", "1");
	                   dao.insert(kcoll, con);
	               }
               }
			}
			json = "[{id:'msg',msg:'success'}]";
		}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
    	return json;
    }
    
	private String queryResourceTree(Context context) throws Exception{

		String bizLine = null;
		Connection connection = this.getConnection(context);
		StringBuffer buf = new StringBuffer();
		IndexedCollection dicIcol = null;
		//查找对应的 条线字典
		try{
			dicIcol = (IndexedCollection)context.getDataElement("dictColl."+dic);
		}catch(ObjectNotFoundException e){
			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "获取对应字典出错！"+dic);
		}
	
		
        try {
        	bizLine = (String) context.getDataValue("bizLine");
        } catch (Exception e) {
        }
        //显示自己条线 以及公共条线的东西
        String condi = "where SYSTEMPK like '%"+bizLine+"%' or SYSTEMPK like '%BL_ALL%' or SYSTEMPK = 'BL_DEFAULT'";///注意：(1)这要求一个菜单下的所有子菜单都属于同一个子系统 (2)系统编号要等长（like时没有加分隔符）
        if(bizLine == null){
        	condi = "where SYSTEMPK = 'BL_DEFAULT'";
        } else if(bizLine.trim().equals("BL_ALL")){
           //所有条线会显示 所有的 模块
        	condi = "";
        }
		String sql = "select RESOURCEID,CNNAME,PARENTID,SYSTEMPK from S_RESOURCE " + condi + " order by ORDERID";
		HashMap resourcesMap = new HashMap();
		List rootResource = new LinkedList();
		
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			state = connection.prepareStatement(sql);
			rs = state.executeQuery();
			while(rs.next()){
				String resourceId = rs.getString(1);
				String cnName = rs.getString(2);
				String parentId = rs.getString(3);
				String systemId = rs.getString(4);
				
				CMISResource resource = (CMISResource)resourcesMap.get(resourceId);
				if(resource == null){
					resource = new CMISResource();
					resourcesMap.put(resourceId, resource);
				}
				resource.resourceId = resourceId;
				
				resource.cnName = cnName+"【<font color=\"red\">"+(XMLFileUtil.DEFAULT_BUSINESSLINE_ID.equals(systemId)?"缺省业务条线":transferBusiLineDic(systemId,dicIcol))+"</font>】";
				resource.systemid = systemId;
				if(parentId == null || "".equals(parentId.trim())){
					resource.parentId = "";
					rootResource.add(resource);
				}else{
					resource.parentId = parentId;
					CMISResource parent = (CMISResource)resourcesMap.get(parentId);
					if(parent == null){
						parent = new CMISResource();
						resourcesMap.put(parentId, parent);
					}
					parent.addCMISResource(resource);
				}
			}
			Iterator iterator = rootResource.iterator();
	        boolean hasNext = iterator.hasNext();
	        buf.append("[");
	        while (hasNext) {
	        	CMISResource resource = (CMISResource)iterator.next();
	            hasNext = iterator.hasNext();
	            buf.append(resource.toJSONString());
	            if (hasNext)
	                buf.append(",");
	        }
			buf.append("]");
		}catch (SQLException e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "error!", e);
			throw e;
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
			if(state != null){
				try {
					state.close();
				} catch (SQLException e) {}
			}
		}
		return buf.toString();
	}
    
	private String queryBizLineCfg(Context context){
		StringBuffer retData = new StringBuffer();
		try {
			String currentUser = null;
			String bizLine = null;
			try{
				/**
				 * 20120426周凤雷
				 * 业务条线改为直接从context中获取，不再从页面获取
				 */
			 // bizLine = (String)context.getDataValue("bizLine");
				if(context.containsKey("bizlineList")){
					 bizLine = (String)context.getDataValue("bizlineList");
				}
			}catch(ObjectNotFoundException e1){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "context中未加载用户的业务条线！",e1);
			}
			try{
			  currentUser = (String)context.getDataValue("currentUserId");
			}catch(ObjectNotFoundException e1){
			}
			/** 该表读取模式，将从XML读取改为读取字典项 */
			TreeMap tMap = new TreeMap();
			IndexedCollection lineIColl = (IndexedCollection)context.getDataElement("dictColl.STD_ZB_BUSILINE");
			if(lineIColl != null && lineIColl.size() > 0){
				for(int i=0;i<lineIColl.size();i++){
					KeyedCollection lineKColl = (KeyedCollection)lineIColl.get(i);
					String enname = (String)lineKColl.getDataValue("enname");
					String cnname = (String)lineKColl.getDataValue("cnname");
					tMap.put(enname, cnname);
					
				}
			}
			
		   /* ResourceBundle res = ResourceBundle.getBundle("cmis");
		    String dir = res.getString("component.config.file.dir");
			String CONFIG_FILM_DIR = ResourceUtils.getFile(dir).getAbsolutePath();
			XMLFileUtil fileUtil = new XMLFileUtil();
			HashMap allConfig = (HashMap)fileUtil.readBusinessLinesFormXMLFile(CONFIG_FILM_DIR);
			for(Iterator itr=allConfig.keySet().iterator(); itr.hasNext();){
				String _bizId = (String)itr.next();
				
				if(_bizId != null && !_bizId.trim().equals(fileUtil.DEFAULT_BUSINESSLINE_ID)){//缺省条线不显示
					HashMap _config = (HashMap)allConfig.get(_bizId);
					if(_config != null){
						tMap.put(_bizId, _config.get("name"));
					}
				}
			}*/
			retData.append("[");
			for(Iterator itr=tMap.keySet().iterator(); itr.hasNext();){
				String _bizId = (String)itr.next();
				///@TODO 需控制当前用户只配置自己所属的条线  是超级管理员则可以配置所有条线
				if(currentUser.trim().equals(PermissionContents.CMIS_SUPER_ADMIN) 
						|| bizLine.indexOf(_bizId) >= 0){
				  retData.append("['").append(_bizId).append("','") .append(tMap.get(_bizId)).append("'],");
				}
			}
			///@TODO 需控制超级管理员可以配所有条线
			String _tmp = retData.substring(0, retData.length()-1);
			retData = new StringBuffer();
			retData.append(_tmp);
			/*if(currentUser.trim().equals(PermissionContents.CMIS_SUPER_ADMIN)){
			  retData.append("['BL_ALL','所有业务条线']");
			}else{
				
				String _tmp = retData.substring(0, retData.length()-1);
				retData = new StringBuffer();
				retData.append(_tmp);
			}*/
			
			retData.append("]");
			
System.err.println(retData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retData.toString();
	}
	
	public String transferBusiLineDic(String enname,IndexedCollection iCol) throws ObjectNotFoundException, InvalidArgumentException {
		
		if(iCol == null || iCol.size() ==0 || enname == null || enname.length() == 0){
			return null;
		}
		KeyedCollection kColTmp = null;
		for(int i=0;i<iCol.size();i++){
			kColTmp = (KeyedCollection)iCol.get(i);
			if(enname.equals(kColTmp.getName())){
				return (String)kColTmp.getDataValue(CMISDataDicService.ATTR_CNNAME);
			}
		}
		return null;
		
	}
    @Override
    public void initialize() {
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

	public String getDic() {
		return dic;
	}

	public void setDic(String dic) {
		this.dic = dic;
	}
    
    
}
