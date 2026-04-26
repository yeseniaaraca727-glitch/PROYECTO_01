package pe.edu.upeu.repository;

import pe.edu.upeu.enums.EstadoSalud;
import pe.edu.upeu.enums.TipoPension;
import pe.edu.upeu.model.Persona;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonaRepository {

    private static final List<Persona> lista = new ArrayList<>();
    private static int contadorId = 1;

    static {
        // Datos de prueba
        lista.add(new Persona(contadorId++, "María González López", 72, "GOLM520310MDFNRR09",
                "Av. Insurgentes 45, CDMX", TipoPension.IMSS, EstadoSalud.BUENO));
        lista.add(new Persona(contadorId++, "José Ramírez Torres", 80, "RATJ430815HDFMRS02",
                "Calle Morelos 12, Guadalajara", TipoPension.ISSSTE, EstadoSalud.REGULAR));
        lista.add(new Persona(contadorId++, "Ana Flores Pérez", 65, "FOPA580920MDFRLR05",
                "Blvd. Juárez 99, Monterrey", TipoPension.BIENESTAR, EstadoSalud.BUENO));
    }

    public List<Persona> findAll() {
        return new ArrayList<>(lista);
    }

    public void save(Persona p) {
        p.setId(contadorId++);
        lista.add(p);
    }

    public void update(Persona p) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == p.getId()) {
                lista.set(i, p);
                return;
            }
        }
    }

    public void delete(int id) {
        lista.removeIf(p -> p.getId() == id);
    }

    public List<Persona> findByRangoEdad(int min, int max) {
        return lista.stream()
                .filter(p -> p.getEdad() >= min && p.getEdad() <= max)
                .collect(Collectors.toList());
    }

    public List<Persona> findByTipoPension(TipoPension tipo) {
        return lista.stream()
                .filter(p -> p.getTipoPension() == tipo)
                .collect(Collectors.toList());
    }
}