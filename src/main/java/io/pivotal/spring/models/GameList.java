package io.pivotal.spring.models;

import java.util.List;

public class GameList {

    private int count;

    private List<Game> games;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameList gameList1 = (GameList) o;

        if (count != gameList1.count) return false;
        return games != null ? games.equals(gameList1.games) : gameList1.games == null;

    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (games != null ? games.hashCode() : 0);
        return result;
    }
}
