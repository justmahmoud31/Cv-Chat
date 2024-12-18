import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                new Thread(() -> {
                    try (DataInputStream input = new DataInputStream(socket.getInputStream());
                         DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                        while (true) {
                            String userName = input.readUTF();
                            System.out.println("Received username: " + userName);

                            if ("exit".equalsIgnoreCase(userName)) {
                                System.out.println("Client requested to exit.");
                                break;
                            }

                            // Lookup user details (mock example)
                            String userNumber = null;
                            String imagePath = null;

                            switch (userName) {
                                case "Ahmed Amin":
                                    userNumber = "321213001";
                                    imagePath = "Team-members/witcher.jpg";
                                    break;
                                case "Mostafa Amin":
                                    userNumber = "321213002";
                                    imagePath = "Team-members/download.jpeg";
                                    break;
                                case "Marwan Mostafa":
                                    userNumber = "321213002";
                                    imagePath = "Team-members/img2.jpg";
                                    break;
                                case "Mostafa Karam":
                                    userNumber = "321213002";
                                    imagePath = "Team-members/img3.jpg";
                                    break;
                                case "Just Mahmoud":
                                    userNumber = "321213002";
                                    imagePath = "Team-members/JM31.jpg";
                                    break;
                                // Add more users as needed
                                default:
                                    output.writeUTF("User not found.");
                                    continue;
                            }

                            String response = "Number: " + userNumber + "\nImage: " + imagePath;
                            output.writeUTF(response);
                        }
                    } catch (IOException e) {
                        System.err.println("Error handling client: " + e.getMessage());
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Error closing socket: " + e.getMessage());
                        }
                        System.out.println("Client disconnected.");
                    }
                }).start();
            }
        }
    }
}
