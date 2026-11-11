package com.example.u4p2.apartado1;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ModeloAEsquemaTests {

	private static final String RECURSO = "com.example.u4p2.apartado1.modelo.Recurso";
	private static final String ESPACIO = "com.example.u4p2.apartado1.modelo.Espacio";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Environment environment;

	@Test
	@DisplayName("Hibernate está configurado explícitamente con create")
	void modoHibernateCreate() {
		assertEquals("create", environment.getProperty("spring.jpa.hibernate.ddl-auto", "").trim());
	}

	@Test
	@DisplayName("Recurso está declarado como entidad y usa la tabla recurso")
	void entidadRecurso() throws Exception {
		assertEntity(RECURSO, "recurso");
	}

	@Test
	@DisplayName("Espacio está declarado como entidad y usa la tabla espacio")
	void entidadEspacio() throws Exception {
		assertEntity(ESPACIO, "espacio");
	}

	@Test
	@DisplayName("El mapeo declara longitudes, nulabilidad, unicidad y precisión")
	void opcionesColumnDeRecurso() throws Exception {
		Class<?> recurso = Class.forName(RECURSO);
		assertColumnMapping(recurso, "codigo", "codigo", false, 12, 0, 0, true);
		assertColumnMapping(recurso, "nombre", "nombre", false, 80, 0, 0, false);
		assertColumnMapping(recurso, "descripcion", "descripcion", true, 250, 0, 0, false);
		assertColumnMapping(recurso, "costeReposicion", "coste_reposicion", false, 0, 8, 2, false);
	}

	@Test
	@DisplayName("El mapeo declara las columnas del espacio")
	void opcionesColumnDeEspacio() throws Exception {
		Class<?> espacio = Class.forName(ESPACIO);
		assertColumnMapping(espacio, "codigo", "codigo_espacio", false, 8, 0, 0, true);
		assertColumnMapping(espacio, "nombre", "nombre_espacio", false, 60, 0, 0, false);
		assertColumnMapping(espacio, "aforo", "aforo", false, 0, 0, 0, false);
	}

	@Test
	@DisplayName("La tabla recurso se ha generado con todas sus columnas")
	void columnasDeRecurso() throws SQLException {
		Map<String, ColumnInfo> columns = columns("recurso");
		assertTrue(columns.keySet().containsAll(
				java.util.Set.of("id", "codigo", "nombre", "descripcion", "coste_reposicion", "prestable")),
				"Faltan columnas en recurso. Columnas encontradas: " + columns.keySet());
		assertEquals(6, columns.size(), "recurso debe tener exactamente seis columnas");
	}

	@Test
	@DisplayName("La tabla espacio se ha generado con todas sus columnas")
	void columnasDeEspacio() throws SQLException {
		Map<String, ColumnInfo> columns = columns("espacio");
		assertTrue(columns.keySet().containsAll(
				java.util.Set.of("id", "codigo_espacio", "nombre_espacio", "aforo", "planta", "accesible")),
				"Faltan columnas en espacio. Columnas encontradas: " + columns.keySet());
		assertEquals(6, columns.size(), "espacio debe tener exactamente seis columnas");
	}

	@Test
	@DisplayName("Las dos tablas tienen clave primaria y los códigos son únicos")
	void clavesYUnicidad() throws SQLException {
		assertPrimaryKey("recurso", "id");
		assertPrimaryKey("espacio", "id");
		assertUniqueColumn("recurso", "codigo");
		assertUniqueColumn("espacio", "codigo_espacio");
	}

	@Test
	@DisplayName("Las columnas de texto y decimales de recurso respetan sus tamaños")
	void tiposDeRecurso() throws SQLException {
		Map<String, ColumnInfo> columns = columns("recurso");
		assertColumn(columns, "codigo", Types.VARCHAR, 12, 0, false);
		assertColumn(columns, "nombre", Types.VARCHAR, 80, 0, false);
		assertColumn(columns, "descripcion", Types.VARCHAR, 250, 0, true);
		assertDecimalColumn(columns, "coste_reposicion", 8, 2, false);
	}

	@Test
	@DisplayName("Las columnas de espacio respetan sus nombres, tipos y nulabilidad")
	void tiposDeEspacio() throws SQLException {
		Map<String, ColumnInfo> columns = columns("espacio");
		assertColumn(columns, "codigo_espacio", Types.VARCHAR, 8, 0, false);
		assertColumn(columns, "nombre_espacio", Types.VARCHAR, 60, 0, false);
		assertColumn(columns, "aforo", Types.INTEGER, -1, 0, false);
		assertColumn(columns, "planta", Types.INTEGER, -1, 0, false);
	}

	@Test
	@DisplayName("Las dos tablas son independientes y no tienen claves foráneas")
	void noHayRelaciones() throws SQLException {
		assertNoForeignKeys("recurso");
		assertNoForeignKeys("espacio");
	}

	private void assertEntity(String className, String expectedTable) throws Exception {
		Class<?> entityClass = Class.forName(className);
		assertNotNull(entityClass.getAnnotation(Entity.class), className + " debe tener @Entity");
		Table table = entityClass.getAnnotation(Table.class);
		assertNotNull(table, className + " debe tener @Table con nombre explícito");
		assertEquals(expectedTable, table.name());

		Field id = entityClass.getDeclaredField("id");
		assertNotNull(id.getAnnotation(Id.class), "id debe tener @Id");
		GeneratedValue generatedValue = id.getAnnotation(GeneratedValue.class);
		assertNotNull(generatedValue, "id debe generarse automáticamente");
		assertEquals(GenerationType.IDENTITY, generatedValue.strategy());
	}

	private void assertColumnMapping(
			Class<?> entityClass,
			String fieldName,
			String expectedColumn,
			boolean nullable,
			int length,
			int precision,
			int scale,
			boolean unique) throws Exception {
		Field field = entityClass.getDeclaredField(fieldName);
		Column column = field.getAnnotation(Column.class);
		assertNotNull(column, fieldName + " debe declarar @Column");
		assertEquals(expectedColumn, column.name());
		assertEquals(nullable, column.nullable());
		assertEquals(unique, column.unique());
		if (length > 0) {
			assertEquals(length, column.length());
		}
		if (precision > 0) {
			assertEquals(precision, column.precision());
			assertEquals(scale, column.scale());
		}
	}

	private Map<String, ColumnInfo> columns(String tableName) throws SQLException {
		Map<String, ColumnInfo> columns = new HashMap<>();
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();
			String actualTableName = actualTableName(metadata, connection.getCatalog(), tableName);
			try (ResultSet result = metadata.getColumns(
					connection.getCatalog(), null, actualTableName, "%")) {
				while (result.next()) {
					String columnName = result.getString("COLUMN_NAME").toLowerCase(Locale.ROOT);
					columns.put(columnName, new ColumnInfo(
							result.getInt("DATA_TYPE"),
							result.getInt("COLUMN_SIZE"),
							result.getInt("DECIMAL_DIGITS"),
							result.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
							result.getString("IS_AUTOINCREMENT")));
				}
			}
		}
		return columns;
	}

	private void assertColumn(
			Map<String, ColumnInfo> columns,
			String name,
			int expectedType,
			int expectedSize,
			int expectedScale,
			boolean nullable) {
		ColumnInfo column = columns.get(name);
		assertNotNull(column, "No existe la columna " + name);
		assertEquals(expectedType, column.type(), "Tipo SQL inesperado en " + name);
		if (expectedSize >= 0) {
			assertEquals(expectedSize, column.size(), "Tamaño inesperado en " + name);
		}
		assertEquals(expectedScale, column.scale(), "Escala inesperada en " + name);
		assertEquals(nullable, column.nullable(), "Nulabilidad inesperada en " + name);
	}

	private void assertDecimalColumn(
			Map<String, ColumnInfo> columns,
			String name,
			int expectedPrecision,
			int expectedScale,
			boolean nullable) {
		ColumnInfo column = columns.get(name);
		assertNotNull(column, "No existe la columna " + name);
		assertTrue(column.type() == Types.DECIMAL || column.type() == Types.NUMERIC,
				"Tipo SQL inesperado en " + name + ": debe ser DECIMAL o NUMERIC");
		assertEquals(expectedPrecision, column.size(), "Precisión inesperada en " + name);
		assertEquals(expectedScale, column.scale(), "Escala inesperada en " + name);
		assertEquals(nullable, column.nullable(), "Nulabilidad inesperada en " + name);
	}

	private void assertNoForeignKeys(String tableName) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();
			String actualTableName = actualTableName(metadata, connection.getCatalog(), tableName);
			try (ResultSet result = metadata.getImportedKeys(
					connection.getCatalog(), null, actualTableName)) {
				assertTrue(!result.next(), "La tabla " + tableName + " no debe tener claves foráneas");
			}
		}
	}

	private void assertPrimaryKey(String tableName, String expectedColumn) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();
			String actualTableName = actualTableName(metadata, connection.getCatalog(), tableName);
			try (ResultSet result = metadata.getPrimaryKeys(
					connection.getCatalog(), null, actualTableName)) {
				assertTrue(result.next(), "La tabla " + tableName + " debe tener clave primaria");
				assertEquals(expectedColumn, result.getString("COLUMN_NAME").toLowerCase(Locale.ROOT));
			}
		}
	}

	private void assertUniqueColumn(String tableName, String expectedColumn) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();
			String actualTableName = actualTableName(metadata, connection.getCatalog(), tableName);
			try (ResultSet result = metadata.getIndexInfo(
					connection.getCatalog(), null, actualTableName, true, false)) {
				while (result.next()) {
					String column = result.getString("COLUMN_NAME");
					if (column != null && column.equalsIgnoreCase(expectedColumn)) {
						return;
					}
				}
			}
		}
		throw new AssertionError("La columna " + tableName + "." + expectedColumn + " debe ser única");
	}

	private String actualTableName(
			DatabaseMetaData metadata, String catalog, String expectedName) throws SQLException {
		try (ResultSet result = metadata.getTables(catalog, null, "%", new String[]{"TABLE"})) {
			while (result.next()) {
				String tableName = result.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(expectedName)) {
					return tableName;
				}
			}
		}
		throw new AssertionError("No existe la tabla " + expectedName);
	}

	private record ColumnInfo(int type, int size, int scale, boolean nullable, String autoIncrement) {
	}
}
