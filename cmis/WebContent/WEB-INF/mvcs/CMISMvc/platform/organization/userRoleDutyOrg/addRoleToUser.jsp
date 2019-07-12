<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="java.util.*"%>
<emp:page>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);

IndexedCollection roleusericoll=(IndexedCollection)context.getDataElement("SRoleuserList");
IndexedCollection roleicoll=(IndexedCollection)context.getDataElement("SRoleList");
ArrayList roleSelected=new ArrayList();
ArrayList roleSelectedList=new ArrayList();
ArrayList roleList=new ArrayList();

for(int i=0;i<roleusericoll.size();i++)
{
	KeyedCollection roleuserkcoll=(KeyedCollection)roleusericoll.get(i);
	String roleno=(String)roleuserkcoll.getDataValue("roleno");
	roleSelected.add(roleno);
	
}

for(int i=0;i<roleicoll.size();i++ )
{
	boolean isSelect=false;
	KeyedCollection rolekcoll=(KeyedCollection)roleicoll.get(i);
	String rolenoList=(String)rolekcoll.getDataValue("roleno");
	for(int j=0;j<roleSelected.size();j++)
	{
		if(rolenoList.equals((String)roleSelected.get(j)))
		{
			isSelect=true;
			roleSelected.remove(j);
			roleSelectedList.add(rolekcoll);
			break;
		}
	}
	if (!isSelect)
	{
		roleList.add(rolekcoll);
	}
}
for(int i=0;i<roleList.size();i++)
{
	KeyedCollection a =(KeyedCollection)roleList.get(i);
}
for(int i=0;i<roleSelectedList.size();i++)
{
	KeyedCollection a =(KeyedCollection)roleSelectedList.get(i);
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
//prepare data for select[roleno]
<%for(int i=0;i<roleSelectedList.size();i++ )
{
%>var rolenoOption<%=i%>=new Option();
rolenoOption<%=i%>.value="<%=(String)((KeyedCollection)roleSelectedList.get(i)).getDataValue("roleno")%>";

rolenoOption<%=i%>.text="<%=(String)((KeyedCollection)roleSelectedList.get(i)).getDataValue("rolename")%>";
document.form1.roleno.options[<%=i%>]=rolenoOption<%=i%>;
selectedOld[<%=i%>]="<%=(String)((KeyedCollection)roleSelectedList.get(i)).getDataValue("roleno")%>";
<%}%>


//prepaer data for select[]
<%for(int i=0;i<roleList.size();i++ )
{
%>var unselectOption<%=i%>=new Option();
unselectOption<%=i%>.value="<%=(String)((KeyedCollection)roleList.get(i)).getDataValue("roleno")%>";

unselectOption<%=i%>.text="<%=(String)((KeyedCollection)roleList.get(i)).getDataValue("rolename")%>";
document.form1.unselectrole.options[<%=i%>]=unselectOption<%=i%>;
unselectedOld[<%=i%>]="<%=(String)((KeyedCollection)roleList.get(i)).getDataValue("roleno")%>";
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
		attribute1 = document.form1.roleno;
		attribute2 = document.form1.unselectrole;
	}
	else
	{
		attribute1 = document.form1.unselectrole;
		attribute2 = document.form1.roleno;
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
	for (var i=temp2.length; i<attribute1.length; i++)
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
	//document.form2.roleno.value="";
	//document.form2.state.value="";
	roleno._setValue("");
	state._setValue("");
}
function see()
{
	//document.form2.roleno.value="";
	//document.form2.state.value="";
	roleno._setValue("");
	state._setValue("");
	var attribute=document.form1.roleno.options;
	for(var i=0; i<unselectedOld.length;i++)
	{	
		for(var j=0;j<attribute.length;j++)
			{
				rolenonew=attribute[j].value;
				if(rolenonew!=""&&rolenonew!=null)
				{
						if(unselectedOld[i]==rolenonew)
						{						
							if(roleno._getValue()=="")
							{
								roleno._setValue(rolenonew);
								state._setValue("true");
							}else{
								roleno._setValue(roleno._getValue()+","+rolenonew);
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
				rolenonew=attribute[j].value;
				if(rolenonew!=""&&rolenonew!=null)
				{
						if(selectedOld[i]==rolenonew)
						{
							if(roleno._getValue()=="")
							{
								roleno._setValue(rolenonew);
								state._setValue("false");
							}else{
								roleno._setValue(roleno._getValue()+","+rolenonew);
								state._setValue(state._getValue()+",false");
							}
						}
					
				}
			}
	}
	if(roleno._getValue()!="" &&state._getValue()!="")
	{
		roleno._toForm(form2);
		state._toForm(form2);
		actorno._toForm(form2);
		form2.submit();
	}else{
		alert("未作任何修改");
	}
}
function returnQueryList()
{
		//var url = '<emp:url action="querySUserList.do"/>';
		//url+='&menuId=thgl';
		//window.location = url;
	window.close();
}
</script>
<jsp:include page="/include.jsp" flush="true"/>
</head>
<body onload="preparedata()">

<form name="form1" action="addroletouser.do" method="POST">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="45%" align="center" valign="top">
				<div align="left">				</div>
				<fieldset><legend align="center"><span class="titletext">已授角色</span></legend>
				<table width="89%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><span class="tdtext"><span class="titletext">角色名称:</span></span></td>
                  </tr>
                  <tr>
                    <td align="center"><br>
                      <select name="roleno" multiple size="12" style="width:250"  onDblclick="move('in');">


</select>
                      <br>
                      <br></td></tr>
                </table>
				</fieldset>
				</td>
                <td width="10%" align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="center">
					<span class="lightgrayinput"><span class="even">
					<button name="save2" onclick="move('in');">>>></button>
					</span></span>                      <br>
                        <span class="lightgrayinput"><span class="even">
                        <button name="save22"  onclick="move('out');"><<<</button>
                        </span></span></td></tr>
                </table></td>
                <td width="45%" align="center" valign="top">
				<div align="left">				</div>
                <fieldset><legend align="center"><span class="titletext">可授角色</span></legend>
                    <table width="89%" border="0" cellspacing="0" cellpadding="0">
                          <tr class="tdtext">
                            <td  align="right">&nbsp;</td>
                          </tr>
                          <tr class="tdtext">
                            <td  align="right"><br>
			<select name="unselectrole" multiple size="12" style="width:250" onDblclick="move('out');">

			
			</select>
			<br><br></td>
                          </tr>
                    </table>
                  </fieldset></td>
              </tr>
          </table>
<p align="center">
<button name="seee" onclick="see();">提交</button>&nbsp;
<button name="return" onclick="returnQueryList();">返回</button>
</p>
</form>
<emp:form id="form2" name="form2" action="addRoleToUser.do" method="POST"> 
<emp:text id="state" label="state" hidden="true" />
<emp:text id="roleno" label="roleno" hidden="true" />
<emp:text id="actorno" label="actorno" hidden="true" />
</emp:form>
</body>
</html>
</emp:page>