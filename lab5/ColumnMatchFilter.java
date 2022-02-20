/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _colIndex1 = input.colNameToIndex(colName1);
        _colIndex2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colIndex1).equals(candidateNext().getValue(_colIndex2))) {
            return true;
        }
        return false;
    }

    private int _colIndex1;

    private int _colIndex2;
}
