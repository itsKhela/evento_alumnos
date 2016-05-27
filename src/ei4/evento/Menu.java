package ei4.evento;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	
	private List<Comida> comidas;
	private int votosTotales;
	private Double presupuestoTotal;
	private Boolean contieneEntrante, contienePrimero,
	contieneSegundo, contienePostre;
	private Integer numPlatosCalientes, numPlatosVegetarianos;
	
	private Menu(List<Comida> comidas){
		
		this.comidas = comidas;
		
		for(Comida c: comidas){
			this.votosTotales += c.getVotos();
			this.presupuestoTotal += c.getPrecio();		
		}		
	}
	
	private Menu(){
		
		this.comidas = new ArrayList<Comida>();
		this.votosTotales = 0;
		this.presupuestoTotal = 0.;
		
	}
	
	private Menu(Menu m){
		this(m.comidas);
	}
	
	public static Menu create(){
		return new Menu();
	}
	
	public static Menu create(List<Comida> comidas){
		return new Menu(comidas);
	}
	
	public static Menu create(Menu menu) {
		return new Menu(menu);
	}
	
	public static Menu createValido(List<Comida> comidas){
		
		Menu m = new Menu(comidas);
		
		if(!solucionCumpleCondiciones(m)){
			throw new IllegalArgumentException();
		}
		return m;
		
	}

	public static boolean solucionCumpleCondiciones(Menu m) {
		
		boolean condition = false;
		
		for(Comida c : m.comidas){
			if (c.esCaliente()){
				m.numPlatosCalientes += 1;
			}
			if (c.esVegetariano()){
				m.numPlatosVegetarianos += 1;
			}
		
			switch(c.getTipo()){
				case "entrante": m.contieneEntrante = true; break;
				case "primero": m.contienePrimero = true; break;
				case "segundo": m.contieneSegundo = true; break;
				case "postre": m.contienePostre = true; break;
				default: break;
				
			}
		}
		
		if(m.numPlatosCalientes >= 2 && m.numPlatosVegetarianos >= 2 && m.contieneEntrante 
				&& m.contienePrimero && m.contieneSegundo && m.contienePostre){
			condition = true;
		}
		return condition;
	}

	@Override
	public String toString() {
		return "Menu con: [votosTotales=" + votosTotales + ", presupuestoTotal=" 
				+ presupuestoTotal+ "]";
	}


	public void add(Comida comida) {
		
		if(!comidas.contains(comida)){
			comidas.add(comida);
			switch(comida.getTipo()){
			case "entrante": this.contieneEntrante = true; break;
			case "primero": this.contienePrimero = true; break;
			case "segundo": this.contieneSegundo = true; break;
			case "postre": this.contienePostre = true; break;
			default: break;
			}
		}
		
	}

}