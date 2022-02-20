/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colIndex = input.colNameToIndex(colName);
        _refStr = ref;
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colIndex).compareTo(_refStr) > 0) {
            return true;
        }
        return false;
    }

    private String _refStr;

    private int _colIndex;
}
