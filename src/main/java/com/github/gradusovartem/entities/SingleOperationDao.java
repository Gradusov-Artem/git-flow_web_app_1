package com.github.gradusovartem.entities;

public class SingleOperationDao {
    private static final OperationDaoDB operationDao = new OperationDaoDB();

    public SingleOperationDao() {};

    public static OperationDaoDB getInstance() {
        return operationDao;
    }
}
