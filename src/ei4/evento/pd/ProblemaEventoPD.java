package ei4.evento.pd;
import java.util.*;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import ei4.evento.*;
import us.lsi.pd.AlgoritmoPD.Sp;
import us.lsi.pd.ProblemaPD;

public class ProblemaEventoPD implements ProblemaPD<Menu, Comida> {
	
	// propiedad individual -> private static
	// propiedad compartida -> public static
	
	//propiedades individuales:
	private static ProblemaEventoPD problemaInicial;
	private static Double presupuestoInicial;
	private Integer votosSolucion;
	private int index;	
	
	//propiedades compartidas:
	public static List<Comida> comidasDisponibles;
	public static int numeroDeComidas;
	
	public static ProblemaEventoPD create(String fichero, Double presupuesto) {
		ProblemaEvento.leeComidas(fichero);
		ProblemaEventoPD.presupuestoInicial = presupuesto;
		ProblemaEventoPD.problemaInicial = new ProblemaEventoPD(0,0.,0);
		
		for(List<Comida> m : ProblemaEvento.comidasPorTipo.values()){
			ProblemaEventoPD.comidasDisponibles.addAll(m);
		}		
		
		return ProblemaEventoPD.problemaInicial;
	}
	
	public static ProblemaEventoPD create(int i, Double costeAcumulado, Integer votosAcumulados) {
		ProblemaEventoPD p = new ProblemaEventoPD(i, costeAcumulado, votosAcumulados);
		return p;
	}
	

	private Double costeAcumulado;
	private Double presupuestoRestante;
	private Integer votosAcumulados;
	
	private ProblemaEventoPD(int index, Double costeAcumulado, Integer votosAcumulados){
		
		this.index = index;
		this.votosAcumulados = votosAcumulados;
		this.costeAcumulado = costeAcumulado;
		this.presupuestoRestante = this.presupuestoRestante - costeAcumulado;		
		
	}
	
	@Override
	public us.lsi.pd.ProblemaPD.Tipo getTipo() {
		return Tipo.Max;
	}
	
	@Override
	public int size() {
		return ProblemaEventoPD.comidasDisponibles.size() - index + 1;
	}
	
	@Override
	public boolean esCasoBase() {
		return (this.presupuestoRestante == 0 || 
				index == ProblemaEventoPD.comidasDisponibles.size() - 1);
	}
	
	@Override
	public Sp<Comida> getSolucionCasoBase() {
		Integer votos = comidasDisponibles.get(index).getVotos();
		Double num = Math.min(presupuestoRestante/votos, comidasDisponibles.get(index).getPrecio());
		Comida alternativa = comidasDisponibles.get(index);
		
		return Sp.create(alternativa, num);	
	}

	@Override
	public Sp<Comida> seleccionaAlternativa(List<Sp<Comida>> ls) {
		Sp<Comida> r = ls.stream().filter(x -> x.propiedad != null).max(Comparator.naturalOrder()).orElse(null);
		votosSolucion = r.alternativa.getVotos();
		return r;
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
		Double votos = a.getVotos() + r.propiedad;
		return Sp.create(a, votos);
	}
	
	@Override
	// TODO: dont have a clue...
	public List<Comida> getAlternativas() {
		List<Comida> ls = Stream.rangeClosed(0, ProblemaEventoPD.multiplicidadesMaximas.get(this.index))
				.filter(x -> this.constraints(x)).boxed().collect(Collectors.toList());
		Collections.reverse(ls);
		return ls;
	}
	
	@Override
	public int getNumeroSubProblemas(Comida a) {
		return 1;
	}
	
	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp) {
		Menu m = new Menu();;
		m.add(ProblemaEventoPD.comidasDisponibles.get(this.index), sp.alternativa);
		return m;
	}
	
	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp, List<Menu> ls) {
		Menu m = ls.get(0);
		m.add(ProblemaEventoPD.comidasDisponibles.get(this.index), sp.alternativa);
		return m;
	}
	
	@Override
	public Double getObjetivoEstimado(Comida a) {
		return this.votosAcumulados.doubleValue()+a.getVotos();
	}
	
	@Override
	public Double getObjetivo() {
		return this.votosAcumulados.doubleValue();
	}
	
}
