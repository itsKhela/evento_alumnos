package ei4.evento.bt;
import us.lsi.bt.*;
import ei4.evento.*;

public class ProblemaEventoBT implements ProblemaBT<Menu, Comida> {
	
	private static Double presupuestoTotal;
	
	public static ProblemaEventoBT create(double d){
		return new ProblemaEventoBT(d);
	}
	private ProblemaEventoBT(Double presupuestoTotal){
		super();
		this.presupuestoTotal = presupuestoTotal;
	}

	@Override
	public ProblemaBT.Tipo getTipo() {
		return Tipo.Max;
	}

	@Override
	public EstadoBT<Menu, Comida> getEstadoInicial() {
		return EstadoEvento.create(this.presupuestoTotal);
	}

}
