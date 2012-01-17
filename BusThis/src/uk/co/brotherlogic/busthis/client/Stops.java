package uk.co.brotherlogic.busthis.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class Stops
{

   List<Stop> stops = new ArrayList<Stop>();

   public Stops()
   {
      load();
   }

   public List<Stop> getClosestStops(final double lat, final double lon, int n)
   {
      // Get the closest
      double dist = 100;
      Stop best = null;
      for (Stop st : stops)
         if (st.getDist(lat, lon) < dist)
         {
            dist = st.getDist(lat, lon);
            best = st;
         }
      System.out.println("STOP = " + best.id + " " + best.lat + " " + best.lon);

      Collections.sort(stops, new Comparator<Stop>()
      {
         @Override
         public int compare(Stop o1, Stop o2)
         {
            if (o1.getDist(lat, lon) - o2.getDist(lat, lon) > 0)
               return 1;
            else
               return -1;
         }
      });

      System.out.println(stops.get(0).id + " or " + stops.get(stops.size() - 1).id);
      for (int i = 0; i < stops.size(); i++)
         if (stops.get(i).id.equals("37021901"))
            System.out.println(i + " / " + stops.size());

      return stops.subList(0, n);
   }

   public void load()
   {
      try
      {

         new RequestBuilder(RequestBuilder.GET, "sheffield_simp.txt").sendRequest("",
               new RequestCallback()
               {
                  @Override
                  public void onResponseReceived(Request req, Response resp)
                  {
                     String text = resp.getText();

                     // Process this line by line
                     String[] lines = text.split("\\s+");

                     for (int i = 0; i < lines.length; i += 3)
                     {
                        Stop s = new Stop(lines[i], Double.parseDouble(lines[i + 1]), Double
                              .parseDouble(lines[i + 2]));
                        stops.add(s);
                     }

                     System.out.println("Read " + stops.size() + " stops");
                  }

                  @Override
                  public void onError(Request res, Throwable throwable)
                  {
                     // handle errors
                  }
               });
      }
      catch (RequestException e)
      {
         e.printStackTrace();
      }
   }
}
