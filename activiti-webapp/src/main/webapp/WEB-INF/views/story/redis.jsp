<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>综合演示用例</title>
	<script>
		$(document).ready(function() {
			$("#account-tab").addClass("active");
		});
	</script>
</head> 
		
<body>
	<h1>Redis 缓存策略演示 当前 redis数据信息(数量:${dbsize} 连接情况:${ping})</h1>
	<ul>
	<li>暂时取消对于spring 配置。可以修改 activiti-common-extension 配置文件applicationContext-common-extension.xml</li>
	</ul>
	<h2>redis演示 添加</h2>
	<form:form id="defaultLoggerForm" action="${ctx}/story/redis/save" method="post" class="form-horizontal">
		<div class="control-group">
			<label for="rootLoggerLevel" class="control-label">key:</label>
			<div class="controls">
					<input type="text" name="redisKey" value="" class="span3"/>
			</div> 
		</div>
		<div class="control-group">
			<label for="rootLoggerLevel" class="control-label">value:</label>
			<div class="controls">
					<input type="text" name="redisValue" value="" class="span3"/>
			</div>
		</div>		 
		<div class="form-actions">
			<input type="submit" value="save" class="btn btn-primary"/>
		</div>
	</form:form>
	
	<h2>redis演示 删除</h2>
	<form:form id="anyLoggerForm" action="${ctx}/story/redis/del" method="post" class="form-horizontal">
		<div class="control-group">
			<label for="loggerName" class="control-label">key:</label>
			<div class="controls">
				<input type="text" name="redisKey" value="" class="span3"/>
			</div>
		</div>
		<div class="form-actions">
			<input type="submit" value="save" class="btn btn-primary"/>
		</div>
	</form:form>
	 
</body>
</html>
