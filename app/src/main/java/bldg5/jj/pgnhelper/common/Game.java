package bldg5.jj.pgnhelper.common;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {
    private Player white;
    private Player black;

    @SerializedName("_gameId")
    @Expose
    private int _gameId;

    @SerializedName("event")
    @Expose
    private String event;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("round")
    @Expose
    private String round;

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("whiteelo")
    @Expose
    private String whiteelo;

    @SerializedName("blackelo")
    @Expose
    private String blackelo;

    @SerializedName("eco")
    @Expose
    private String eco;

    @SerializedName("pgn")
    @Expose
    private String pgn;

    public String getEvent() {
        return event;
    }

    public void setEvent(String _event) {
        event = _event;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String _site) {
        site = _site;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String _date) {
        date = _date;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String _round) {
        round = _round;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String _result) {
        result = _result;
    }

    public String getWhiteelo() {
        return whiteelo;
    }

    public void setWhiteelo(String _whiteelo) {
        whiteelo = _whiteelo;
    }

    public String getBlackelo() {
        return blackelo;
    }

    public void setBlackelo(String _blackelo) {
        blackelo = _blackelo;
    }

    public String getEco() {
        return eco;
    }

    public void setEco(String _eco) {
        eco = _eco;
    }

    public String getPgn() {
        return pgn;
    }

    public void setPgn(String _pgn) {
        pgn = _pgn;
    }

}
