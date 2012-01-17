package uk.co.brotherlogic.busthis.client;

public class Stop
{
   String id;
   double lat;
   double lon;

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
}
