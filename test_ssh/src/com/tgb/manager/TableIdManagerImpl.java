package com.tgb.manager;

import java.util.List;

import com.tgb.dao.TableIdDao;
import com.tgb.dao.UserDao;
import com.tgb.entity.TableID;
import com.tgb.entity.User_T;

public class TableIdManagerImpl implements TableIdManager {

	private TableIdDao tableIdDao;
	

	public void setTableIdDao(TableIdDao tableIdDao) {
		this.tableIdDao = tableIdDao;
	}

	@Override
	public TableID getTable(String tableName) {
		return tableIdDao.getTable(tableName);
	}

	@Override
	public boolean updateTableId(TableID tableID) {
		return tableIdDao.updateTableId(tableID);
	}

	

}
