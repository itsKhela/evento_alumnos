package ei4.evento.bt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import us.lsi.bt.EstadoBT;
import ei4.evento.*;

public class EstadoEvento implements EstadoBT<Menu, Comida> {
	
	private Menu menu;
	
	public static EstadoEvento create(){
		return new EstadoEvento();
	}
	
	private EstadoEvento(Menu menu){
		super();
		this.menu = Menu.create(menu);
	}
	
	private EstadoEvento(){
		super();
		this.menu = Menu.create();
	}

	@Override
	public void avanza(Comida a) {
		this.menu.add(a);		
	}

	@Override
	public void retrocede(Comida a) {
		Preconditions.checkArgument(a.equals(this.menu.last()));
		this.menu.remove(a);		
	}

	@Override
	public int size() {
		return menu.numeroDeComidas();
	}

	@Override
	public boolean isFinal() {//TODO:
		return false;
		
	}

	@Override
	public List<Comida> getAlternativas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Menu getSolucion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjetivo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjetivoEstimado(Comida a) {
		// TODO Auto-generated method stub
		return null;
	}
}
