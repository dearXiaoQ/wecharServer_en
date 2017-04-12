<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <%@taglib
		uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@taglib
		prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	<!DOCTYPE html>
	<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title></title>

<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc.css">
<link rel="stylesheet" type="text/css" href="css/showTip.css">

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/woxiangyao.js"></script>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>

<body>

	<div class="sjsc-title2">
		<h3 class="sjsc-t2l">confirm payment</h3>
		<a href="javascript:window.location.href='orderList.html'"
			class="sjsc-t2r"><img src="images/back.png" alt=""
			style="width:20px;height: 20px;padding-top: 11px;padding-left: 5px"></a>
	</div>

	<div class="my-dd">
		<div class="my-info">
			<c:forEach items="${map['list']}" var="list" varStatus="s">
				<input type="hidden" value="${list.order_id}" id="orderId">

				<c:set value="ord${s.index}" var="ord"></c:set>
				<div class="my-k1">
					<ul class="my-p1">

						<li class="my-spl f-l" id="orderId">Order Number：${list.order_id}</li>
						<li class="my-spr f-r">${list.add_time}</li>

						<div style="clear:both;"></div>
					</ul>


					<c:forEach items="${map[ord]}" var="ordList">
						<dl class="my-dl1">
							<dt>
								<a href="#"><img src="${ordList.goods_img}"
									style="width:70px"></a>
							</dt>
							<dd>
								<h3>
									<a href="#">${ordList.goods_name}</a>
								</h3>
								<p class="my-dp1">
									Price：<span>￥${ordList.goods_price}</span>
								</p>
								<div class="my-jdt">
									<p class="jdt-p1 f-l">Number：</p>

									<p class="jdt-shuzi f-l">${ordList.goods_num}</p>
									<div style="clear:both;"></div>
								</div>
							</dd>
							<div style="clear:both;"></div>
						</dl>
					</c:forEach>
					<!-- 
    <div class="drdd-info2">
    	<p class="p1 f-l">地址：<span >${list.addr_name}</span></p>
        <div style="clear:both;"></div>
    </div>  -->
					<!-- 
    <div class="drdd-info2">
    <p class="p1 f-l">配送方式：<span>
    	<c:if test="${list.receive==''}">快递</c:if>
    	<c:if test="${list.receive!=''}">自提点：${list.receive}</c:if></span></p>
        <div style="clear:both;"></div>
    </div>
     -->
					<c:if test="${!empty list.note}">
						<div class="drdd-info2">
							<p class="p1 f-l">
								Remarks：<span>${list.note}</span>
							</p>
							<div style="clear:both;"></div>
						</div>
					</c:if>
					<div class="my-p2">
						<span class="my-sp3 f-l">total：${list.goods_total_num}goods</span>
						<p class="my-sp3 f-r">totalled：￥${list.goods_total}</p>
						<div style="clear:both;"></div>
					</div>
				</div>
			</c:forEach>
		</div>
		<c:forEach items="${map['list']}" var="list">
			<c:if test="${list.goods_total==0}">
				<button class="drdd-btn"
					onclick="window.location.href='orderUpdate.html?order_id=${list.order_id}'">Confirm order</button>
			</c:if>
			<c:if test="${list.goods_total>0}">
				<button class="drdd-btn" onclick="callpay()">WeChat payment</button>
			</c:if>

		</c:forEach>
	</div>
	<script type="text/javascript">
    	wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "<%=request.getAttribute("appId")%>", 
    timestamp: "<%=request.getAttribute("timeStampConfig")%>",
    nonceStr: "<%=request.getAttribute("nonceStrConfig")%>",  // 必填，生成签名的随机串
    signature: "<%=request.getAttribute("signatureConfig")%>",// 必填，签名，见附录1
    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
  	function callpay(){
  	wx.scanQRCode({
    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    success: function (res) {
    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
   
   // alert(result);
   if(result.indexOf("号桌") > 0) {
   	
		setTimeout('alertInfo()', 1000);
    } else {
    	setTimeout("alert('Payment failure！')", 1000);
	}
 
}
});}

	
	function alertInfo() {
		var b = confirm('Confirm payment');
			if (b) {
					simulatePay();    		
				}
		}
		
		
	/** 微信支付 */
	function pay(){
			 WeixinJSBridge.invoke('getBrandWCPayRequest',{
			 "appId" : "<%=request.getAttribute("appId")%>","timeStamp" : "<%=request.getAttribute("timeStamp")%>", "nonceStr" : "<%=request.getAttribute("nonceStr")%>", "package" : "<%=request.getAttribute("package")%>","signType" : "MD5", "paySign" : "<%=request.getAttribute("paySign")%>" 
   			},function(res){
				WeixinJSBridge.log(res.err_msg);
	            if(res.err_msg == "get_brand_wcpay_request:ok"){  
	                alert("WeChat payment success!");  
	                window.location.href='orderList.html';
	            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
	                alert("User cancelled payment!");  
	            }else{  
	               alert("Payment failure!");  
	            }  
			});
	}
	
	/** 模拟微信支付 */
	function simulatePay(){
				 WeixinJSBridge.invoke('getBrandWCPayRequest',{
			 "appId" : "<%=request.getAttribute("appId")%>","timeStamp" : "<%=request.getAttribute("timeStamp")%>", "nonceStr" : "<%=request.getAttribute("nonceStr")%>", "package" : "<%=request.getAttribute("package")%>","signType" : "MD5", "paySign" : "<%=request.getAttribute("paySign")%>"
			}, function(res) {
				//WeixinJSBridge.log(res.err_msg);
				//    alert("微信支付成功！");
				modifyOrderStatus();

			});

		}

		/** 通知服务器修改订单状态（仅限模拟微信支付阶段） */
		function modifyOrderStatus() {
			var orderId = $('#orderId').val();
			$.ajax({
				url : 'modifyOrderStatus.html',
				type : 'post',
				data : 'order_id=' + orderId,
				success : function(rs) {
					var data = eval('(' + rs + ')');
					//console.log("modifyOrderStatus "  + data.rs_code + Date.parse(new Date()));
					if (data.rs_code == 1) { //修改订单状态
						setTimeout("alertFinaly()", 1000);
						//window.location.href = 'orderList.html';

					} else if (data.rs_code == 0) { //修改状态失败
						showTip("Payment failure！");
					}
				}
			});
		}

		function alertFinaly() {
			alert("WeChat payment success！");
			window.location.href = 'orderList.html';

		}
	</script>
</body>
	</html> </i>