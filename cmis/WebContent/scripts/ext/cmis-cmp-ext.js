/**
 * 功能树
 * 
 * @class XTree
 * @extends Ext.tree.TreePanel
 */
XTree = Ext.extend(Ext.tree.TreePanel, {
			jsonData : null,
			singleClickExpand : true,
			initComponent : function() {
				this.hiddenPkgs = [];
				Ext.apply(this, {
							loader : new Ext.tree.TreeLoader({
										preloadChildren : false,
										clearOnLoad : false
									}),
							root : this.jsonData,
							rootVisible : false,
							bbar : cextUtils.createTreeFilterBar(this, 170)
						});
				XTree.superclass.initComponent.apply(this, arguments);
			}
		});
Ext.reg('xtree', XTree);
CPanel = Ext.extend(Ext.Panel, {
	layout : 'border',
	jsonData : null,
	activateId : null,
	paths : [],
	pathHandler : function(node, ct) {
		ct.paths.push(node.text);
		if (node.parentNode && !node.parentNode.isRoot) {
			ct.pathHandler(node.parentNode, ct);
		}
	},
	/**
	 * 更新 导航路径
	 * 
	 * @return { 导航路径 }
	 */
	updatePath : function(path) {
		var nav = Ext.get('nav');
		if (!path) {
			path = "";
			for (var i = this.paths.length - 1; i >= 0; i--) {
				path += this.paths[i];
				if (i != 0) {
					path += "->";
				}
			}
		}
		nav.dom.innerHTML = path;
		return path;
	},
	clickNodeHander : function(node) {
		var tabpanel = this.items.item(1);
		this.paths = [];
		this.pathHandler(node, this);
		this.paths.push(this.title);
		this.updatePath.call(this);
//		if (node.leaf) {
		if(node.attributes.action&&node.attributes.action!=""){
			var src = encodeURL(node.attributes.action);
			if (src)
				if (src.indexOf("?") > -1) {
					src += "&menuId=" + node.id;
				} else {
					src += "?menuId=" + node.id;
				}
			var html = '<iframe  id='
					+ "frame_"
					+ node.id
					+ '  src="'
					+ src
					+ '" frameborder="0" style="padding:3px" scrolling="Auto"  height="100%"  width="100%" ></iframe>';
			var npanel = Ext.getCmp(node.id);
			if (!npanel) {
				/*
				 * var retBtn = new Ext.Button({ text : '返 回', iconCls :
				 * 'return', handler : function() { var t = Ext.getDom("frame_" +
				 * node.id); if (t && t.contentWindow.location.href != src) {
				 * t.contentWindow.location.href = src; } } }); var refBtn = new
				 * Ext.Button({ text : '刷 新', iconCls : 'refresh', handler :
				 * function() { var t = Ext.getDom("frame_" + node.id); if (t) {
				 * t.contentWindow.location.reload(); } } }); var clsBtn = new
				 * Ext.Button({ text : '关 闭', iconCls : 'close', handler :
				 * function() { var i = tabpanel.items.items.length - 2;
				 * tabpanel.remove(npanel); tabpanel.activate(i); } });
				 */
				npanel = new Ext.Panel({
							xtype : 'panel',
							id : node.id,
							baseSrc : src,
							// bbar : ['->',retBtn, refBtn, clsBtn],
							iconCls : 'class',
							title : node.text,
							html : html,
							closable : true
						});
				
				var ct = this;
				this.on('activate', function(p) {
							APanelId = p.activateId;// 回归原始激活面板
							p.updatePath(npanel.pathName);
						});
				npanel.on('activate', function(p) {
							ct.activateId = p.id;// 设置本面板活动Id
							APanelId = p.id;
							ct.paths = [];
							ct.pathHandler(node, ct);
							ct.paths.push(ct.title);
							p.pathName = ct.updatePath.call(ct);
						}, this);
				npanel.on('afterrender', function(p) {
							var t = Ext.getDom("frame_" + node.id);
							if(Ext.isIE)
								p.body.setHeight(p.ownerCt.body.getHeight());
							if (t) {
								p.body
										.mask("<span class='waitCls'>正在加载页面 "
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
				// 如果是单页模式 移除其他已打开的面板 节省浏览器内存
				if (isSingle && tabpanel.items.length > 1) {
					tabpanel.items.each(function(item) {
								if (item.closable) {
									tabpanel.remove(item);
								}
							});
				}
				tabpanel.add(npanel);
			} else {
				if (isRefresh) {// 如果单击节点 始终刷新
					var t = Ext.getDom("frame_" + node.id);
					if (t) {
						npanel.body
								.mask("<span class='waitCls'>正在重新加载页面 "
										+ npanel.title + " 请稍后...</span>");
						t.contentWindow.location.href = src;
					}
				}
			}
			tabpanel.activate(npanel);

		}
	},
	initComponent : function() {
		this.on('activate', function(p) {
					// APanelId = p.id;
					var nav = Ext.get('nav');
					var text = nav.dom.innerHTML;
					if (text.indexOf("->") < 0) {
						nav.dom.innerHTML = p.title;
					}
				}, this);
		Ext.apply(this, {
			items : [{
						title : this.title,
						region : 'west',
						xtype : 'xtree',
						iconCls : 'cmp',
						split : false,
						jsonData : this.jsonData,
						autoScroll : true,
						margins : '2 2 2 0',
						listeners : {
							'click' : this.clickNodeHander,
							scope : this
						},
						width : 220,
						collapsible : true
					}, {
						xtype : 'tabpanel',
						margins : '2 0 2 2',
						enableTabScroll : true,
//						layout:'fit',
						plain : false,
						frame : false,
						plugins : [new Ext.ux.TabCloseMenu()],
						tabPosition : 'bottom',
						region : 'center',
						activeItem : 0,
						items : [{
							title : '欢迎使用' + this.title,
							iconCls : 'home',
							html : '<iframe  src="welcome.jsp?title='
									+ this.title
									+ '" frameborder="0" style="padding:3px" scrolling="Auto"  height="100%"  width="100%" ></iframe>'
						}]
					}]
		})
		CPanel.superclass.initComponent.apply(this, arguments);
	}
});
Ext.reg('cpanel', CPanel);
/**
 * 系统组件面板
 * 
 * @class CenterPanel
 * @extends Ext.TabPanel
 */
CenterPanel = Ext.extend(Ext.TabPanel, {
			initComponent : function() {
				Ext.apply(this, {
							tabPosition : 'top',
							plain : false,
							frame : false,
							enableTabScroll : true,
							border : false
						});
				CenterPanel.superclass.initComponent.apply(this, arguments);
			},
			onRender : function() {
				ajaxMenuTree(this, this.buildTabPanel);
				CenterPanel.superclass.onRender.apply(this, arguments);
			},
			buildTabPanel : function(root) {
				Ext.each(root, function(o) {
							this.add({
										xtype : 'cpanel',
										id : o.id,
//										iconCls : hasIcon?'cmp':null,
										title : o.label,
										jsonData : o
									});
						}, this);
				this.doLayout(true);
				this.activate(0);
			}
		});
Ext.reg('centerpanel', CenterPanel);

