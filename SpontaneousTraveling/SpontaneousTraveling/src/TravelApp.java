/**
 * Created by Vishal and David on 11/29/16.
 */
import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

class TravelApp {

    /* Checks all outgoing flights for a fun and spontaneous trip on Christmas Eve!
     */

    //Implementing a linear probing hash table
    int arrayLength = 50;
    String[] city = new String[arrayLength];
    String[] cost = new String[arrayLength];

    public void insert(String destination, String flight){
        int hashResult = hash(destination);

        if (city[hashResult] == null){
            city[hashResult] = destination;
            cost[hashResult] = flight;
        }

        else{
            int i = 0;
            int loop = hashResult;
            while (city[loop] != null){
                i++;
                loop = (hashResult + i) % arrayLength;
            }
            city[loop] = destination;
            cost[loop] = flight;
        }
    }
    public boolean get(String key){
        try {
            int iGet = hash(key);
            int initial = hash(key);
            if (city[iGet] == key) {
                System.out.println(cost[iGet]); //part of code
            } else {
                int i = 0;
                while (!city[iGet].equals(key)) {
                    i++;

                    iGet = (initial + i) % arrayLength;
                }
                System.out.println("The cost of this trip from Cleveland would be $" + cost[iGet]);
            }
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
        catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }






    public int hash (String s) {

        int sum = 0;

        for (int i = 0; i < s.length(); i++) {
            sum = sum + s.charAt(i);
        }

        int hashResult = sum % arrayLength;
        //System.out.println(hashResult);
        return hashResult;
    }

    //prints the hashtable
    public void print(){
        for (int i = 0; i < arrayLength; i++){
            if (city[i] == null)
                System.out.println(i + ": null");
            else {
                System.out.println(i + ": " + city[i] + ", " + cost[i]);
            }
        }
    }
    public String getCity(int iGet){
        System.out.println(city[iGet]);
        return city[iGet];
    }

    public void getData(){

        try {
            //URL xml = new URL("http://partners.api.skyscanner.net/apiservices/browsequotes/v1.0/US/USD/en-US/BOS/LAX/2016-12-30?apiKey=vi577882541691137348249559591252");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder  = factory.newDocumentBuilder();
            Document document = builder.parse("everywhere.xml");
            document.getDocumentElement().normalize();

            //Targeting first Quote because there's only one element in this list
            NodeList quotes = document.getElementsByTagName("Quotes");
            Node quote = quotes.item(0);
            Element quoteElement = (Element)quote;
            NodeList quoteDtos = document.getElementsByTagName("QuoteDto"); //just for size(#of iterations)

            //Actual Data in quotes
            NodeList quoteIds = quoteElement.getElementsByTagName("QuoteId");
            NodeList minPrices = quoteElement.getElementsByTagName("MinPrice");
            NodeList carrierIds = quoteElement.getElementsByTagName("int");
            NodeList destinationIds = quoteElement.getElementsByTagName("DestinationId");

            //Same as quotes, only one Carrier item
            NodeList carriers = document.getElementsByTagName("Carriers");
            Node carrier = carriers.item(0);
            Element carrierElement = (Element)carrier;
            NodeList carrierDtos = document.getElementsByTagName("CarriersDto");



            //Carrier data
            NodeList carrierNameIds = carrierElement.getElementsByTagName("CarrierId");
            NodeList carrierNames = carrierElement.getElementsByTagName("Name");


            NodeList places = document.getElementsByTagName("Places");
            Node place = places.item(0);
            Element placeElement = (Element)place;
            NodeList placesDto = document.getElementsByTagName("PlaceDto"); // for size of # of iterations

            NodeList placeIds = placeElement.getElementsByTagName("PlaceId");
            NodeList placeNames = placeElement.getElementsByTagName("Name");
            System.out.println("Possible Destinations: ");




            for (int i = 0; i < quoteDtos.getLength();i++) {
                //Targets every needed item in the list
                Node carrierId = carrierIds.item(i);
                Node minPrice = minPrices.item(i);
                Node quoteId = quoteIds.item(i);
                Node destinationId = destinationIds.item(i);
                //To element to get internal text
                Element quoteIdElement = (Element) quoteId;
                Element minPriceElement = (Element) minPrice;
                Element carrierIdElement = (Element) carrierId;
                Element destinationElement = (Element)destinationId;



                String finalQuoteId = quoteIdElement.getTextContent();
                String finalLowestPrice = minPriceElement.getTextContent();


                for (int j = 0; j < placesDto.getLength();j++){
                    Node placeID = placeIds.item(j);
                    Element placeIdElement = (Element)placeID;
                        if (placeIdElement.getTextContent().equals(destinationElement.getTextContent())) {
                            Node placeName = placeNames.item(j);
                            Element placeNameElement = (Element) placeName;
                            System.out.println(placeNameElement.getTextContent());
                            String destination = placeNameElement.getTextContent();
                            insert(destination, finalLowestPrice);//hashing these into the table
                            break;
                        }

                }

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }




    public static void main(String[] args){
        TravelApp a = new TravelApp();
        Scanner scanner = new Scanner(System.in);

            a.getData();
            System.out.println("Input Airport exactly as shown above:");
            String destination = scanner.nextLine();
            if (!a.get(destination)){
                System.out.println("WRONG INPUT GOODBYE");
                System.exit(0);
            }
    }
}
