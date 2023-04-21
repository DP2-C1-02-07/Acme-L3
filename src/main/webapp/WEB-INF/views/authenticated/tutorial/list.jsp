<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.tutorial.list.label.code" path="code"/>
	<acme:list-column code="authenticated.tutorial.list.label.title" path="title"/>
	<acme:list-column code="authenticated.tutorial.list.label.abstractTutorial" path="abstractTutorial"/>
	<acme:list-column code="authenticated.tutorial.list.label.estimatedTotalTime" path="estimatedTotalTime"/>
</acme:list>
