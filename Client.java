import java.io.BufferedReader;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    Client() {
        try 
        {
            System.out.println("Sending Request");
            socket = new Socket("127.0.0.1", 8887);
            System.out.println("Connection done ");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("Reader Started");
            while(true)
            {
                try {
                    String msg=br.readLine();
                    if(msg.equals("Exit"))
                    {
                        System.out.println("Server Terminate");
                        break;
                    }
                    System.out.println("Server : "+msg);
                    
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
