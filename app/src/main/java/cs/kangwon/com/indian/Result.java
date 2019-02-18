package cs.kangwon.com.indian;

/**
 * Created by Administrator on 2015-12-02.
 */
public class Result{
    String Round;
    String result;
    String myCard;
    String yourCard;
    String chip;
    public Result(String R, String result, String myCard, String yourCard, String chip){
        this.Round = R;
        this.result = result;
        this.myCard = myCard;
        this.yourCard = yourCard;
        this.chip = chip;
    }
    public String getRound(){
        return Round;
    }
    public void setRound(String R){
        this.Round = R;
    }
    public String getResult(){
        return result;
    }
    public void setResult(String result){
        this.result = result;
    }
    public String getMyCard(){
        return myCard;
    }
    public void setMyCard(String myCard){
        this.myCard = myCard;
    }
    public String getYourCard(){
        return yourCard;
    }
    public void setYourCard(String yourCard){
        this.yourCard = yourCard;
    }
    public String getChip(){
        return chip;
    }
    public void setChip(String chip){
        this.chip = chip;
    }
}
