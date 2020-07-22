package com.definesys.dsgc.service.svcgen.bean.db;

public class TblDTO {
    private String tblName;
    private String[] pkCols;
    private ColDTO[] cols;

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public String[] getPkCols() {
        return pkCols;
    }

    public void setPkCols(String[] pkCols) {
        this.pkCols = pkCols;
    }

    public ColDTO[] getCols() {
        return cols;
    }

    public void setCols(ColDTO[] cols) {
        this.cols = cols;
    }
}
