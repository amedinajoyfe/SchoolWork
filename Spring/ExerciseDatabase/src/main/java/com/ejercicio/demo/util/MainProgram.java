package com.ejercicio.demo.util;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MainProgram {
    private static Map<String, Set<String>> duplicatedIds = new HashMap<>();
    private static Logger logger = Logger.getLogger(MainProgram.class.getName());
    private static String LogError = "errores.log";

    private static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/farmville_db";
        String usuario = "root";
        String password = "1234";
        Connection newConnection = DriverManager.getConnection(url, usuario, password);
        newConnection.setAutoCommit(false);
        return newConnection;
    }

    public static void main(String[] args) {
        String rutaRecursos = "src/main/resources/ficheros_csv_farmvile";
        int totalArchivosCSV = 0;
        try {
            File carpetaPrincipal = new File(rutaRecursos);
            if (carpetaPrincipal.isDirectory()) {
                File[] archivosCSV = carpetaPrincipal.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
                if (archivosCSV != null) {
                    for (File archivoCSV : archivosCSV) {
                        String CSVRoute = archivoCSV.getAbsolutePath();
                        System.out.println("Procesando archivo: " + CSVRoute);
                        try (Reader reader = new FileReader(archivoCSV);
                             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
                            for (CSVRecord csvRecord : csvParser) {
                                try {
                                    boolean alreadyExists = false;
                                    switch (archivoCSV.getName()) {
                                        case "construcciones.csv":
                                            alreadyExists = checkRegistry("construcciones",
                                                    csvRecord);
                                            break;
                                        case "granjero_granjero.csv":
                                            alreadyExists = checkRegistry(
                                                    "granjero_granjero", csvRecord);
                                            break;
                                        case "granjeros.csv":
                                            alreadyExists = checkRegistry("granjeros",
                                                    csvRecord);
                                            break;
                                        case "plantaciones.csv":
                                            alreadyExists = checkRegistry("plantaciones",
                                                    csvRecord);
                                            break;
                                        case "riegos.csv":
                                            alreadyExists = checkRegistry("riegos",
                                                    csvRecord);
                                            break;
                                        case "tractores.csv":
                                            alreadyExists = checkRegistry("tractores",
                                                    csvRecord);
                                            break;
                                        default:
                                            System.out.println("Tabla no reconocida: " + archivoCSV.getName());
                                    }
                                    if (alreadyExists) {
                                        handleDuplicate(archivoCSV.getName(), csvRecord);
                                    } else {
                                        insertIntoDatabase(archivoCSV.getName(), csvRecord);
                                    }
                                } catch (Exception e) {
                                    handleError(csvRecord, e);
                                }
                            }
                        }
                        totalArchivosCSV++;
                    }
                }
                System.out.println(
                        "Proceso completado. Total de archivos CSV procesados: " + totalArchivosCSV);
            } else {
                System.out.println("La ruta especificada no es un directorio válido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!duplicatedIds.isEmpty()) {
            System.out.println("Registros duplicados encontrados:");
            for (Map.Entry<String, Set<String>> entry : duplicatedIds.entrySet()) {
                String name = entry.getKey();
                Set<String> names = entry.getValue();

                System.out.print(name.replace(".csv", "") + ": ");

                for (String id : names) {
                    System.out.print(id + ", ");
                }

                System.out.println();
            }
        }
    }

    private static boolean checkRegistry(String tabla, CSVRecord csvRecord) {
        // No sabía como utilizar el rollback y a la vez el try with resources
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + tabla + " WHERE id = ?");
            String id = csvRecord.get(0);
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private static void handleDuplicate(String tabla, CSVRecord csvRecord) {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tabla.replace(".csv", "") + " WHERE id = ?");
            String idDuplicado = csvRecord.get(0);
            preparedStatement.setString(1, idDuplicado);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Procesando registro duplicado");
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 0; i < csvRecord.size(); i++) {
                        if (!csvRecord.get(i).equals(resultSet.getString(i + 1))) {
                            String columnName = metaData.getColumnName(i + 1);
                            PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tabla.replace(".csv", "") + " SET " + columnName + " = ? WHERE id = ?");
                            updateStatement.setString(1, csvRecord.get(i));
                            updateStatement.setString(2, idDuplicado);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }

            if (duplicatedIds.containsKey(tabla)) {
                duplicatedIds.get(tabla).add(idDuplicado);
            } else {
                Set<String> ids = new HashSet<>();
                ids.add(idDuplicado);
                duplicatedIds.put(tabla, ids);
            }

        } catch (Exception e) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void insertIntoDatabase(String tabla, CSVRecord csvRecord) {
        try {
            String query = "";
            switch (tabla) {
                case "construcciones.csv":
                    query = "INSERT INTO construcciones (id, nombre, precio, id_granjero) VALUES (?, ?, ?, ?)";
                    break;
                case "granjero_granjero.csv":
                    query = "INSERT INTO granjero_granjero (id, id_vecino, puntos_compartidos) VALUES (?, ?, ?)";
                    break;
                case "granjeros.csv":
                    query = "INSERT INTO granjeros (id, nombre, descripcion, dinero, puntos, nivel) VALUES (?, ?, ?, ?, ?, ?)";
                    break;
                case "plantaciones.csv":
                    query = "INSERT INTO plantaciones (id, nombre, precio_compra, precio_venta, proxima_cosecha, id_granjero) VALUES (?, ?, ?, ?, ?, ?)";
                    break;
                case "riegos.csv":
                    query = "INSERT INTO riegos (id, tipo, velocidad, id_plantacion) VALUES (?, ?, ?, ?)";
                    break;
                case "tractores.csv":
                    query = "INSERT INTO tractores (id, modelo, velocidad, precio_venta, id_construccion) VALUES (?, ?, ?, ?, ?)";
                    break;
                default:
                    System.out.println("Tabla no reconocida: " + tabla);
            }

            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (int i = 0; i < csvRecord.size(); i++) {
                    preparedStatement.setString(i + 1, csvRecord.get(i));
                }
                preparedStatement.executeUpdate();
                System.out.println("Registro insertado en la tabla '" + tabla + "'.");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private static void handleError(CSVRecord csvRecord, Exception e) {
        // Registra el registro que ha causado el error en el archivo errores.log
        try {
            FileHandler fileHandler = new FileHandler(LogError, true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.severe("Error al procesar el registro: " + csvRecord.toString());
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
