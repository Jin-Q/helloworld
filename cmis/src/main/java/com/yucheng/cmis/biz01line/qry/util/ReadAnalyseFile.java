package com.yucheng.cmis.biz01line.qry.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;
import com.yucheng.cmis.pub.CMISDao;

public class ReadAnalyseFile {
	private static final Logger logger = Logger.getLogger(CMISDao.class);
	private final String OUT_ENCODE = "UTF-8";
	//private final String IN_ENCODE = "GBK";
	private final char CIRCUMSCRIPTION = '|';

	public Collection<String> readFile(String fileName, int start, int end)
			throws CMISException {
		Collection<String> col = null;
		File file = null;
		ByteArrayOutputStream bos = null;
		InputStreamReader bufferReader = null;

		int offset = 0;
		int i = -1;
		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");
		URL url = QryGenPageComponent.class.getResource("");
		path = url.getPath();
		path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", "");
		path = path + File.separator + dir;
		// path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";

		try {
			fileName = path + File.separator + fileName;
			logger.info("" + fileName);
			file = new File(fileName);
			if (!file.exists()) {
				throw new CMISException("未发现文件名为『" + fileName + "』的文件");
			}

			bufferReader = new InputStreamReader(new FileInputStream(file));
			bos = new ByteArrayOutputStream();
			col = new ArrayList<String>();
			while ((i = bufferReader.read()) != -1) {
				if ((offset == 0) || (offset >= start && offset < end)) {
					if (i == '\n') {
						col.add(bos.toString());
						bos.close();
						bos = new ByteArrayOutputStream();
					} 
					
					else {
						bos.write(i);
						
					}
				} else if (offset > end) {
					break;
				}
				if (i == '\n') {
					offset++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException("读文件错!『" + e.getMessage() + "』");
		} finally {
			try {
				if (bos != null)
					bos.close();
				bos = null;
			} catch (Exception e) {
			}
			try {
				if (bufferReader != null)
					bufferReader.close();
			} catch (Exception e) {
			}
		}
	
		return col;
	}

	public String genHtml2(String fileName, int start, int end,
			String replaceStr, String replaceStr2, String title_replace,
			String title_replace2) throws CMISException {
		String str = "";
		File file = null;
		ByteArrayOutputStream bos = null;
		InputStream bufferReader = null;

		int offset = 0;
		byte i = -1;
		//StringBuilder sb = new StringBuilder();

		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");
		URL url = QryGenPageComponent.class.getResource("");
		path = url.getPath();
		path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", "");
		path = path + File.separator + dir;
		// path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		try {
			fileName = path + File.separator + fileName;
			file = new File(fileName, "gbk");
			if (!file.exists()) {
				throw new CMISException("未发现文件名为『" + fileName + "』的文件");
			}

			bufferReader = new BufferedInputStream(new FileInputStream(file));
			bos = new ByteArrayOutputStream();

			// title_replace2 = new String(title_replace2.getBytes(),
			// OUT_ENCODE);
			// title_replace = new String(title_replace.getBytes(), OUT_ENCODE);
			// replaceStr2 = new String(replaceStr2.getBytes(), OUT_ENCODE);
			// replaceStr = new String(replaceStr.getBytes(), OUT_ENCODE);
			// String iso = "iso8859-1";
			while ((i = (byte) bufferReader.read()) != -1) {
				if (offset == 0) {
					if (i == '\n') {
						bos.write(title_replace2.getBytes(OUT_ENCODE));
						// sb.append(title_replace2);
					} else if (i == this.CIRCUMSCRIPTION) {
						bos.write(title_replace.getBytes(OUT_ENCODE));
						// bos.write(" ".getBytes());
						// sb.append(title_replace);
					} else {
						bos.write(i);
						// sb.append((char)i);
					}
				} else if (offset >= start && offset <= end) {
					if (i == '\n') {
						bos.write(replaceStr2.getBytes(OUT_ENCODE));
						// sb.append(replaceStr2);
					} else if (i == this.CIRCUMSCRIPTION) {
						bos.write(replaceStr.getBytes(OUT_ENCODE));
						// bos.write(" ".getBytes());
						// sb.append(replaceStr);
					} else {
						bos.write(i);
						// sb.append((char)i);
					}
				} else if (offset > end) {
					break;
				}
				if (i == '\n') {
					offset++;
				}
			}
			bos.flush();
			
			str = new String(bos.toString().getBytes(OUT_ENCODE), OUT_ENCODE);
			// str = new String(sb.toString().getBytes("gbk"),OUT_ENCODE);
			// System.out.println("1111111111111111"+new
			// String(sb.toString().getBytes(),OUT_ENCODE));
			// System.out.println("2222222222222"+new
			// String(sb.toString().getBytes("iso8859-1"),OUT_ENCODE));
			// System.out.println("333333333333333"+new
			// String(sb.toString().getBytes("gbk"),OUT_ENCODE));
			// System.out.println("4444444444444444"+new
			// String(sb.toString().getBytes("iso8859-1"),"gbk"));
			// System.out.println("5555555555555555"+new
			// String(sb.toString().getBytes("utf-8"),"gbk"));
			// System.out.println("66666666666666666"+new
			// String(sb.toString().getBytes("utf-8"),"iso8859-1"));
			// System.out.println("8888888888888888"+new
			// String(sb.toString().getBytes("gbk"),"iso8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException("读文件错!『" + e.getMessage() + "』");
		} finally {
			try {
				if (bos != null)
					bos.close();
				bos = null;
			} catch (Exception e) {
			}
			try {
				if (bufferReader != null)
					bufferReader.close();
				bufferReader = null;
			} catch (Exception e) {
			}
		}
	//	str=str.replaceAll("(num)","");
		return str;
	}

	public String genHtml(String fileName, int start, int end,
			String replaceStr, String replaceStr2, String title_replace,
			String title_replace2,int size) throws CMISException {
		String f=fileName;
		int st=start;
		//int ed=end;
		String rst=replaceStr;
		String rst2=replaceStr2;
		String tr=title_replace;
		String tr2=title_replace2;
		int sz=size;
		this.genSum(f,st,sz,rst,rst2,tr,tr2);
		String str = "";
		
		File file = null;
		ByteArrayOutputStream bos = null;
		BufferedReader bufferReader = null;

		int offset = 0;
		//byte i = -1;
		StringBuilder sb = new StringBuilder();

		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");
		URL url = QryGenPageComponent.class.getResource("");
		path = url.getPath();
		path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", "");
		path = path + File.separator + dir;
		// path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		try {
			fileName = path + File.separator + fileName;
			
			file = new File(fileName);
			if (!file.exists()) {
				throw new CMISException("未发现文件名为『" + fileName + "』的文件");
			}
			//File f2 = new File(file.getPath()+"000");  
			//file.renameTo(f2);
			//file.deleteOnExit();
			
			//System.out.println("*****************************"+f2.getPath());
//			String sumAll= genSum(f,st,ed,rst,rst2,tr,tr2);
			bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),OUT_ENCODE));
			
			String line = "" ;
			
			if ((line = bufferReader.readLine()) != null) {
//					line = new String(line.getBytes(), OUT_ENCODE);
					sb.append("<tr class='emp_table_title'>");
					line=line.replaceAll("-num", "");//去掉num的显示
					String[] lines = line.split("\\|");
//						System.out.println("lines=[" + lines + "]");
					for(String s : lines) 
						sb.append("<th nowrap>").append(s.trim().equals("")?"&nbsp;":s).append("</th>");
					sb.append("</tr>");
					offset++;
			}
			
//			String[] a=line.split("\\|");
//			for(int b=0;b<a.length;b++)
//						a[b]="";
//			BigDecimal f;
			String align="center";
			while ((line = bufferReader.readLine()) != null) {
				if (offset >= start && offset <= end) {
					sb.append("<tr>");

					String[] lines = line.split("\\|");
					String s=null;
					for(int x=0;x<lines.length;x++) {
						align="center" ;
						s=lines[x];
						if(s.indexOf("-num")>0){
							s=s.replace("-num","");
							BigDecimal big = new BigDecimal(s);
							DecimalFormat fmt = new DecimalFormat("#########0.00");
							s = fmt.format(big);
							align="right";
						}
						/**如果返回的结果文件中包含 查询结果模板编号，说明有配置内部链接   2014-03-29 唐顺岩*/
						if(s.indexOf("&@&")>0){
							String[] values = s.split("&@&");
							sb.append("<td nowrap style='text-align:"+align+"'><a href='#' onclick=\"javascript:showLink(\'"+values[1].trim()+"\',\'"+values[0].trim()+"\')\">").append(values[0].trim()).append("</a></td>");
						/**END*/
						}else{
							sb.append("<td nowrap style='text-align:"+align+"'>").append(s.trim()).append("</td>");
						}
					}
					sb.append("</tr>");
				} 
				else if(offset>end){
					break;
				}
				offset++;
			}
			
//			for(int y=0;y<a.length;y++)
//			{	String j=a[y];
//				if(j!=null&&j!=""){
//					BigDecimal x=new BigDecimal(j);
//					sb.append("<td nowrap>").append(x).append("</td>");
//				}else {
//					if(y==0)
//						j="合计："+j;
//				sb.append("<td nowrap>").append(j).append("</td>");
//				}
//			}
			sb.append("</tr>");
					
			str = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException("读文件错!『" + e.getMessage() + "』");
		} finally {
			try {
				if (bos != null)
					bos.close();
				bos = null;
			} catch (Exception e) {
			}
			try {
				if (bufferReader != null)
					bufferReader.close();
				bufferReader = null;
			} catch (Exception e) {
			}
		}
		//str=str.replaceAll("-num", "");
		return str;
	}
	
	
	public void genSum(String fileName, int start, int end,
			String replaceStr, String replaceStr2, String title_replace,
			String title_replace2) throws CMISException {
		String str = "";
		boolean isExist=false;
		boolean isCount=true;
		File file = null;
		ByteArrayOutputStream bos = null;
		BufferedReader bufferReader = null;
		OutputStream bufferWriter = null;
		int offset = 0;
		//byte i = -1;
		StringBuilder sb = new StringBuilder();

		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");
		URL url = QryGenPageComponent.class.getResource("");
		path = url.getPath();
		path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", "");
		path = path + File.separator + dir;
			try {
			fileName = path + File.separator + fileName;
			
			file = new File(fileName);
			if (!file.exists()) {
				throw new CMISException("未发现文件名为『" + fileName + "』的文件");
			}
		
			bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),OUT_ENCODE));
			bufferWriter = new BufferedOutputStream(new FileOutputStream(file,true));
			String line = "" ;
			
				if ((line = bufferReader.readLine()) != null) {
//						sb.append("<tr class='emp_table_title'>");
						line=line.replaceAll("-num", "");//去掉num的显示
						//String[] lines = line.split("\\|");
//						for(String s : lines) 
//							sb.append("<th nowrap>").append(s.trim().equals("")?"&nbsp;":s).append("</th>");
//						sb.append("</tr>");
						offset++;
				}
			
			String[] a=line.split("\\|");
			for(int b=0;b<a.length;b++){
				a[b]="";
			}
			BigDecimal f;
			DecimalFormat fmt = new DecimalFormat("#########0.00");
			/* modified by yangzy 2014/11/06 查询分析改造 start */
			String readflag = "no";
			while ((line = bufferReader.readLine()) != null) {
				//sb.append("\\|");
				readflag = "yes";
				String[] lines = line.split("\\|");
				String s=null;
				for(int x=0;x<lines.length;x++) {
					s=lines[x];
					if(s.indexOf("-num")>0){
						s=s.replace("-num","");
						
						if(a[x].equals("")){
							f=new BigDecimal(s);
						}else{
							f=new BigDecimal(s).add(new BigDecimal(a[x]));
						}
						a[x]=fmt.format(f); //合计项，转换成金额类型，保留2为小数  2014-04-18						
					}else if(s.indexOf("总计")>=0){
					  a[x]=s;
//						sb.append("<td nowrap>").append(s).append("</td>");
					}else{
						isCount = false;
					}
				}
//					sb.append("</tr>");
				offset++;
			}
			if("no".equals(readflag)){
				isCount = false;
				str = "无符合条件的记录！";
				
				bufferWriter.write(str.getBytes(OUT_ENCODE));
			}
			/* modified by yangzy 2014/11/06 查询分析改造 end */
			if(isCount){
				for(int y=0;y<a.length;y++){
					String j=a[y];
					if(y==0&&a[y].indexOf("总计")<0){
						j="总计："+j;
					}else if(a[y].indexOf("总计")>=0){
						isExist=true;
					}
					if(null!=j && !"".equals(j) && !"总计：".equals(j)){
						sb.append(j+"-num").append("|");
					}else{
						sb.append(j).append("|");
					}
				}
				str = sb.toString();
				if(bufferReader!=null){
					bufferReader.close();
				}
				if(!isExist){
					bufferWriter.write(str.getBytes(OUT_ENCODE));
				}
				//插入总计到临时文件中。
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException("读文件错!『" + e.getMessage() + "』");
		} finally {
			try {
				if (bos != null)
					bos.close();
				bos = null;
			} catch (Exception e) {
			}
			try {
				if (bufferReader != null)
					bufferReader.close();
				if(bufferWriter !=null)
					bufferWriter.close();
				bufferWriter.close();
				bufferReader .close();
			} catch (Exception e) {
			}
			
			if(bufferReader!=null){
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		return str;
	}
	
	public static void main(String[] args) throws Exception {
		String sql="select  cus_id,cert_code,cert_type,cus_name FROM(select cus_id,cus_name,cert_type,cert_code from cus_base where  (1=1)  and |input_id:$currentUserId$|)";
		String s = "$input_id:$currentUserId$";
		String ss = "input_id in (admin)";
		sql = sql.replace("\u007C"+ s+"\u007C", ss);
		System.out.println(sql);
	}

}
