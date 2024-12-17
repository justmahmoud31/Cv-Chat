import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                String request = input.readUTF();
                System.out.println("Client request: " + request);

                File htmlFile = new File("student_project.html");
                BufferedReader reader = new BufferedReader(new FileReader(htmlFile));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line + "\n");
                }

                output.writeUTF(response.toString());

                String message = input.readUTF();
                System.out.println("Received message: " + message);

                reader.close();
                socket.close();
            }
        }

    }
}
