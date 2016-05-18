package ei4.evento;

import java.util.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import us.lsi.stream.Stream2;

public class ProblemaEvento {

	public static List<String> tipos;
	public static Map<String, List<Comida>> comidasPorTipo; 
	public static Integer presupuestoTotal;
	
	public ProblemaEvento(String file) {
		super();
		leeComidas(file);
	}
	
	public static void leeComidas(String file){	
		List<String> ls = Stream2.fromFile(file).toList();
		comidasPorTipo = new HashMap<String, List<Comida>>();
		tipos = Lists.newArrayList();
		int index = 0;
		for(String s : ls){
			String[] at = Stream2.fromString(s, ",").<String>toArray((int x)->new String[x]);
			Preconditions.checkArgument(at.length==6);
			Comida a = Comida.create(index, at);
			if (!comidasPorTipo.containsKey(a.getTipo())){
				tipos.add(a.getTipo());
				comidasPorTipo.put(a.getTipo(), new ArrayList<Comida>());
			}
			comidasPorTipo.get(a.getTipo()).add(a);
			index++;
		}
	}

	public static ProblemaEvento create(String file) {		
		return new ProblemaEvento(file);
	}

}
