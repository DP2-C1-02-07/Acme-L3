<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2>
	<acme:message code="administrator.systemConfiguration.form.title"/>
</h2>

<acme:form>
	<acme:input-textbox code="administrator.systemConfiguration.form.label.systemCurrency" path="systemCurrency"/>
	<acme:input-textbox code="administrator.systemConfiguration.form.label.acceptedCurrencies" path="acceptedCurrencies"/>
	
	<acme:submit code="administrator.systemConfiguration.form.button.update" action="/administrator/system-configuration/update"/>	
</acme:form>