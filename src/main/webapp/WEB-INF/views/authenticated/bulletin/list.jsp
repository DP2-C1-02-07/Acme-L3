<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>



<acme:list>

	<acme:list-column code="authenticated.bulletin.form.label.instantiationMoment" path="instantiationMoment"/>
	<acme:list-column code="authenticated.bulletin.form.label.title" path="title"/>
	<acme:list-column code="authenticated.bulletin.form.label.flag" path="flag"/>


</acme:list>
