package ar.com.ada.creditos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.yahoo.config.provision.zone.ZoneId;

import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.creditos.*;
import ar.com.ada.creditos.entities.Cliente;
import ar.com.ada.creditos.entities.Prestamo;
import ar.com.ada.creditos.excepciones.*;
import ar.com.ada.creditos.managers.ClienteManager;
import ar.com.ada.creditos.managers.PrestamoManager;
import ar.com.ada.creditos.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected ClienteManager ABMCliente = new ClienteManager();

    protected PrestamoManager ABMPrestamo = new PrestamoManager();// creamos cada manager

    public void iniciar() throws Exception {

        try {

            ABMCliente.setup();// conecta al manager a la base de datos a traves del metodo set up
            ABMPrestamo.setup();// idem

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            alta();
                        } catch (ClienteDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        baja();
                        break;

                    case 3:
                        modifica();
                        break;

                    case 4:
                        listar();
                        break;

                    case 5:
                        listarPorNombre();
                        break;
                    case 6:
                        cargarPrestamo();
                        break;
                    case 7:
                        // listarPrestamo();Esta version recorre por prestamo y la siguiente por Cliente
                        listarPrestamoPorCliente();
                        break;
                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMCliente.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void alta() throws Exception {
        Cliente cliente = new Cliente();
        System.out.println("Ingrese el nombre:");
        cliente.setNombre(Teclado.nextLine());
        System.out.println("Ingrese el DNI:");
        cliente.setDni(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese la domicilio:");
        cliente.setDomicilio(Teclado.nextLine());

        System.out.println("Ingrese el Domicilio alternativo(OPCIONAL):");

        String domAlternativo = Teclado.nextLine();

        if (domAlternativo != null)
            cliente.setDomicilioAlternativo(domAlternativo);

        Prestamo prestamo = new Prestamo();
        BigDecimal importePrestamo = new BigDecimal(5000);
        prestamo.setImporte(importePrestamo); // forma 1
        prestamo.setFecha(new Date()); // Fecha Actual de la maquina forma 2.
        prestamo.setFechaAlta(new Date());// Fecha generada por la maquina. Esta fecha no debe venir de front.
        prestamo.setCuotas(10); // Le vamos a dar 10 cuotas por defecto
        prestamo.setCliente(cliente);

        ABMCliente.create(cliente);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Cliente generada con exito.  " + cliente.getClienteId);
         */

        System.out.println("Cliente generada con exito.  " + cliente);

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Cliente:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {

            try {

                ABMCliente.delete(clienteEncontrado);
                System.out
                        .println("El registro del cliente " + clienteEncontrado.getClienteId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una cliente. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Cliente:");
        String dni = Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.readByDNI(dni);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {
            ABMCliente.delete(clienteEncontrado);
            System.out.println("El registro del DNI " + clienteEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la cliente a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la cliente a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(clienteEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la cliente desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    clienteEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    clienteEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
                    clienteEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    Teclado.nextLine();
                    clienteEncontrado.setDomicilioAlternativo(Teclado.nextLine());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMCliente.update(clienteEncontrado);

            System.out.println("El registro de " + clienteEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Cliente no encontrado.");
        }

    }

    public void listar() {

        List<Cliente> todos = ABMCliente.buscarTodos();
        for (Cliente c : todos) {
            mostrarCliente(c);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Cliente> clientes = ABMCliente.buscarPor(nombre);
        for (Cliente cliente : clientes) {
            mostrarCliente(cliente);
        }
    }

    public void mostrarCliente(Cliente cliente) {

        System.out.print("Id: " + cliente.getClienteId() + " Nombre: " + cliente.getNombre() + " DNI: "
                + cliente.getDni() + " Domicilio: " + cliente.getDomicilio());

        if (cliente.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + cliente.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un cliente.");
        System.out.println("2. Para eliminar un cliente.");
        System.out.println("3. Para modificar un cliente.");
        System.out.println("4. Para ver el listado.");
        System.out.println("5. Buscar un cliente por nombre especifico(SQL Injection)).");
        System.out.println("6. Cargar un préstamo a un cliente.");
        System.out.println("7. Listar préstamos.");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }

    public void cargarPrestamo() {

        System.out.println("Ingrese el Id del Cliente");
        int ClienteID = Teclado.nextInt();
        Cliente cl = ABMCliente.read(ClienteID);
        if (cl == null) {
            System.out.println("El cliente no existe");
            return;
        }
        Prestamo p = new Prestamo();
        p.setCliente(cl);
        System.out.println("Ingrese el monto del préstamo");
        p.setImporte(Teclado.nextBigDecimal());
        System.out.println("Ingrese la cantidad de cuotas");
        p.setCuotas(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese la fecha");

        String expectedPattern = "dd/mm/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try {
            Date fecha = formatter.parse(Teclado.nextLine());
            p.setFecha(fecha);
        } catch (ParseException e) {
            System.out.println("Hay un error en la fecha. Vuelva a intentarlo");
            return;
        }

        p.setFechaAlta(new Date());
        ABMPrestamo.create(p);// Paso final para guardar todo en la base de datos a traves del manager
        System.out.println("Se ha creado su préstamo con éxito");

    }

    public void listarPrestamo() {
        List<Prestamo> todos = ABMPrestamo.buscarTodos();
        for (Prestamo p : todos) {
            mostrarPrestamo(p);

        }
    }

    private void mostrarPrestamo(Prestamo p) {

        System.out.println("Id: " + p.getPrestamoId() + " Cliente " + p.getCliente().getNombre() + " Importe: "
                + p.getImporte() + " Cuotas: " + p.getCuotas() + " Fecha de alta: " + p.getFechaAlta());

    }

    public void listarPrestamoPorCliente() {
        List<Cliente> todos = ABMCliente.buscarTodos();
        for (Cliente cliente : todos) {
            mostrarPrestamo(cliente);

        }
    }

    private void mostrarPrestamo(Cliente cliente) {

        for (Prestamo p : cliente.getPrestamos()) {
            mostrarPrestamo(p);
        }

    }

}