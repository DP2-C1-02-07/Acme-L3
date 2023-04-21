<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="authenticated.bulletin.form.label.title" path="title"/>
		<acme:input-textarea code="authenticated.bulletin.form.label.instantiationMoment" path="instantiationMoment"/>
	
	<acme:input-textarea code="authenticated.bulletin.form.label.message" path="message"/>
	<acme:input-url code="authenticated.bulletin.form.label.link" path="link"/>
		<acme:input-textarea code="assistant.tutorial.form.label.flag" path="flag"/>
		<acme:input-checkbox code="Confirm" path="confirmation"/>
	
			<acme:submit code="administrator.offer.form.button.create" action="/administrator/bulletin/create"/>

</acme:form>