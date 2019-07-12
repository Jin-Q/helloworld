package com.yucheng.cmis.biz01line.fnc.config.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.fnc.config.agent.FncConfTemplateAgent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.StringUtil;

/**
 * @Classname FncConfTemplateComponent.java
 * @Version 1.0
 * @Since 1.0 2008-10-6 下午08:26:55
 * @Copyright yuchengtech
 * @Author Yu
 * @Description：
 * @Lastmodified
 * @Author
 */
public class FncConfTemplateComponent extends CMISComponent {

	private FncConfTemplate fncConfTemplate;

	// private BizConfig bizConfig;
	
	public String addFncConfTemplate(FncConfTemplate pfncConfTemplate)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfTemplateAgent fncConfTemplateAgent = (FncConfTemplateAgent) this
				.getAgentInstance("FncConfTemplate");

		// 新增一条成员信息
		flagInfo = fncConfTemplateAgent.addRecord(pfncConfTemplate);

		return flagInfo;
	};

	/**
	 * 
	 * @param pfncConfItems
	 *            报表配置项目列表信息
	 * @return flagInfo 信息编码
	 * @throws ComponentException
	 */
	public String modifyFncConfTemplate(FncConfTemplate pfncConfTemplate)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfTemplateAgent fncConfTemplateAgent = (FncConfTemplateAgent) this
		.getAgentInstance("FncConfTemplate");

		// 通过代理类进行修改操作
		flagInfo = fncConfTemplateAgent.updateRecord(pfncConfTemplate);

		return flagInfo;
	};

	/**
	 * 
	 * @param itemId
	 *            项目编号
	 * @return pfncConfItems 报表配置项目列表信息
	 * @throws ComponentException
	 */
	public FncConfTemplate findFncConfTemplate(String fncId)
			throws ComponentException {

		FncConfTemplate pfncConfTemplate = new FncConfTemplate();
		// 创建业务代理类
		FncConfTemplateAgent fncConfTemplateAgent = (FncConfTemplateAgent) this
		.getAgentInstance("FncConfTemplate");
		// 通过代理类进行查看操作
		pfncConfTemplate = fncConfTemplateAgent.queryDetail(fncId);

		return pfncConfTemplate;

	};

	/**
	 * 
	 * @param itemId
	 *            项目编号
	 * @return flagInfo 信息编码
	 * @throws ComponentException
	 */

	public String removeFncConfTemplate(String fncId) throws ComponentException {

		String flagInfo = CMISMessage.DEFEAT;
		// 创建业务代理类
		FncConfTemplateAgent fncConfTemplateAgent = (FncConfTemplateAgent) this
		.getAgentInstance("FncConfTemplate");
		//通过代理类进行删除操作
		flagInfo = fncConfTemplateAgent.deleteRecord(fncId);
		return flagInfo;
	};
	
	/**
	 * 从内存中获取财务模板对象
	 * @param fncId 模板id
	 * @return
	 * @throws EMPException
	 */
	public FncConfTemplate findFncConfTemplateFromCashe(String fncId)
	throws EMPException {
		FncConfTemplate rt = new FncConfTemplate();		
		rt=FNCFactory.getFNCFactoryInstance().getTemplateInstance(fncId);
		return rt;
	};
	
	/**
	 * 生成js界面
	 * @param tempNo
	 * @throws Exception
	 */
	public void generateFncJs(String styleId) throws Exception{
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO) this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);	
			String condition = " where style_id='"+styleId+"' and fnc_conf_cal_frm is not null order by fnc_conf_order";
			List list = new ArrayList();
			list.add("item_id");
			list.add("fnc_conf_cal_frm");
			IndexedCollection iCollCal = dao.queryList("FncConfDefFmt", list, condition, connection);
			
			StringBuffer sb = new StringBuffer();
			sb.append("function calculate(){");
			Hashtable map = new Hashtable();
			for(int i=0;i<iCollCal.size();i++){
				KeyedCollection kCollCal = (KeyedCollection) iCollCal.get(i);
				String itemId = (String) kCollCal.getDataValue("item_id");
				String strCal = (String) kCollCal.getDataValue("fnc_conf_cal_frm");//计算公式
				map.clear();
				map.put(itemId, strCal);
				String sResult = getCalculateBbRpt(map);
				sb.append("\n").append(sResult);
			}
			sb.append("\n}");
			
			//生成js文件
//			String JSStr=this.genJavaScript();
			this.genFile(styleId+".js", styleId, sb.toString(),"js");
		
		} catch (AgentException e) {
			throw new ComponentException(e);
		}		
	}
	
	/**
	 * 生成文件
	 * 无论是jsp,还是js都通过此方法生成
	 * @param fileName
	 * @param folderName
	 * @param str
	 * @param type
	 * @throws ComponentException
	 * @throws FileNotFoundException
	 */
	private void genFile(String fileName,String folderName,String str,String type) throws ComponentException, FileNotFoundException{
	 	   File file = null;
	 	   OutputStreamWriter writer=null;
		   String folder="";
		   ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("qry.jsp.path");  
			String jsdir = res.getString("qry.js.path");
			String path=ResourceUtils.getFile(dir).getAbsolutePath();
			URL url=QryGenPageComponent.class.getResource("");
			path=url.getPath(); 
			path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 
//			logger.info("模型生成根路径："+path);
//			
//			//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
//			logger.info("模型生成jsp文件路径："+dir);
//			logger.info("模型生成js文件路径:"+jsdir);		   
		   try {
			   if (type.equals("jsp")){
				   //folder=path+dir;//生成目录换成共享盘下文件夹不需要获取根路径
				   folder=dir;
				   file = new File(folder);
			   }else{//type.equals("js")
				   //folder=path+jsdir+"/fnc";//生成目录换成共享盘下文件夹不需要获取根路径
				   folder=jsdir+"/fnc";
				   file = new File(folder);
			   }
			  folder=folder+File.separator+folderName;
			  file = new File(folder);
			  //如果文件夹不存在则创建文件夹
			  if(!file.exists() ){
				  if(!file.mkdirs()){
					  throw new ComponentException("创建文件夹["+folderName+"]失败!");
				  }
			  }
			  fileName = folder+File.separator+fileName;//"mvcs"+File.separator+"CMISMvc"+ File.separator +"analyse"+ File.separator +"jsps"+ File.separator + 
			  file = new File(fileName);
			  if( file.exists() ){
				  file.delete();
			  }
			  if(!file.createNewFile()){
				  throw new ComponentException("创建文件失败!");
			  }
			  /*初始化文件生成类*/
			  //writer = new BufferedWriter(new FileWriter(file));
			  writer= new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			  
			  writer.write(str);//new String(str.getBytes("ISO-8859-1"),"UTF-8")
			  writer.flush();
		   } catch (Exception e) {
			  throw new ComponentException("生成文件失败!"+e.getMessage());
		   } finally{
			   try {
				   if( writer != null ) writer.close();
			   } catch (Exception e) {}
		   }
		}
	
	//自动计算资产负债表和损益表
    public String getCalculateBbRpt(Hashtable map) throws Exception {
    	StringBuffer sb1 = new StringBuffer();
    	Map mapTmp = new HashMap();
    	
    	Enumeration en = map.keys();
		while (en.hasMoreElements()) {
			String itemId = en.nextElement().toString();
			String calcula = (String) map.get(itemId);
			
			String[] checkFormula = new String[2];
            checkFormula[0] = calcula;
            checkFormula[1] = calcula;
            mapTmp.put(itemId ,checkFormula);
            
            while (!StringUtil.getParamString(checkFormula[0], '{', '}').equals("")){
            	String param = StringUtil.getParamString(checkFormula[0], '{', '}');//取一个中括号间的字符串（表.科目.期初/期末）
 	            String[] str = StringUtil.strToArray(param,'.');//资产负债表和损益配置不含‘.’

 	           if (str.length == 1) {
 	             String tmpId = StringUtil.getParamString(str[0], '[', ']');//取出itemId
 	             tmpId = tmpId.trim();
// 	             String strTmp = "parseFloat((document.getElementById('rptstyle."+tmpId+".data2').value).replace(',',''))";
 	             String strTmp = "parseFloat((''==rptstyle."+tmpId+".data2._getValue()?0:rptstyle."+tmpId+".data2._getValue()))";
 	             checkFormula[0] = checkFormula[0].replace("{"+param+"}", strTmp);
 	             
 	           }
            }
            sb1.append("rptstyle.").append(itemId).append(".data2._setValue(");
            sb1.append(checkFormula[0]).append(".toFixed(2)+'');");
            System.out.println(sb1.toString());
		}
		return sb1.toString(); 
    }

}
