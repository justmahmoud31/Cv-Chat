import java.io.*;
import java.net.*;
import java.awt.Desktop;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8000);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF("GET student_project.html");
        String response = input.readUTF();

        BufferedWriter writer = new BufferedWriter(new FileWriter("response.html"));
        writer.write(response);
        writer.close();

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new File("response.html").toURI());
        }

        output.writeUTF("I RECEIVED THE INFORMATION OF STUDENT'S PROJECT GROUP");

        socket.close();
    }
}

