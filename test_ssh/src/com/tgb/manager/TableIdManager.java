package com.tgb.manager;

import java.util.List;

import com.tgb.entity.TableID;
import com.tgb.entity.User_T;

public interface TableIdManager {

	public TableID getTable(String tableName);
	
	public boolean updateTableId(TableID tableID );
}
