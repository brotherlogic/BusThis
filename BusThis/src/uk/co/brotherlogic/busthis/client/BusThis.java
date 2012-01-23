package uk.co.brotherlogic.busthis.client;

import java.io.IOException;
import java.util.List;

import com.google.code.gwt.geolocation.client.Coordinates;
import com.google.code.gwt.geolocation.client.Geolocation;
import com.google.code.gwt.geolocation.client.Position;
import com.google.code.gwt.geolocation.client.PositionCallback;
import com.google.code.gwt.geolocation.client.PositionError;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BusThis implements EntryPoint
{
   Stops stops = new Stops();

   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad()
   {
      /*
       * Asynchronously loads the Maps API.
       * 
       * The first parameter should be a valid Maps API Key to deploy this
       * application on a public server, but a blank key will work for an
       * application served from localhost.
       */
      Maps.loadMapsApi("", "2", false, new Runnable()
      {
         @Override
         public void run()
         {
            if (Geolocation.isSupported())
            {
               System.out.println("Getting current location");
               Geolocation geo = Geolocation.getGeolocation();
               geo.getCurrentPosition(new PositionCallback()
               {
                  @Override
                  public void onFailure(PositionError error)
                  {
                     // Handle failure
                  }

                  @Override
                  public void onSuccess(Position position)
                  {
                     System.out.println("Got current location");
                     Coordinates coords = position.getCoords();
                     build(coords);
                  }
               });

            }
            else
               build(null);
         }
      });
   }

   MapWidget map;
   DockLayoutPanel dock;

   private void display(String stopID)
   {
      // Clear the map
      dock.remove(map);

      // Put in the stop data
      Stop st = new Stop(stopID, 0, 0);
      try
      {
         List<Bus> buses = st.getBuses();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void build(Coordinates coords)
   {
      LatLng cawkerCity = null;

      if (coords != null)
         cawkerCity = LatLng.newInstance(coords.getLatitude(), coords.getLongitude());
      else
         // Open a map centered on Cawker City, KS USA
         cawkerCity = LatLng.newInstance(39.509, -98.434);

      map = new MapWidget(cawkerCity, 2);
      map.setSize("100%", "100%");

      // Add some controls for the zoom level
      // map.addControl(new LargeMapControl());

      // Zoom to the best level of detail
      map.setZoomLevel(18);

      List<Stop> bstops = stops.getClosestStops(cawkerCity.getLatitude(),
            cawkerCity.getLongitude(), 10);
      for (final Stop st : bstops)
      {
         System.out.println("Adding " + st.getLat() + "," + st.getLon());
         final Marker m = new Marker(LatLng.newInstance(st.getLat(), st.getLon()));
         m.addMarkerClickHandler(new MarkerClickHandler()
         {
            @Override
            public void onClick(MarkerClickEvent event)
            {
               display(st.id);
            }
         });
         map.addOverlay(m);
      }

      dock = new DockLayoutPanel(Unit.PX);
      dock.add(map);

      // Add the map to the HTML host page
      RootLayoutPanel.get().add(dock);
   }
}
