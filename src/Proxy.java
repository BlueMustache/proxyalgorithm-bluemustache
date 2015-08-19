import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Proxy {

public final static double AIRCONSTANT = 1900.0; //950.0; // km/hr
public static LookupService ls;
	
	public static void main(String[] args) throws Exception {
		//http://money.cnn.com/2014/11/26/luxury/supersonic-jet/
		File database = new File (loadTempFile("resources/dat/GeoLiteCity.dat"));
		try {
			LookupService ls = new LookupService(database);
			Proxy.ls = ls;
		} catch (IOException e) {
			e.printStackTrace();
		}
		runCode();
	}
	
	public static void runCode() throws Exception{
		InetAddress ip1 = InetAddress.getByName("107.220.60.250"); //my ip
		InetAddress ip2 = InetAddress.getByName("37.58.52.55"); //ip of german vpn server
		if (isFeasible(getLat(ip1), getLon(ip2), getLat(ip2), getLon(ip2), 120)) { //9000 is the base line
			System.out.println("No proxy detected.");
		} else {
			System.out.println("Nice try script kiddy!");
		}
		System.out.println(getLat(ip1));
		System.out.println(getLon(ip1));
		System.out.println(getLat(ip2));
		System.out.println(getLon(ip2));
	}
	
	
	public static float getLat(InetAddress ip){
		 Location loc = ls.getLocation(ip);
		 float lattitude = loc.latitude;
		 return lattitude;
	}
	
	public static float getLon(InetAddress ip){
		 Location loc = ls.getLocation(ip);
		 float longitude = loc.longitude;
		 return longitude;
	}
	
	public static String loadTempFile(String name) throws IOException {
	    InputStream in = Proxy.class.getResourceAsStream(name);
	    byte[] buffer = new byte[1024];
	    int read = -1;
	    File temp = File.createTempFile(name, "");
	    temp.deleteOnExit();
	    FileOutputStream fos = new FileOutputStream(temp);

	    while((read = in.read(buffer)) != -1) {
	        fos.write(buffer, 0, read);
	    }
	    fos.close();
	    in.close();

	    return temp.getAbsolutePath();
	}
	
	public static boolean isFeasible(double lat1, double lng1, double lat2, double lng2, double dur){
	    int r = 6371; // average radius of the earth in km
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lng2 - lng1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double d = r * c;
		double calc = ((d/(AIRCONSTANT))*3600); //amount of time in seconds that a plane going at the speed of the air constant takes to go the distance
		System.out.println(calc);
		if (dur < calc) {
			return false;
		} else {
			return true;
		}
	}
	
}
