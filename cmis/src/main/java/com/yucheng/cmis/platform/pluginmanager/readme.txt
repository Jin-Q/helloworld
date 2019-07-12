１、这是插件管理模块，管理插件的安装、卸载、启用、停用等
２、装载工程文件和数据库文件的依据是插件包下的*.reg注文件，该文件应该记录了插件包下的工和文件的相对路径，以及该插件依赖数据库建表语句、初始化数据的文件。
３、该模块的功能及基本设计
	安装：根据插件包下的*.reg文件注册信息来复制工程文件（java、jsp、action、table）到项目中
		根据插件包下的*.reg文件注册来建数据库表、初始化数据
		写注册信息到S_Plugin_Reg表中:将插件包下的*.reg文件插入到插件注册表中，以方便后面的卸载使用
		
	卸载：根据S_Plugin_Reg表中的信息，删除文件、drop数据库表；
	启用：todo
	停用：todo
	
4、S_Plugin_Reg表结构
	id	plugin_modual_name	resource_path	db_path	res_java_path	res_jsp_path	res_table_path	res_action_path	db_uninstall_sql
	path中都放的是相对路径，
	db_uninstall_sql　这个是二进制字段，放sql文件还是怎样？
		在加载插件的时候，将该文件复制到项目工程中，在卸载的时候调用
		
	create table S_Plugin_Reg(
		plugin_modual_id varchar2(32),--插件英文名　ＰＫ
		plugin_modual_name varchar2(80),--插件／模块名称
		resource_path varchar2(200),--工程文件相对路径
		db_path varchar2(200),--数据库相对路径
		res_java_path varchar2(200),--java文件相对路径
		res_jsp_path varchar2(200),--jsp文件相对路径
		res_table_path varchar2(200),--table文件相对路径
		res_action_path varchar2(200),--action文件相对路径
		res_service_path varchar2(200),--服务注册文件相对路径
		res_sql_path varchar2(200),--命名SQL配置文件相对路径
		res_js_path varchar2(200),--JS文件相对路径
		res_imag_path varchar2(200),--imag文件相对路径
		res_css_path varchar2(200),--css文件相对路径
		db_uninstall_sql varchar2(200),--数据库清理文件相对路径
		install_date varchar2(10),--插件安装日期
		plugin_version varchar2(20),--插件版本
		plugin_status char(3)--插件状态　010-启用 020-停用
	);
	
	注：调用示例放在本模块下，特别对是技术组件来说
	