<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.banner.form.label.instantiationMoment" path="instantationMoment"/>	
	<acme:list-column code="administrator.banner.form.label.displayStartMoment" path="displayStartMoment"/>
	<acme:list-column code="administrator.banner.form.label.displayEndMoment" path="displayEndMoment"/>
	<acme:list-column code="administrator.banner.form.label.pictureLink" path="pictureLink"/>
	<acme:list-column code="administrator.banner.form.label.slogan" path="slogan"/>
	<acme:list-column code="administrator.banner.form.label.documentLink" path="documentLink"/>
	
</acme:list>

<acme:button code="administrator.banner.form.button.create" action="/administrator/banner/create"/>