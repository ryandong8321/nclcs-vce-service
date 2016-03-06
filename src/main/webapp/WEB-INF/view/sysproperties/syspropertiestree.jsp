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
<link href="<%=basePath%>assets/jstree/dist/themes/default/style.min.css" rel="stylesheet" type="text/css"/>
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
						<li class="">
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
						<li class="active">
							<a href="<%=basePath%>syspropertiesmanagement/syspropertiestree.do">
							<i class="icon-docs"></i>
							<span class="title">系统属性管理</span>
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
				<li>
					<a href="javascript:;">
					<i class="icon-book-open"></i>
					<span class="title">学生作业管理</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/downloadassignmentlist.do">
							<i class="icon-action-redo"></i>
							<span class="title">下载学生作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/downloadappointmentassignmentlist.do">
							<i class="icon-login"></i>
							<span class="title">下载代审作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/uploadassignmentlist.do">
							<i class="icon-action-undo"></i>
							<span class="title">上传已审作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/uploadappointmentassignmentlist.do">
							<i class="icon-logout"></i>
							<span class="title">上传代审作业</span>
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
							<button type="button" class="btn blue">保存</button>
							<button type="button" class="btn default" data-dismiss="modal">取消</button>
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
					<h1>VCE课程管理系统-系统属性管理</h1>
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
					<!-- <div class="note note-danger note-shadow">
						<p>
							 NOTE: The below datatable is not connected to a real database so the filter and sorting is just simulated for demo purposes only.
						</p>
					</div> -->
					<!-- Begin: life time stats -->
					<div class="portlet light">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-user font-blue-sharp"></i>
								<span class="caption-subject font-green-sharp bold uppercase">系统属性信息</span>
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
											<a href="javascript:deleteProperty();">
											删除选择的系统属性 </a>
										</li>
										<!-- <li>
											<a href="javascript:;">
											Export to Excel </a>
										</li>
										<li>
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
							<div id="tree_2" class="tree-demo">
							</div>
						</div>
					</div>
					<!-- End: life time stats -->
				</div>
				<div class="col-md-6 col-sm-12">
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="tabbable tabbable-custom tabbable-noborder tabbable-reversed">
						<div class="portlet light bordered form-fit">
							<div class="portlet-title">
								<div class="caption">
									<i class="icon-user font-blue-hoki"></i>
									<span class="caption-subject font-blue-hoki bold uppercase">系统属性信息</span>
								</div>
								<div class="actions">
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:createProperty();">
									<i class="icon-cloud-upload"></i>
									</a>
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:modifyProperty();">
									<i class="icon-wrench"></i>
									</a>
								</div>
							</div>
							<div class="portlet-body form">
								<!-- BEGIN FORM-->
								<form action="<%=basePath%>syspropertiesmanagement/savesysproperties.do" Method="POST" class="form-horizontal form-bordered form-label-stripped" id="frmsysuserinfo">
									<div class="form-body">
										<div class="form-group">
											<label class="control-label col-md-3">上级信息<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="parentName" placeholder="上级属性" class="form-control" id="parentName" readOnly="readonly"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">系统属性<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="propertyName" placeholder="系统属性" class="form-control" id="propertyName"  readOnly="readonly"/>
											</div>
										</div>
									</div>
									<div class="form-actions">
										<div class="row">
											<div class="col-md-offset-3 col-md-9">
												<button type="button" class="btn blue" id="btnSubmit" onclick="javascript:saveProperty();"><i class="fa fa-check"></i> 保存</button>
												<button type="button" class="btn default" onclick="javascript:;" id="btnCancel">取消</button>
											</div>
										</div>
									</div>
									<input type="hidden" id="propertyId" name="propertyId" value="${sysproperty.id }" />
									<input type="hidden" id="propertyParentId" name="propertyParentId" value="${sysproperty.propertyParentId }" />
									<input type="hidden" id="parentIds" name="parentIds" value="${parentIds }" />
									<input type="hidden" id="operation_status" name="operation_status" value="${operation_status }" />
								</form>
								<!-- END FORM-->
							</div>
						</div>
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
<script src="<%=basePath%>assets/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootbox/bootbox.min.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/pages/scripts/ui-alert-dialog-api.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
     jQuery(document).ready(function() {    
        Metronic.init(); // init metronic core components
        Layout.init(); // init current layout
		Demo.init(); // init demo features
		
		//
		$(function () {
			$('#tree_2').jstree({ 
				'core' : {
					"multiple" : false,
				    'data' : {
						contentType: "application/json; charset=utf-8",
				        url: "<%=basePath%>syspropertiesmanagement/inittreedata.do",
				        dataType: 'json'
				    }
				},
				"checkbox" : {
					"keep_selected_style" : true,
					"three_state":false,
					"whole_node":false,
					"tie_selection":false,
					"cascade":"down+undetermined"
			    },
				"types" : {
	                "default" : {
	                    "icon" : "fa fa-folder icon-state-warning icon-lg"
	                },
	                "file" : {
	                    "icon" : "fa fa-file icon-state-warning icon-lg"
	                }
	            },
	            "plugins": ["types","checkbox"]
			});
		});
		
		//add event listener
		$('#tree_2').on("changed.jstree", function (e, data) {
		  	/* alert(data.selected);
			var i, j, r = [];
		    for(i = 0, j = data.selected.length; i < j; i++) {
		      r.push(data.instance.get_node(data.selected[i]).text);
		    }
		    alert(r); */
		    $("#parentName").val($('#tree_2').jstree(true).get_node(data.instance.get_parent(data.selected)).text);
		    $("#propertyName").val(data.instance.get_node(data.selected).text);
		    $("#propertyId").val(data.selected);
		    $("#propertyParentId").val(data.instance.get_parent(data.selected));
		});
		
		var result="${result}";
		try{
			if (result){
				showMessage(result);
			}
		}catch(error){
		}
	});
     
   	//refresh tree
	function refreshTree(node){
		if (node){
			$('#tree_2').jstree(true).load_node(node);
		}else{
			$('#tree_2').jstree(true).refresh();
		}
	}
   	
	//get tree instance
	function getInstanceOfTree(tree){
		var treeInstance;
		if (tree){
			treeInstance=$('#'+tree).jstree(true);
		}else{
			treeInstance=$('#tree_2').jstree(true);
		}
		return treeInstance;
	}
	
	//create property button action
	function createProperty(){
		$("#propertyName").attr('readonly', false);
		
		var ref=getInstanceOfTree(),selection=ref.get_selected();
		
		$("#parentName").val(ref.get_node(selection).text);
	    $("#propertyName").val("");
	    $("#propertyId").val(null);
	    $("#propertyParentId").val(selection);
	}
	
	//modify property button action
	function modifyProperty() {
		var ref=getInstanceOfTree();
		if (!ref.get_selected()){
			showMessage("Hitting text to select option that you want to modify.");
			return;
		}
		$("#propertyName").attr('readonly', false);
	}
	
	//delete property button action
	function deleteProperty() {
		var ref=getInstanceOfTree();
		if(!ref.get_checked()){
			showMessage("select option you want to delete.");
			return;
		}
		bootbox.confirm("<font size='3'>You checked option(s) will be deleted and all included subclass were deleted at the same time.</font>", function (result){
			if (result==true){
				$.ajax({
					type : "POST",
					async : false,
					contentType : "application/json; charset=utf-8",
					url : "<%=basePath%>syspropertiesmanagement/deleteproperties.do",
			        data: "{'propertyIds':'"+ref.get_checked()+"'}",
			        dataType: 'json',
			        success: function(result) {
			        	if (result.status==1){
			        		cleanAllFields();
			   			 	refreshTree();
			        	}
			        	showMessage(result.data);
			        }
			    });
			}
		});
	}
	
	function cleanAllFields(){
		$("#parentName").val("");
	    $("#propertyName").val("");
	    $("#propertyId").val(null);
	    $("#propertyParentId").val(null);
	}

	//check field and do save action
	function saveProperty() {
		var flag = true;

		if (!$("#propertyName").val()) {
			constructAlertMessage("请输入信息系统名称!");
			flag = false;
		}

		if (!flag) {
			showMessage();
			return;
		}

		savePropertyInfo();
	}
	
	//save property information
	function savePropertyInfo(){
		var id, parentId, propertyName;
		
		id = $("#propertyId").val();
		parentId = $("#propertyParentId").val();
		propertyName = $("#propertyName").val();

		$.ajax({
			type : "POST",
			async : false,
			contentType : "application/json; charset=utf-8",
			url : "<%=basePath%>syspropertiesmanagement/savesysproperties.do",
	        data: "{'propertyId':'"+id+"' , 'propertyParentId':'"+parentId+"' , 'propertyName':'"+propertyName+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	if (result.status==1){
	   			 	$("#propertyName").attr('readonly',true);
	   			 	refreshTree();
	   			 	openTreeNode();
	        	}
	        	showMessage(result.data);
	        }
	    });
	}
	
	function openTreeNode(node){
		var ref=getInstanceOfTree();
		if (node){
			operationNode=node;
		}else{
			operationNode=ref.get_selected();
		}
		if (!ref.is_open(operationNode)){
			ref.open_node(operationNode,false,0);
		}
	}

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
		message="";
	}
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>