<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>


<acme:form>
	<acme:input-textbox code="authenticated.tutorial.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.title" path="title"/>
	<acme:input-textarea code="authenticated.tutorial.form.label.abstractTutorial" path="abstractTutorial"/>
	<acme:input-double code="authenticated.tutorial.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.title-course" path="course.title"/>
	
	<label>
		<acme:message code="authenticated.tutorial.form.label.assistant"/>
	</label>
	
	<acme:input-textarea code="authenticated.tutorial.form.label.supervisor" path="assistant.supervisor"/>
	<acme:input-textarea code="authenticated.tutorial.form.label.expertise-fields" path="assistant.expertiseFields"/>
	<acme:input-textarea code="authenticated.tutorial.form.label.assistant-resume" path="assistant.resume"/>
	<acme:input-textarea code="authenticated.tutorial.form.label.assistant-info" path="assistant.info"/>
	
</acme:form>