package ei4.evento;

public class Comida{

	private Integer codigo;
	private String nombre;
	private Integer votos;
	private Double precio;
	private String tipo;
	private boolean caliente;
	private boolean vegetariano;
	
	private Comida(Integer codigo, String nombre, Integer votos, Double precio,
			String tipo, boolean caliente, boolean vegetariano) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.votos = votos;
		this.precio = precio;
		this.tipo = tipo;
		this.caliente = caliente;
		this.vegetariano = vegetariano;
	}
	
	private Comida(int codigo, String[] fm) {
		super();
		this.codigo = codigo;
		this.nombre = fm[0];
		this.votos = new Integer(fm[1]);
		this.precio = new Double(fm[2]);
		this.tipo = fm[3];
		this.caliente = new Boolean(fm[4]);
		this.vegetariano = new Boolean(fm[5]);
	}
	
	public static Comida create(Integer codigo, String nombre, Integer votos, Double precio,
			String tipo, boolean caliente, boolean vegetariano) {
		return new Comida(codigo, nombre, votos, precio, tipo, caliente, vegetariano);
	}
	
	public static Comida create(int codigo, String[] fm) {
		return new Comida(codigo, fm);
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Integer getVotos() {
		return votos;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public boolean esCaliente() {
		return caliente;
	}
	
	public boolean esVegetariano() {
		return vegetariano;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comida other = (Comida) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}

}

