package tema;
import java.util.ArrayList;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        ArrayList<Account> accounts = JsonInput.deserializeAccounts();
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("No accounts found!");
            return;
        }
        Game game = Game.getInstance(accounts);
        game.run();
    }
}