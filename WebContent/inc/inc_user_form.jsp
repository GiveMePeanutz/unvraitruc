<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<label for="usernameUser">Username <span class="requis">*</span></label>
<input type="text" id="usernameUser" name="usernameUser" value="<c:out value="${user.username}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['usernameUser']}</span>
<br />

<label for="passwordUser">Password</label>
<input type="text" id="passwordUser" name="passwordUser" value="<c:out value="${user.password}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['passwordUser']}</span>
<br />

<label for="nameUser">Name <span class="requis">*</span></label>
<input type="text" id="nameUser" name="nameUser" value="<c:out value="${user.name}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['nameUser']}</span>
<br />

<label for="firstNameUser">First Name </label>
<input type="text" id="firstNameUser" name="firstNameUser" value="<c:out value="${user.firstName}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['firstNameUser']}</span>
<br />

<label for="sexUser">Sex </label>
<input type="radio" id="sexUser" name="sexUser" value="<c:out value="${user.sex}"/>" />Male 
<input type="radio" id="sexUser" name="sexUser" value="<c:out value="${user.sex}"/>" />Female
<span class="error">${form.errors['sexUser']}</span>
<br />

<label for="addressUser">Home Address<span class="requis">*</span></label>
<input type="text" id="addressUser" name="addressUser" value="<c:out value="${user.address}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['addressUser']}</span>
<br />

<label for="telephoneUser">Phone Number <span class="requis">*</span></label>
<input type="text" id="phoneUser" name="phoneUser" value="<c:out value="${user.phone}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['phoneUser']}</span>
<br />

<label for="emailUser">Email Address</label>
<input type="email" id="emailUser" name="emailUser" value="<c:out value="${user.email}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['emailUser']}</span>
<br />

<label for="birthUser">Date of Birth</label>
<input type="datetime" id="birthUser" name="birthUser" value="<c:out value="${user.birth}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['birthUser']}</span>
<br />

<label for="classUser">Class</label>
<input type="text" id="classUser" name="classUser" value="<c:out value="${user.class}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['classUser']}</span>
<br />

<label for="photoUser">Photo</label>
<input type="file" id="photoUser" name="photoUser" />
<span class="error">${form.errors['photoUser']}</span>
<br />

