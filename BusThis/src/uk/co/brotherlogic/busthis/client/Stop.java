package uk.co.brotherlogic.busthis.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class Stop
{
   String id;
   double lat;
   double lon;
   private static final String UPDATE_URL = "http://tsy.acislive.com/pip/stop_simulator_table.asp?NaPTAN=REPLACE&bMap=1&offset=0&refresh=30&pscode=95&dest=&duegate=90&PAC=REPLACE";

   public double getDist(double lat, double lon)
   {
      return (this.lat - lat) * (this.lat - lat) + (this.lon - lon) * (this.lon - lon);
   }

   public Stop(String id, double lon, double lat)
   {
      this.id = id;
      this.lat = lat;
      this.lon = lon;
   }

   public double getLat()
   {
      return lat;
   }

   public double getLon()
   {
      return lon;
   }

   public List<Bus> getBuses() throws IOException
   {
      List<Bus> buses = new ArrayList<Bus>();

      String url = URL.encode(UPDATE_URL.replace("REPLACE", id));
      RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

      try
      {
         System.out.println("Sending request: " + url);
         Request request = builder.sendRequest(null, new RequestCallback()
         {
            @Override
            public void onError(Request request, Throwable exception)
            {
               System.err.println("Couldn't retrieve JSON");
            }

            @Override
            public void onResponseReceived(Request request, Response response)
            {
               System.err.println("Response: " + response.getStatusCode());
               System.err.println("Response2: " + response.getStatusText());
               if (200 == response.getStatusCode())
                  System.out.println(response.getText());
            }
         });
      }
      catch (RequestException e)
      {
         e.printStackTrace();
      }

      return buses;
   }
}
