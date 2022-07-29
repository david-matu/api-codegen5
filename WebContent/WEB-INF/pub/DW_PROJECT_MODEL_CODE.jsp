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
    <title>${model.entityName} DW Model | CodePower ~ Dave</title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />
    <!-- CSS Files -->
    <link href="${assets}/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${assets}/css/light-bootstrap-dashboard.css?v=2.0.0 " rel="stylesheet" />
    <!-- CSS Just for demo purpose, don't include it in your project -->
    <link href="${assets}/css/demo.css" rel="stylesheet" />
    
    <link href="${assets }/themes/prism_night.css" rel="stylesheet" />
    
    <!-- CodeMirror -->
    <link rel="stylesheet" href="${assets }/codemirror/lib/codemirror.css">
	<link rel="stylesheet" href="${assets }/codemirror/addon/display/fullscreen.css">
	<link rel="stylesheet" href="${assets }/codemirror/theme/night.css">
	<script src="${assets }/codemirror/lib/codemirror.js"></script>
	<script src="${assets }/codemirror/mode/xml/xml.js"></script>
	<script src="${assets }/codemirror/addon/display/fullscreen.js"></script>
	<link rel="stylesheet" href="${assets }/codemirror/addon/hint/show-hint.css">
	<script src="${assets }/codemirror/addon/hint/show-hint.js"></script>
	<script src="${assets }/codemirror/clike.min.js"></script>
	<style>.CodeMirror {border: 2px inset #dee;}</style>
</head>

<body class="">
    <div class="wrapper">
        <jsp:include page="__sidebar_dw.jsp"></jsp:include>
        
        <div class="main-panel pt-0">
            <!-- Navbar -->
            <jsp:include page="__navbar.jsp"></jsp:include>
            <!-- End Navbar -->
            
            <div class="content px-0 mt-0 mx-0 pt-0">                
                <div class="row px-0 mx-0">
                    	<h2 class="mb-0 col-12 font-weight-bold p-2 mx-0 w-100 d-flex" style="background: #000;; color: #0F0;">
                    		Dropwizard Code Output
                    		<a class="font-weight-bold text-white ml-auto d-flex" href="${pageContext.request.contextPath }/projects/${currentProject.projectID}">
	                        	<i class="nc-icon nc-mobile"></i>
	                            <p>${currentProject.name }</p>
	                        </a>
                    	</h2>
                    	<h2 class="col-12 mt-0 p-2 w-100 mx-0">
                    		<span class="col-12 font-weight-bold p-2 mx-0 w-100" style="background: #000; color: yellow;">${model.entityName }</span>
                    	</h2>
                    	
                    	<div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-txt-gen-pom">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Configuration File (config.yml)</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="50" style="height:80vh;" id="txt-gen-config" class="java-code-e">${genConfigFile }</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-txt-gen-pom">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">POM File (pom.xml)</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="50" style="height:80vh;" id="txt-gen-pom" class="java-code-e">${genPOM}</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="area-gen-txt-gen-pom">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Configuration Class</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="50" style="height:80vh;" id="txt-gen-config-class" >${genConfigClass}</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="area-gen-txt-gen-pom">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Main Class</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="50" style="height:80vh;" id="txt-gen-main-class">${genMainClass}</textarea>
                            	</div>
                            </div>
                        </div>
                    	
                    	<div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-entity">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Model class</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<%-- <pre class=""><code class="language-java java-code">${genClass }</code></pre> --%>
                            		<textarea class="form-control w-100 language-java java-code" id="java-class-code33" rows="50" style="height: 80vh;">${genEntity }</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-mapper">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Mapper</u></strong>
                            	</div>
                            	<div class="card-body">
                            		
                            		<form>
		                        		<textarea rows="50" style="height:80vh;" id="gen-mapper" class="java-code">${genMapper}</textarea>
		                        	</form>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-dao">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Entity DAO</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea id="txt-gen-dao33" class="java-code" name="code" rows="50" style="height:80vh;">${genDAO }</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-txt-gen-pom">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Entity Service</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="50" style="height:80vh;" class="java-code" id="txt-gen-pom">${genService }</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-update">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Resource Class (Endpoint)</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="150" style="height:80vh;" id="gen-resource">${genResource}</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <h1>Miscellaneous</h1>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-update">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">App Constants</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<textarea rows="150" style="height:80vh;" id="gen-resource">${genAppConstants}</textarea>
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
<script src="${assets}/js/demo.js"></script>
<script src="${assets }/themes/prism_u.js"></script>

<script>
	$(document).ready(function(){		
		/*CodeMirror*/		
		var javaEditor = CodeMirror.fromTextArea(document.getElementById("txt-gen-config"), {
		  lineNumbers: true,
		  matchBrackets: true,
		  mode: "text/x-java",
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
		
		var pom = CodeMirror.fromTextArea(document.getElementById("txt-gen-pom"), {
		  lineNumbers: true,
		  matchBrackets: true,
		  mode: "text/xml",
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
		
		var configClass = CodeMirror.fromTextArea(document.getElementById("txt-gen-config-class"), {
		  lineNumbers: true,
		  matchBrackets: true,
		  mode: "text/x-java",
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
		
		var mainClass = CodeMirror.fromTextArea(document.getElementById("txt-gen-main-class"), {
			  lineNumbers: true,
			  matchBrackets: true,
			  mode: "text/x-java",
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
		
		
		
		/* var editorMapper = CodeMirror.fromTextArea(document.getElementById("gen-mapper"), {
			mode: "text/x-java",
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
	    }); */
		
		var editor = CodeMirror.fromTextArea(document.getElementById("gen-resource"), {
			mode: "text/x-java",
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
		
		var editor = CodeMirror.fromTextArea(document.getElementById("txt-gen-config"), {
			mode: "text/x-java",
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
		
		var jContainers = document.getElementsByClassName("java-code");
		
		for(var i in jContainers) {
			//console.log();
			
			var hasError = false;
			
			try {
				var ob = jContainers[i].getAttribute("autofocus");	
			} catch(err) {
				hasError = true;
				console.log(jContainers[i] + " cannot be parsed! Checked by Dave's Code.");
			}
			
			if(hasError == false) {
				CodeMirror.fromTextArea(jContainers[i], {
					mode: "text/x-java",
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
			}
		}
		
		var mac = CodeMirror.keyMap.default == CodeMirror.keyMap.macDefault;
	    CodeMirror.keyMap.default[(mac ? "Cmd" : "Ctrl") + "-Space"] = "autocomplete";
	});
	
</script>
</html>