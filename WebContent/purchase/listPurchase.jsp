<%@ page contentType="text/html; charset=EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>���� �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">


	function fncGetList(currentPage) {
		//document.getElementById("currentPage").value = currentPage;
		$("#currentPage").val(currentPage)
		//document.detailForm.submit();
		$("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase?buyerId=${param.buyerId}").submit();
		
	}
	
	
	$(function() {
		
		$(".ct_list_pop td:nth-child(1)").on("click",function(){
			var tranNo=$(this).data('param1');
			//self.location="/purchase/getPurchase?tranNo="+tranNo;
			
			var i=$(this).text().trim();

			$.ajax(
					{
						url : "/purchase/json/getPurchase/"+tranNo,
						method : "GET" ,
						dataType : "json" ,
						headers : {
							"Accept" : "application/json",
							"Content-Type" : "application/json"
						},
						success : function(JSONData , status) {
							//alert(status);
							//alert("JSONData : \n"+JSONData);
							
							var displayValue = "<h3>"
							
								+"��ǰ��ȣ : "+JSONData.purchaseProd.prodNo+"<br/>"
								+"������ ���̵� "+JSONData.buyer.userId+"<br/>"
								+"���Ź�� : "+JSONData.paymentOption+"<br/>"
								+"������ �̸� : "+JSONData.receiverName+"<br/>"
								+"������ ����ó : "+JSONData.receiverPhone+"<br/>"
								+"������ �ּ� : "+JSONData.divyAddr+"<br/>"
								+"���ſ�û����: "+JSONData.divyRequest+"<br/>"
								+"��������: "+JSONData.receiverDate+"<br/>"
								+"�ֹ���: "+JSONData.orderDate+"<br/><br/>" 
								+"<a href='/purchase/updatePurchase?tranNo="+JSONData.tranNo+"'>[����]</a>"
								+"</h3>";
								
								alert(displayValue);
								
							$("h3").remove();
							$("#"+i+"").html(displayValue);
						}
				
			});
			
		})
		
		$(".ct_list_pop td:nth-child(3)").on("click",function(){
			var userId=$(this).data('param1');
			self.location="/user/getUser?userId="+userId;
		})
		
		$(".ct_list_pop td:nth-child(11)").on("click",function(){
			var tranNo=$(this).data('param1');
			var userId=$(this).data('param2');
			self.location="/purchase/updateTranCode?tranNo="+tranNo+"&tranCode=3&buyerId="+userId;
		})
		

	});
	
	
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<!--<form name="detailForm" action="/purchase/listPurchase?buyerId=${param.buyerId}" method="post">-->
<form name="detailForm">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">���� �����ȸ</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">��ü ${resultPage.totalCount} �Ǽ�, ����  ${resultPage.currentPage} ������</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ��ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��ȭ��ȣ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">�����Ȳ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��������</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	<c:set var="i" value="0" />
	<c:forEach var="purchase" items="${list}">
		<c:set var="i" value="${ i+1 }" />
		<tr class="ct_list_pop">
		<td align="center" data-param1="${purchase.tranNo}" >
			<!--<a href="/purchase/getPurchase?tranNo=${purchase.tranNo}">${ i }</a>-->
			${ i }			
		</td>
		<td></td>
		<td align="left" data-param1="${purchase.buyer.userId}">
			<!--<a href="/user/getUser?userId=${purchase.buyer.userId}">${purchase.buyer.userId}</a>-->
			${purchase.buyer.userId}
		</td>
		<td></td>
		<td align="left">${purchase.receiverName}</td>
		<td></td>
		<td align="left">${purchase.receiverPhone}</td>
		<td></td>
		<td align="left">
		
		<c:if test="${purchase.tranCode=='1  '}">
			���� ���ſϷ� �����Դϴ�.
		</c:if>
		<c:if test="${purchase.tranCode=='2  '}">
			���� ����� �����Դϴ�.
		</c:if>
		<c:if test="${purchase.tranCode=='3  '}">
			���� ��ۿϷ� �����Դϴ�.
		</c:if>
	
		</td>
		<td></td>
		<td id="tranCode3" align="left" data-param1="${purchase.tranNo}" data-param2="${user.userId}">
		
		<c:if test="${purchase.tranCode=='2  '}">
			<!--<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=3&buyerId=${user.userId}">���ǵ���</a>-->
			���ǵ���
		</c:if>
		<c:if test="${purchase.tranCode=='3  '}">
			
		</c:if>
		
		
		</td>
	</tr>
	<tr>
		<td id="${i}"colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	
	</c:forEach>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center">
		<input type="hidden" id="currentPage" name="currentPage" value=""/>
		 
			<jsp:include page="../common/pageNavigator.jsp"/>
		
		</td>
	</tr>
</table>

<!--  ������ Navigator �� -->
</form>

</div>

</body>
</html>