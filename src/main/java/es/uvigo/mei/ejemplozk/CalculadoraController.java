package es.uvigo.mei.ejemplozk;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class CalculadoraController extends SelectorComposer<Window> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox operando1Text;
	@Wire
	private Textbox operando2Text;
	@Wire
	private Label resultadoLabel;

	@Wire
	private Listbox operacionesListbox;

	private ListModelList<Operacion> operaciones;
	private int contadorOperaciones;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		contadorOperaciones = 0;
		operaciones = new ListModelList<>();
		operacionesListbox.setModel(operaciones);
	}

	public ListModelList<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(ListModelList<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	@Listen("onClick = #sumarButton")
	public void sumar() {
		int operando1 = Integer.parseInt(operando1Text.getValue());
		int operando2 = Integer.parseInt(operando2Text.getValue());
		int resultado = operando1 + operando2;
		resultadoLabel.setValue(Integer.toString(resultado));
		registrarOperacion(TipoOperacion.SUMA, operando1, operando2, resultado);
	}

	@Listen("onClick = #restarButton")
	public void restar() {
		int operando1 = Integer.parseInt(operando1Text.getValue());
		int operando2 = Integer.parseInt(operando2Text.getValue());
		int resultado = operando1 - operando2;
		resultadoLabel.setValue(Integer.toString(resultado));
		registrarOperacion(TipoOperacion.RESTA, operando1, operando2, resultado);
	}

	@Listen("onClick = #multiplicarButton")
	public void multiplicar() {
		int operando1 = Integer.parseInt(operando1Text.getValue());
		int operando2 = Integer.parseInt(operando2Text.getValue());
		int resultado = operando1 * operando2;
		resultadoLabel.setValue(Integer.toString(resultado));
		registrarOperacion(TipoOperacion.MULTIPLICACION, operando1, operando2, resultado);
	}

	@Listen("onClick = #dividirButton")
	public void dividir() {
		int operando1 = Integer.parseInt(operando1Text.getValue());
		int operando2 = Integer.parseInt(operando2Text.getValue());
		int resultado = operando1 / operando2;
		resultadoLabel.setValue(Integer.toString(resultado));
		registrarOperacion(TipoOperacion.DIVISION, operando1, operando2, resultado);
	}

	private void registrarOperacion(TipoOperacion tipo, int operando1, int operando2, int resultado) {
		contadorOperaciones++;
		operaciones.add(new Operacion(contadorOperaciones, tipo, operando1, operando2, resultado));
		// operacionesListbox.setModel(operaciones);
	}

	@Listen("onClick = #eliminarButton")
	public void eliminar() {
		if (operacionesListbox.getSelectedItem() != null) {
			Operacion operacion = operacionesListbox.getSelectedItem().getValue();
			operaciones.remove(operacion);
		}
	}

	@Listen("onClick = #eliminarTodasButton")
	public void eliminarTodas() {
		operaciones.clear();
		contadorOperaciones = 0;
	}
}
