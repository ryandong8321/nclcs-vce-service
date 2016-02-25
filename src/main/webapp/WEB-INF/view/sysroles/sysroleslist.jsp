<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String userName=""+request.getSession().getAttribute("u_name");
	request.setAttribute("BasePath", basePath);
%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>New Chinese Language and Culture School</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/select2/select2.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/bootstrap_table/bootstrap-table.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/jquery-multi-select/css/multi-select.css"/>
<!-- END PAGE LEVEL STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="<%=basePath%>assets/metronic/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/admin/layout4/css/layout.css" rel="stylesheet" type="text/css"/>
<link id="style_color" href="<%=basePath%>assets/metronic/assets/admin/layout4/css/themes/light.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/admin/layout4/css/custom.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body class="page-header-fixed page-sidebar-closed-hide-logo ">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<a href="#">
			<img src="<%=basePath%>assets/image/logo_word_1.png" alt="logo" class="logo-default"/>
			</a>
			<div class="menu-toggler sidebar-toggler">
				<!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
			</div>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
		</a>
		<!-- END RESPONSIVE MENU TOGGLER -->
		<!-- BEGIN PAGE ACTIONS -->
		<!-- DOC: Remove "hide" class to enable the page header actions -->
		<!-- END PAGE ACTIONS -->
		<!-- BEGIN PAGE TOP -->
		<div class="page-top">
			<!-- BEGIN HEADER SEARCH BOX -->
			<!-- END HEADER SEARCH BOX -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<li class="separator hide">
					</li>
					<!-- BEGIN NOTIFICATION DROPDOWN -->
					<!-- END NOTIFICATION DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN INBOX DROPDOWN -->
					<!-- END INBOX DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN TODO DROPDOWN -->
					<!-- END TODO DROPDOWN -->
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
					<li class="dropdown dropdown-user dropdown-dark">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<span class="username username-hide-on-mobile">
						<%=userName %> </span>
						<!-- DOC: Do not remove below empty space(&nbsp;) as its purposely used -->
						<img alt="" class="img-circle" src="<%=basePath%>assets/image/logo_image_only_1.png"/>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<!-- <li>
								<a href="extra_profile.html">
								<i class="icon-user"></i> My Profile </a>
							</li>
							<li>
								<a href="page_calendar.html">
								<i class="icon-calendar"></i> My Calendar </a>
							</li> -->
							<li>
								<a href="<%=basePath%>sysnotificationmanagement/sysnotificationdetail.do">
								<i class="icon-envelope-open"></i> My Inbox 
								</a>
							</li>
							<!-- <li>
								<a href="page_todo.html">
								<i class="icon-rocket"></i> My Tasks <span class="badge badge-success">
								7 </span>
								</a>
							</li> -->
							<li class="divider">
							</li>
							<!-- <li>
								<a href="extra_lock.html">
								<i class="icon-lock"></i> Lock Screen </a>
							</li> -->
							<li>
								<a href="<%=basePath%>sysusersmanagement/userlogout.do">
								<i class="icon-key"></i> Log Out </a>
							</li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END PAGE TOP -->
	</div>
	<!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
	<!-- BEGIN SIDEBAR -->
	<div class="page-sidebar-wrapper">
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
			<!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
			<!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
			<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
			<!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
			<!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
			<ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
				<li class="start ">
					<a href="<%=basePath%>sysnotificationmanagement/sysnotificationdetail.do">
					<i class="icon-home"></i>
					<span class="title">我的通知</span>
					</a>
				</li>
				<c:if test="${_sys_privilege eq 1 || _sys_privilege eq 2 }">
				<li class="active open">
					<a href="javascript:;">
					<i class="icon-settings"></i>
					<span class="title">系统服务</span>
					<span class="arrow open"></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>sysusersmanagement/sysuserslist.do">
							<i class="icon-user"></i>
							<span class="title">用户管理</span>
							</a>
						</li>
						<li class="active">
							<a href="<%=basePath%>sysrolesmanagement/sysroleslist.do">
							<i class="icon-user-following"></i>
							<span class="title">角色管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>sysgroupsmanagement/sysgroupstree.do">
							<i class="icon-users"></i>
							<span class="title">群组管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>syspropertiesmanagement/syspropertiestree.do">
							<i class="icon-docs"></i>
							<span class="title">系统信息管理</span>
							</a>
						</li>
					</ul>
				</li>
				</c:if>
				<c:if test="${_sys_privilege eq 1 || _sys_privilege eq 2 || _sys_privilege eq 3 || _sys_privilege eq 4}">
				<li>
					<a href="javascript:;">
					<i class="icon-graduation"></i>
					<span class="title">学生学级管理</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>appstudentsinfomanagement/appstudentsinfolist.do">
							<i class="icon-pencil"></i>
							<span class="title">学生信息管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appstudentsinfomanagement/appstudentsscorelist.do">
							<i class="icon-bar-chart"></i>
							<span class="title">学生成绩管理</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="<%=basePath%>sysnotificationmanagement/sysnotificationlist.do">
					<i class="icon-envelope-letter"></i>
					<span class="title">通知管理</span>
					</a>
				</li>
				</c:if>
				<li class="">
					<a href="<%=basePath%>sysusersmanagement/sysuserspersonal.do">
					<i class="icon-user-follow"></i>
					<span class="title">个人信息</span>
					</a>
				</li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
	</div>
	<!-- END SIDEBAR -->
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<div class="page-content">
			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<div class="modal fade" id="portlet-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
							<h4 class="modal-title">Modal title</h4>
						</div>
						<div class="modal-body">
							 Widget settings form goes here
						</div>
						<div class="modal-footer">
							<button type="button" class="btn blue">Save changes</button>
							<button type="button" class="btn default" data-dismiss="modal">Close</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<!-- BEGIN PAGE HEADER-->
			<!-- BEGIN PAGE HEAD -->
			<div class="page-head">
				<!-- BEGIN PAGE TITLE -->
				<div class="page-title">
					<h1>VCE课程管理系统-角色管理</h1>
				</div>
				<!-- END PAGE TITLE -->
				<!-- BEGIN PAGE TOOLBAR -->
				<!-- END PAGE TOOLBAR -->
			</div>
			<!-- END PAGE HEAD -->
			<!-- BEGIN PAGE BREADCRUMB -->
			<!-- END PAGE BREADCRUMB -->
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
			<div class="row">
				<div class="col-md-6 col-sm-12">
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="portlet light">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-user font-blue-sharp"></i>
								<span class="caption-subject font-green-sharp bold uppercase">角色信息</span>
								<span class="caption-helper"></span>
							</div>
							<div class="actions">
								<div class="btn-group">
									<a class="btn btn-default btn-circle" href="javascript:;" data-toggle="dropdown">
									<i class="fa fa-share"></i>
									<span class="hidden-480">
									Tools </span>
									<i class="fa fa-angle-down"></i>
									</a>
									<ul class="dropdown-menu pull-right">
										<li>
											<a href="javascript:deleteInfo();">
											删除选择的角色 </a>
										</li>
										<li>
											<a href="javascript:showPrivilege();">
												 角色授权
											</a>
										</li>
										<!-- <li>
											<a href="javascript:;">
											Export to XML </a>
										</li>
										<li class="divider">
										</li>
										<li>
											<a href="javascript:;">
											Print Invoices </a>
										</li> -->
									</ul>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-container">
								<!-- BEGIN TABLE DATA-->
									<table id="table" 
										data-toggle="table" 
										data-side-pagination="server"
										data-pagination="true" 
										data-url="<%=basePath%>sysrolesmanagement/initsysrolestable.do">
										<thead>
											<tr>
												<th data-field="state" data-checkbox="true"></th>
												<th data-field="id">编号</th>
												<th data-field="roleName">角色名称</th>
												<th data-field="roleMemo">角色简介</th>
											</tr>
										</thead>
									</table>
								<!-- END TABLE DATA-->
							</div>
						</div>
					</div>
					<!-- END EXAMPLE TABLE PORTLET-->
				</div>
				<div class="col-md-6 col-sm-12">
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="tabbable tabbable-custom tabbable-noborder tabbable-reversed">
						<div class="portlet light bordered form-fit">
							<div class="portlet-title">
								<div class="caption">
									<i class="icon-user font-blue-hoki"></i>
									<span class="caption-subject font-blue-hoki bold uppercase">用户信息</span>
								</div>
								<div class="actions">
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:createInfo();">
									<i class="icon-cloud-upload"></i>
									</a>
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:modifyInfo();">
									<i class="icon-wrench"></i>
									</a>
								</div>
							</div>
							<div class="portlet-body form">
								<!-- BEGIN FORM-->
								<form action="<%=basePath%>sysusersmanagement/savesysusers.do" Method="POST" class="form-horizontal form-bordered form-label-stripped" id="frmsysuserinfo">
									<div class="form-body">
										<div class="form-group">
											<label class="control-label col-md-3">角色名称<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="roleName" placeholder="角色名称" class="form-control" id="roleName" readOnly="readonly"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">角色简介</label>
											<div class="col-md-9">
												<input type="text" name="roleMemo" placeholder="角色简介" class="form-control" id="roleMemo"  readOnly="readonly"/>
											</div>
										</div>
									</div>
									<div class="form-actions">
										<div class="row">
											<div class="col-md-offset-3 col-md-9">
												<button type="button" class="btn blue" id="btnSubmit" onclick="javascript:saveRole();"><i class="fa fa-check"></i> 保存</button>
												<button type="button" class="btn default" onclick="javascript:;" id="btnCancel">取消</button>
											</div>
										</div>
									</div>
									<input type="hidden" name="roleId" value="" id="roleId" />
								</form>
								<form action="<%=basePath%>sysrolesmanagement/deletesysroles.do" Method="POST" id="frmdeleteinfo">
									<input type="hidden" name="deleteId" value="" id="deleteId" />
								</form>
								<!-- END FORM-->
							</div>
						</div>
						<!-- modal -->
						<div class="modal fade" id="ajax" role="basic" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="row">
										<div class="col-md-12">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
												<h4 class="modal-title">选择用户</h4>
											</div>
											<div class="modal-body">
												<div class="scroller" style="height:500px" data-always-visible="1" data-rail-visible1="1">
													<div class="row" id="div_multi_select_parent">
														<div class="col-md-6" id="div_multi_select">
															<select multiple="multiple" class="multi-select" id="sys_users_roles" name="sys_users_roles">
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="modal-footer">
												<button type="button" data-dismiss="modal" class="btn default">Close</button>
												<button type="button" class="btn blue" onclick="javascript:saveSelected()">Save changes</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- modal end -->
					</div>
					<!-- END EXAMPLE TABLE PORTLET-->
				</div>
			</div>
			<!-- END PAGE CONTENT-->
		</div>
	</div>
	<!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/respond.min.js"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/select2/select2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/bootstrap_table/bootstrap-table.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-multi-select/js/jquery.multi-select.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
     jQuery(document).ready(function() {    
        Metronic.init(); // init metronic core components
        Layout.init(); // init current layout
		Demo.init(); // init demo features
		
		//initiate table
		$table=$('#table');
		
		//add event listener
		$('#table').on("check.bs.table", function (e, data) {
			cleanAllFields();
			$("#roleId").val(data.id);
			$("#roleName").val(data.roleName);
			$("#roleMemo").val(data.roleMemo);
			/*selections=$table.bootstrapTable('getSelections');
			if (selections.length>1){
				$table.bootstrapTable('uncheckAll');
				$table.bootstrapTable('checkBy', {field:'id', values:[selectedId]});
			}*/
		});
		
		$('#table').on("uncheck.bs.table", function (e, data) {
			if ($("#roleName").val()){
				selectedId=data.id;
				if (selectedId==$("#roleId").val()){
					cleanAllFields();
				}
			}
		});
		
		//init multi select & modal
		//$('#sys_users_roles').multiSelect();
		$('#ajax').on('show.bs.modal', function (){
			
			$("#div_multi_select").remove();
			$("#div_multi_select_parent").append('<div class="col-md-6" id="div_multi_select"><select multiple="multiple" class="multi-select" id="sys_users_roles" name="sys_users_roles"></select></div>"');
			
			//$('#sys_users_roles').multiSelect('deselect_all');
			var sysRoleUsers;
			$.ajax({
		        type: "POST",
		        async:false,
		        contentType: "application/json; charset=utf-8",
		        url: "<%=basePath%>sysrolesmanagement/findsysusers.do",
		        data: "{'roleId':'"+$("#roleId").val()+"'}",
		        dataType: 'json',
		        success: function(result) {
		        	sysRoleUsers=result.data;
		        }
		    });
			$('#sys_users_roles').multiSelect();
			$('#sys_users_roles').multiSelect('addOption', sysRoleUsers);
			
			var arr=[];
			$.each(sysRoleUsers,function(idx,item){
				if (item.isSelected){
					arr.push(""+item.value);
				}
			});
			$('#sys_users_roles').multiSelect('select', arr);
		});
		
		var result="${result}";
		try{
			if (result){
				showMessage(result);
			}
		}catch(error){
		}
	});
   		//refresh table
		function refreshTable(table){
			var $table=getInstanceOfTable();
			$table.bootstrapTable('refresh');
		}
		
		//get table instance
		function getInstanceOfTable(table){
			var $table;
			if (table){
				$table=$("#"+table);
			}else{
				$table=$("#table");
			}
			return $table;
		}
     
     function createInfo(){
    	 $("#roleName").attr('readonly', false);
    	 $("#roleMemo").attr('readonly', false);
    	 
    	 $("#roleName").val("");
    	 $("#roleMemo").val("");
    	 $("#roleId").val(null);
     }
     
     function modifyInfo() {
		if (!$("#roleId").val()){
			showMessage("Select a row that you want to modify.");
			return;
		}
		 $("#roleName").attr('readonly', false);
    	 $("#roleMemo").attr('readonly', false);
	}
     
     function deleteInfo() {
		var ref=getInstanceOfTable(), selections=ref.bootstrapTable('getSelections');
		var m=[];
		if(JSON.stringify(selections)=="[]"){
			showMessage("Selecte row(s) that you want to delete.");
			return;
		}
		bootbox.confirm("<font size='3'>You selected row(s) will be deleted.</font>", function (result){
			if (result==true){
				$(selections).each(function(){
					m.push(this.id);
				});
				
				$.ajax({
					type : "POST",
					async : false,
					contentType : "application/json; charset=utf-8",
					url : "<%=basePath%>sysrolesmanagement/deletesysroles.do",
			        data: "{'roleIds':'"+m.join()+"'}",
			        dataType: 'json',
			        success: function(result) {
			        	if (result.status==1){
			        		cleanAllFields();
			   			 	refreshTable();
			        	}
			        	showMessage(result.data);
			        }
			    });
			}
		});
	}
     
     function cleanAllFields(){
		$("#roleName").attr('readonly',true);
		$("#roleMemo").attr('readonly',true);
	    $("#roleName").val("");
	    $("#roleMemo").val("");
	    $("#roleId").val(null);
	}
     
     function saveRole() {
		var flag = true;

		if (!$("#roleName").val()) {
			constructAlertMessage("请输入角色名称!");
			flag = false;
		}

		if (!flag) {
			showMessage();
			return;
		}

		saveRoleInfo();
	}
     
     function saveRoleInfo(){
		var id, roleName,roleMemo;
		
		id = $("#roleId").val();
		roleName = $("#roleName").val();
		roleMemo = $("#roleMemo").val();

		$.ajax({
			type : "POST",
			async : false,
			contentType : "application/json; charset=utf-8",
			url : "<%=basePath%>sysrolesmanagement/savesysroles.do",
	        data: "{'roleId':'"+id+"' , 'roleName':'"+roleName+"', 'roleMemo':'"+roleMemo+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	if (result.status==1){
	   			 	refreshTable();
	   			 	cleanAllFields();
	        	}
	        	showMessage(result.data);
	        }
	    });
	}
     
     function saveSelected(){
    	 
    	 $.ajax({
 			type : "POST",
 			async : false,
 			contentType : "application/json; charset=utf-8",
 			url : "<%=basePath%>sysrolesmanagement/saveroleuserrelationship.do",
 	        data: "{'roleId':'"+$("#roleId").val()+"' , 'userIds':'"+$("#sys_users_roles").val()+"'}",
 	        dataType: 'json',
 	        success: function(result) {
 	        	if (result.status==1){
 	        		$("#ajax").modal('hide');
 	        	}
 	        	showMessage(result.data);
 	        }
 	    });
     }
     
     function showPrivilege(){
    	 if ($("#roleId").val()){
    		 $("#ajax").modal('show');
    	 }else{
    		 showMessage("请先选择角色!");
    	 }
     }
     
   //construct message for display
	var message;
	function constructAlertMessage(msg){
		if (!message){
			message=msg;
		}else{
			message+="<br/>"+msg;
		}
	}

	function showMessage(msg) {
		if (msg) {
			message = msg;
		}
		bootbox.alert("<font size='4'>"+message+"</font>"); 
		/* alert(message); */
	}
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>