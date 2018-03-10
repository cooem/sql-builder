package module;

import java.sql.*;

public class Builder {
    private enum DeleteType {HARD, SOFT}

    private final Connection con = Connector.getConnection();
    private PreparedStatement stmt = null;
    private String sql = null;
    private ResultSet tableColumns = null;

    public Boolean insert(String table, String[] column, String[] value) {
        try {
            sql = "INSERT INTO " + table + " VALUES {" + this.makeParamInsert(column) + ")";

            tableColumns = this.getTableColumns(table);

            stmt = con.prepareStatement(sql);

            this.executeData(tableColumns, column, value);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(String table, String[] column, String[] value, Integer primaryValue) {
        try {
            sql = "UPDATE " + table + " SET " + this.makeParamUpdate(column) + " WHERE " + primaryValue + " = ?";

            tableColumns = this.getTableColumns(table);

            stmt = con.prepareStatement(sql);

            this.executeData(tableColumns, column, value, primaryValue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(String table, String primary, Integer primaryValue) {
        try {
            sql = "DELETE FROM " + table + " WHERE " + primary + " = ?";

            stmt = con.prepareStatement(sql);

            this.executeData(DeleteType.HARD, primaryValue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean softDelete(String table, String primary, Integer primaryValue) {
        try {
            sql = "UPDATE " + table + " SET is_delete = ? WHERE " + primary + " = ?";

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
                    this.inputData(i + 1, tableColumns.getString("TYPE_NAME"), value[i]);
                }
            }
        }
        stmt.execute();
    }

    private void executeData(ResultSet tableColumns, String[] column, String[] value, Integer keyValue) throws SQLException {
        for (int i = 0; i < column.length; i++) {
            while (tableColumns.next()) {
                if (column[i] == tableColumns.getString("COLUMN_NAME")) {
                    this.inputData(i + 1, tableColumns.getString("TYPE_NAME"), value[i]);
                }
            }
        }
        this.inputData(column.length + 1, DataType.VARCHAR.toString(), String.valueOf(keyValue));
        stmt.executeUpdate();
    }

    private void inputData(Integer index, String dataType, String value) throws SQLException {
        if (dataType.equals("CHAR") || dataType.equals("VARCHAR") || dataType.equals("TEXT") || dataType.equals("TINYTEXT") || dataType.equals("MEDIUMTEXT") || dataType.equals("LONGTEXT")) {
            stmt.setString(index, value);
        } else if (dataType.equals("TINYINT")) {
            stmt.setShort(index, Short.parseShort(value));
        } else if (dataType.equals("SMALLINT") || dataType.equals("MEDIUMINT")) {
            stmt.setInt(index, Integer.parseInt(value));
        } else if (dataType.equals("INT") || dataType.equals("BIGINT")) {
            stmt.setLong(index, Long.parseLong(value));
        } else if (dataType.equals("DOUBLE")) {
            stmt.setDouble(index, Double.parseDouble(value));
        } else if (dataType.equals("FLOAT")) {
            stmt.setFloat(index, Float.parseFloat(value));
        }
    }

    private String makeParamInsert(String[] column) {
        String prepare = "";
        for (int i = 0; i < column.length; i++) {
            if (i < column.length - 1) {
                prepare += "?, ";
            }

            if (i == column.length - 1) {
                prepare += "? ";
            }
        }
        return prepare;
    }

    private String makeParamUpdate(String[] column){
        String prepare = "";
        for(int i = 0; i < column.length; i++) {
            if(i < column.length -1){
                prepare += column[i]+" = ?,";
            }

            if(i == column.length -1){
                prepare += column[i]+" = ? ";
            }
        }
        System.out.println(prepare);
        return prepare;
    }
}
