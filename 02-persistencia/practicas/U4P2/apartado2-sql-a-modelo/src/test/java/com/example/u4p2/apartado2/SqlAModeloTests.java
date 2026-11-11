package com.example.u4p2.apartado2;

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
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SqlAModeloTests {

	private static final String MATERIAL =
			"com.example.u4p2.apartado2.modelo.MaterialAula";
	private static final String PUESTO =
			"com.example.u4p2.apartado2.modelo.PuestoTrabajo";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Environment environment;

	@Test
	@DisplayName("Hibernate está configurado explícitamente con validate")
	void modoHibernateValidate() {
		assertEquals("validate", environment.getProperty("spring.jpa.hibernate.ddl-auto", "").trim());
	}

	@Test
	@DisplayName("MaterialAula está mapeada a la tabla material_aula")
	void entidadMaterial() throws Exception {
		assertEntity(MATERIAL, "material_aula");
	}

	@Test
	@DisplayName("PuestoTrabajo está mapeada a la tabla puesto_trabajo")
	void entidadPuesto() throws Exception {
		assertEntity(PUESTO, "puesto_trabajo");
	}

	@Test
	@DisplayName("El mapeo de MaterialAula usa los nombres exactos del SQL")
	void columnasDeMaterialMapeadas() throws Exception {
		Class<?> entity = Class.forName(MATERIAL);
		assertColumnMapping(entity, "referencia", "referencia", false, 12, 0);
		assertColumnMapping(entity, "nombre", "nombre_material", false, 80, 0);
		assertColumnMapping(entity, "descripcion", "descripcion", true, 250, 0);
		assertColumnMapping(entity, "costeReposicion", "coste_reposicion", false, 0, 8, 2);
		assertColumnMapping(entity, "unidades", "unidades", false, 0, 0);
		assertUniqueMapping(entity, "referencia");
	}

	@Test
	@DisplayName("El mapeo de PuestoTrabajo usa los nombres exactos del SQL")
	void columnasDePuestoMapeadas() throws Exception {
		Class<?> entity = Class.forName(PUESTO);
		assertColumnMapping(entity, "codigo", "codigo_puesto", false, 10, 0);
		assertColumnMapping(entity, "denominacion", "denominacion", false, 60, 0);
		assertColumnMapping(entity, "planta", "planta", false, 0, 0);
		assertColumnMapping(entity, "tieneElectricidad", "tiene_electricidad", false, 0, 0);
		assertUniqueMapping(entity, "codigo");
	}

	@Test
	@DisplayName("El SQL define claves primarias y códigos únicos")
	void clavesYUnicidadDelSql() throws SQLException {
		assertPrimaryKey("material_aula", "id");
		assertPrimaryKey("puesto_trabajo", "id");
		assertUniqueColumn("material_aula", "referencia");
		assertUniqueColumn("puesto_trabajo", "codigo_puesto");
	}

	@Test
	@DisplayName("El SQL proporcionado crea exactamente las tablas y columnas de material")
	void sqlCreaTablaMaterial() throws SQLException {
		Map<String, ColumnInfo> columns = columns("material_aula");
		assertTrue(columns.keySet().containsAll(java.util.Set.of(
				"id", "referencia", "nombre_material", "descripcion",
				"coste_reposicion", "unidades")));
		assertEquals(6, columns.size());
		assertColumn(columns, "referencia", Types.VARCHAR, 12, 0, false);
		assertColumn(columns, "nombre_material", Types.VARCHAR, 80, 0, false);
		assertColumn(columns, "descripcion", Types.VARCHAR, 250, 0, true);
		assertColumn(columns, "coste_reposicion", Types.DECIMAL, 8, 2, false);
	}

	@Test
	@DisplayName("El SQL proporcionado crea exactamente las tablas y columnas de puesto")
	void sqlCreaTablaPuesto() throws SQLException {
		Map<String, ColumnInfo> columns = columns("puesto_trabajo");
		assertTrue(columns.keySet().containsAll(java.util.Set.of(
				"id", "codigo_puesto", "denominacion", "planta", "tiene_electricidad")));
		assertEquals(5, columns.size());
		assertColumn(columns, "codigo_puesto", Types.VARCHAR, 10, 0, false);
		assertColumn(columns, "denominacion", Types.VARCHAR, 60, 0, false);
		assertColumn(columns, "planta", Types.INTEGER, -1, 0, false);
	}

	@Test
	@DisplayName("Las tablas entregadas no tienen claves foráneas")
	void tablasIndependientes() throws SQLException {
		assertNoForeignKeys("material_aula");
		assertNoForeignKeys("puesto_trabajo");
	}

	private void assertEntity(String className, String expectedTable) throws Exception {
		Class<?> entityClass = Class.forName(className);
		assertNotNull(entityClass.getAnnotation(Entity.class), className + " debe tener @Entity");
		Table table = entityClass.getAnnotation(Table.class);
		assertNotNull(table, className + " debe tener @Table con nombre explícito");
		assertEquals(expectedTable, table.name());

		Field id = entityClass.getDeclaredField("id");
		assertNotNull(id.getAnnotation(Id.class), "id debe tener @Id");
		assertNull(id.getAnnotation(GeneratedValue.class),
				"El SQL asigna el identificador; no debe generarlo Hibernate");
	}

	private void assertUniqueMapping(Class<?> entityClass, String fieldName) throws Exception {
		Field field = entityClass.getDeclaredField(fieldName);
		Column column = field.getAnnotation(Column.class);
		assertNotNull(column, fieldName + " debe declarar @Column");
		assertTrue(column.unique(), fieldName + " debe mapearse como único");
	}

	private void assertColumnMapping(
			Class<?> entityClass,
			String fieldName,
			String expectedColumn,
			boolean nullable,
			int length,
			int precision,
			int scale) throws Exception {
		Field field = entityClass.getDeclaredField(fieldName);
		Column column = field.getAnnotation(Column.class);
		assertNotNull(column, fieldName + " debe declarar @Column");
		assertEquals(expectedColumn, column.name());
		assertEquals(nullable, column.nullable());
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
			try (ResultSet result = metadata.getColumns(connection.getCatalog(), null, "%", "%")) {
				while (result.next()) {
					if (result.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
						String columnName = result.getString("COLUMN_NAME").toLowerCase(Locale.ROOT);
						columns.put(columnName, new ColumnInfo(
								result.getInt("DATA_TYPE"),
								result.getInt("COLUMN_SIZE"),
								result.getInt("DECIMAL_DIGITS"),
								result.getInt("NULLABLE") == DatabaseMetaData.columnNullable));
					}
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

	private void assertColumnMapping(
			Class<?> entityClass,
			String fieldName,
			String expectedColumn,
			boolean nullable,
			int length,
			int precision) throws Exception {
		assertColumnMapping(entityClass, fieldName, expectedColumn, nullable, length, precision, 0);
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

	private record ColumnInfo(int type, int size, int scale, boolean nullable) {
	}
}
