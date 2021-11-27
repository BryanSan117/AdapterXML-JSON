package edu.uspg;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import edu.uspg.model.ListaAlumnos;
import edu.uspg.model.Alumno;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class AdapterApplication {

    static String carnet;
    static String nombres;
    static String apellidos;
    static String correo;
    static int opcion = 0;

    static Scanner scn = new Scanner(System.in);
    static ArrayList<Alumno> listaAlumno = new ArrayList();

    static Alumno alumno;

    public static void main(String[] args) {
        menuPrincipal();
    }

        public static void menuPrincipal() {
        do {

            System.out.println(" ");
            System.out.println("\t ------Menú principal------");
            System.out.println("------Elija una opcion------");
            System.out.println("1. Ingresar datos del Alumno");
            System.out.println("2. Mostrar datos en XML");
            System.out.println("3. Mostrar datos en JSON");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            opcion = scn.nextInt();

            switch (opcion) {

                case 1:

                    agregarAlumno();

                    break;

                case 2:

                    System.out.println("\t---Datos en XML---");
                    objectToXML(alumno);
                    documentoXML();
                    //listObjectTOXML(listaAlumno);

                    break;

                case 3:

                    System.out.println("\t---Datos en JSON---");
                    Json(listaAlumno);

            }

        } while (opcion != 4);
    }
    
    
    public static void agregarAlumno() {

        System.out.println("Por favor ingrese los datos: ");

        System.out.println("Ingrese carnet: ");
        carnet = scn.next();

        System.out.println("Ingrese nombre: ");
        nombres = scn.next();

        System.out.println("Ingrese apellido: ");
        apellidos = scn.next();

        System.out.println("Ingrese correo electronico: ");
        correo = scn.next();

        alumno = new Alumno(carnet, nombres, apellidos, correo);
        listaAlumno.add(alumno);

        menuPrincipal();
    }


    public static void listObjectTOXML(ListaAlumnos listaAlumnos) {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ListaAlumnos.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();

            jaxbMarshaller.marshal(listaAlumnos, sw);

            String xmlData = sw.toString();

            System.out.println(xmlData);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void objectToXML(Alumno alumno) {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Alumno.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();

            jaxbMarshaller.marshal(alumno, sw);

            String xmlData = sw.toString();

            System.out.println(xmlData);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void documentoXML() {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            DOMImplementation implementation = builder.getDOMImplementation();

            Document documento = implementation.createDocument(null, "Alumnoxml", null);
            documento.setXmlVersion("1.0");

            Element alumnosxml = documento.createElement("alumnosxml");
            Element alumnoxml = documento.createElement("alumnoxml");

            // carnet
            Element carnet = documento.createElement("carnet");
            Text textCarnet = documento.createTextNode(alumno.getCarnet());
            carnet.appendChild(textCarnet);
            alumnoxml.appendChild(carnet);

            // nombres
            Element nombres = documento.createElement("nombres");
            Text textNombres = documento.createTextNode(alumno.getNombres());
            nombres.appendChild(textNombres);
            alumnoxml.appendChild(nombres);

            // apellidos
            Element apellidos = documento.createElement("apellidos");
            Text textApellidos = documento.createTextNode(alumno.getApellidos());
            apellidos.appendChild(textApellidos);
            alumnoxml.appendChild(apellidos);

            // Precio
            Element correo = documento.createElement("correo");
            Text textCorreo = documento.createTextNode(alumno.getCorreo());
            correo.appendChild(textCorreo);
            alumnoxml.appendChild(correo);

            alumnosxml.appendChild(alumnoxml);

            documento.getDocumentElement().appendChild(alumnosxml);

            Source source = new DOMSource(documento);

            Result result = new StreamResult(new File("Alumnoxml" + ".xml"));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void Json(ArrayList<Alumno> listaAlumno) {

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();

        for (int i = 0; i < listaAlumno.size(); i++) {

            JSONObject obj1 = new JSONObject();
            obj1.put("carnet", listaAlumno.get(i).getCarnet());
            obj1.put("Nombre", listaAlumno.get(i).getNombres());
            obj1.put("Apellido", listaAlumno.get(i).getApellidos());
            obj1.put("Correo", listaAlumno.get(i).getCorreo());
            array.add(obj1);
        }

        obj.put("Nombres", array);

        System.out.println(obj);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos_alumno.json"))) {
            bw.write(obj.toString());
            System.out.println("Fichero creado");
        } catch (IOException ex) {
            Logger.getLogger(AdapterApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
