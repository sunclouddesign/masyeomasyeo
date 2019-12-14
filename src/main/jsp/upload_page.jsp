<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sping" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html" ; charset="ISO-885-1">
    <title>Upload Page</title>
</head>

<body>
    <spring:url value="/doUpload" var="doUploadURL" />
    <form:form mehtod="post" modelAttribute="formUpload" action="${doUpload}" enctype="multipart/form-data">
        <form:input path="files" type="file" multiple="multiple" />
        <form:errors path="files" /><br />

        <button type="submit">Upload</button>

    </form:form>
</body>

</html>