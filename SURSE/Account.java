package tema;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Account {
    private Information information;
    private ArrayList<Character> characters;
    private int gamesNumber;

    public Account(ArrayList<Character> characters, int gamesNumber, Information information) {
        this.information = information;
        this.characters = characters;
        this.gamesNumber = gamesNumber;
    }


    public Information getInformation() {
        return information;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public int getgamesNumber() {
        return gamesNumber;
    }

    public void incrementgamesNumber() {
        this.gamesNumber++;
    }

    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private SortedSet<String> favGames;

        private Information(Credentials credentials, SortedSet<String> favoriteGames, String name, String country) {
            this.credentials = credentials;
            this.favGames = favoriteGames;
            this.name = name;
            this.country = country;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public SortedSet<String> getFavoriteGames() {
            return favGames;
        }

        public  void addFavoriteGame(String game) {
            this.favGames.add(game);
        }

        public void removeFavoriteGame(String game) {
            this.favGames.remove(game);
        }

        public static class Builder{
            private Credentials credentials;
            private String name;
            private String country;
            private SortedSet<String> favGames = new TreeSet<>();

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }
            public Builder setName(String name) {
                this.name = name;
                return this;
            }
            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }
            public Builder setFavoriteGames(SortedSet<String> favoriteGames) {
                this.favGames = favoriteGames;
                return this;
            }
            public Builder addFavoriteGame(String game) {
                this.favGames.add(game);
                return this;
            }
            public Information build() {
                return new Information(credentials, favGames, name, country);
            }
        }
    }
}
