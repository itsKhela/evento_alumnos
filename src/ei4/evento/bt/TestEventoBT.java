package ei4.evento.bt;

import ei4.evento.*;
import us.lsi.algoritmos.Algoritmos;
import us.lsi.bt.*;

public class TestEventoBT {

	public static void main(String[] args) {
		AlgoritmoBT.soloLaPrimeraSolucion = false;
		AlgoritmoBT.isRandomize = false;
		AlgoritmoBT.numeroDeSoluciones = 100;
		ProblemaEvento.create("ficheros/Comidas.txt");
		ProblemaEvento.presupuestoTotal = 10;
		System.out.println("------");
		System.out.println("Tipos:\n" + ProblemaEvento.tipos);
		System.out.println("------");
		System.out.println("Comidas:\n" + ProblemaEvento.comidasPorTipo);
		System.out.println("------");
		System.out.println("Presupuesto total: " + ProblemaEvento.presupuestoTotal);
		System.out.println("------");
		
		//TODO
		AlgoritmoBT<Menu, Comida> a = Algoritmos.createBT(p);
		a.ejecuta();

		if (a.soluciones.isEmpty()) 
			System.out.println("Solución no encontrada");
		else{
			System.out.println("Solución: " + a.solucion);
		}
	}
}
