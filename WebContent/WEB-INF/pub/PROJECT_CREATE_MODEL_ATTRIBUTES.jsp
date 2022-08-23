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
    <title>Define Attributes for ${model.entityName} Model | CodePower ~ Dave</title>
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
        <jsp:include page="__sidebar.jsp"></jsp:include>
        
        <div class="main-panel">
            <!-- Navbar -->
            <jsp:include page="__navbar.jsp"></jsp:include>
            <!-- End Navbar -->
            
            <div class="content">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-8 mx-0 px-0">
                        	<h3 class="card shadow bg-primary text-white font-weight-bold p-2 mt-0 d-flex">Project: <a class="font-weight-bold text-dark bg-warning text-left p-md-2" href="${pageContext.request.contextPath }/projects/${currentProject.projectID}">${currentProject.name }</a></h3>
                        	
                        	<div class="card">
					            <div class="card-body" style="background: #FFF;}">
					              <h5 class="card-title bg-light mb-2">Model Info: ${model.entityName }</h5>
					
					              <!-- Horizontal Form -->
					              <form action="${pageContext.request.contextPath }/create-project-model-attributes/${model.modelID}" method="POST">
					                <div class="row mb-3">
					                  <label for="project-name" class="col-sm-12 col-form-label">Entity
					                  	<small class="mx-auto">(Follow Java conventions for naming variables such as camel casing)</small>
					                  </label>
					                  <div class="col-sm-12">
					                  	<input type="hidden" class="form-control" name="project-id" value="${project.projectID }">
					                  	<input type="hidden" class="form-control" name="modelID" value="${model.modelID}">
					                  	<input type="hidden" class="form-control" name="intent" value="update-model-attributes">
					                    
					                    <c:choose>
					                    	<c:when test="${model.customAttributes != '' || model.customAttributes != null }">
					                    		<c:set var="modelAttr" value="${model.customAttributes }"></c:set>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<c:set var="modelAttr" value="''"></c:set>
					                    	</c:otherwise>
					                    </c:choose>
					                    
					                    <textarea rows="10"  class="form-control h-100 font-weight-bold" name="customAttributes">
					                    	<c:forEach items="${variables }" var="v" varStatus="loop">
				                    			<c:choose><c:when test="${loop.index+1 < variables.size() }">${v},</c:when><c:otherwise>${v }</c:otherwise></c:choose>
				                    		</c:forEach>
					                    </textarea>
					                    
					                    <%--
					                    <textarea rows="10"  class="form-control h-100 font-weight-bold" name="customAttributes">
					                    	<c:choose>
					                    		<c:when test="${modelAttr != '' }">
					                    			${modelAttr }
					                    		</c:when>
					                    		<c:otherwise>
					                    			<c:forEach items="${variables }" var="v" varStatus="loop">
						                    			<c:choose><c:when test="${loop.index+1 < variables.size() }">${v},</c:when><c:otherwise>${v }</c:otherwise></c:choose>
						                    		</c:forEach>
					                    		</c:otherwise>
					                    	</c:choose>
					                    </textarea>
					                     --%>
					                  </div>
					                </div>
					                 
					                <div class="text-center">
					                  <button type="submit" class="btn btn-primary">CONTINUE</button>
					                  <button type="reset" class="btn btn-secondary d-none">Reset</button>
					                </div>
					              </form><!-- End Horizontal Form -->
					
					            </div>
					        </div>
                        </div>
                        <div class="col-md-4 mx-0 px-0 px-md-2"  style="height: 80vh; overflow-y: scroll;">
                            <div class="card">
                            	<div class="card-header">
                            		<strong>Pairing Table: <u>${model.table }</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<strong class="card-title">${table.columnList.size() } Attributes/ Columns</strong>
						              <!-- Table with stripped rows -->
						              <table class="table table-striped">
						                <thead>
						                  <tr>
						                    <th scope="col">#</th>
						                    <th scope="col">Column Name</th>
						                    <th scope="col">DATA TYPE</th>
						                  </tr>
						                </thead>
						                <tbody class="border-primary">
						                  <c:forEach items="${table.columnList }" var="col" varStatus="loop">
						                  <tr>
						                    <th scope="row">${loop.index + 1 }</th>
						                    <td>${col }</td>
						                    <td>${table.dataTypeList.get(loop.index) }</td>
						                  </tr>
						                  </c:forEach>
						                </tbody>
						              </table>
						              <!-- End Table with stripped rows -->
                            	</div>
                            </div>
                        </div>
                    </div>
                    <!-- /ROW -->
                </div>
                
                <div class="row px-0 mx-0">
                    	<h2 class="col-12 font-weight-bold p-2 mx-0 w-100" style="background: black; color: yellow">Code Output</h2>
                    	<div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-class">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Model class</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<%-- <pre class=""><code class="language-java java-code">${genClass }</code></pre> --%>
                            		<textarea class="form-control w-100" id="java-class-code" rows="50" style="height: 80vh;">${genClass }</textarea>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-create">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">CRUD Create</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre class="d-none"><code class="language-java">${genCRUD_Create}</code></pre>
                            		<form>
		                        		<textarea id="code" name="code" rows="50" style="height: "80vh;">${genCRUD_Create }</textarea>
		                        	</form>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-read">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">CRUD Read</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${genCRUD_Read_Single}</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-read-list">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">CRUD Read List</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${genCRUD_Read_List}</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-crud-update">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">CRUD Update</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${genCRUD_Update}</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-servlet-create">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Servlet: CREATE</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${servletCreate }</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-servlet-read">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Servlet: READ</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${servletRead }</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-servlet-read-list">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Servlet: READ /L</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${servletReadList }</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-servlet-update">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Servlet: UPDATE</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<pre><code class="language-java">${servletUpdate}</code></pre>
                            	</div>
                            </div>
                        </div>
                        
                        <div class="col-md-12 mx-0 px-0 px-md-2 mb-2" id="gen-servlet-update">
                            <div class="card">
                            	<div class="card-header bg-light">
                            		<strong class="card-title">Form: Create</u></strong>
                            	</div>
                            	<div class="card-body">
                            		<div class="btn-group">
                            			<a target="_blank" href="${pageContext.request.contextPath }/view/code/${modelID}" class="btn btn-sm btn-light">View HTML Code</a>
                            			<a target="_blank" href="${pageContext.request.contextPath }/view/code/plain/${modelID}" class="btn btn-sm btn-dark ml-3">View Formatted Code</a>
                            		</div>
                            		<div class="card mb-3 p-md-2">
                            			<textarea rows="" height="100%" class="form-control" style="background: #000; color: yellow; min-height: 90vh;">${formCreate}</textarea>
                            		</div>
                            		<%-- <pre class="language-html">${formCreate }</pre> --%>
                            		<textarea rows="" class="d-none form-control w-100 bg-dark p-md-3" style="background: #000; color: yellow; min-height: 50vh;">
                            			${formCreate.trim() }
                            		</textarea>
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
		
		var javaEditor = CodeMirror.fromTextArea(document.getElementById("java-class-code"), {
			mode: "text/x-java",
			lineNumbers: true,
		    indentWithTabs: true,
		    smartIndent: true,
		    matchBrackets: true,
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
		
		var mac = CodeMirror.keyMap.default == CodeMirror.keyMap.macDefault;
	    CodeMirror.keyMap.default[(mac ? "Cmd" : "Ctrl") + "-Space"] = "autocomplete";
	});
	
</script>

<%-- <script src="${assets }/themes/run_prettify.js"></script> --%>


<!--
<script type="text/javascript">//<![CDATA[
(function () {
  function htmlEscape(s) {
    return s
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');
  }

  // this page's own source code
  var quineHtml = htmlEscape('${formCreate}');
  
  /**
  	<!DOCTYPE html>\n<html>\n' +
    document.documentElement.innerHTML +
    '\n<\/html>
  */

  // Highlight the operative parts:
  quineHtml = quineHtml.replace(
    /&lt;script src[\s\S]*?&gt;&lt;\/script&gt;|&lt;!--\?[\s\S]*?--&gt;|&lt;pre\b[\s\S]*?&lt;\/pre&gt;/g,
    '<span class="operative">$&<\/span>');

  // insert into PRE
  document.getElementById("quine").innerHTML = quineHtml;
})();
//]]>
</script>
 <!-- 
 <script type="text/javascript">
 	$(document).ready(function() { 		
 		
 		//String ht = '${formCreate}';
 		
 		//$('#quine').text(ht); 
 	});
 	
 	function escapeHtml(str) {
	    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
	}
 </script> -->
</html>