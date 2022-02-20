/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colIndex = input.colNameToIndex(colName);
        _matchStr = match;
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colIndex).equals(_matchStr)) {
            return true;
        }
        return false;
    }

    private String _matchStr;

    private int _colIndex;
}
