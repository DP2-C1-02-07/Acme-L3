<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.session.list.label.title" path="title"/>
	<acme:list-column code="company.session.list.label.startDate" path="startDate"/>
	<acme:list-column code="company.session.list.label.finishDate" path="finishDate"/>
</acme:list>

<acme:button code="company.session.list.button.create" action="/company/session/create"/>
<acme:button code="company.session.list.button.create.addendum" action="/company/session/create-addendum"/>