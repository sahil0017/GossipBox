import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.*;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
//import javax.swing.border.Border;

public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Message Box");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageField = new JTextField();
    Font font = new Font("Roboto", Font.PLAIN, 25);

    public Server() {
        try {
             server=new ServerSocket(8888);
             System.out.println("Server is ready to accept connection");
             System.out.println("Server is waiting");
             createGui();

             socket=server.accept();
             
             handleEvent();

             br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out=new PrintWriter(socket.getOutputStream());

             startReading();
             startWriting();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void handleEvent() {
        messageField.addKeyListener(new KeyListener() {

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
                   String msg=messageField.getText();
                   messageArea.append(" me : "+msg+"\n");
                   out.println(msg);
                   out.flush();
                   messageField.setText("");
                   messageField.requestFocus();
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

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageField.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(font);
        messageArea.setFont(font);
        messageField.setFont(font);
        messageArea.setEditable(false);

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane pane=new JScrollPane(messageArea);
        this.add(pane,BorderLayout.CENTER);
        this.add(messageField,BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("Reader Started");
        try{
            while(!socket.isClosed())
            {
               
                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client Terminated the chat");
                    JOptionPane.showMessageDialog(null,"Client terminated the connection ");
                    messageField.setEnabled(false);
                    socket.close();
                    break;
                }
               // System.out.println("Client : "+msg);
                messageArea.append("Client : "+msg+"\n");
                
            }
        }catch(Exception e)
                {
                   // e.printStackTrace();
                   System.out.println("Connection Closed");
                }

        };
        new Thread(r1).start();

    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("Writer Started");
            try {
            while(!socket.isClosed())
            {
                
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    
                
            }
            System.out.println("Connection closed");
        }catch (Exception e) {
                    
                e.printStackTrace();
            }

        };
        new Thread(r2).start();
    }

     public static void main(String[]args)
    {
        System.out.println("This is server .. going to start. ");
        new Server();
    }
}
