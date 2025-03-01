package tema;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginGUI extends JFrame {
    private JTextField emailF;
    private JPasswordField passwF;
    private JLabel errMes;
    private ArrayList<Account> allAc;

    public LoginGUI(ArrayList<Account> accounts) {
        super("Login Page");
        this.allAc = accounts;

        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        setLayout(new BorderLayout(5,5));
        
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.PINK);
        add(panel, BorderLayout.WEST);

        panel.setPreferredSize(new Dimension(300, 300));
        
        JLabel title = new JLabel("WELCOME TO LEAGUE OF WARRIORS");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14));
        title.setBounds(20, 10, 280, 25);
        panel.add(title);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 50, 80, 25);
        panel.add(emailLabel);

        emailF = new JTextField();
        emailF.setBounds(100, 50, 170, 25);
        panel.add(emailF);

        JLabel passwLabel = new JLabel("Password:");
        passwLabel.setBounds(20, 90, 80, 25);
        panel.add(passwLabel);

        passwF = new JPasswordField();
        passwF.setBounds(100, 90, 170, 25);
        panel.add(passwF);

        errMes = new JLabel("");
        errMes.setForeground(Color.RED);
        errMes.setBounds(100, 120, 170, 30);
        panel.add(errMes);

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(100, 160, 100, 30);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            Account acc = goodLogin();
            if (acc != null) {
                Game game = Game.getInstance(allAc);
                game.setCurrAccount(acc);
                dispose();
                new CharacterSelectionGUI(game);
            }
        });

        JPanel imag = new JPanel(new BorderLayout());
        add(imag, BorderLayout.CENTER);

        JLabel img = new JLabel(new ImageIcon("src/imagini/login.png"));
        img.setHorizontalAlignment(SwingConstants.CENTER);
        imag.add(img, BorderLayout.CENTER);
    }

    private Account goodLogin() {
        String email = emailF.getText();
        String pass = new String(passwF.getPassword());

        for (Account acc : allAc) {
            if (acc.getInformation().getCredentials().getEmail().equals(email) &&
                    acc.getInformation().getCredentials().getPassword().equals(pass)) {
                errMes.setText("");
                return acc;
            }
        }
        errMes.setText("Invalid email or password!");
        return null;
    }
}
