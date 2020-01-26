import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestResultSet extends AbstractResultSet {
    private Map<String, ArrayList<String>> stringFields = new HashMap<>();
    private Map<String, ArrayList<Integer>> intFields = new HashMap<>();
    private int rowsNumber = -1;
    int currentRow = -1;

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public void setIntField(String fieldName, ArrayList<Integer> values) throws Exception {
        if (values.size() == rowsNumber) {
            intFields.put(fieldName, values);
        } else {
            throw new Exception("Values array size doesn't match rows number");
        }
    }

    public void setStringField(String fieldName, ArrayList<String> values) throws Exception {
        if (values.size() == rowsNumber) {
            stringFields.put(fieldName, values);
        } else {
            throw new Exception("Values array size doesn't match rows number");
        }
    }

    public String getStringFromRow(String fieldName, int rowNumber) {
        ArrayList<String> array = stringFields.get(fieldName);
        if (array != null) {
            return array.get(rowNumber);
        }
        return "";
    }

    public int getIntFromRow(String fieldName, int rowNumber) {
        ArrayList<Integer> array = intFields.get(fieldName);
        if (array != null) {
            return array.get(rowNumber);
        }
        return 0;
    }

    @Override
    public boolean next() throws SQLException {
        if (currentRow >= rowsNumber - 1) return false;
        else {
            currentRow++;
            return true;
        }
    }

    @Override
    public String getString(String s) throws SQLException {
        ArrayList<String> array = stringFields.get(s);
        if (array != null) {
            return array.get(currentRow);
        }
        return null;
    }

    @Override
    public int getInt(String s) throws SQLException {
        ArrayList<Integer> array = intFields.get(s);
        if (array != null) {
            return array.get(currentRow);
        }
        return 0;

    }
}
