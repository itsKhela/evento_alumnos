package ei4.evento.pd;

import java.util.*;

import com.google.common.base.Preconditions;

import ei4.evento.*;
import us.lsi.pd.AlgoritmoPD.Sp;
import us.lsi.pd.ProblemaPD;

public class ProblemaEventoPD implements ProblemaPD<Menu, Comida> {

	private static ProblemaEventoPD problemaInicial;

	// propiedades individuales:
	private static Double presupuesto;
	private int index;
	private Double votosSolucion = Double.MIN_VALUE;

	// propiedades compartidas:
	public static List<Comida> comidasDisponibles = new ArrayList<Comida>();
	public static int numeroDeComidas = 0;

	public static ProblemaEventoPD create(String fichero, Double presupuesto) {
		ProblemaEvento.leeComidas(fichero);
		ProblemaEventoPD.presupuesto = presupuesto;
		ProblemaEventoPD.problemaInicial = new ProblemaEventoPD(0, 0., 0, 0, 0, new boolean[4]);

		for (List<Comida> m : ProblemaEvento.comidasPorTipo.values()) {
			for (Comida c : m) {
				if (c != null) {
					ProblemaEventoPD.comidasDisponibles.add(c);
					ProblemaEventoPD.numeroDeComidas += 1;
				}
			}
		}
		
		Collections.sort(ProblemaEventoPD.comidasDisponibles, (x, y) -> -1 * x.getVotos().compareTo(y.getVotos()));

		return ProblemaEventoPD.problemaInicial;
	}

	public static ProblemaEventoPD create(int i, Double costeAcumulado, Integer votosAcumulados,
			Integer numPlatosCalientesAcum, Integer numPlatosVegetarianosAcum, boolean[] tieneTiposPlato) {
		ProblemaEventoPD p = new ProblemaEventoPD(i, costeAcumulado, votosAcumulados, numPlatosCalientesAcum,
				numPlatosVegetarianosAcum, tieneTiposPlato);
		return p;
	}

	private Double costeAcumulado = 0.;
	private Double presupuestoRestante = 0.;
	private Integer votosAcumulados = 0;
	private Integer numPlatosCalientesAcum, numPlatosVegetarianosAcum;
	private boolean[] tieneTiposDePlato; // 0 - tiene entrante, 1 tiene primero,
										 // 2 - tiene segundo, 3 - tiene postre

	private ProblemaEventoPD(int index, Double costeAcumulado, Integer votosAcumulados, Integer numPlatosCalientesAcum,
			Integer numPlatosVegetarianosAcum, boolean[] tieneTiposPlato) {

		this.index = index;
		this.votosAcumulados = votosAcumulados;
		this.costeAcumulado = costeAcumulado;
		this.presupuestoRestante = this.presupuestoRestante - costeAcumulado;
		this.numPlatosCalientesAcum = numPlatosCalientesAcum;
		this.numPlatosVegetarianosAcum = numPlatosVegetarianosAcum;
		this.tieneTiposDePlato = tieneTiposPlato;

	}

	private Boolean constraints(Comida c) {
		boolean res = true;
		switch (c.getTipo()) {
		case "entrante":
			res = !tieneTiposDePlato[0];
			break;
		case "primero":
			res = !tieneTiposDePlato[1];
			break;
		case "segundo":
			res = !tieneTiposDePlato[2];
			break;
		case "postre":
			res = !tieneTiposDePlato[3];
			break;
		}
		if (!res) {
			return false;
		}

		return (this.costeAcumulado + c.getPrecio() <= presupuesto);
	}

	@Override
	public us.lsi.pd.ProblemaPD.Tipo getTipo() {
		return Tipo.Max;
	}

	@Override
	public int size() {
		return ProblemaEventoPD.numeroDeComidas - index + 1;
	}

	@Override
	public List<Comida> getAlternativas() {
		List<Comida> ls = ProblemaEventoPD.comidasDisponibles;

		for (Comida c : comidasDisponibles) {
			if (!constraints(c)) {
				ls.remove(c);
			}
		}

		return ls;
	}

	@Override
	public boolean esCasoBase() {
		
		boolean yaContieneTodosLosTiposDePlato = true;
		
		for(boolean b : this.tieneTiposDePlato){
			if(!b){
				yaContieneTodosLosTiposDePlato = false;
				break;
			}
		}
		
		return (this.presupuestoRestante == 0 || index == ProblemaEventoPD.numeroDeComidas || yaContieneTodosLosTiposDePlato);
	}

	@Override
	public Sp<Comida> getSolucionCasoBase() {
		Comida c = comidasDisponibles.get(index);
		Double votos = c.getVotos().doubleValue();
		votosSolucion = votos;

		return Sp.create(c, votos);
	}

	@Override
	public ProblemaPD<Menu, Comida> getSubProblema(Comida a, int np) {
		Preconditions.checkArgument(np == 0);
		Double costeAcumulado = this.costeAcumulado + a.getPrecio();
		Integer votosAcumulados = this.votosAcumulados + a.getVotos();
		boolean[] tieneTiposDePlato = this.tieneTiposDePlato;

		Integer numPlatosCalientesAcum = this.numPlatosCalientesAcum;
		Integer numPlatosVegetarianosAcum = this.numPlatosVegetarianosAcum;

		if (a.esCaliente()) {
			numPlatosCalientesAcum += 1;
		}

		if (a.esVegetariano()) {
			numPlatosVegetarianosAcum += 1;
		}

		switch (a.getTipo()) {
		case "entrante":
			tieneTiposDePlato[0] = true;
			break;
		case "primero":
			tieneTiposDePlato[1] = true;
			break;
		case "segundo":
			tieneTiposDePlato[2] = true;
			break;
		case "postre":
			tieneTiposDePlato[3] = true;
			break;
		default:
			break;

		}

		return ProblemaEventoPD.create(index + 1, costeAcumulado, votosAcumulados, numPlatosCalientesAcum,
				numPlatosVegetarianosAcum, tieneTiposDePlato);
	}

	@Override
	public Sp<Comida> combinaSolucionesParciales(Comida a, List<Sp<Comida>> ls) {
		Sp<Comida> r = ls.get(0);
		Double votos = a.getVotos().doubleValue() + r.propiedad;
		return Sp.create(a, votos);
	}

	@Override
	public Sp<Comida> seleccionaAlternativa(List<Sp<Comida>> ls) {
		Sp<Comida> r = ls.stream().filter(x -> x.propiedad != null).max(Comparator.naturalOrder()).orElse(null);
		votosSolucion = r != null ? r.propiedad : Double.MIN_VALUE;
		return r;
	}

	@Override
	public int getNumeroSubProblemas(Comida a) {
		return 1;
	}

	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp) {
		Menu m = Menu.create();
		m.add(sp.alternativa);
		return m;
	}

	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp, List<Menu> ls) {
		Menu m = ls.get(0);
		m.add(sp.alternativa);
		return m;
	}

	@Override
	public Double getObjetivo() {
		return this.votosAcumulados + this.votosSolucion;
	}

	@Override
	public Double getObjetivoEstimado(Comida a) {
		return this.votosAcumulados + a.getVotos().doubleValue();
	}

}
