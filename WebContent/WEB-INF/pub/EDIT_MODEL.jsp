<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="com.dave.codepower.codegen.util.CodeUtil"%>

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
    <title>Edit Model | CodePower ~ Dave</title>
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
                        <div class="col-md-8 mx-0 px-0">
                        	<h3 class="card shadow bg-primary text-white font-weight-bold p-2 mt-0 d-flex">Edit: <a class="font-weight-bold text-dark bg-warning text-left p-md-2" href="${pageContext.request.contextPath }/projects/${currentProject.projectID}">${model.entityName }</a></h3>
                        	
                        	<div class="card">
					            <div class="card-body" style="background: #FFF;}">
					              <h5 class="card-title bg-light mb-2">Model Info</h5>
					
					              <!-- Horizontal Form -->
					              <form action="${pageContext.request.contextPath }/edit/model/${model.modelID}" method="post">
					                <div class="row mb-3">
					                  <label for="project-name" class="col-sm-2 col-form-label">Entity</label>
					                  <div class="col-sm-10">
					                  	<input type="hidden" class="form-control" name="project-id" value="${model.projectID }">
					                  	<input type="hidden" class="form-control" name="model-id" value="${model.modelID }">
					                    <input type="text" class="form-control" name="entity-name" value="${model.entityName }">
					                  </div>
					                  <small class="mx-auto">(Follow Java conventions for naming variables such as camel casing)</small>
					                </div>
					                <div class="row mb-3">
					                  <label for="description" class="col-sm-2 col-form-label">Pairing Table: </label>
					                  <div class="col-sm-10">
					                    <input type="text" class="form-control" name="pair-table" value="${model.table}">
					                  </div>
					                  <small class="mx-auto">(Name of the table that maps to this model)</small>
					                </div>
					                 
					                <div class="text-center">
					                  <button type="submit" class="btn btn-primary">Proceed</button>
					                  <button type="reset" class="btn btn-secondary d-none">Reset</button>
					                </div>
					              </form><!-- End Horizontal Form -->
								  
					            </div>
					        </div>
                        </div>
                        <div class="col-md-4 mx-0 px-0 px-md-2">
                            <jsp:include page="__DAVE_BADGE.jsp"></jsp:include>
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
<script src="${assets}/js/demo.js"></script>

<script>
	$(document).ready(function(){
		jQuery("small.date-created").timeago();
		
		/*CodeMirror*/
		var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
	      lineNumbers: true,
	      theme: "night",
	      extraKeys: {
	        "F11": function(cm) {
	          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
	        },
	        "Esc": function(cm) {
	          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
	        }
	      }
		
		
	    });
	});
</script>
</html>