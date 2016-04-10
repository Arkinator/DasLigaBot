package com.planed.ctlBot.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Entity
public class CtlMatch {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String player1;
    private String player2;
    private String winner;

    public CtlMatch() {
    }

    public CtlMatch(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }


    @Override
    public String toString() {
        return "CtlMatch{" +
                "id=" + id +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", winner='" + winner + '\'' +
                '}';
    }
}
