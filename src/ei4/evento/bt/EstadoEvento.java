package ei4.evento.bt;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;

import us.lsi.bt.EstadoBT;
import ei4.evento.*;

public class EstadoEvento implements EstadoBT<Menu, Comida> {
	
	public static Double mayorNumeroDeVotosObtenido = Double.MIN_VALUE;
	public static Menu mejorMenuObtenido = null;
	
	private Double presupuestoAcumulado;
	private Integer numeroDePlatosVegetarianosAcum;
	private Integer numeroDePlatosCalientesAcum;
	private int index;
	
	public static EstadoEvento create(Double presupuestoTotal){
		return new EstadoEvento(presupuestoTotal);
	}
	
	public static EstadoEvento create(List<Comida> comidas, Double presupuestoTotal){
		return new EstadoEvento(comidas, presupuestoTotal);
	}
	
	private Menu menu;
 	private List<Comida> listaDeComidas;
 	
	private EstadoEvento(Double presupuesto) {
		super();
		this.index = 0;
		this.listaDeComidas = new ArrayList<Comida>();
		this.menu = Menu.create(listaDeComidas);
		this.numeroDePlatosCalientesAcum = 0;
		this.numeroDePlatosVegetarianosAcum = 0;
		this.presupuestoAcumulado = presupuesto;
	}
	
	
	private EstadoEvento(List<Comida> comidas, Double presupuestoTotal) {
		super();
		this.listaDeComidas = Lists.newArrayList(comidas);
		this.menu = Menu.create(listaDeComidas);
		this.presupuestoAcumulado = presupuestoTotal;
	}
	

	@Override
	public void avanza(Comida a) {
		this.listaDeComidas.add(a);
		this.menu = Menu.create(this.listaDeComidas);
		if(a.esCaliente()) numeroDePlatosCalientesAcum++;
		if(a.esVegetariano()) numeroDePlatosVegetarianosAcum++;
		index++;
	}

	@Override
	public void retrocede(Comida a) {
		this.listaDeComidas.remove(a);
		this.menu = Menu.create(this.listaDeComidas);
	}

	@Override
	public int size() {
		return menu.getComidasDisponibles().size();
	}

	@Override
	public boolean isFinal() {
		return menu.getComidasDisponibles().isEmpty();
	}

	@Override
	public List<Comida> getAlternativas() {
		
		List<Comida> res = new ArrayList<Comida>();
		
		if(menu.getComidasDisponibles().get(index).getPrecio() < presupuestoAcumulado) {
			res.add(menu.getComidasDisponibles().get(index));
		}
		
		return res;
		
	}

	@Override
	public Menu getSolucion() {
		if(constraints() && numeroDePlatosCalientesAcum >= 2 
				&& numeroDePlatosVegetarianosAcum >= 2){
			return menu;
		}
		return null;
	}

	@Override
	public Double getObjetivo() {
		return menu.getVotos();
	}

	@Override
	public Double getObjetivoEstimado(Comida a) {
		
		return menu.getVotos()+a.getVotos();
	}
	
	private boolean constraints() {
		int[] comidasDeCadaTipo = {0,0,0,0};

		for (Comida c : menu.getComidasDisponibles()) {
			switch (c.getTipo()) {
			case "entrante":
				comidasDeCadaTipo[0]+=1; break;
			case "primero":
				comidasDeCadaTipo[1]+=1; break;
			case "segundo":
				comidasDeCadaTipo[2]+=1; break;
			case "postre":
				comidasDeCadaTipo[3]+=1; break;
			}
		}
		
		for (Integer i : comidasDeCadaTipo) {
			if (i < 1) {
				return false;
			}
		}
		return true;
	}

	
}
