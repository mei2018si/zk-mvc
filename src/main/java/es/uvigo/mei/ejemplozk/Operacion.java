package es.uvigo.mei.ejemplozk;


public class Operacion {
	private int id;
	private TipoOperacion tipo;
	private int operando1;
	private int operando2;
	private int resultado;
	
	public Operacion(int id, TipoOperacion tipo, int operando1, int operando2, int resultado) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.operando1 = operando1;
		this.operando2 = operando2;
		this.resultado = resultado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoOperacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperacion tipo) {
		this.tipo = tipo;
	}

	public int getOperando1() {
		return operando1;
	}

	public void setOperando1(int operando1) {
		this.operando1 = operando1;
	}

	public int getOperando2() {
		return operando2;
	}

	public void setOperando2(int operando2) {
		this.operando2 = operando2;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operacion other = (Operacion) obj;
		
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Operacion [id=" + id + ", " + tipo + "(" + operando1 + ", " + operando2 + ") = " + resultado + "]";
	}

	
	
	
}
