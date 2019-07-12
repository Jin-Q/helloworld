<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<html>
<head>
<title>我的个性化首页</title>
<link 
	href="<emp:file fileName='scripts/ext/resources/css/ext-all-notheme.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/ext/base.css'/>"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
		baseUrl="<emp:file fileName=''/>";
	</script>
<script type="text/javascript"
	src="<emp:file fileName='scripts/ext/ext-base.js'/>"></script>
<script type="text/javascript"
	src="<emp:file fileName='scripts/ext/ext-all.js'/>"></script>
<script type="text/javascript"
	src="<emp:file fileName='scripts/ext/ext-lang-zh_CN.js'/>"></script>
<script type="text/javascript"
	src="<emp:file fileName='scripts/ext/window-ext.js'/>"></script>
<script type="text/javascript"
	src="<emp:file fileName='scripts/ext/Portal.js'/>"></script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/cmis-util.js'/>"></script>
</head>
<style>
.x-panel{
	background-color: #fff;
}
</style>
<script type="text/javascript">
	Ext.BLANK_IMAGE_URL ="<emp:file fileName='scripts/ext/resources/images/default/s.gif'/>";
	Ext.QuickTips.init();
	var deleteUrl="<emp:url action='moveCustomHomePageGadget.do'/>&operator=delete&gadgetId=";
	var queryUrl = "<emp:url action='queryCustomHomePageGadget.do'/>&operator=query";
	function encodeURL(url) {
		if (url) {
			if(url.indexOf("http://") != -1)
				return url;
			var sample = "<emp:url action='[sample]'/>";
			var newurl = sample.replace("[sample]",url);
			var b = 0;
			var reg = new RegExp("(.*)\\?(.*)\\?(.*)");
			return newurl.replace(reg, "$1?$2&$3");
		}
	};
	var tools = [{
        id:'refresh',
        qtip:'刷新',
        handler: function(e,t,p){
			p.body.mask("<span class='waitCls'>正在加载页面 "
				+ p.title + " 请稍后...</span>");
			var f = Ext.getDom("frame_" + p.id);
			if (f) {
				f.contentWindow.location.reload();
			}
        }
    },{
        id:'maximize',
        qtip:'最大化',
        handler: function(e,t,p){
        	cWinUtils.open(p.maxUrl,p.title,'80%',420,true);
        }
    },{
        id:'close',
        qtip:'删除',
        handler: function(e, target, panel){
    	Ext.MessageBox.confirm('系统提示', '是否确定删除小部件'+panel.title+'?', function(o) {
			if (o == 'yes'){
				Ext.Ajax.request({
					url : deleteUrl+panel.id,
					success : function(response, opts) {
						var json = Ext.decode(response.responseText);
						if(json.flag)
							panel.ownerCt.remove(panel, true);
					},
					failure : function(response, opts) {
					}
				});
				}
		}, this);
    	
            
        }
    }];
	
	Ext.onReady(function(){
		cextUtils.applyTheme(baseUrl);
		var leftItem=new Ext.ux.PortalColumn({
			 columnWidth:.30,
			 border:false,
	        style:'padding:2px'
		});
		var centerItem=new Ext.ux.PortalColumn({
			 columnWidth:.40,
			 border:false,
			 style:'padding:2px'
		});
		var rightItem=new Ext.ux.PortalColumn({
			 columnWidth:.30,
			 border:false,
			 style:'padding:2px'
		});
		var viewport=new Ext.Viewport({
			layout : 'border',
			border:false,
			plain:true,
			cls:'x-panel',
			items : [{
		        region: 'north',
		        html: '<div align="right" style="font-size:12px;color:black;padding:2 20 2 1;"><a href="#" onclick="doAddGadget()">添加工具集</a></div>',
		        height: 18,
		        plain:true,
		        border: false
		    },{region: 'center', border:false,xtype:"portal",items:[leftItem,centerItem,rightItem]}]
		});
		var layout=function(panelArray){
			Ext.each(panelArray,function(attrs){
				var gadgetAttr =  attrs.split(",");
				if(gadgetAttr.length != 7) return;
				var id = gadgetAttr[0];//ID
				var title = gadgetAttr[1];//标题
				var color = gadgetAttr[2];//颜色
				var height =new Number(gadgetAttr[3].replace('px','').toString());//高度
				var width = gadgetAttr[4];//宽度
				var url = encodeURL(gadgetAttr[5]); //iframe的URL
				var url_resize =encodeURL(gadgetAttr[6]); //最大化时URL
				var html = '<iframe id="frame_'+id+'" src="'
					+ url
					+ '" frameborder="0" style="padding:3px" scrolling="Auto"  width="100%" height="100%"></iframe>';
				var panel=new Ext.ux.Portlet({
					id:id,
					title:title,
					height:height.valueOf(),
					maxUrl:url_resize,
					html:html,
				//	margins:'5 0 5 5',
					autoScroll:true,
		            tools: tools
				});
				panel.on('afterrender', function(p) {
					var t = Ext.getDom("frame_" + p.id);
					if (t) {
						p.body.mask("<span class='waitCls'>正在加载页面 "
								+ p.title + " 请稍后...</span>");
						if (Ext.isIE) {
							t.onreadystatechange = function() {
								if (t.readyState
										&& t.readyState == 'complete')
									p.body.unmask();
							}
						} else {
							t.onload = function() {
								p.body.unmask();
							}
						}
					}
				});
				var l=leftItem.items.length;
				var c=centerItem.items.length;
				var r=rightItem.items.length;
				if(c<=l&&c<=r){
					centerItem.add(panel);
				} else if(l<=c&&l<=r){
					leftItem.add(panel);
				}else if(r<=l&&r<=c){
					rightItem.add(panel);
				}
			},this);
			viewport.doLayout();
			
		}
		Ext.Ajax.request({
			url : queryUrl,
			success : function(response, opts) {
				var json = Ext.decode(response.responseText);
				var panelArray=json.gadgetStr.split(";")
				layout.call(this,panelArray);
			},
			failure : function(response, opts) {
			}
		});
		
						
	});
	/**
	 * 添加工具
	 */
	function doAddGadget(){
		var url = "<emp:url action='queryHomePageGadget.do'/>&operator=queryByUser"
		window.location = url;
	}
	</script>
<body>

</body>
</html>
