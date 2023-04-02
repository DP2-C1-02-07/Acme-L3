<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.job.list.label.code" path="code" width="10%"/>
	<acme:list-column code="any.job.list.label.title" path="title" width="10%"/>
	<acme:list-column code="any.job.list.label.anAbstract" path="anAbstract" width="10%"/>
	<acme:list-column code="any.job.list.label.courseType" path="courseType" width="10%"/>
	<acme:list-column code="any.job.list.label.retailPrice" path="retailPrice" width="10%"/>
	<acme:list-column code="any.job.list.label.furtherInformation" path="furtherInformation" width="10%"/>
</acme:list>
<acme:message code="info.text"/>