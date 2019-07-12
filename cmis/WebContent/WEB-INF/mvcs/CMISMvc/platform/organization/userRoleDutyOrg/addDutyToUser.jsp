<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="java.util.*"%>
<emp:page>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	IndexedCollection dutyusericoll = (IndexedCollection) context.getDataElement("SDutyuserList");
	IndexedCollection dutyicoll = (IndexedCollection) context.getDataElement("SDutyList");

	ArrayList dutySelected = new ArrayList();
	ArrayList dutySelectedList = new ArrayList();
	ArrayList dutyList = new ArrayList();

	for (int i = 0; i < dutyusericoll.size(); i++) {
		KeyedCollection dutyuserkcoll = (KeyedCollection) dutyusericoll
		.get(i);
		String dutyno = (String) dutyuserkcoll.getDataValue("dutyno");
		dutySelected.add(dutyno);

	}

	for (int i = 0; i < dutyicoll.size(); i++) {
		boolean isSelect = false;
		KeyedCollection dutykcoll = (KeyedCollection) dutyicoll.get(i);
		String dutynoList = (String) dutykcoll.getDataValue("dutyno");
		for (int j = 0; j < dutySelected.size(); j++) {
			if (dutynoList.equals((String) dutySelected.get(j))) {
		isSelect = true;
		dutySelected.remove(j);
		dutySelectedList.add(dutykcoll);
		break;
			}
		}
		if (!isSelect) {
			dutyList.add(dutykcoll);
		}
	}
	for (int i = 0; i < dutyList.size(); i++) {
		KeyedCollection a = (KeyedCollection) dutyList.get(i);
	}
	for (int i = 0; i < dutySelectedList.size(); i++) {
		KeyedCollection a = (KeyedCollection) dutySelectedList.get(i);
	}
%>
<html>
<head>
<link href="<emp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<script language="Javascript">

var selectedOld = new Array();
var unselectedOld = new Array();
var selectedNew =new Array();

function preparedata()
{
//prepare data for select[dutyno]
<%for(int i=0;i<dutySelectedList.size();i++ )
{
%>
var dutynoOption<%=i%>=new Option();
dutynoOption<%=i%>.value="<%=(String)((KeyedCollection)dutySelectedList.get(i)).getDataValue("dutyno")%>";

dutynoOption<%=i%>.text="<%=(String)((KeyedCollection)dutySelectedList.get(i)).getDataValue("dutyname")%>";
document.form1.dutyno.options[<%=i%>]=dutynoOption<%=i%>;
selectedOld[<%=i%>]="<%=(String)((KeyedCollection)dutySelectedList.get(i)).getDataValue("dutyno")%>";
<%}%>


//prepaer data for select[]
<%for(int i=0;i<dutyList.size();i++ )
{
%>
var unselectOption<%=i%>=new Option();
unselectOption<%=i%>.value="<%=(String)((KeyedCollection)dutyList.get(i)).getDataValue("dutyno")%>";

unselectOption<%=i%>.text="<%=(String)((KeyedCollection)dutyList.get(i)).getDataValue("dutyname")%>";
document.form1.unselectrole.options[<%=i%>]=unselectOption<%=i%>;
unselectedOld[<%=i%>]="<%=(String)((KeyedCollection)dutyList.get(i)).getDataValue("dutyno")%>";
<%}%>

};

function move(side)
{
	var temp1 = new Array();
	var temp2 = new Array();
	var tempa = new Array();
	var tempb = new Array();
	var current1 = 0;
	var current2 = 0;
	var y=0;
	var attribute;

	//assign what select attribute treat as attribute1 and attribute2
	if (side == "in")
	{
		attribute1 = document.form1.dutyno;
		attribute2 = document.form1.unselectrole;
	}
	else
	{
		attribute1 = document.form1.unselectrole;
		attribute2 = document.form1.dutyno;
	}

	//fill an array with old values
	for (var i = 0; i < attribute2.length; i++)
	{
		y=current1++
		temp1[y] = attribute2.options[i].value;
		tempa[y] = attribute2.options[i].text;
	}

	//assign new values to arrays
	for (var i = 0; i < attribute1.length; i++)
	{
		if ( attribute1.options[i].selected )
		{
			y=current1++
			temp1[y] = attribute1.options[i].value;
			tempa[y] = attribute1.options[i].text;
		}
		else
		{
			y=current2++
			temp2[y] = attribute1.options[i].value;
			tempb[y] = attribute1.options[i].text;
		}
	}

	//generating new options
	for (var i = 0; i < temp1.length; i++)
	{
		attribute2.options[i] = new Option();
		attribute2.options[i].value = temp1[i];
		attribute2.options[i].text =  tempa[i];
	}

	//generating new options
	for (var i=temp2.length; i<attribute1.lenght; i++)
	{
		attribute1.options[i] = null;
	}
	attribute1.length = temp2.length;
	if (temp2.length>0)
	{
		for (var i = 0; i < temp2.length; i++)
		{
			attribute1.options[i] = new Option();
			attribute1.options[i].value = temp2[i];
			attribute1.options[i].text =  tempb[i];
		}
	}
	dutyno._setValue("");
	state._setValue("");
}
function see()
{
	dutyno._setValue("");
	state._setValue("");
	var attribute=document.form1.dutyno.options;
	for(var i=0; i<unselectedOld.length;i++)
	{	
		for(var j=0;j<attribute.length;j++)
			{
				dutynonew=attribute[j].value;
				
				if(dutynonew!=""&&dutynonew!=null)
				{
						if(unselectedOld[i]==dutynonew)
						{
							if(dutyno._getValue()=="")
							{
								dutyno._setValue(dutynonew);
								state._setValue("true");
							}else{
								dutyno._setValue(dutyno._getValue()+","+dutynonew);
								state._setValue(state._getValue()+",true");
							}
						
						}
					
				}
			}
	}

	attribute=document.form1.unselectrole.options;
	for(var i=0; i<selectedOld.length;i++)
	{	
		for(var j=0;j<attribute.length;j++)
			{
				dutynonew=attribute[j].value;
				
				if(dutynonew!=""&&dutynonew!=null)
				{
						if(selectedOld[i]==dutynonew)
						{
							if(dutyno._getValue()=="")
							{
								dutyno._setValue(dutynonew);
								state._setValue("false");
							}else{
								dutyno._setValue(dutyno._getValue()+","+dutynonew);
								state._setValue(state._getValue()+",false");
							}
						}
					
				}
			}
	}


	if(dutyno._getValue()!="" &&state._getValue()!="")
	{
		dutyno._toForm(form2);
		state._toForm(form2);
		actorno._toForm(form2);
		form2.submit();
	}else{
		alert("未作任何修改");
	}

}
function returnQueryList()
{
		//var url = '<emp:url action="querySUserList.do"/>&menuId=thgl';
		//window.location = url;
	window.close();
}
</script>
<jsp:include page="/include.jsp" flush="true"/>
</head>
<body onload="preparedata()">

<form name="form1" isLayoutContent="false" action="addroletouser" method="POST">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="45%" align="center" valign="top">
			<div align="left"></div>
			<fieldset><legend align="center"><span class="titletext">已授岗位</span></legend>
				<table width="89%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><span class="tdtext"><span class="titletext">岗位名称:</span></span></td>
					</tr>
					<tr>
						<td align="center"><br>
							<select name="dutyno" multiple size="12" style="width:250"
								onDblclick="move('in');">
							</select>
							<br>
							<br>
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
		<td width="10%" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center">
						<span class="lightgrayinput"><span class="even">
							<button name="save2" onclick="move('in');">>>></button>
						</span></span>
						<br>
						<span class="lightgrayinput"><span class="even">
							<button name="save22" onclick="move('out');"><<<</button>
						</span></span>
					</td>
				</tr>
			</table>
		</td>
		<td width="45%" align="center" valign="top">
			<div align="left"></div>
			<fieldset><legend align="center"><span class="titletext">可授岗位</span></legend>
				<table width="89%" border="0" cellspacing="0" cellpadding="0">
					<tr class="tdtext">
						<td align="right">&nbsp;</td>
					</tr>
					<tr class="tdtext">
						<td align="right"><br>
							<select name="unselectrole" multiple size="12" style="width:250" 
								onDblclick="move('out');">
							</select>
							<br>
							<br>
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
<p align="center">
<button name="seee" onclick="see();">提交</button>
&nbsp;
<button name="return" onclick="returnQueryList();">返回</button>

</p>

</form>
<emp:form id="form2" name="form2" action="addDutyToUser.do" method="POST">
<emp:text id="state" label="state" hidden="true" />
<emp:text id="dutyno" label="dutyno" hidden="true" />
<emp:text id="actorno" label="actorno" hidden="true" />
</emp:form>
</body>
</html>
</emp:page>