package com.yucheng.cmis.pub;

public class XmlForExcelUtil {
	public String beginExcel(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <?xml version=\"1.0\"?> \n");
		sb.append(" <?mso-application progid=\"Excel.Sheet\"?> \n");
		sb.append(" <Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" \n");
		sb.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\" \n");
		sb.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" \n");
		sb.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" \n");
		sb.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\"> \n");
		return sb.toString();
	}
	
	public String endExcel(){
		StringBuffer sb = new StringBuffer();
		sb.append(" </Workbook> \n");
		return sb.toString();
	}
	
	public String beginStyles(){
		StringBuffer sb = new StringBuffer();
		sb.append("<Styles> \n");
		return sb.toString();
	}
	
	public String endStyles(){
		StringBuffer sb = new StringBuffer();
		sb.append("</Styles> \n");
		return sb.toString();
	}
	
	
	
	
	public String setStyle_Default(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Style ss:ID=\"Default\" ss:Name=\"Normal\"> \n");
		sb.append("  <Alignment ss:Vertical=\"Center\"/> \n");
		sb.append("  <Borders/> \n");
		sb.append("  <Font ss:FontName=\"����\" x:CharSet=\"134\" ss:Size=\"12\"/> \n");
		sb.append("  <Interior/> \n");
		sb.append("  <NumberFormat/> \n");
		sb.append("  <Protection/> \n");
		sb.append(" </Style> \n");
		return sb.toString();
	}
	
	/**
	 * ���ñ�ͷ��ʽ
	 * @return
	 */
	public String setStyle_head(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Style ss:ID=\"head\"> \n");
		sb.append("  <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\"/>\n");
		sb.append("  <Borders> \n");
		sb.append("   <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("  </Borders> \n");
		sb.append("  <Font ss:FontName=\"����\" x:CharSet=\"134\" ss:Size=\"12\" ss:Bold=\"1\"/> \n");
		sb.append("  <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/> \n");
		sb.append(" </Style> \n");
		return sb.toString();
	}
	
	/**
	 * �����ַ���ʽ
	 * @return
	 */
	public String setStyle_String(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Style ss:ID=\"str\"> \n");
		sb.append("  <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\"/>\n");
		sb.append("  <Borders> \n");
		sb.append("   <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("  </Borders> \n");
		sb.append(" </Style> \n");
		return sb.toString();
	}
	
	/**
	 * ��������������ʽ
	 * @return
	 */
	public String setStyle_Number(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Style ss:ID=\"num\"> \n");
		sb.append("  <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Center\"/>\n");
		sb.append("  <Borders> \n");
		sb.append("   <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("   <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/> \n");
		sb.append("  </Borders> \n");
		sb.append(" </Style> \n");
		return sb.toString();
	}
	
	/**
	 * ��ʼ�������д
	 * @return
	 */
	public String beginWorksheet(String width, int columnCount){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Worksheet ss:Name=\"default\"> \n"); 
		sb.append("  <Table> \n");		
		sb.append("   <Column ss:AutoFitWidth=\"0\" ss:Width=\""+width+"\" ss:Span=\""+columnCount+"\"/>");
		return sb.toString();
	}
	
	/**
	 * ���������д
	 * @return
	 */
	public String endWorksheet(){
		StringBuffer sb = new StringBuffer();
		sb.append("  </Table> \n"); 
		sb.append(" </Worksheet> \n");		
		return sb.toString();
	}
	
	public String beginRow(){
		StringBuffer sb = new StringBuffer();
		sb.append(" <Row ss:AutoFitHeight=\"0\"> \n");
		return sb.toString();
	}
	
	public String endRow(){
		StringBuffer sb = new StringBuffer();
		sb.append(" </Row> \n");
		return sb.toString();
	}
	
	public String writeHead(String value){
		StringBuffer sb = new StringBuffer();
		sb.append("  <Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">");
		sb.append(value);
		sb.append("</Data></Cell> \n");
		return sb.toString();
	}
	
	public String writeStrValue(String value){
		StringBuffer sb = new StringBuffer();
		sb.append("  <Cell ss:StyleID=\"str\"><Data ss:Type=\"String\">");
		sb.append(value);
		sb.append("</Data></Cell> \n");
		return sb.toString();
	}
	
	public String writeNumValue(String value){
		StringBuffer sb = new StringBuffer();
		sb.append("  <Cell ss:StyleID=\"num\"><Data ss:Type=\"Number\">");
		sb.append(value);
		sb.append("</Data></Cell> \n");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		XmlForExcelUtil xml = new XmlForExcelUtil();
		System.out.println(xml.beginExcel());
	}
}
