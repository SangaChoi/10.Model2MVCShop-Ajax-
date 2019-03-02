<%@ page contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">


function fncGetList(currentPage) {
	$("#currentPage").val(currentPage)
	
	$("form").attr("method" , "POST").attr("action" , "/product/listProduct?menu=${param.menu}").submit();
}

$(function() {
	
	 $( "td.ct_btn01:contains('검색')" ).on("click" , function() {

		//alert(  $( "td.ct_btn01:contains('검색')" ).html() );
		fncGetList(1);
	})
	
	
	$(".ct_list_pop td:nth-child(3)").on("click",function(){
	var prodNo=$(this).data('param1');
	var proTranCode=$(this).data('param2');
	
	if(${param.menu=='manage'}){
		self.location ="/product/updateProduct?prodNo="+prodNo+"&menu=${param.menu}";
		
	}else if(${param.menu=='search'} && proTranCode=='0'){
		//self.location ="/product/getProduct?prodNo="+prodNo+"&menu=${param.menu}";
		
		var prodName = $(this).text().trim();
		
		$.ajax(
				{
					url : "/product/json/getProduct/"+prodNo+"?menu=${param.menu}",
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
							+"상품명 : "+JSONData.prodName+"<br/>"
							+"상품상세정보: "+JSONData.prodDetail+"<br/>"
							+"제조일자 : "+JSONData.manuDate+"<br/>"
							+"가격 : "+JSONData.price+"<br/>"
							+"상품이미지 : "+
							"<img src='/images/uploadFiles/"+JSONData.fileName+"' align='absmiddle'/>"
							+"<br/><br/>"							
							+"<a href='/purchase/addPurchase?prodNo="+JSONData.prodNo+"'>[구매]</a>"
							+"</h3>";
							
						$("h3").remove();
						$("#"+prodName+"").html(displayValue);
					}
			
		});

	}
	
	});
	
	$(".ct_list_pop:nth-child(4n+6)" ).css("background-color" , "whitesmoke");
	
	
	$("#tranCode2").on("click",function(){
		var prodNo=$(this).data('param1');
		self.location ="/purchase/updateTranCodeByProd?prodNo="+prodNo+"&tranCode=2";
	});
	
	
});





</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>

				<c:if test="${param.menu=='manage'}">
					<td width="93%" class="ct_ttl01">					
					상품 관리					
					</td>
				</c:if>
				
				<c:if test="${param.menu=='search'}">
					<td width="93%" class="ct_ttl01">					
					상품 목록조회				
					</td>
				</c:if>		
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
	
	
	
		<td align="right">
		
			<select name="searchCondition" class="ct_input_g" style="width:80px">

				<option value="0" ${! empty search.searchCondition && search.searchCondition==0 ? "selected" : ""}>상품번호</option>
				<option value="1" ${! empty search.searchCondition && search.searchCondition==1 ? "selected" : ""}>상품명</option>
				<option value="2" ${! empty search.searchCondition && search.searchCondition==2 ? "selected" : ""}>상품가격</option>
					
			</select>
			<input type="text" name="searchKeyword"  value="${! empty search.searchKeyword ? search.searchKeyword : ''}" 
			class="ct_input_g" style="width:200px; height:19px" />
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<!--<a href="javascript:fncGetList('1');">검색</a>-->
						검색
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격
		</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	<c:set var="i" value="0" />
	<c:forEach var="product" items="${list}">
		<c:set var="i" value="${ i+1 }" />
		<tr class="ct_list_pop">
			<td align="center">${ i }</td>
			<td></td>
			<td align="left" data-param1="${product.prodNo}" data-param2="${product.proTranCode}">
			<img src="/images/uploadFiles/${product.fileName}" width="100" height="80" align="absmiddle"/>
			${product.prodName}
			<!-- <c:if test="${param.menu=='manage'}">
				<a href="/product/updateProduct?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
				${product.prodName}
				</c:if>
				
				<c:if test="${param.menu=='search' && product.proTranCode!='0'}">
				${product.prodName}
				</c:if>
				
				<c:if test="${param.menu=='search' && product.proTranCode=='0'}">
				<a href="/product/getProduct?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
				</c:if>
			-->	
			</td>	
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<td align="left">${product.regDate}</td>
			<td></td>
			<td align="left">
		
		<c:choose>
			<c:when test="${product.proTranCode=='0'}">
				판매중
			</c:when>
			
			<c:when test="${param.menu=='manage'}">
				<c:if test="${product.proTranCode=='1  '}">
					구매완료 
					<!-- <a href="/purchase/updateTranCodeByProd?prodNo=${product.prodNo}&tranCode=2">배송하기</a>-->
					<span id="tranCode2" data-param1="${product.prodNo}">배송하기</span>
				</c:if>
				<c:if test="${product.proTranCode=='2  '}">
					배송중
				</c:if>
				<c:if test="${product.proTranCode=='3  '}">
					배송완료
				</c:if>			
			</c:when>
			
			<c:when test="${param.menu=='search' && user.role=='admin'}">
				<c:if test="${product.proTranCode=='1  '}">
					구매완료
				</c:if>
				<c:if test="${product.proTranCode=='2  '}">
					배송중
				</c:if>
				<c:if test="${product.proTranCode=='3  '}">
					배송완료
				</c:if>	
			</c:when>
			
			<c:otherwise>
				재고없음
			</c:otherwise>	
		</c:choose>
	
			</td>	
		</tr>
		<tr>
			<!-- <td colspan="11" bgcolor="D6D7D6" height="1"></td>-->
			<td id="${product.prodName}" colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>			
	</c:forEach>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
		<input type="hidden" id="currentPage" name="currentPage" value=""/>

			<jsp:include page="../common/pageNavigator.jsp"/>	
			
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
