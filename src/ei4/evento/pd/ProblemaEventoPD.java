package ei4.evento.pd;
import java.util.*;
import java.util.stream.*;

import ei4.evento.*;
import us.lsi.pd.AlgoritmoPD.Sp;
import us.lsi.pd.ProblemaPD;

public class ProblemaEventoPD implements ProblemaPD<Menu, Comida> {
	private static ProblemaEventoPD problemaInicial;
	private static Integer presupuesto;
	private Double valorSolucion = Double.MIN_VALUE;
	private static List<Comida> comidas;
	public static Integer numeroDeComidasEnMenu;	
	private int index;

	

	public static ProblemaEventoPD create(String fichero, Integer c) {
		ProblemaEvento.leeComidas(fichero);
		ProblemaEventoPD.presupuesto = ProblemaEvento.presupuestoTotal;
		ProblemaEventoPD.problemaInicial = new ProblemaEventoPD();  //TODO: check parameters inside new ProblemaEventoPD

		ProblemaEventoPD.comidas = ProblemaEvento.(); // TODO: fill this method
		
		return ProblemaEventoPD.problemaInicial;
	}
	
	@Override
	public us.lsi.pd.ProblemaPD.Tipo getTipo() {
		return Tipo.Max;
	}
	
	@Override
	public int size() {
		return ProblemaEventoPD.comidas.size() - index + 1;
	}
	
	@Override
	public boolean esCasoBase() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Sp<Comida> getSolucionCasoBase() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Sp<Comida> seleccionaAlternativa(List<Sp<Comida>> ls) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ProblemaPD<Menu, Comida> getSubProblema(Comida a, int np) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Sp<Comida> combinaSolucionesParciales(Comida a, List<Sp<Comida>> ls) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Comida> getAlternativas() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getNumeroSubProblemas(Comida a) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Menu getSolucionReconstruida(Sp<Comida> sp, List<Menu> ls) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Double getObjetivoEstimado(Comida a) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Double getObjetivo() {
		// TODO Auto-generated method stub
		return null;
	}
	
/*	private static Integer capacidadInicial;
	private int index;

	public static List<ObjetoMochila> objetos;
	public static Integer numeroDeObjetos;

	public static ProblemaEventoPD create(String fichero, Integer c) {
		ProblemaEvento.leeObjetosDisponibles(fichero);
		ProblemaEventoPD.objetos = ProblemaEvento.getObjetosDisponibles();
		ProblemaEventoPD.numeroDeObjetos = ProblemaEventoPD.objetos.size();

		ProblemaEventoPD.multiplicidadesMaximas = ProblemaEvento.getObjetosDisponibles().stream()
				.mapToInt(x -> x.getNumMaxDeUnidades()).boxed().collect(Collectors.toList());
		ProblemaEventoPD.capacidadInicial = c;
		ProblemaEventoPD.problemaInicial = new ProblemaEventoPD(0, 0, 0.);

		return ProblemaEventoPD.problemaInicial;
	}

	public static ProblemaEventoPD create(int index, int pesoAcumulado, double valorAcumulado) {
		ProblemaEventoPD p = new ProblemaEventoPD(index, pesoAcumulado, valorAcumulado);
		return p;
	}

	private Integer pesoAcumulado;
	private Integer capacidadRestante;
	private Double valorAcumulado;

	private ProblemaEventoPD(int index, int pesoAcumulado, double valorAcumulado) {
		this.index = index;
		this.pesoAcumulado = pesoAcumulado;
		this.capacidadRestante = capacidadInicial - this.pesoAcumulado;
		this.valorAcumulado = valorAcumulado;
	}

	private Boolean constraints(Integer a) {
		return this.pesoAcumulado + a * objetos.get(index).getPeso() <= capacidadInicial;
	}


	@Override
	public int size() {
		return ProblemaEventoPD.numeroDeObjetos - index + 1;
	}

	@Override
	public List<Integer> getAlternativas() {
		List<Integer> ls = IntStream.rangeClosed(0, ProblemaEventoPD.multiplicidadesMaximas.get(this.index))
				.filter(x -> this.constraints(x)).boxed().collect(Collectors.toList());
		Collections.reverse(ls);
		return ls;
	}

	@Override
	public boolean esCasoBase() {
		return this.capacidadRestante == 0 || index == ProblemaEventoPD.numeroDeObjetos - 1;
	}

	@Override
	public Sp<Integer> getSolucionCasoBase() {
		Integer peso = objetos.get(index).getPeso();
		int num = Math.min(capacidadRestante / peso, objetos.get(index).getNumMaxDeUnidades());
		Double val = (double) num * objetos.get(index).getValor();
		valorSolucion = val;
		return Sp.create(num, val);
	}

	@Override
	public ProblemaPD<Multiset<ObjetoMochila>, Integer> getSubProblema(Integer a, int np) {
		Preconditions.checkArgument(np == 0);
		Integer pesoAcumulado = this.pesoAcumulado + a * objetos.get(index).getPeso();
		Double valorAcumulado = this.valorAcumulado + a * objetos.get(index).getValor();
		return ProblemaEventoPD.create(index + 1, pesoAcumulado, valorAcumulado);
	}

	@Override
	public Sp<Integer> combinaSolucionesParciales(Integer a, List<Sp<Integer>> ls) {
		Sp<Integer> r = ls.get(0);
		Double valor = a * objetos.get(index).getValor() + r.propiedad;
		return Sp.create(a, valor);
	}

	@Override
	public Sp<Integer> seleccionaAlternativa(List<Sp<Integer>> ls) {
		Sp<Integer> r = ls.stream().filter(x -> x.propiedad != null).max(Comparator.naturalOrder()).orElse(null);
		valorSolucion = r != null ? r.propiedad : Double.MIN_VALUE;
		return r;
	}

	@Override
	public int getNumeroSubProblemas(Integer a) {
		return 1;
	}

	@Override
	public Multiset<ObjetoMochila> getSolucionReconstruida(Sp<Integer> sp) {
		Multiset<ObjetoMochila> m = HashMultiset.create();
		m.add(ProblemaEventoPD.objetos.get(this.index), sp.alternativa);
		return m;
	}

	@Override
	public Multiset<ObjetoMochila> getSolucionReconstruida(Sp<Integer> sp, List<Multiset<ObjetoMochila>> ls) {
		Multiset<ObjetoMochila> m = ls.get(0);
		m.add(ProblemaEventoPD.objetos.get(this.index), sp.alternativa);
		return m;
	}

	@Override
	public Double getObjetivoEstimado(Integer a) {
		return this.valorAcumulado + this.getCotaSuperiorValorEstimado(a);
	}

	@Override
	public Double getObjetivo() {
		return this.valorAcumulado + this.valorSolucion;
	}

	public Integer getCotaSuperiorValorEstimado(Integer a) {
		Double r = 0.;
		Double capacidadRestante = (double) (capacidadInicial - pesoAcumulado);
		Double nu = (double) a;
		int ind = this.index;
		while (true) {
			r = r + nu * objetos.get(ind).getValor();
			capacidadRestante = capacidadRestante - nu * objetos.get(ind).getPeso();
			ind++;
			if (ind >= objetos.size() || capacidadRestante <= 0.)
				break;
			nu = Math.min(multiplicidadesMaximas.get(ind), capacidadRestante / objetos.get(ind).getPeso());
		}
		return (int) Math.ceil(r);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacidadRestante == null) ? 0 : capacidadRestante.hashCode());
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProblemaEventoPD))
			return false;
		ProblemaEventoPD other = (ProblemaEventoPD) obj;
		if (capacidadRestante == null) {
			if (other.capacidadRestante != null)
				return false;
		} else if (!capacidadRestante.equals(other.capacidadRestante))
			return false;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + index + ", " + capacidadRestante + ")";
	}*/


}
