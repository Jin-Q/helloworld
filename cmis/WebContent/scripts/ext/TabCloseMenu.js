/*
 * Ext JS Library 2.0.2 Copyright(c) 2006-2008, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */

// Very simple plugin for adding a close context menu to tabs
Ext.ux.TabCloseMenu = function() {
	var tabs, menu, ctxItem;
	this.init = function(tp) {
		tabs = tp;
		tabs.on('contextmenu', onContextMenu);
	}

	function onContextMenu(ts, item, e) {
		if (!menu) { // create context menu on first right click
			menu = new Ext.menu.Menu([{
						id : tabs.id + '-close',
						text : '关闭选项卡',
						iconCls : 'close',
						handler : function() {
							tabs.remove(ctxItem);
						}
					}, {
						id : tabs.id + '-close-others',
						text : '关闭其他选项卡',
						iconCls : 'close',
						handler : function() {
							tabs.items.each(function(item) {
										if (item.closable && item != ctxItem) {
											tabs.remove(item);
										}
									});
						}
					},'-', {
						text : '返回到起始页',
						iconCls : 'return',
						handler : function() {
							var t = Ext.getDom("frame_" + ctxItem.id);
							if (t && t.contentWindow.location.href != ctxItem.baseSrc) {
								t.contentWindow.location.href = ctxItem.baseSrc;
							}
						}
					}, {
						text : '刷新页面',
						iconCls : 'refresh',
						handler : function() {
							var t = Ext.getDom("frame_" + ctxItem.id);
							if (t) {
								t.contentWindow.location.reload();
							}
						}
					},'-',{
						text : '添加快捷方式',
						iconCls : 'add',
						handler : function() {
							 var handleSuccess = function(o){
								if(o.responseText !== undefined) {
									try {
										var jsonstr = eval("("+o.responseText+")");
									} catch(e) {
										alert("Parse jsonstr define error!"+e);
										return ;
									}
									var cheakInfo = jsonstr.cheakInfo;
									if(cheakInfo=="success"){
										alert("添加成功，刷新页面或重新登录后生效");
										return;
									}else{
										alert('添加失败：'+cheakInfo);
										return ;
									}
								}
							};
							var handleFailure = function(o){
							};
							var callback = {
								success:handleSuccess,
								failure:handleFailure
							};
							var u = add_short_url;
							u +='&curMenuId='+ctxItem.id;
							var obj1 = YAHOO.util.Connect.asyncRequest('POST',u, callback);
						}
					},{
						text : '删除快捷方式',
						iconCls : 'remove',
						handler : function() {
							 var handleSuccess = function(o){
								if(o.responseText !== undefined) {
									try {
										var jsonstr = eval("("+o.responseText+")");
									} catch(e) {
										alert("Parse jsonstr define error!"+e);
										return ;
									}
									var cheakInfo = jsonstr.cheakInfo;
									if(cheakInfo=="success"){
										alert("删除成功，刷新页面或重新登录后生效");
										return;
									}else{
										alert('删除失败');
										return ;
									}
								}
							};
							var handleFailure = function(o){
							};
							var callback = {
								success:handleSuccess,
								failure:handleFailure
							};
							var u = remove_short_url;
							u +='&id='+ctxItem.id;
							var obj1 = YAHOO.util.Connect.asyncRequest('POST',u, callback);
						}
					}]);
		}
		ctxItem = item;
		var items = menu.items;
		items.get(tabs.id + '-close').setDisabled(!item.closable);
		var disableOthers = true;
		tabs.items.each(function() {
					if (this != item && this.closable) {
						disableOthers = false;
						return false;
					}
				});
		items.get(tabs.id + '-close-others').setDisabled(disableOthers);
		if(ctxItem.closable)
			menu.showAt(e.getPoint());
	}
};