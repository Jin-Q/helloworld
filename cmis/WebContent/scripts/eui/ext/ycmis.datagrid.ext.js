
var QUERY_STABLECOLUMSHOWSETLIST_SQL = 'querySTableColumshowSetListAction.do?EMP_SID='+empId;//加载缓存中列表显示设置信息。
var SET_STABLECOLUMSHOWSETLIST_SQL  =  'addSTableColumshowSet.do?EMP_SID='+empId;//配置列表显示项

var syncsAndSaveColums  =  'syncsAndSaveColums.do?EMP_SID='+empId;//同步缓存数据并更新到数据库
var clearSTableColumshowSetMapTemp  =  'clearSTableColumshowSetMapTemp.do?EMP_SID='+empId;//清除缓存中存放的信息

/** 
 *  
 * @requires jQuery,EasyUI 
 *  
 * 为datagrid、treegrid增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中 
 */
var createGridHeaderContextMenu = function(e, field) {
    e.preventDefault();
    var grid = $(this);/* grid本身 */
    var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
    
    var isColumFilter = grid.attr("isColumFilter");
    if(isColumFilter == "true"){
    	if (!headerContextMenu) {
            var tmenu = $('<div class="tableSetClass" style="width:150px;max-height:300px;overflow:auto;"></div>').appendTo('body');
            var fields = grid.datagrid('getColumnFields');
            for ( var i = 0; i < fields.length; i++) {
                var fildOption = grid.datagrid('getColumnOption', fields[i]);
                if (!fildOption.hidden) {
                    $('<div iconCls="icon-ok" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                } else {
                    $('<div iconCls="icon-empty" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                }
            }
            $('<a data-options="iconAlign:left,disabled:false,plain:false" id="save" class="easyui-linkbutton l-btn" href="javascript:void(0)"><span class="l-btn-left"><span class="l-btn-text">确定</span></span></a>').appendTo(tmenu);
            $('<a data-options="iconAlign:left,disabled:false,plain:false" id="cancle" class="easyui-linkbutton l-btn" href="javascript:void(0)"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a>').appendTo(tmenu);
            
            $("#save").click(function(event) {
            	$(".tableSetClass").hide();
            	var json = {'table_id':grid[0].id};
            	$.ajax({
            		type: "POST",
            		url: syncsAndSaveColums,
            		data: json,
            		dataType:"json",
            		success: function(data) {
	            		var tableId = grid[0].id;
	            		doShowOrNot(tableId);
            		},
            		error: function(XMLHttpRequest, textStatus, errorThrown) {
            			EMP.alertException(errorThrown);
            		}
            	});
            	
            });
            $("#cancle").click(function(event) {
            	var json = {'table_id':grid[0].id};
            	$.ajax({ 
            		type: "POST", 
            		url: clearSTableColumshowSetMapTemp, 
            		data: json,
            		dataType:"html",
            		success: function(data) {
            		
            		},
            		error: function(XMLHttpRequest, textStatus, errorThrown) {
            			EMP.alertException(errorThrown);
            		}
            	});
            	$(".tableSetClass").hide();
            });
            
            headerContextMenu = this.headerContextMenu = tmenu.menu({
                onClick : function(item) {
                    var field = $(item.target).attr('field');
                    if (item.iconCls == 'icon-ok') {
                    	var json = {'table_id':grid[0].id,'colum_cde':field,'is_show':'false'};
                    	ajaxSetTableColumHandle(json);
                        grid.datagrid('hideColumn', field);
                        $(this).menu('setIcon', {
                            target : item.target,
                            iconCls : 'icon-empty'
                        });
                    } else {
                    	var json = {'table_id':grid[0].id,'colum_cde':field,'is_show':'true'};
                    	ajaxSetTableColumHandle(json);
                        grid.datagrid('showColumn', field);
                        $(this).menu('setIcon', {
                            target : item.target,
                            iconCls : 'icon-ok'
                        });
                    }
                    $(".tableSetClass").show();
                }
            });
        }
    	headerContextMenu.menu('show', {  
            left : e.pageX,  
            top : e.pageY  
        }); 	
    }
};

$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

//执行列表列的隐藏展示
function doShowOrNot(tableId){
	$.ajax({
		type: "POST",
		url: QUERY_STABLECOLUMSHOWSETLIST_SQL,
		data: {"table_Id":tableId},
		dataType:"html",
		success: function(data) {
			try{
				var jsonstr = eval("("+data+")");
			} catch(e){
				EMP.alertException(data);
				return;
			}
			if(jsonstr != null && jsonstr.data != null && jsonstr.data.length > 0){
				var obj = eval("("+jsonstr.data+")");
				$.each(obj, function(i, n){
					if(n.is_show == 'false'){
						$('#'+tableId).datagrid("hideColumn", n.column_cde);
					}else if(n.is_show == 'true'){
						$('#'+tableId).datagrid("showColumn", n.column_cde);
					}
				})
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			EMP.alertException(errorThrown);
		}
	});
}

function ajaxSetTableColumHandle(json){
	$.ajax({
		type: "POST",
		url: SET_STABLECOLUMSHOWSETLIST_SQL,
		data: json,
		dataType:"html",
		success: function(data) {
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			EMP.alertException(errorThrown);
		}
	});
}


/** 
 * 为datagrid数据加载完成后，处理datagrid列表。 
 */ 
var tableLoadSuccessHandle =function (){
	var tableId = this.id;
   	$.ajax({ 
		type: "POST", 
		url: QUERY_STABLECOLUMSHOWSETLIST_SQL, 
		data: {"table_Id":tableId},
		dataType:"html",
		success: function(data) {
			try{
				var jsonstr = eval("("+data+")");
			}catch(e){
				EMP.alertException(data);
				return;
			}
			if(jsonstr != null && jsonstr.data != null && jsonstr.data.length > 0){
				var obj = eval("("+jsonstr.data+")");
				$.each(obj, function(i, n){
					if(n.is_show == 'false'){
						$('#'+tableId).datagrid("hideColumn", n.column_cde);
					}else if(n.is_show == 'true'){
						$('#'+tableId).datagrid("showColumn", n.column_cde);
					}
				})
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			EMP.alertException(errorThrown);
		}
	});
}

/**
 * 如果全局禁用了鼠标右键，表格内容部分也禁用右键
 */ 
$.fn.datagrid.defaults.onRowContextMenu = function(e, _650, _651) {
    if(EMPTools.disableRightClickMenu){
        e.preventDefault();
    }
}