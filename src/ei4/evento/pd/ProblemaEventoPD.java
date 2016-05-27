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
		ProblemaEventoPD.problemaInicial = new ProblemaEventoPD(0, 0., 0);

		for (List<Comida> m : ProblemaEvento.comidasPorTipo.values()) {
			for (Comida c : m) {
				if (c != null) {
					ProblemaEventoPD.comidasDisponibles.add(c);
					ProblemaEventoPD.numeroDeComidas += 1;
				}
			}
		}

		return ProblemaEventoPD.problemaInicial;
	}

	public static ProblemaEventoPD create(int i, Double costeAcumulado, Integer votosAcumulados) {
		ProblemaEventoPD p = new ProblemaEventoPD(i, costeAcumulado, votosAcumulados);
		return p;
	}

	private Double costeAcumulado = 0.;
	private Double presupuestoRestante = 0.;
	private Integer votosAcumulados = 0;

	private ProblemaEventoPD(int index, Double costeAcumulado, Integer votosAcumulados) {

		this.index = index;
		this.votosAcumulados = votosAcumulados;
		this.costeAcumulado = costeAcumulado;
		this.presupuestoRestante = this.presupuestoRestante - costeAcumulado;

	}

	private Boolean constraints(Comida c) {
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
		return (this.presupuestoRestante == 0 || index == ProblemaEventoPD.numeroDeComidas);
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
		return ProblemaEventoPD.create(index + 1, costeAcumulado, votosAcumulados);
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
