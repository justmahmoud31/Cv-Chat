import java.io.*;
import java.net.*;
import java.awt.Desktop;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8000);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server. Type 'exit' to quit.");

            while (true) {
                System.out.print("Enter the name of the user (or 'exit' to quit): ");
                String userName = consoleReader.readLine();

                if ("exit".equalsIgnoreCase(userName)) {
                    System.out.println("Exiting...");
                    break;
                }

                output.writeUTF(userName); // Send username to server
                String serverResponse = input.readUTF(); // Get server response
                System.out.println("Server: " + serverResponse);

                // If the response contains valid user data, create an HTML file
                String[] responseParts = serverResponse.split("\n");
                if (responseParts.length == 2 && responseParts[0].startsWith("Number:") && responseParts[1].startsWith("Image:")) {
                    String number = responseParts[0].split(": ")[1];
                    String imagePath = responseParts[1].split(": ")[1];

                    String htmlContent =
                            "<!DOCTYPE html>\n" +
                                    "<html lang=\"en\">\n" +
                                    "<head>\n" +
                                    "    <meta charset=\"UTF-8\">\n" +
                                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                                    "    <title>Chat Response</title>\n" +
                                    "    <style>\n" +
                                    "        body { font-family: Arial, sans-serif; text-align: center; }\n" +
                                    "        img { max-width: 300px; border: 1px solid #ccc; margin-top: 20px; }\n" +
                                    "    </style>\n" +
                                    "</head>\n" +
                                    "<body>\n" +
                                    "    <h1>User Information</h1>\n" +
                                    "    <p>Number: " + number + "</p>\n" +
                                    "    <img src='" + imagePath + "' alt='User Image'/>\n" +
                                    "</body>\n" +
                                    "</html>";

                    BufferedWriter writer = new BufferedWriter(new FileWriter("response.html"));
                    writer.write(htmlContent);
                    writer.close();

                    // Open the generated HTML file in the default browser
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new File("response.html").toURI());
                    }
                } else {
                    System.out.println("Invalid response from server or user not found.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
        }
    }
}
