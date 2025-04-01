package ca.bcit.comp2522.gameproject.mastermind;

public interface PlayerAction 
{
    public static final class TruthScanRequest implements PlayerAction
    {
        public TruthScanRequest(){}
    }

    public static final class GuessSummaryRequest implements PlayerAction
    {}
}
