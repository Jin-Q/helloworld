package com.yucheng.cmis.platform.msiview.op;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.msiview.ViewModualRelyOnRelation;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * 模块依赖关系查看
 * @author yuhq
 *
 */
public class ViewModualRelyOnRelationOp extends CMISOperation{

	@Override
	public String doExecute(Context context) throws EMPException {
		try {
			
			String modualId = (String)context.getDataValue("modual_id");
			
			String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
			ViewModualRelyOnRelation obj = ViewModualRelyOnRelation.getInstance();
			
			List<String> fileList = new ArrayList<String>();
			List<String> relyList = new ArrayList<String>();
			this.searchFolder(projectPath+"/src/main/java/com/yucheng/cmis/", modualId, new String[]{"baffleplate","msi"}, fileList);
			
			if(fileList.size()>0){
				String filePath = fileList.get(0);
				relyList.add("自动检测到模块ID["+modualId+"]的JAVA目录是:"+filePath+"\n");
				relyList.add("检则该模块的依赖关系如下：\n");
				relyList.add("\n");
				
				
				String relyOn = obj.getModualRelyOnRelationWithXML(projectPath+"/src/main/java/com/yucheng/",filePath);
				

				HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				request.setAttribute("modualRelOn", relyList);
				
				System.out.println(relyOn);
				
				///relyOn = new String(relyOn.getBytes("GBK"),"GBK");
				
				//relyOn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><modual_rel><modual id=\"organization\" name=\"组织机构管理模块\"><services id=\"organizationServices\" name=\"com.yucheng.cmis.standard.platform.organization.interfaces.OrganizationServiceInterface\" desc=\"组织机构管理模块对外提供的常用服务接口\"><service name=\"getRolesByUserId\" desc=\"获得该用户拥有的角色集合\" /></services></modual></modual_rel>";
				
				request.setAttribute("xmlStr", relyOn.getBytes("UTF-8"));
				
				//context.addDataField("relyOn", relyOn);
			}
			
		} catch (Exception e) {
			throw new EMPException(e);
		}
		
		return "0";
	}
	
	
	/**
	 * <p>
	 * 	在指定目录下搜索目标目录，支持过滤指定目录
	 * </p>
	 * 
	 * @param path 搜索根目录
	 * @param targetFolder 需搜索的目录名（不含路径）
	 * @param exceptFolder 需过滤的目录，过滤目录的子目也将被过滤
	 */
	private void searchFolder(String path, String targetFolder, String[] exceptFolders, List<String> retList) throws Exception{
		try {
			if(retList == null) retList = new ArrayList<String>();
			
			
			//如果根目录就是目标目录，则直接返回
			File folder = new File(path);
			if(folder.getName().equals(targetFolder)){
				retList.add(this.convertFileSeparator(folder.getPath()));
				return ;
			}
			
			File[] files = folder.listFiles();
			if(files==null) return;
			for (int i = 0; i < files.length; i++) {
				boolean flag = false;
				File file = files[i];
				
				if(file.getName().equals(targetFolder)) {
					retList.add(this.convertFileSeparator(file.getPath()));
					return ;
				}
				
				if(exceptFolders!=null && exceptFolders.length>0){
					for (int j = 0; j < exceptFolders.length; j++) {
						if(file.getName().equals(exceptFolders[j])){
							flag = true;
							break;
						}
					}
				}
				
				if(file.isDirectory() && !flag )
					searchFolder(file.getPath(), targetFolder, exceptFolders, retList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将文件分隔符统一替换成"\"
	 * @param path 文件路径
	 * @return 替换后的文件路径
	 */
	private String convertFileSeparator(String path){
		if(path.contains("\\\\"))
			path = path.replace("\\\\", "/");
		
		if(path.contains("\\")){
			path = path.replace(File.separator, "/");
		}
		
		return path;
	}

}
