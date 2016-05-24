package ei4.evento;

import java.util.List;

public class Menu {
	
	public List<Comida> comidas;
	public int votosTotales;
	public Double presupuestoTotal;

	public Menu(List<Comida> comidas){
		
		this.comidas = comidas;
		
		for(Comida c: comidas){
			this.votosTotales += c.getVotos();
			this.presupuestoTotal += c.getPrecio();		
		}		
	}
	
	public Menu(){
		
		this.comidas = null;
		this.votosTotales = 0;
		this.presupuestoTotal = 0.;
		
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
	
	
}
