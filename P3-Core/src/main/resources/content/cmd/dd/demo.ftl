<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>plugin-web-framework</title>
		<script type="text/javascript" src="../plug-in/js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
  			$("#aa").click(function(){alert('hello world')});
  	    });
		</script>
	</head>
	<body>
		<div><h1>Freemarker 页面</h1></div><br><br>
		
		<img alt="" src="${request.contextPath}/plug-in/image/jeecg.png"><br><br>
		
		<a href="#" id="aa">点击我</a><br><br>
	</body>
</html>