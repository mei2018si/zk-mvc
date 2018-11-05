## Ejemplo aplicación ZK con patrón MVC
* Crear una calculadora de enteros con un "historial" de las operaciones realizadas.
* Se usa el patrón MVC (Model-View-Controller)

### Crear proyecto empleando arquetipos ZK
```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate \
             -DarchetypeCatalog=http://mavensync.zkoss.org/maven2/ \
             -DarchetypeGroupId=org.zkoss \
             -DarchetypeArtifactId=zk-archetype-webapp \
             -DgroupId=es.uvigo.mei \
             -DartifactId=zk-mvc \
             -Dversion=1.0 \
             -Dpackage=es.uvigo.mei.ejemplozk
```

Se usa el arquetipo para aplicaciones web ZK (`zk-archetype-web`) del repositorio de ZK Framework
* El catálogo del arquetipos del ZK Framework no es compatible con las versiones más recientes del `maven-archetype-pluing`, por lo que se fuerza el uso de la versión 2.4 del mismo.

#### Ajustes en el proyecto
1. Ajustar la versión de Java usada por el plugin `maven-compiler-plugin` en el `pom.xml` generado (establecer la versión 1.8)
```
cd zk-mvc
nano pom.xml
```
```xml
<projet>
   ...
   <build>
       <plugins>
          ...
          <!-- Compile java -->
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <version>2.3.2</version>
                  <configuration>
                       <source>1.8</source>
                       <target>1.8</target>
                  </configuration>
          </plugin>
          ...
       </plugins>
   <build>
   ...
</project>
```

2. Ajustar el descriptor de despliegue `web.xml` para hacer uso de la versión 3.0 de la especificación Servlet (se incluye una variante para Servlet 3.0, `web.servlet-3.xml`)
```
cd src/main/webapp/WEB-INF
mv web.servlet-3.xml web.xml
cd ../../../..
```

### Crear vista index.zul
```
nano src/main/webapp/index.zul
```

```xml
<zk>
	<window title="Calculadora ZK MVC" border="normal" width="800px"
		apply="es.uvigo.mei.ejemplozk.CalculadoraController">
		<vbox>
			<hbox>
				Operando 1:
				<textbox id="operando1Text" constraint="no empty" />
			</hbox>
			<hbox>
				Operando 2:
				<textbox id="operando2Text" constraint="no empty" />
			</hbox>
			<hbox>
				Resultado:
				<label id="resultadoLabel" />
			</hbox>
		</vbox>

		<separator height="10px" />

		<hbox>
			<button id="sumarButton" label="Sumar" />
			<button id="restarButton" label="Restar" />
			<button id="multiplicarButton" label="Multiplicar" />
			<button id="dividirButton" label="Dividir" />
		</hbox>

		<separator height="10px" />

		Historial de operaciones

		<listbox id="operacionesListbox"
			emptyMessage="No hay operaciones que mostrar">
			<listhead>
				<listheader label="Id" />
				<listheader label="Tipo" />
				<listheader label="Operando 1" />
				<listheader label="Operando 2" />
				<listheader label="Resultado" />
			</listhead>
			<template name="model" >
				<listitem>
					<listcell label="${each.id}"></listcell>
					<listcell label="${each.tipo}"></listcell>
					<listcell label="${each.operando1}"></listcell>
					<listcell label="${each.operando2}"></listcell>
					<listcell label="${each.resultado}"></listcell>
				</listitem>
			</template>
		</listbox>

		<separator height="10px" />

		<hbox>
			<button id="eliminarButton" label="Eliminar Seleccionada" />
			<button id="eliminarTodasButton" label="Eliminar Todas" />
		</hbox>
	</window>
</zk>
```

Descarga: [index.zul](https://github.com/mei2018si/zk-mvc/raw/master/src/main/webapp/index.zul)


### Crear controlador CalculadoraControlador.java
```
nano src/main/java/es/uvigo/mei/ejemplozk/CalculadoraControlador.java
```

```java
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
```
Descarga: [CalculadoraController.java](https://github.com/mei2018si/zk-mvc/raw/master/src/main/java/es/uvigo/mei/ejemplozk/CalculadoraController.java)

### Crear clases complementarias
```
nano src/main/java/es/uvigo/mei/ejemplozk/TipoOperacion.java
```

```java
package es.uvigo.mei.ejemplozk;

public enum TipoOperacion {
	SUMA, RESTA, MULTIPLICACION, DIVISION
}
```
Descarga: [TipoOperacion.java](https://github.com/mei2018si/zk-mvc/raw/master/src/main/java/es/uvigo/mei/ejemplozk/TipoOperacion.java)



```
nano src/main/java/es/uvigo/mei/ejemplozk/CalculadoraControlador.java
```

```java
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
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Operacion other = (Operacion) obj;

		return id == other.id;
	}

	@Override
	public String toString() {
		return "Operacion [id=" + id + ", " + tipo + "(" + operando1 + ", " + operando2 + ") = " + resultado + "]";
	}
}
```

Descarga: [Operacion.java](https://github.com/mei2018si/zk-mvc/raw/master/src/main/java/es/uvigo/mei/ejemplozk/Operacion.java)


### Compilar y ejecutar el proyecto con maven
```
mvn install
mvn jetty:run
```

El despligue de la aplicación ZK requiere disponer de un servidor de aplicaciones Java EE (realmente basta un contenedor de Servlet compatible con la versión 3.0)
* El fichero `pom.xml` declara el uso del plugin `jetty-maven-plugin` 
* Este plugin ofrece el goal `jetty:run` que descarga un contenedor de servlets ligero Jetty y depliega la aplicación ZK sobre el mismo.
* Más información: [jetty-maven-plugin](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html)


La aplicación desplegada estará disponible en la URL [http://localhost:8080/zk-mvc](http://localhost:8080/zk-mvc)

### Proyecto resultante
Disponible en github: [https://github.com/mei2018si/zk-mvc](https://github.com/mei2018si/zk-mvc)


```
git clone https://github.com/mei2018si/zk-mvc.git
```
Puede importase en Eclipse (ver [zk-en-eclipse](https://github.com/mei2018si/documentos-si/blob/master/zk-en-eclipse.md))
