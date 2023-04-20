<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="lecturer.courseLecture.form.label.course.code" path="courseCode" readonly="true"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:input-select code="lecturer.courseLecture.form.label.lecture.create" path="lecture" choices="${lectures}"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'delete'}">
		<acme:input-select code="lecturer.courseLecture.form.label.lecture.delete" path="lecture" choices="${lectures}"/>
	</jstl:if>
	
	<acme:submit test="${_command == 'create'}" code="lecturer.courseLecture.form.button.create" action="/lecturer/course-lecture/create?courseId=${courseId}"/>
	<acme:submit test="${_command == 'delete'}" code="lecturer.courseLecture.form.button.delete" action="/lecturer/course-lecture/delete?courseId=${courseId}"/>
</acme:form>