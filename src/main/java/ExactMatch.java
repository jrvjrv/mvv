
public class ExactMatch implements IWhiteListMatch {

    private final String _matchString;


    public ExactMatch( String matchString ) {
        _matchString = matchString;
    }

    @Override public boolean isMatch( String testString ) {
        return testString.equals( _matchString );
    }
}