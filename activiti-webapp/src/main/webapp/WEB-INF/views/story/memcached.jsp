<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
	<title>Memcached演示</title>
	<script>
		$(document).ready(function() {
			$("#schedule-tab").addClass("active");
		});
	</script>
</head>

<body>
	<h1>Memcached演示</h1>

	<h2>技术说明：</h2>
	<ul>
		<li>可直接调用getClient()取出Spy的原版MemcachedClient来使用.</li>
		<li>操作方法类似于redis</li>
		<li>暂时取消对于spring 配置。可以修改 activiti-common-extension 配置文件applicationContext-common-extension.xml</li>
		<li><import resource="cache/applicationContext-memcached.xml" /></li>
	</ul>

	<h2>nosql 故事：</h2>
	<ul>
		<li>大部分noSQl 数据库都类似.一般在于分布式的环境部署。 后期设计分布式</li>
	</ul>
</body>
</html>