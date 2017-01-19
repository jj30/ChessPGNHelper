package bldg5.jj.pgnhelper.common;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Game implements Serializable {
    @SerializedName("Event")
    @Expose
    private String event;

    @SerializedName("Site")
    @Expose
    private String site;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Round")
    @Expose
    private String round;

    @SerializedName("Result")
    @Expose
    private String result;

    @SerializedName("WhiteELO")
    @Expose
    private String whiteelo;

    @SerializedName("BlackELO")
    @Expose
    private String blackelo;

    @SerializedName("White")
    @Expose
    private String white;

    @SerializedName("Black")
    @Expose
    private String black;

    @SerializedName("ECO")
    @Expose
    private String eco;

    @SerializedName("PGN")
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

    public String getBlack() {
        return black;
    }

    public void setBlack(String _black) {
        black = _black;
    }

    public String getBlackelo() {
        return blackelo;
    }

    public void setBlackelo(String _blackelo) {
        blackelo = _blackelo;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String _white) {
        white = _white;
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
