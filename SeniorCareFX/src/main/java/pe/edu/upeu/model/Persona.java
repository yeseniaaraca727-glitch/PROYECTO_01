package pe.edu.upeu.model;

import pe.edu.upeu.enums.EstadoSalud;
import pe.edu.upeu.enums.TipoPension;
import javafx.beans.property.*;

public class Persona {
    private IntegerProperty id;
    private StringProperty nombreCompleto;
    private IntegerProperty edad;
    private StringProperty curp;
    private StringProperty domicilio;
    private ObjectProperty<TipoPension> tipoPension;
    private ObjectProperty<EstadoSalud> estadoSalud;

    public Persona() {
        this.id = new SimpleIntegerProperty();
        this.nombreCompleto = new SimpleStringProperty();
        this.edad = new SimpleIntegerProperty();
        this.curp = new SimpleStringProperty();
        this.domicilio = new SimpleStringProperty();
        this.tipoPension = new SimpleObjectProperty<>();
        this.estadoSalud = new SimpleObjectProperty<>();
    }

    public Persona(int id, String nombreCompleto, int edad, String curp,
                   String domicilio, TipoPension tipoPension, EstadoSalud estadoSalud) {
        this();
        setId(id);
        setNombreCompleto(nombreCompleto);
        setEdad(edad);
        setCurp(curp);
        setDomicilio(domicilio);
        setTipoPension(tipoPension);
        setEstadoSalud(estadoSalud);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Nombre
    public String getNombreCompleto() { return nombreCompleto.get(); }
    public void setNombreCompleto(String v) { this.nombreCompleto.set(v); }
    public StringProperty nombreCompletoProperty() { return nombreCompleto; }

    // Edad
    public int getEdad() { return edad.get(); }
    public void setEdad(int v) { this.edad.set(v); }
    public IntegerProperty edadProperty() { return edad; }

    // CURP
    public String getCurp() { return curp.get(); }
    public void setCurp(String v) { this.curp.set(v); }
    public StringProperty curpProperty() { return curp; }

    // Domicilio
    public String getDomicilio() { return domicilio.get(); }
    public void setDomicilio(String v) { this.domicilio.set(v); }
    public StringProperty domicilioProperty() { return domicilio; }

    // Tipo Pensión
    public TipoPension getTipoPension() { return tipoPension.get(); }
    public void setTipoPension(TipoPension v) { this.tipoPension.set(v); }
    public ObjectProperty<TipoPension> tipoPensionProperty() { return tipoPension; }

    // Estado Salud
    public EstadoSalud getEstadoSalud() { return estadoSalud.get(); }
    public void setEstadoSalud(EstadoSalud v) { this.estadoSalud.set(v); }
    public ObjectProperty<EstadoSalud> estadoSaludProperty() { return estadoSalud; }
}