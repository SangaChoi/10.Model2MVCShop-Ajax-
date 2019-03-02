package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public interface PurchaseDAO {
	
	public void insertPurchase(Purchase purchase) throws Exception;
	
	public List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception;

	public Purchase findPurchase(int tranNo) throws Exception;
	
	public void updatePurchase(Purchase purchase) throws Exception;
	
	public int getTotalCount(User user) throws Exception;
	
	public String makeCurrentPageSql(String sql , Search search);
	
	public void updateTranCode(Purchase purchase) throws Exception;
	
}
