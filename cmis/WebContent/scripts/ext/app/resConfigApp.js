/**
 * 菜单、权限配置
 */
Ext.onReady(function() {
			Ext.QuickTips.init();
			cextUtils.applyTheme(baseUrl);
			layout();
		});
function layout() {
	
	  var bizlinedata =new Ext.data.Store(  {
	      proxy: new Ext.data.HttpProxy({url:bizLinecfgUrl}),
	      reader: new Ext.data.ArrayReader({},[{name:'value'},{name:'text'}])
	   });
	  //载入
	  bizlinedata.load(); 

	
	var selectLine = new Ext.form.ComboBox({
		    region:'center',
		    fieldLabel: '业务条线',
			store: bizlinedata,
			mode: 'local',
			width: 200,
			valueField: 'value',
			displayField: 'text',
			triggerAction: 'all'
	     }
		);
	
	selectLine.on('select',function(comboBox){
		/**		
		   对于超级管理员，可以配置所有条线的菜单及其权限
		   对于条线管理员，则只能配置本条线的菜单及其权限
		   
		   一个菜单，可属于一个或多个条线，菜单没有定义所属条线时，则属于所有条线
		   对于非所有条线的菜单  一次只能配置 一个条线的权限
		*/
		roleTree.getLoader().baseParams['bizLine'] = comboBox.getValue();
		roleTree.getRootNode().reload();
		
		resTree.getLoader().baseParams['bizLine'] = comboBox.getValue();
		resTree.getRootNode().reload();
		
	});
	
	var bannerForm = new Ext.form.FormPanel({
		height : 25,
		region:'north',
		labelAlign:'right',
		items:[selectLine]
	});
	
	/*var*/ roleTree = new RTree({
				region : 'west',
				collapsible : true,
				title : '角色列表',
				width : 300,
				split : true,
				buttons:[new Ext.Button({
					text:'新增',
					handler:function(){
						//EMPTools.openWindow(addRoleUrl,'newwindow');
					  
					   openWinExt(addRoleUrl);
					    
					}
				}),new Ext.Button({
					text:'修改',
					handler:function(){
					  updateRoleHandler();
					}
				}),new Ext.Button({
					text:'删除',
					handler:function(){
						deleteRoleHandler();
					}
				})],
				listeners: {
		            'checkchange': function(node, checked) {
		              if(resTree.curNode != null && resTree.curNode != '--'){
						actionTree.getLoader().baseParams['resId'] = resTree.curNode; //NodeId
						
						var resnode = resTree.getNodeById(resTree.curNode);
						actionTree.setTitle('‘'+resnode.text + '’操作列表');
						actionTree.getRootNode().reload();
						
						rightTree.getLoader().baseParams['resId'] = resTree.curNode;
						rightTree.getLoader().baseParams['roleId'] = roleTree.getChecked('id').toString();
						rightTree.setTitle('‘'+resnode.text + '’权限列表');
						rightTree.getRootNode().reload();
		              }
                    }
	            },
				xtype : 'rtree'
			});
	
	/*var*/ actionTree = new ATree({
		region : 'center',
		title : '操作列表',
		width : 300,
		height : 200,
		curNode: '--',
		listeners : {
			'click' : function(node){
		       actionTree.curNode = node.id;
	        },
			scope : this
		},
		buttons : [{
					text : '新增',
					handler : function() {
						addActionHandler();
					},
					scope : this},
					{
					text : '修改',
					handler : function() {
					updateActionHandler();
					},
					scope : this},
					{
					text : '删除',
					handler : function() {
					deleteActionHandler();
					},
					scope : this
					}
		          ],
		xtype : 'atree'
	});
	//修改角色
	var updateRoleHandler = function(){
		var root= roleTree.getRootNode().item(0);
		if(!root.hasChildNodes()){
			alert("不存在角色！");
			return;
		}else{
			var count=0;
			var subNode=null;
			var roleId="";
			var i=0;
			do{
				subNode=root.item(i++);
				if(subNode.attributes.checked==true){
					count++;
					roleId+=subNode.attributes.id;
				}
			}while(!subNode.isLast());
			if(count<1){
				alert("请选择角色！");
				return;
			}else if(count>1){
				alert("只能选择一个角色！");
				return;
			}else{
				var url=updateRoleUrl+"&roleno="+roleId;
				 openWinExt(url);
				//EMPTools.openWindow(url,'newwindow');
				//window.location=updateRoleUrl+"&roleno="+roleId;
			}
		}
	}
	//删除角色
	var deleteRoleHandler = function(){
		var root= roleTree.getRootNode().item(0);
		if(!root.hasChildNodes()){
			alert("不存在角色！");
			return;
		}else{
			var count=0;
			var subNode=null;
			var roleIds="";
			var i=0;
			do{
				subNode=root.item(i++);
				if(subNode.attributes.checked==true){
					count++;
					roleIds+=subNode.attributes.id+",";
				}
			}while(!subNode.isLast());
			if(count<1){
				alert("请选择角色！");
				return;
			}else{
				if(confirm("是否确认要删除角色以及角色权限、角色用户？")){
					roleIds=roleIds.substring(0,roleIds.length-1);
					var url=deleteRoleUrl+"&roleNos="+roleIds;
					Ext.Ajax.request({
						url : url,
						success : function(response, opts) {
						    try{
						    	var json = response.responseText;
						    	if(json == '0'){
						    		//resTree.getLoader().baseParams['bizLine'] = comboBox.getValue();
						    		roleTree.getRootNode().reload();
						    	}else{
						    		alert("删除失败!");
						    	}
						    }catch(e){
						    	alert(response.responseText);
						    	return;
						    }
						},
						failure : function(response, opts) {
						}
					});
					//window.location=deleteRoleUrl+"&roleNos="+roleIds;
				}
			}
		}
	}
	var addActionHandler = function(){
		
		if(resTree.curNode == '--'){
			alert("请先选择需要增加操作的资源！");
			return;
		}
		var url = EMPTools.encodeURI(addActionUrl+'&s_resourceaction.resourceid='+resTree.curNode);
	    //EMPTools.openWindow(url,'newwindow'); 
		openWinExt(url);
	}
	
	var updateActionHandler = function(){
		if(resTree.curNode == '--'){
			alert("请先选择需要操作的资源！");
			return;
		}
		
		if(actionTree.curNode == '--'){
			alert("请先选择需要修改的操作!");
			return;
		}
		var url = upActionUrl+"&resourceid="+resTree.curNode+'&actid='+actionTree.curNode;
		url = EMPTools.encodeURI(url);
		//EMPTools.openWindow(url,'newwindow');
		openWinExt(url);
		//alert(actionTree.curNode);
	}
	var deleteActionHandler = function(){
		if(resTree.curNode == '--'){
			alert("请先选择需要操作的资源！");
			return;
		}
		
		if(actionTree.curNode == '--'){
			alert("请先选择需要修改的操作!");
			return;
		}
		var url = delActionUrl+"&resourceid="+resTree.curNode+'&actid='+actionTree.curNode;
		url = EMPTools.encodeURI(url);
		Ext.Ajax.request({
			url : url,
			success : function(response, opts) {
			    try{
			    	var json = response.responseText;
			    	if(json == '0'){
			    		//resTree.getLoader().baseParams['bizLine'] = comboBox.getValue();
			    		actionTree.getRootNode().reload();
			    		actionTree.curNode = '--';
			    		rightTree.getRootNode().reload();
			    		rightTree.curNode = '--';
			    	}else{
			    		alert("删除失败!");
			    	}
			    }catch(e){
			    	alert(response.responseText);
			    	return;
			    }
			},
			failure : function(response, opts) {
			}
		});
		
	}
	
	var clickRightHandler = function(node) {
		
		rightTree.curNode = node.id;
	}
	/*var*/ rightTree = new ACLTree({
		region : 'south',
		curNode: '--',
		title : '权限列表',
		width : 300,
		height : 200,
		split : true,
		listeners : {
			'click' : clickRightHandler,
			scope : this
		},
		buttons : [{
					text : '分配权限',
					handler : function() {
					  grantRightHandler();
					},
					scope : this
				  },
				  {
					text : '回收权限',
					handler : function() {
					  revokeRightHandler();
				    },
						scope : this
				  }
		        ],
		xtype : 'acltree'
	});
	
	var grantRightHandler = function(){
		var _resSel = resTree.curNode;
		var _roleSel = roleTree.getChecked('id').toString();
		var _actionSel = actionTree.getChecked('id').toString();
		if(_resSel == '--'){
			alert('请选择待配置权限的菜单');
			return;
		}
		if(_roleSel == ''){
			alert('请选择待配置权限的角色');
			return;
		}
		if(_actionSel == ''){
			alert('请选择待配置权限的操作');
			return;
		}
		Ext.Ajax.request({
			url : grantRightUrl+"&resId="+_resSel+"&roleId="+_roleSel+"&actionId="+_actionSel,
			success : function(response, opts) {
			    try{
			    	var json = Ext.decode(response.responseText);
			    }catch(e){
			    	alert(response.responseText);
			    	return;
			    }
			    var resnode = resTree.getNodeById(resTree.curNode);
				rightTree.getLoader().baseParams['resId'] = resTree.curNode;
				rightTree.getLoader().baseParams['roleId'] = roleTree.getChecked('id').toString();
				rightTree.setTitle('‘'+resnode.text + '’权限列表');
				rightTree.getRootNode().reload();
			},
			failure : function(response, opts) {
			}
		});
	}
	var revokeRightHandler = function(){
		var _resSel = resTree.curNode;
		var _roleSel = roleTree.getChecked('id').toString();
		var _actionSel = rightTree.curNode;
		if(_resSel == '--'){
			alert('请选择待回收权限的菜单');
			return;
		}
		if(_roleSel == ''){
			alert('请选择待回收权限的角色');
			return;
		}
		if(_actionSel == '' || _actionSel=='root' ){
			alert('请选择待回收权限的操作');
			return;
		}
		
		var rightnode = rightTree.getNodeById(_actionSel);
		if(rightnode){
			Ext.MessageBox.confirm('系统提示', '是否确定回收所选角色的['+rightnode.text+']权限', function(o) {
				if (o == 'yes'){
					Ext.Ajax.request({
						url : grantRightUrl+"&resId="+_resSel+"&roleId="+_roleSel+"&actionId="+_actionSel+"&optype=revoke",
						success : function(response, opts) {
						    try{
						    	var json = Ext.decode(response.responseText);
						    }catch(e){
						    	alert(response.responseText);
						    	return;
						    }
						    var resnode = resTree.getNodeById(resTree.curNode);
							rightTree.getLoader().baseParams['resId'] = resTree.curNode;
							rightTree.getLoader().baseParams['roleId'] = roleTree.getChecked('id').toString();
							rightTree.setTitle('‘'+resnode.text + '’权限列表');
							rightTree.getRootNode().reload();
						},
						failure : function(response, opts) {
						}
					});	
				}
			}, this);
		}
	}

	var viewEast = new Ext.Panel({
				layout : 'border',
				region : 'east',
				width : 300,
				split : true,
				items : [actionTree,rightTree]
		});
	
	var clickResourceHandler = function(node) {
	
		resTree.curNode = node.id;
		
		actionTree.getLoader().baseParams['resId'] = node.id;
		actionTree.setTitle('‘'+node.text + '’操作列表');
		actionTree.getRootNode().reload();
		
		rightTree.getLoader().baseParams['resId'] = node.id;
		rightTree.getLoader().baseParams['roleId'] = roleTree.getChecked('id').toString();
		rightTree.setTitle('‘'+node.text + '’权限列表');
		rightTree.getRootNode().reload();
	}
	
	var resmenu = new Ext.menu.Menu({
		id: 'resMenu',
		items:[
		 {
		 text: '新增',
		 iconCls:'add',
		 handler: function(){
			 addResourceHandler();
		 }
		},
		{
		 text: '修改',
		 iconCls:'refresh',
		 handler: function(){
			updateResourceHandler();
		   }
		},
		{
		 text: '删除',
		 iconCls:'remove',
		 handler: function(){
			deleteResourceHandler();
		  }
		}
		]
	});
	
	/*var*/ resTree = new MTree({
				region : 'center',
				xtype : 'mtree',
				title : '系统菜单 ',
				curNode:'--',
				listeners : {
					'click' : clickResourceHandler,
					scope : this
				},
				border : true,
				split : true,
				expanded:false,//设置初始不展开
				buttons : [{
					text : '新增',
					handler : function() {
						addResourceHandler();
					},
					scope : this},
					{
						text : '修改',
						handler : function() {
							updateResourceHandler();
						},
						scope : this},
					{
						text : '删除',
						handler : function() {
							deleteResourceHandler();
				   		  },
						scope : this},
					{
						text : '记录级权限',
						handler : function() {
							recordRestrictHandler();
						},
						scope : this}
				]
			});

	resTree.on("contextmenu",function(node,e){
		e.preventDefault();
		node.select();
		resmenu.showAt(e.getXY());
		resTree.curNode = node.id;
	});
	//记录集权限设置函数
	var recordRestrictHandler = function(){
		if(resTree.curNode == '--'){
			alert("请先选择需要配置记录级权限的资源！");
			return;
		}
		var url = EMPTools.encodeURI(restrictUrl+'&resourceid='+resTree.curNode);
		openWinExtRecord(url);
	}
	//模块新增函数
	var addResourceHandler = function(){
		//如果还未选定上级资源不允许新增
		if(resTree.curNode == '--'){
			alert("请先选择需要新增资源的上级资源！");
			return;
		}
		var url = EMPTools.encodeURI(addResUrl+'&s_resource.parentid='+resTree.curNode);
	    //EMPTools.openWindow(url,'newwindow');
		openWinExt(url);
	}
  //模块修改函数
   var updateResourceHandler = function(){
	 //如果还未选定上级资源不允许新增
		if(resTree.curNode == '--'){
			alert("请先选择需要修改的资源！");
			return;
		}
		var url = EMPTools.encodeURI(updResUrl+'&resourceid='+resTree.curNode);
	    //EMPTools.openWindow(url,'newwindow');   
		openWinExt(url);
   }
   //模块删除函数
   var deleteResourceHandler = function(){
	   if(resTree.curNode == '--'){
			alert("请先选择需要删除的资源！");
			return;
		}
	   if(confirm("删除资源将同步删除所有关连的下级资源、资源操作、资源权限等记录\n是否确认要删除？")){
		   //把id中的特殊字符编码一下
			var url = delResUrl+"&resourceid="+encodeURIComponent(resTree.curNode);
			Ext.Ajax.request({
				url : url,
				success : function(response, opts) {
				    try{
				    	var json = response.responseText;
				    	if(json == '0'){
				    		//resTree.getLoader().baseParams['bizLine'] = comboBox.getValue();
				    		resTree.getRootNode().reload();
				    		resTree.curNode = '--';
				    	}else{
				    		alert("删除失败!");
				    	}
				    }catch(e){
				    	alert(response.responseText);
				    	return;
				    }
				},
				failure : function(response, opts) {
				}
			});
		}   
   };
   
   
	var viewAll = new Ext.Viewport({
				layout : 'border',
				items : [bannerForm, resTree, roleTree, viewEast]
			});
	
	//npanel.show();
};
function colseWinExt(){
	var npanel = Ext.getCmp("openWinExt");
	if(npanel){
		npanel.hide();
	}
	
}
function openWinExtRecord(url){
	var npanel = Ext.getCmp("openWinExt");
	
	if(!npanel){
		var npanel = new Ext.Window ({
			id : "openWinExt",
			modal : true,
			closeAction : 'hide', 
			//buttons : [retBtn, refBtn, clsBtn],
			height : 400,
			width  : 900 ,
			//renderTo :
			html : '<iframe id="operateWindowFrame"  src="#" frameborder="0" style="padding:3px" scrolling="Auto"  height="100%"  width="100%" ></iframe>'
		});
		
		var callbackFn=function(p,t){
			p.body.unmask();
		}
		npanel.on('afterrender', function(p) {
					var t = Ext.getDom("operateWindowFrame");
					if (t) {
						p.body.mask("<font color='red'>正在加载页面   请稍后...</font>");
						
						if (Ext.isIE) {
							t.onreadystatechange = function() {
								if (t.readyState&& t.readyState == 'complete')
									callbackFn.call(this,p);
							}
						} else {
							t.onload = function() {
								callbackFn.call(this,p);
							}
						}
						
					}
		});
	}
	npanel.show();
	var t = Ext.getDom("operateWindowFrame");
	if (t) {
		npanel.body.mask("<font color='red'>正在加载页面   请稍后...</font>");
		t.contentWindow.location.href = url+"&closeFun=window.parent.colseWinExt()";
	}
}
function openWinExt(url){
	var npanel = Ext.getCmp("openWinExt");
	
	if(!npanel){
		var npanel = new Ext.Window ({
			id : "openWinExt",
			modal : true,
			closeAction : 'hide', 
			//buttons : [retBtn, refBtn, clsBtn],
			height : 400,
			width  : 900 ,
			//renderTo :
			html : '<iframe id="operateWindowFrame"  src="#" frameborder="0" style="padding:3px" scrolling="Auto"  height="100%"  width="100%" ></iframe>'
		});
		
		var callbackFn=function(p,t){
			p.body.unmask();
		}
		npanel.on('afterrender', function(p) {
					var t = Ext.getDom("operateWindowFrame");
					if (t) {
						p.body.mask("<font color='red'>正在加载页面   请稍后...</font>");
						
						if (Ext.isIE) {
							t.onreadystatechange = function() {
								if (t.readyState&& t.readyState == 'complete')
									callbackFn.call(this,p);
							}
						} else {
							t.onload = function() {
								callbackFn.call(this,p);
							}
						}
						
					}
		});
	}
	npanel.show();
	var t = Ext.getDom("operateWindowFrame");
	if (t) {
		npanel.body.mask("<font color='red'>正在加载页面   请稍后...</font>");
		t.contentWindow.location.href = url+"&closeFun=window.parent.colseWinExt()";
	}
}
MTree = Ext.extend(Ext.tree.TreePanel, {
			
			autoScroll : true,
			singleClickExpand : true,
			initComponent : function() {
				
				Ext.apply(this, {
							loader : new Ext.tree.TreeLoader({
								        dataUrl : resTreeUrl,
										preloadChildren : false,
										createNode: resCreateNode,
										clearOnLoad : false
									}),
							root : {
										nodeType : 'async',
										draggable : false,
										id : 'src'
									},
							rootVisible : false
						});
				MTree.superclass.initComponent.apply(this, arguments);
			},
			onRender : function() {
				MTree.superclass.onRender.apply(this, arguments);
			}
		});
Ext.reg('mtree', MTree);

var resCreateNode = function(attr) {

	try {
		attr.text = attr.label ? attr.label : attr.text;
		attr.leaf = attr.children ? false : true;
	} catch (e) {
	}

	if (this.baseAttrs) {
		Ext.applyIf(attr, this.baseAttrs);
	}
	if (this.applyLoader !== false && !attr.loader) {
		attr.loader = this;
	}
	if (Ext.isString(attr.uiProvider)) {
		
		attr.uiProvider = this.uiProviders[attr.uiProvider]
				|| eval(attr.uiProvider);
	}
	if (attr.nodeType) {
		
		return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
		
	} else {
		var t = attr.leaf
				? new Ext.tree.TreeNode(attr)
				: new Ext.tree.AsyncTreeNode(attr);
	    
		if(attr.parentid == null || attr.parentid == ""){
			t.expanded = false;//展开首级菜单
		}
		
		if(attr.leaf){
			t.qtip = t.href;
		}
        
		return t;
	}
}

RTree = Ext.extend(Ext.tree.TreePanel, {
			singleClickExpand : true,
			autoScroll : true,
			initComponent : function() {
				Ext.apply(this, {
							
							loader : new Ext.tree.TreeLoader({
								dataUrl : roleTreeUrl,
								preloadChildren : false,
								clearOnLoad : false,
								createNode: resCreateNode
							}),
							root : {
								nodeType : 'async',
								draggable : false,
								id : 'src'
							},
							rootVisible : false
						});
				RTree.superclass.initComponent.apply(this, arguments);
			},

			onRender : function() {
				RTree.superclass.onRender.apply(this, arguments);
			}
		});
Ext.reg('rtree', RTree);


ATree = Ext.extend(Ext.tree.TreePanel, {
	singleClickExpand : true,
	autoScroll : true,
	initComponent : function() {
		Ext.apply(this, {
					dataUrl : actTreeUrl,
					root : {
						nodeType : 'async',
						draggable : false,
						id : 'src'
					},
					rootVisible : false
				});
		ATree.superclass.initComponent.apply(this, arguments);
	},
	onRender : function() {
		ATree.superclass.onRender.apply(this, arguments);
	}
});
Ext.reg('atree', ATree);

ACLTree = Ext.extend(Ext.tree.TreePanel, {
	singleClickExpand : true,
	autoScroll : true,
	initComponent : function() {
		Ext.apply(this, {
					dataUrl : rightTreeUrl,
					root : {
						nodeType : 'async',
						draggable : false,
						id : 'src'
					},
					rootVisible : false
				});
		ACLTree.superclass.initComponent.apply(this, arguments);
	},
	onRender : function() {
		ACLTree.superclass.onRender.apply(this, arguments);
	}
});
Ext.reg('acltree', ACLTree);