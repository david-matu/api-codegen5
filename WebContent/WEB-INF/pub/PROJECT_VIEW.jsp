<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.dave.codepower.codegen.util.CodeUtil" %>

<c:url var="assets" scope="page" value="/assets"></c:url> 
<%-- <c:url var="assets" scope="page" value="http://d3b9eqxzrvw9g6.cloudfront.net"></c:url> --%> 

<!--
=========================================================
 Light Bootstrap Dashboard - v2.0.1
=========================================================

 Product Page: https://www.creative-tim.com/product/light-bootstrap-dashboard
 Copyright 2019 Creative Tim (https://www.creative-tim.com)
 Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard/blob/master/LICENSE)

 Coded by Creative Tim

=========================================================

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  -->
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <link rel="apple-touch-icon" sizes="76x76" href="${assets}/img/apple-icon.png">
    <link rel="icon" type="image/png" href="${assets}/img/favicon.ico">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>${currentProject.name } Project | CodePower ~ Dave</title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />
    <!-- CSS Files -->
    <link href="${assets}/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${assets}/css/light-bootstrap-dashboard.css?v=2.0.0 " rel="stylesheet" />
    <!-- CSS Just for demo purpose, don't include it in your project -->
    <link href="${assets}/css/demo.css" rel="stylesheet" />
</head>

<body>
    <div class="wrapper">
        <jsp:include page="__sidebar.jsp"></jsp:include>
        
        <div class="main-panel">
            <!-- Navbar -->
            <jsp:include page="__navbar.jsp"></jsp:include>
            <!-- End Navbar -->
            
            <div class="content">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12 mx-0 px-0 px-md-1">
					        <div class="card border-success">
			                  <div class="card-header d-md-flex w-100 justify-content-between p-md-3" style="background: forestgreen; color: #FFF;">
			                    <h3 class="mb-1 font-weight-bold">${currentProject.name }</h3>
			                    <span>Created <small class="date-created font-weight-bold" title="${currentProject.dateCreated }">${currentProject.dateCreated }</small></span>
			                  </div>
			                  <div class="card-body">
				                  <p class="mb-1">${currentProject.description }</p>
				                  <small>Resident Database: <strong class="text-dark">${currentProject.dbName }</strong></small>
				                  <hr>
				                  <a class="btn btn-fill btn-light mb-2" href="${pageContext.request.contextPath }/create-project-model"><i class="fa fa-fw fa-plus-circle fa-lg"></i> Add Model</a>
				                  <a class="btn btn-fill btn-primary mb-2" style="border: 2px groove #FFC; border-radius: 8px;" href="${pageContext.request.contextPath }/generate-api-project/${currentProject.projectID}"><i class="fa fa-fw fa-cog fa-lg"></i> Generate Dropwizard API Project in Directory</a>
				                  <a class="btn btn-outline-danger mb-2" style="border: 3px groove #00F; border-radius: 8px; color: #F00; font-weight: bolder;" href="${pageContext.request.contextPath }/generate-angular-project/${currentProject.projectID}"><i class="fa fa-fw fa-cog fa-lg"></i> Generate Angular Code</a>
				                  <c:forEach items="${modelList }" var="model">
				                  <div class="col-md-12 panel px-md-2" href="${pageContext.request.contextPath }/#" id="parent-${model.modelID }">
		                  			<div class="card font-weight-bold mx-0 px-0">
		                  				<div class="card-header font-weight-bold bg-light text-primary">${model.entityName }</div>
		                  				<div class="card-body text-dark">
		                  					<p>Pairing table: <strong>${model.table }</strong></p> 
		                  				</div>
		                  				
		                  				<div class="card-footer px-0">
		                  					<a href="${pageContext.request.contextPath }/show-model-code/${model.modelID}" class="btn btn-sm btn-fill" style="background: #000; color: yellow;" >CodeGen Output</a>
		                  					<a href="${pageContext.request.contextPath }/dw-model-code/${model.modelID}" class="btn btn-sm btn-fill" style="background: blue; color: #FFF;" >Dropwizard-format Output</a>
		                  					<a href="${pageContext.request.contextPath }/edit/model/${model.modelID}" class="btn btn-sm btn-primary btn-fill">Edit</a>
		                  					<a href="${pageContext.request.contextPath }/delete" data-asset-name="${model.entityName }" data-id="${model.modelID }" data-table="MODEL" data-key="MODEL_ID" class="delete-btn btn btn-sm btn-danger btn-fill">Delete</a>
		                  				</div>
		                  			</div>
	                  			  </div>
	                  			  </c:forEach>                  			  
			                  </div>
			                  
			                  <div class="card-footer pb-0 mx-0 px-0">
			                  	<hr class="bg-success pb-1">
			                  </div>			                  
		                  	</div>
                        </div>
                    </div>
                </div>
            </div>
            
            <jsp:include page="__footer.jsp"></jsp:include>
        </div>
    </div>
</body>
<!--   Core JS Files   -->
<script src="${assets}/js/core/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="${assets}/js/core/popper.min.js" type="text/javascript"></script>
<script src="${assets}/js/core/bootstrap.min.js" type="text/javascript"></script>
<!--  Plugin for Switches, full documentation here: http://www.jque.re/plugins/version3/bootstrap.switch/ -->
<script src="${assets}/js/plugins/bootstrap-switch.js"></script>
<!--  Google Maps Plugin    -->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
<!--  Chartist Plugin  -->
<script src="${assets}/js/plugins/chartist.min.js"></script>
<!--  Notifications Plugin    -->
<script src="${assets}/js/plugins/bootstrap-notify.js"></script>
<!-- Control Center for Light Bootstrap Dashboard: scripts for the example pages etc -->
<script src="${assets}/js/light-bootstrap-dashboard.js?v=2.0.0 " type="text/javascript"></script>
<!-- Light Bootstrap Dashboard DEMO methods, don't include it in your project! -->
<script src="${assets}/js/jquery.timeago.js"></script>
<script src="${assets}/js/demo.js"></script>

<script>
	$(document).ready(function(){
		jQuery("small.date-created").timeago();
	})
</script>
</html>