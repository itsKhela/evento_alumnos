package ei4.evento.pd;
import ei4.evento.*;
import us.lsi.algoritmos.Algoritmos;
import us.lsi.pd.AlgoritmoPD;

public class TestEventoPD {

	public static void main(String[] args) {
		AlgoritmoPD.isRandomize = false;
		ProblemaEvento.create("ficheros/Comidas.txt");
		ProblemaEvento.presupuestoTotal = 10;
		System.out.println("------");
		System.out.println("Tipos:\n" + ProblemaEvento.tipos);
		System.out.println("------");
		System.out.println("Comidas:\n" + ProblemaEvento.comidasPorTipo);
		System.out.println("------");
		System.out.println("Presupuesto total: " + ProblemaEvento.presupuestoTotal);
		System.out.println("------");
		
		ProblemaEventoPD  p = ProblemaEventoPD.create("ficheros/Comidas.txt", ProblemaEvento.presupuestoTotal.doubleValue());
		AlgoritmoPD<Menu, Comida> a = Algoritmos.createPD(p);
		a.ejecuta();
		if (a.solucionesParciales.get(p) != null){
			System.out.println("Votos: " + a.solucionesParciales.get(p).propiedad);
			System.out.println("Solucion: " + a.getSolucion(p));
			a.showAllGraph("ficheros/solucion.gv", "Solucion", p);
		}else{
			System.out.println("Solución no encontrada");
		}
		
	}
}
