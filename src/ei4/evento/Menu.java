package ei4.evento;

import java.util.List;

public class Menu {
	
	private List<Comida> comidas;
	private int votosTotales;
	private Double presupuestoTotal;
	private Boolean contieneVegetariano, contieneFrio, 
	contieneCaliente, contieneEntrante, contienePrimero,
	contieneSegundo, contienePostre;
	
	private Menu(List<Comida> comidas){
		
		this.comidas = comidas;
		
		for(Comida c: comidas){
			this.votosTotales += c.getVotos();
			this.presupuestoTotal += c.getPrecio();		
		}		
	}
	
	private Menu(){
		
		this.comidas = null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Menu con: [votosTotales=" + votosTotales + ", presupuestoTotal=" 
				+ presupuestoTotal+ "]";
	}

	public void add(Comida comida, Comida alternativa) {
		
		if(!comidas.contains(comida)){
			if (!comidas.contains(alternativa)){
				comidas.add(alternativa);
			}
			comidas.add(comida);
		}
		
	}
	
	public void add(Comida comida) {
		
		if(!comidas.contains(comida)){
			comidas.add(comida);
		}
		
	}
	
	public void remove(Comida c){
		if(comidas.contains(c)){
			comidas.remove(c);
		}
	}
	
	public Comida last(){
		return comidas.get(comidas.size()-1);
	}
	
	public void calculaPropiedadesDerivadas(){
		
	}

}
