//FILE::GoogleAPI_Search.java
//AUTHOR::Kevin.P.Barnett
//DATE::Apr.08.2017

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleAPI_Search
{
    public static void main(String[] args) throws IOException
    {
        String url = "https://www.googleapis.com/books/v1/volumes?q=How+to+be+an+Adult+intitle&David+Richo+intitle";

        // Create a URL and open a connection
        URL YahooURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) YahooURL.openConnection();

        // Set the HTTP Request type method to GET (Default: GET)
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        // Created a BufferedReader to read the contents of the request.
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }

        // MAKE SURE TO CLOSE YOUR CONNECTION!
        in.close();

        // response is the contents of the XML
        System.out.println(response.toString());
    }
}