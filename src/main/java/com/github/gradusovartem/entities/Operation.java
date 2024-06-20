package com.github.gradusovartem.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * class Operation - содержит составляющие операции
 */
public class Operation {
    private int id;
    private String comment;
    private LocalDateTime dt_operation;
    private int oper_1;
    private int oper_2;
    private String operation;
    private int result;

    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern ( "M/d/yy H:mm" );

    public Operation(){

    }

    /**
     * constructor Operation
     */
    public Operation(Operation data) {
        this.id = data.getId();
        this.comment = data.getComment();
        this.dt_operation = LocalDateTime.parse(LocalDateTime.now().format(ISO_FORMATTER), ISO_FORMATTER);
        this.oper_1 = data.getOper_1();
        this.oper_2 = data.getOper_2();
        this.operation = data.getOperation();
        this.result = data.getResult();
    }

    /**
     * method getId возвращает id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * method getCommand возвращает comment
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * method getOper_1 возвращает oper_1
     * @return
     */
    public int getOper_1() {
        return oper_1;
    }

    /**
     * method getOper_2 возвращает oper_2
     * @return
     */
    public int getOper_2() {
        return oper_2;
    }

    /**
     * method getOperation возвращает operation
     * @return
     */
    public String getOperation() {
        return operation;
    }

    public LocalDateTime getDt_operation() {
        return dt_operation;
    } // LocalDateTime
    public int getResult() {
        return result;
    }

    /**
     * method setId записывает id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * method setComment записывает comment
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * method setOper_1 записывает oper_1
     * @param oper_1
     */
    public void setOper_1(int oper_1) {
        this.oper_1 = oper_1;
    }

    /**
     * method setOper_2 записывает oper_2
     * @param oper_2
     */
    public void setOper_2(int oper_2) {
        this.oper_2 = oper_2;
    }

    /**
     * method setOperation записывает operation
     * @param operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setDt_operation(LocalDateTime dt_operation) {
        this.dt_operation = dt_operation;
    }
    public void setResult(int result) {
        this.result = result;
    }

    public void setMethod(int i, Object o) {
        if (i == 1) {
            setId((Integer) o);
        }
        else if (i == 2) {
            setComment((String) o);
        }
        else if (i == 3) {
            setDt_operation((LocalDateTime) o);
        }
        else if (i == 4) {
            setOper_1((Integer) o);
        }
        else if (i == 5) {
            setOper_2((Integer) o);
        }
        else if (i == 6) {
            setOperation((String) o);
        }
        else if (i == 7) {
            setResult((Integer) o);
        }
    }
}
