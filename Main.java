import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {

            ContentReader connection = new ContentReader("https://en.wikipedia.org/wiki/Email_address");
            connection.startApp();

        } catch (IOException | NullPointerException e) {
            System.out.println("THERE IS A PROBLEM WITH A CONNECTION");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("TYPED WWW ADRESS IS INCORRECT OR DOES NOT EXIST");
        } catch (Exception e) {
            System.out.println("THERE IS AN OTHER ERROR");
        }
    }
}