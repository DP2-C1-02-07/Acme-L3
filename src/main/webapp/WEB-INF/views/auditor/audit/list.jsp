<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.audit.form.label.code" path="code"/>
	<acme:list-column code="auditor.audit.form.label.conclusion" path="conclusion"/>
	<acme:list-column code="auditor.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:list-column code="auditor.audit.form.label.weakPoints" path="weakPoints"/>
</acme:list>

<acme:button code="auditor.audit.form.button.create" action="/auditor/audit/create"/>