package com.abc.dao;

import java.util.List;

import com.abc.model.RetailModule;

public interface RetailAccessObject {
	void create(RetailModule product);
	RetailModule read(int product_id);
	List<RetailModule> getAll();
	void delete(int product_id);
}
