package cz.ictsystem.lib;

import java.util.Hashtable;

public class DbStructure {
	Hashtable<Integer, Table> mTables;
	
	public DbStructure (){
		mTables = new Hashtable<Integer, Table>();
	}

	public DbStructure addTable(Table table){
		mTables.put(table.getNameId(), table);
		return this;
	}
	
	public Table getTable(int name){
		return mTables.get(name);
	}
	
	public Hashtable<Integer, Table> getTables(){
		return mTables;
	}
	
}
