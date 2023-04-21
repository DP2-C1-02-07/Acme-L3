<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.course.list.label.code" path="code"/>
	<acme:list-column code="any.course.list.label.title" path="title"/>
	<acme:list-column code="any.course.list.label.anAbstract" path="anAbstract"/>
</acme:list>
