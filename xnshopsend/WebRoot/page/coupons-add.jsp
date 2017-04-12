<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc.css">
<script type="text/javascript" src="js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="css/showTip.css">
<script type="text/javascript" src="js/showTip.js"></script>
</head>
<body id="wrap">
    <div class="sjsc-title2">
    	<h3 class="sjsc-t2l">Cash Coupon</h3>
        <a href="javascript:history.back();" class="sjsc-t2r"><img src="images/back.png" alt="" style="width:20px;height: 20px;padding-top: 11px;padding-left: 5px"/></a>
    </div><br>
    <ul class="qhdj-ul1">
    	<li><input type="text" placeholder="请输入兑换码" id="cps_code"/></li>
    </ul>
    <br><br>
	<button class="drdd-btn" onclick="add()">Submit</button>
    
    	<script type="text/javascript">
	function add(){
		var cps_code= $('#cps_code').val();
		$.ajax({
			url:'cpsInsert.html',
			type:'post',
			data:'cps_code='+cps_code,
			success:function(rs){
				if(rs==1){
					showTip("Exchange success！");
					location.reload();
				}
				else if(rs==-1){
					showTip('You have redeemed the coupon！');
				}
				else if(rs==-5){
					showTip('Conversion code error or expired！');
				}
				else{
					showTip("fail！");
				}
			}
		})
	}
	</script>
</body>
</html>
