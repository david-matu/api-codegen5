<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
    <title>SQL Statement Analysis Report | CodePower ~ Dave</title>
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
                        <div class="col-md-12 mx-auto" style="min-height: calc(100vh - 190px); background: #000000;">
					      	<div class="card card-shadow">
					      		<div class="card-header font-weight-bold">
					      			Queried Table: <strong>${sqlData.table }</strong>
					      		</div>
					      	</div>
					      	
					      	<div class="card-body card-shadow">
					      		<strong>Columns</strong><hr>
					      		<ul class="list-styled">
					      			<c:forEach items="${sqlData.columnList }" var="col">
					      				<li class="text-white font-weight-bold">${col }</li>
					      			</c:forEach>
					      		</ul>
					      	</div>
					      	<div class="table-responsive card-footer bg-light">
					      		<strong>Data</strong><hr>
					      		<table class="table table-responsive table-striped">
					              <thead class="bg-light text-dark" style="font-size: 0.8rem;">
					                <tr>
					                	<c:forEach items="${sqlData.columnList}" var="col">
					                		<th class="text-primary font-weight-bold">${col }</th>
					                	</c:forEach>
					                </tr>
					              </thead>
					              <tbody class="">
					              	<c:forEach items="${sqlData.dataList }" var="dataList">
					              		<tr>
							      			<c:forEach items="${dataList }" var="colData">
							      				<td class="bg-light mb-1">${colData}</td>
							      			</c:forEach>
						      			</tr>
					      			</c:forEach>
					              </tbody>
					             </table>
					      	</div>
					      </div>
                        <div class="col-md-12 px-0">
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

</html>