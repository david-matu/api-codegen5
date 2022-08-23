<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="assets" scope="page" value="/assets"></c:url> 

<!-- Mini Modal -->
                    <div class="modal fade modal-large modal-primary" id="notification-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header justify-content-center">
                                    <div class="modal-profile">
                                        <i class="nc-icon nc-bulb-63"></i>
                                    </div>
                                </div>
                                <div class="modal-body text-center">
                                    <pre id="modal-content"></pre>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-link btn-simple d-none">Back</button>
                                    <button type="button" class="btn btn-success btn-fill pull-right float-right btn-simple" data-dismiss="modal">OKAY</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--  End Modal -->

<footer class="footer" style="background-color: #000;">
                <div class="container-fluid">
                    <nav>
                        <ul class="footer-menu">
                            <li>
                                <a href="#">
                                    Home
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    Company
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    Portfolio
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    Blog
                                </a>
                            </li>
                        </ul>
                        <p class="copyright text-center">
                            Template provided by <a href="http://www.creative-tim.com">Creative Tim</a>, made with love for a better web
                        </p>
                    </nav>
                </div>
            </footer>
            <div class="container-fluid" style="background: #000; color: #FFF; font-weight: bolder; text-align: center;">
            	©
                <script>
                    document.write(new Date().getFullYear())
                </script>
                <a href="http://destine.co.ke/dave" class="font-weight-bold" style="color: #0F0;">Dave Co.</a><br>
            </div>