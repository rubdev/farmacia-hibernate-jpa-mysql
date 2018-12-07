package test;

import dev.ruben.farmacia.modelo.Farmacia;
import dev.ruben.farmacia.modelo.Medicamento;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author rubensegura
 */
public class TestFarmacia {

    private static Scanner sc = new Scanner(System.in);
    private static EntityManager manager;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("aplicacion");
    
    // MAIN DE LA APLICACIÓN
    
    public static void main(String[] args) {
        menuFarmaciasMedicamentos();
    }
    
    // FUNCIONES DE LA APLICACIÓN
    
    public static void menuFarmaciasMedicamentos() {
        int opcion;
        boolean ejecucion = true;
        do {
            // me busco el ultimo id siempre para trabajar con el
            Long idNuevaFarmacia = ultimoIdFarmacia();
            System.out.println("###############################################");
            System.out.println("##### GESTIÓN DE FARMACIAS Y MEDICAMENTOS #####");
            System.out.println("FUNCIONES PRINCIPALES: ");
            System.out.println("1. Nueva farmacia");
            System.out.println("2. Nuevo medicamento en una farmacia");
            System.out.println("3. Modificar nombre farmacia");
            System.out.println("4. Modificar stock de medicamento");
            System.out.println("5. Eliminar una farmacia");
            System.out.println("6. Eliminar un medicamento de una farmacia");
            System.out.println("CONSULTAS: ");
            System.out.println("7. Listar todas las farmacias");
            System.out.println("8. Listar todos los medicamentos");
            System.out.println("9. Buscar farmacia por nombre");
            System.out.println("10. Buscar medicamento por nombre");
            System.out.println("11. Buscar medicamentos de una farmacia");
            System.out.println("Introduce -1 para salir de la aplicación");
            System.out.print("Introduce opción: ");
            opcion = sc.nextInt();
            System.out.println("###############################################");
            sc.nextLine(); //limpio el buffer
            switch (opcion) {
                case -1:
                    ejecucion = false;
                    break;
                case 1:
                    nuevaFarmacia();
                    break;
                case 2:
                    nuevoMedicamento();
                    break;
                case 3:
                    modificarFarmacia();
                    break;
                case 4:
                    modificarCantidadMedicamento();
                    break;
                case 5:
                    //borrarFarmacia();
                    borrarFarmaciaV2();
                    break;
                case 6:
                    borrarMedicamentoFarmacia();
                    break;
                case 7:
                    listarFarmacias();
                    break;
                case 8:
                    listarMedicamentos();
                    break;
                case 9:
                    buscaFarmaciaNombreLista();
                    break;
                case 10:
                    buscarMedicamentoPorNombre();
                    break;
                case 11:
                    verMedicamentosEnFarmacia();
                    break;
                default:
                    System.out.println("No es una opción válida");
                    break;
            }
        } while (ejecucion);
    }

    /**
     * Obtiene el siguiente id disponible para usar al insertar una nueva
     * farmacia en base de datos
     *
     * @return idNuevaFarmacia
     */
    public static Long ultimoIdFarmacia() {
        manager = emf.createEntityManager();
        Query consulta = manager.createQuery("select max(f.id) from Farmacia f");
        List<Long> maximo = consulta.getResultList();
        Long idNuevaFarmacia = null,
                ultimoID = null;
        for (Long max : maximo) {
            ultimoID = max;
        }
        idNuevaFarmacia = ultimoID + 1;
        manager.close();
        return idNuevaFarmacia;
    }

    /**
     * Obtiene el siguiente id disponible para usar al insertar un nuevo
     * medicamento en base de datos
     *
     * @return idNuevoMedicamento
     */
    public static Long ultimoIdMedicamento() {
        manager = emf.createEntityManager();
        Query consulta = manager.createQuery("select max(m.id) from Medicamento m");
        List<Long> maximo = consulta.getResultList();
        Long idNuevoMedicamento = null,
                ultimoID = null;
        for (Long max : maximo) {
            ultimoID = max;
        }
        idNuevoMedicamento = ultimoID + 1;
        manager.close();
        return idNuevoMedicamento;
    }

    /**
     * Inserta una nueva farmacia en la base de datos
     */
    public static void nuevaFarmacia() {
        System.out.println("*** INSERCIÓN DE NUEVA FARMACIA ***");
        Long idNuevaFarmacia = ultimoIdFarmacia();
        System.out.println("Introduce el nombre de la farmacia: ");
        String nombre = sc.nextLine();
        Farmacia f = new Farmacia(idNuevaFarmacia, nombre);
        emf = Persistence.createEntityManagerFactory("aplicacion");
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(f);
        manager.getTransaction().commit();
        manager.close();
    }
    
    /**
     * Inserta un nuevo medicamento en la base de datos
     */
    public static void nuevoMedicamento() {
        System.out.println("*** INSERCIÓN DE NUEVO MEDICAMENTO ***");
        Long idNuevoMedicamento = ultimoIdMedicamento();
        System.out.println("Introduce el nombre del medicamento:");
        String nombre = sc.nextLine();
        System.out.println("Introduce la cantidad disponible:");
        int cantidad = sc.nextInt();
        Long idFarmacia = buscaFarmaciaPorNombre();
        System.out.println("TENGO EL ID DE LA FARMACIA A INSERTAR MEDICAMENTO => "+idFarmacia);
        Farmacia f = new Farmacia();
        emf = Persistence.createEntityManagerFactory("aplicacion");
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        f = manager.find(Farmacia.class, idFarmacia);
        Medicamento m = new Medicamento(idNuevoMedicamento, nombre, cantidad, f);
        manager.persist(m);
        manager.getTransaction().commit();
        manager.close(); 
    }
    
    /**
     * Busca coincidencias sobre un nombre de farmacia y devuelve el id
     * de la que tú elijas para poder usarlo en cualquier consulta relacionada
     * con un medicamento
     * 
     * @return idFarmacia
     */
    public static Long buscaFarmaciaPorNombre() {
        Long idFarmacia = null;
        int tamLista;
        do {
            System.out.println("Dime el nombre de la farmacia: ");
            String nombreFarmacia = sc.nextLine();
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            Query consulta = manager.createQuery("FROM Farmacia WHERE nombre LIKE '%" + nombreFarmacia + "%'");
            List<Farmacia> farmacias = (List<Farmacia>) consulta.getResultList();
            tamLista = farmacias.size();
            if (tamLista > 0) {
                System.out.println("Se han encontrado " + tamLista + " coincidencias:");
                for (Farmacia farmacia : farmacias) {
                    System.out.println(farmacia.toString());
                }
            } else {
                System.out.println("No se han encontrado coincidencias para " + nombreFarmacia);
            }
        } while (tamLista < 1);
        manager.close();
        System.out.println("¿Que id de farmacia seleccionas?");
        idFarmacia = sc.nextLong();
        return idFarmacia;
    }
    
    /**
     * Lista farmacias buscadas por su nombre
     */
    public static void buscaFarmaciaNombreLista() {
        int tamLista;
        do {
            System.out.println("Dime el nombre de la farmacia: ");
            String nombreFarmacia = sc.nextLine();
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            Query consulta = manager.createQuery("FROM Farmacia WHERE nombre LIKE '%" + nombreFarmacia + "%'");
            List<Farmacia> farmacias = (List<Farmacia>) consulta.getResultList();
            tamLista = farmacias.size();
            if (tamLista > 0) {
                System.out.println("Se han encontrado " + tamLista + " coincidencias:");
                for (Farmacia farmacia : farmacias) {
                    System.out.println(farmacia.toString());
                }
            } else {
                System.out.println("No se han encontrado coincidencias para " + nombreFarmacia);
            }
        } while (tamLista < 1);
        manager.close();
    }
    
    /**
     * Modifica el nombre de una farmacia 
     */
    public static void modificarFarmacia() {
        System.out.println("*** MODIFICACIÓN DE NOMBRE DE FARMACIA ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        sc.nextLine();
        System.out.println("Introduce el nuevo nombre de la farmacia:");
        String nuevoNombre = sc.nextLine();
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        Query query = manager.createQuery("UPDATE Farmacia set nombre = :nuevoNombre where id = :idFarmacia");
        query.setParameter("nuevoNombre", nuevoNombre);
        query.setParameter("idFarmacia", idFarmacia);
        int resultado = query.executeUpdate();
        System.out.println("Resultado de la actualización: "+resultado);
    }
    
    /**
     * Actualiza el stock de un medicamento en una farmacia
     * 
     * @param id
     * @return idMedicamento
     */
    public static Long listarMedicamentosDeFarmacia(Long id) {
        sc.nextLine(); //limpio buffer
        manager = emf.createEntityManager();
        Query query = manager.createQuery("FROM Medicamento WHERE FARMACIA_ID = :idFarmacia");
        query.setParameter("idFarmacia", id);
        List<Medicamento> lista = (List<Medicamento>) query.getResultList();
        for (Medicamento medicamento : lista) {
            System.out.println(medicamento.toString());
        }
        System.out.println("Introduce el id del medicamento:");
        Long idMedicamento = Long.parseLong(sc.nextLine());
        return idMedicamento;
    }
    
    /**
     * Ver listado de medicamentos en una farmacia
     */
    public static void verMedicamentosEnFarmacia() {
        System.out.println("*** LISTADO DE MEDICAMENTOS EN UNA FARMACIA ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        manager = emf.createEntityManager();
        Query query = manager.createQuery("FROM Medicamento WHERE FARMACIA_ID = :idFarmacia");
        query.setParameter("idFarmacia", idFarmacia);
        List<Medicamento> lista = (List<Medicamento>) query.getResultList();
        System.out.println("¿Qué medicamento quieres buscar en la farmacia");
        for (Medicamento medicamento : lista) {
            System.out.println(medicamento.toString());
        }
    }
    
    /**
     * Buscar medicamentos y listar resultados por su nombre
     */
    public static void buscarMedicamentoPorNombre() {
        System.out.println("*** BUSCAR MEDICAMENTO POR SU NOMBRE ***");
        String busqueda;
        System.out.println("¿Qué medicamento quieres buscar?");
        busqueda = sc.nextLine();
        manager = emf.createEntityManager();
        Query query = manager.createQuery("FROM Medicamento WHERE nombre LIKE '%"+busqueda+"%'");
        List<Medicamento> lista = (List<Medicamento>) query.getResultList();
        if (lista.size() > 0) {
            System.out.println("* Coincidencias encontradas *");
            for (Medicamento medicamento : lista) {
                System.out.println(medicamento.toString());
            } 
        } else {
            System.err.println("* No se encontraron coincidencias en la búsqueda *");
        }
    }
    
    /**
     * Modifica cantidad o nombre de un medicamento
     */
    public static void modificarCantidadMedicamento() {
        System.out.println("*** MODIFICACIÓN DE STOCK DE UN MEDICAMENTO ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        Long idMedicamento = listarMedicamentosDeFarmacia(idFarmacia);
        int cantidad;
        do {
            System.out.println("¿Qué nueva cantidad va a tener el medicamento?");
            cantidad = Integer.parseInt(sc.nextLine());
        } while (cantidad < 1);
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        Query query = manager.createQuery("UPDATE Medicamento set cantidad = :nuevaCantidad where id = :idMedicamento");
        query.setParameter("nuevaCantidad", cantidad);
        query.setParameter("idMedicamento", idMedicamento);
        int resultado = query.executeUpdate();
        if (resultado == 1) {
            System.out.println("* Se ha actualizado el stock correctamente *");
        } else {
            System.err.println("Ocurrión un error al borrar la farmacia");
        }
        manager.getTransaction().commit();
        manager.close(); 
    }
    
    /**
     * Elimina una farmacia de la base de datos
     */
    public static void borrarFarmacia() {
        System.out.println("*** BORRADO DE UNA FARMACIA ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        System.out.println("EL ID DE LA FARMACIA A BORRAR ES => "+idFarmacia);
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        Query query = manager.createQuery("DELETE Farmacia WHERE id = :farmaciaBorrar");
        query.setParameter("farmaciaBorrar", idFarmacia);
        int resultado = query.executeUpdate();
        if (resultado == 1) {
            System.out.println("* Se ha borrado la farmacia con éxito *");
        } else {
            System.err.println("Ocurrión un error al borrar la farmacia");
        }
        manager.getTransaction().commit();
        manager.close(); 
    }
    
    /**
     * BORRAR FARMACIA V2 CON MERGE DE OBJETO
     */
    public static void borrarFarmaciaV2() {
        System.out.println("*** BORRADO DE UNA FARMACIA Y SUS MEDICAMENTOS ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        Farmacia f = new Farmacia();
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        f = manager.find(Farmacia.class, idFarmacia);
        f=manager.merge(f);
        manager.remove(f);
        manager.getTransaction().commit();
        manager.close();
    }
    
    /**
     * Elimina un medicamento de una farmacia
     */
    public static void borrarMedicamentoFarmacia() {
        System.out.println("*** BORRADO DE MEDICAMENTO EN UNA FARMACIA ***");
        Long idFarmacia = buscaFarmaciaPorNombre();
        Long idMedicamento = listarMedicamentosDeFarmacia(idFarmacia);
        System.out.println("ID FARMACIA "+idFarmacia+" Y EL ID MEDICAMENTO "+idMedicamento+" SERÁ BORRADO");
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        Query query = manager.createQuery("DELETE Medicamento WHERE id = :medicamentoBorrar");
        query.setParameter("medicamentoBorrar", idMedicamento);
        int resultado = query.executeUpdate();
        if (resultado == 1) {
            System.out.println("* Se ha borrado el medicamento con éxito *");
        } else {
            System.err.println("Ocurrión un error al borrar la farmacia");
        }
        manager.getTransaction().commit();
        manager.close();
    }
    
    /**
     * Listar todos los medicamentos
     */
    public static void listarMedicamentos() {
        System.out.println("*** LISTADO DE TODOS LOS MEDICAMENTOS ***");
        manager = emf.createEntityManager();
        Query query = manager.createQuery("FROM Medicamento");
        List<Medicamento> lista = (List<Medicamento>) query.getResultList();
        for (Medicamento medicamento : lista) {
            System.out.println(medicamento.toString());
        }
    }
    
    /**
     * Listar todas las farmacias
     */
    public static void listarFarmacias() {
        System.out.println("*** LISTADO DE TODAS LAS FARMACIAS ***");
        manager = emf.createEntityManager();
        Query query = manager.createQuery("FROM Farmacia");
        List<Farmacia> lista = (List<Farmacia>) query.getResultList();
        for (Farmacia farmacia : lista) {
            farmacia.toString();
        }
    }
    
}
