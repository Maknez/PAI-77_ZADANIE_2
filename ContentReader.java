import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentReader {

    private URL             url;
    private URLConnection   urlConnection;
    private InetAddress     iNetAddress;
    private BufferedReader  bufferedReader;
    private String          pageContent;

    private final String    HEADBEGIN = "<head>";
    private final String    HEADCLOSE = "</head>";
    private final String    LINKPATTERN = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
    private final String    EMAILPATTERN = "\\b[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+\\b";


    private Pattern         headOpeningPatternCompiled = Pattern.compile(HEADBEGIN);
    private Pattern         headEnclosingPatternCompiled = Pattern.compile(HEADCLOSE);
    private Pattern         linkPatternCompiled = Pattern.compile(LINKPATTERN);
    private Pattern         emailPatternCompiled = Pattern.compile(EMAILPATTERN);

    public ContentReader(String url) throws UnknownHostException {
        try {
            this.url = new URL(url);
            this.iNetAddress = InetAddress.getByName(this.url.getHost());
        } catch (MalformedURLException e) {
            System.out.println("TYPED WWW ADRESS IS INCORRECT");
        }
    }

    public void connect() {
        try {
            this.urlConnection = this.url.openConnection();
        } catch (IOException | NullPointerException e) {
            System.out.println("THERE IS A PROBLEM WITH A CONNECTION TO: " + this.url);
        }
    }

    public BufferedReader getBufferedReader() {
        try {
            return this.bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        } catch (IOException e) {
            System.out.println("BufferedReader nie został poprawnie zainicjalizowany.");
            return null;
        }
    }

    public String getPageContent(BufferedReader websiteContent) throws IOException {
        String s;
        StringBuilder sb = new StringBuilder();
        while((s = websiteContent.readLine()) != null) {
            sb.append("\n" + s);
        }
        return sb.toString();
    }

    public void findHeadContent(String websiteContent) throws IOException {
        Matcher headOpenTag = headOpeningPatternCompiled.matcher(websiteContent);
        Matcher headCloseTag = headEnclosingPatternCompiled.matcher(websiteContent);

        File fileLinks = new File("HeadData.txt");
        if (!fileLinks.exists()) {
            fileLinks.createNewFile();
        }
        FileWriter fw = new FileWriter(fileLinks.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        if(headOpenTag.find() && headCloseTag.find()) {

            bw.write(websiteContent.substring(headOpenTag.end(), headCloseTag.start()));
            bw.close();

        } else {
            System.out.println("<head>Content not found</head>");
        }
    }

    public void findEmailAddresses(String websiteContent) throws IOException {
        Matcher emailMatcher = emailPatternCompiled.matcher(websiteContent);
        int count = 0;

        File fileLinks = new File("MailData.txt");
        if (!fileLinks.exists()) {
            fileLinks.createNewFile();
        }
        FileWriter fw = new FileWriter(fileLinks.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        while(emailMatcher.find()) {
            count++;
            bw.write(emailMatcher.group());
            bw.newLine();
        }
        bw.close();
        System.out.println("Founded " + count + " mails.");
    }

    public void findLinks(String websiteContent) throws IOException {
        Matcher linkMatcher = linkPatternCompiled.matcher(websiteContent);
        int count = 0;

        File fileLinks = new File("LinkData.txt");
        if (!fileLinks.exists()) {
            fileLinks.createNewFile();
        }
        FileWriter fw = new FileWriter(fileLinks.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        while(linkMatcher.find()) {
            count++;
            bw.write(linkMatcher.group());
            bw.newLine();
        }
        bw.close();
        System.out.println("Founded " + count + " links.");
    }

    public  void startApp() throws Exception {

        connect();
        pageContent = getPageContent(getBufferedReader());
        findHeadContent(pageContent);
        findEmailAddresses(pageContent);
        findLinks(pageContent);
    }
}

