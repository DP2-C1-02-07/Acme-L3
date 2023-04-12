<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.practica.list.label.code" path="code"/>
	<acme:list-column code="authenticated.practica.list.label.title" path="title"/>
	<acme:list-column code="authenticated.practica.list.label.estimatedTime" path="estimatedTime"/>
</acme:list>

<acme:button code="company.practicum.list.button.create" action="/company/practicum/create"/>
