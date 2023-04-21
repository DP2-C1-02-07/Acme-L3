<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.lecturer.form.label.link" path="link"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.statement" path="statement"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.weakFeatures" path="weakFeatures"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.strongFeatures" path="strongFeatures"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.lecturer.form.button.create" action="/authenticated/student/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.lecturer.form.button.update" action="/authenticated/student/update"/>
</acme:form>