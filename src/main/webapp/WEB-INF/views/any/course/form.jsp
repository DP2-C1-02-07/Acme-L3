<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.job.form.label.code" path="code"/>
	<acme:input-textbox code="any.job.form.label.title" path="title"/>
	<acme:input-textbox code="any.job.form.label.anAbstract" path="anAbstract"/>
	<acme:input-textbox code="any.job.form.label.courseType" path="courseType"/>
	<acme:input-money code="any.job.form.label.retailPrice" path="retailPrice"/>
	<acme:input-url code="any.job.form.label.furtherInformation" path="furtherInformation"/>
</acme:form>