/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colIndex = input.colNameToIndex(colName);
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colIndex).contains(_subStr)) {
            return true;
        }
        return false;
    }

    private String _subStr;

    private int _colIndex;
}
