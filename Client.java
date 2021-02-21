import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.BorderLayout;
//import java.awt.Color;

import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
//import javax.swing.text.AttributeSet.ColorAttribute;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Message Box");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    Font font = new Font("Roboto", Font.PLAIN, 25);

    Client() {
        try {
             System.out.println("Sending Request");
             socket = new Socket("127.0.0.1", 8888);
             System.out.println("Connection done ");

             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out=new PrintWriter(socket.getOutputStream());

             createGui();
             handleEvents();

             startReading();
             startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                

            }

            @Override
            public void keyPressed(KeyEvent e) {
               

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10)
                {
                    //System.out.println("You have pressed Enter Button ");
                    String msg=messageInput.getText();
                    messageArea.append(" me : "+msg+"\n");
                    out.println(msg);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGui()
    {
        this.setTitle("Gossip Box");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setEditable(false);
       // heading.setBackground(Color.CYAN);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //heading.setIcon(new ImageIcon("gossip.jpg"));

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }


    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("Reader Started");
            while(true)
            {
                try {
                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server Terminate");
                        JOptionPane.showMessageDialog(null,"Server Terminated the chat ");
                        messageInput.setEnabled(false);
                        break;
                    }
                  //  System.out.println("Server : "+msg);
                  messageArea.append("Server : "+msg+"\n");
                    
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }

        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("Writer Startted");

            try{
                while(true){

                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                String content=br1.readLine();
                out.println(content);
                out.flush();
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();
    }

    public static void main(String [] args)
    {
        System.out.println("This is client ");
        new Client();
    }
    
}
