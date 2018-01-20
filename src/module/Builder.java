package module;

import java.sql.*;

public class Builder {
    private enum DeleteType { HARD, SOFT }

    private final Connection con = Connector.getConnection();
    private PreparedStatement stmt = null;
    private String sql = null;
    private ResultSet tableColumns = null;

    public Boolean insert(String table, String[] column, String[] value) {
        try {
            sql = "INSERT INTO "+table+" VALUES {"+this.makeParamInsert(column)+")";

            tableColumns = this.getTableColumns(table);

            stmt = con.prepareStatement(sql);

            this.executeData(tableColumns, column, value);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(String table, String primary, Integer primaryValue) {
        try {
            sql = "DELETE FROM "+table+" WHERE "+primary+" = ?";

            stmt = con.prepareStatement(sql);

            this.executeData(DeleteType.HARD, primaryValue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean softDelete(String table, String primary, Integer primaryValue){
        try {
            sql = "UPDATE "+table+" SET is_delete = ? WHERE "+primary+" = ?";

            stmt = con.prepareStatement(sql);

            this.executeData(DeleteType.SOFT, primaryValue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ResultSet getTableColumns(String table) throws SQLException {
        DatabaseMetaData metadata = con.getMetaData();
        return metadata.getColumns(null, null, table, null);
    }

    private void executeData(DeleteType deleteType, Integer value) throws SQLException {
        switch (deleteType) {
            case HARD:
                stmt.setInt(1, value);
                break;
            case SOFT:
                stmt.setString(1, "1");
                stmt.setInt(2, value);
                break;
        }
        stmt.executeUpdate();
    }

    private void executeData(ResultSet tableColumns, String[] column, String[] value) throws SQLException {
        for (int i = 0; i < column.length; i++) {
            while (tableColumns.next()) {
                if (column[i] == tableColumns.getString("COLUMN_NAME")) {
                    this.inputData(i, tableColumns.getString("TYPE_NAME"), value[i]);
                }
            }
        }
        stmt.execute();
    }

    private void executeData(ResultSet tableColumns, String[] column, String[] value, String keyValue) throws SQLException {
        for (int i = 0; i < column.length; i++) {
            while (tableColumns.next()) {
                if (column[i] == tableColumns.getString("COLUMN_NAME")) {
                    this.inputData(i, tableColumns.getString("TYPE_NAME"), value[i]);
                }
            }
        }
        this.inputData(column.length, DataType.VARCHAR.toString(), keyValue);
        stmt.executeUpdate();
    }

    private void inputData(Integer index, String dataType, String value) {

    }

    private String makeParamInsert(String[] column) {
        String prepare = "";
        for(int i = 0; i < column.length; i++) {
            if(i < column.length -1){
                prepare += "?, ";
            }

            if(i == column.length -1){
                prepare += "? ";
            }
        }
        return prepare;
    }
}
