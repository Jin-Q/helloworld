package com.yucheng.cmis.pub.util;

/**
 * <p>Title:Excel文件的java对象模型</p>
 * <p>Copyright:yucheng Copyright (c) 2008</p>
 * <p>Company: yuchengtech</p>
 * @author ljy
 * @version 1.0
 */
public class ExcelVO {
	public String excelname;//文档名称
	public String urlpath;//文档路径
	public int sheetnum;//工作表数
	public String createtime;//创建日期
	public String edittime;//修改日期
	public long filesize;//文档大小
	public String lastedituser;//最后修改人
	public String RWattribute;//读写权限
	public SheetVO[] sheets;//存放了SheetVO的数组
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("Sheet工作表数:"+sheets.length+"\n");
		CellVO cvo;
		for(int n=0;n<sheets.length;n++){
			sb.append("==================工作表: "+n+"("+sheets[n].sheetname+") ===================\n");
			sb.append("行数:"+sheets[n].rownum+"\n");
			sb.append("列数:"+sheets[n].colnum+"\n");
			for(int i=0;i<sheets[n].rownum;i++){				
				for(int j=0;j<sheets[n].colnum;j++){
					cvo=sheets[n].cells[i][j];
					sb.append(cvo.toString()+"  ");
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
