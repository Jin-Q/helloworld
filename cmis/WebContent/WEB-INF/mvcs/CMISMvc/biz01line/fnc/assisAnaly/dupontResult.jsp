<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<script>
/*
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
    num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
    cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
    num = num.substring(0,num.length-(4*i+3))+','+
    num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + num + '.' + cents);
}
</script>
<style type="text/css">
td,p,li,select,input,textarea {font-size:12px}
A IMG {border:0}
.img01{border:1px solid #999999}

.f14 {font-size:14px}
.f12 {font-size:12px}

.l15{line-height:150%}
.l13{line-height:130%}
.lh19{line-height:19px;}

A:link {color:#0015EA}
A:hover {color:#ff0000;}

A.sinatail:link,A.sinatail:visited {text-decoration:underline;color:#0000ff;font-size:12px}
A.sinatail:active,A.sinatail:hover {text-decoration:underline;color:#ff0000;font-size:12px}

A.title1:link,A.title1:visited,A.title1:active,A.title1:hover {FONT-WEIGHT: bold; FONT-SIZE: 17px; COLOR: #000000; FONT-FAMILY: arial; TEXT-DECORATION: none;}
A.title2:link,A.title2:visited {COLOR: #000000; TEXT-DECORATION: none;}
A.title2:active,A.title2:hover {COLOR: #ff0000; TEXT-DECORATION: none;}

.aWhite A:link,.aWhite A:visited {text-decoration:none;color:#ffffff}
.aWhite A:active,.aWhite A:hover {text-decoration:underline;color:#CC0000}
.aWhiteY A:link,.aWhiteY A:visited {text-decoration:none;color:#ffffff}
.aWhiteY A:active,.aWhiteY A:hover {text-decoration:underline;color:#ffff00}
.aDBlue A:link,.aDBlue A:visited {text-decoration:underline;color:#0B457E}
.aDBlue A:active,.aDBlue A:hover {text-decoration:underline;color:#CC0000}
.aBlack A:link,.aBlack A:visited {text-decoration:none;color:#000000}
.aBlack A:active,.aBlack A:hover {text-decoration:underline;color:#CC0000}
.aRed A:link,.aRed A:visited {text-decoration:none;color:#ff0000}
.aRed A:active,.aRed A:hover {text-decoration:underline;color:#CC0000}

.White{color:#ffffff;}
.Red{color:#ff0000;}
.DRed{color:#B60101;}
.Green{color:#009900;}


.pdTop3{padding-top:3px;}
.pdLeft5{padding-left:5px;padding-top:2px;height:20px;}
.pdLeft5b{padding-left:5px;padding-top:2px;}
.curL{padding-left:5px;padding-top:2px;background-color: #007CC8;height:21px;color:#ffffff;font-weight:bold;}
.curR{padding-left:5px;padding-top:2px;border:1px solid #007CC8;height:21px;color:#009C24;font-weight:bold;}

.un a:link,.noUn a:visited,.noUn a:active,.noUn a:hover{text-decoration:underline;}
.noUn a:link,.noUn a:visited,.noUn a:active,.noUn a:hover{text-decoration:none;}
.width77{width:77px;}
.pdTop2{padding-top:2px;}

.bd01{border-top:1px solid #9CCEFE;border-left:1px solid #9CCEFE;border-right:1px solid #11406F;border-bottom:1px solid #11406F;cursor:hand;padding-left:10px;}
.bd02{background-color: #ffffff;border:1px solid #99CCFF;padding-top:2px;padding-left:12px;font-size:14px;}
.pdSub{padding:4 3 4 12;line-height:20px;}

</style>

<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kcoll = (KeyedCollection)context.getDataElement("DupontAnaly");	
%>

<table width='630' border='0' cellpadding='0' cellspacing='0' style="border:1 solid #013164;border-top:1 solid #335982" bgcolor="#EEF1F4">
<tr>
<td>

<table border='0' cellpadding='0' cellspacing='12' bgcolor="#FFFFFF">
<tr>			
<td class='l15'>

<table width="100%" border="0" align="center">
<tr> 
<td> 
<B><font size='4'> 杜邦分析</font></B><br><br>
<table width="100%" border="0" cellspacing="0">
	<tr>
		<td width="226">&nbsp;</td>
		<td width="100" align="center">
		<table width="100%" border="0" cellspacing="1" cellpadding="3">
			<tr bgcolor="6699CC" align="center">
				<td height="15"><font color="#FFFFFF">净资产收益率</font></td>
			</tr>
			<tr>
				<td bgcolor="ededed" align="center" height="15"><script language="javascript">
				document.write(formatCurrency('<%=kcoll.getDataValue("净资产收益率")%>'))</script></td>
			</tr>
		</table>
		</td>
		<td class="f18" align="center">&nbsp;</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="208" height="28">&nbsp;</td>
		<td width="70" class="bottom_line"
			style="border-right: 1px solid #999999">&nbsp;</td>
		<td width="62" class="bottom_line">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0">
<tr> 
<td width="156" align="center">&nbsp; </td>
<td width="100" align="center"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">总资产收益率</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">document.write(formatCurrency("<%=kcoll.getDataValue("总资产收益率")%>"))</script></td>
</tr>
</table>
</td>
<td class="f18" width="30" align="center">X</td>
<td width="100" align="center"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">权益乘数</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15">1/(1-<script  language="javascript">document.write(formatCurrency("<%=kcoll.getDataValue("权益乘数")%>"))</script>)</td>
</tr>
</table>
</td>
<td class="f18" valign="top"> 
<table width="100%" border="0" height="100%" cellspacing="0" style="border:1px dotted #999999" cellpadding="0">
<tr> 
<td style="padding-top:3px;padding-left:3px"><font color="#666666">=资产总额/股东权益<br>
=1/（1-资产负债率）<br>
=1/（1-负债总额/资产总额）x100％</font></td>
</tr>
</table>
</td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
<td width="168" height="28">&nbsp;</td>
<td width="40" class="bottom_line" style="border-right:1px solid #999999">&nbsp;</td>
<td width="213" class="bottom_line">&nbsp;</td>
<td>&nbsp;</td>
</tr>
</table>

<table width="100%" border="0" cellspacing="0">
<tr align="center"> 
<td width="117">&nbsp; </td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">主营业务利润率</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script language = "javascript">document.write(formatCurrency("<%=kcoll.getDataValue("主营业务利润率")%>"))</script></td>
</tr>
</table>
</td>
<td class="f18" width="149">X</td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">总资产周转率</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script language = "javascript">document.write(formatCurrency("<%=kcoll.getDataValue("总资产周转率")%>"))</script></td>
</tr>
</table>
</td>
<td class="f18">&nbsp;</td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
<td width="109" height="28">&nbsp;</td>
<td width="60" class="bottom_line" style="border-right:1px solid #999999">&nbsp;</td>
<td width="62" class="bottom_line">&nbsp;</td>
<td width="124">&nbsp;</td>
<td width="66" class="bottom_line" style="border-right:1px solid #999999">&nbsp;</td>
<td width="58" class="bottom_line">&nbsp;</td>
<td>&nbsp;</td>
</tr>
</table>

<table width="100%" border="0" cellspacing="0">
<tr align="center"> 
<td width="57">&nbsp; </td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">净利润</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script language = "javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("净利润")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">/</td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">主营业务收入</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("主营业务收入")%>'))</script></td>
</tr>
</table>
</td>
<td width="20" class="f18">&nbsp;</td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">主营业务收入</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("主营业务收入")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">/</td>
<td width="100"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr bgcolor="6699CC" align="center"> 
<td height="15"><font color="#FFFFFF">资产总额</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("资产总计")%>'))</script></td>
</tr>
</table>
</td>
<td width="64">&nbsp;</td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
<td width="45" height="28">&nbsp;</td>
<td width="65" class="bottom_line" style="border-right:1px solid #999999">&nbsp;</td>
<td width="241" class="bottom_line">&nbsp;</td>
<td width="98">&nbsp;</td>
<td width="29" class="bottom_line" style="border-right:1px solid #999999">&nbsp;</td>
<td width="73" class="bottom_line">&nbsp;</td>
<td>&nbsp;</td>
</tr>
</table>

<table width="100%" border="0" cellspacing="0">
<tr align="center"> 
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">主营业务收入</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("主营业务收入")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">-</td>
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">全部成本</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("全部成本")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">+</td>
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">其他利润</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("其他业务利润")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">-</td>
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">所得税</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("所得税")%>'))</script></td>
</tr>
</table>
</td>
<td>&nbsp;</td>
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">流动资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("流动资产合计")%>'))</script></td>
</tr>
</table>
</td>
<td class="f18">+</td>
<td width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">长期资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("长期资产")%>'))</script></td>
</tr>
</table>
</td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
<td width="44">&nbsp;</td>
<td valign="top"> 
<table width="100" border="0" cellspacing="0" cellpadding="0" height="230">
<tr> 
<td width="100" height="30">&nbsp;</td>
<td width="17">&nbsp;</td>
<td rowspan="5" valign="top" width="50"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="205">
<tr> 
<td style="border-left:1px solid #999999">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">主营业务成本</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("主营业务成本")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">营业费用</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("营业费用")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">管理费用</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("管理费用")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">财务费用</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("财务费用")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
</table>
</td>
<td width="115"> 
<table width="100" border="0" cellspacing="0" cellpadding="0" height="230">
<tr> 
<td width="100" height="30">&nbsp;</td>
<td width="17">&nbsp;</td>
<td rowspan="6" valign="top"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="255">
<tr> 
<td style="border-left:1px solid #999999">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">货币资金</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("货币资金")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">短期投资</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("短期投资")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">应收账款</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("应收账款")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">存货</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("存货")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">其他流动资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("其他流动资产")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="../../../images/arrow2.gif" width="17" height="7"></td>
</tr>
</table>
</td>
<td width="130" valign="top"> 
<table width="100" border="0" cellspacing="0" cellpadding="0" height="230">
<tr> 
<td width="100" height="30">&nbsp;</td>
<td width="17">&nbsp;</td>
<td rowspan="5" valign="top" width="50"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="205">
<tr> 
<td style="border-left:1px solid #999999">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr> 
<td align="right" height="50" width="90"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">长期投资</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("长期投资")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="http://image2.sina.com.cn/cj/stock/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">固定资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("固定资产")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="http://image2.sina.com.cn/cj/stock/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">无形资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("无形资产")%>'))</script></td>
</tr>
</table>
</td>
<td><img src="http://image2.sina.com.cn/cj/stock/arrow2.gif" width="17" height="7"></td>
</tr>
<tr> 
<td align="right" height="50"> 
<table width="100%" border="0" cellspacing="1" cellpadding="3">
<tr align="center"> 
<td height="15" bgcolor="6699CC"><font color="#FFFFFF">其他长期资产</font></td>
</tr>
<tr> 
<td bgcolor="ededed" align="center" height="15"><script  language="javascript">
	document.write(formatCurrency('<%=kcoll.getDataValue("其他长期资产")%>'))</script></td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>

</td>
</tr>
</table>

	<br><br>
</td>
</tr>
<tr>
	<td height='5' bgcolor='#B4CEE7'>
	</td>
</tr>
</table>