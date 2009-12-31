package util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapUtil {
	
	
	
	
	//print a map
	public static void printMap(Map  map){
		
		Set keySet = map.keySet();
		Iterator i = keySet.iterator();
		
		while(i.hasNext()){
			Object key = i.next();
			Object value = map.get(key);
			
			System.out.println("["+key+" -- "+value+"]");
			
		}
		
		
	}

}
