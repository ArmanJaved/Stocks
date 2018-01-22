package stocksymbolapp.Top.Losers;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class LosersDataModel {


    String asp_name;
    String mentions ;
    String positive;
    String negative;
    String netural;
    String status;
    String asp_rate;

    public LosersDataModel(String asp_name, String mentions, String positive, String negative, String netural, String status, String asp_rate) {
        this.asp_name = asp_name;
        this.mentions = mentions;
        this.positive = positive;
        this.negative = negative;
        this.netural = netural;
        this.status = status;
        this.asp_rate = asp_rate;
    }

    public String getAsp_name() {
        return asp_name;
    }

    public String getMentions() {
        return mentions;
    }

    public String getPositive() {
        return positive;
    }

    public String getNegative() {
        return negative;
    }

    public String getNetural() {
        return netural;
    }

    public String getStatus() {
        return status;
    }

    public String getAsp_rate() {
        return asp_rate;
    }
}
