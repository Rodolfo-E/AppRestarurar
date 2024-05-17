package pe.com.senace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pe.com.domain.bean.AlfrescoException;
import pe.com.domain.bean.AlfrescoNode;
import pe.com.domain.service.AlfrescoService;


public class restaurar {

	private static String host;
//	public static String port;
	private static String user;
	private static String password;
	private static String ruta_excel;

	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final Charset ISO = Charset.forName("ISO-8859-1");

	public static void main(String[] args) {

		Properties propiedad = new Properties();

		try {
			propiedad.load(new FileReader(args[0]));
		
			host = propiedad.getProperty("HOST");
			//port = propiedad.getProperty("PORT");
			user = propiedad.getProperty("USER");
			password = propiedad.getProperty("PASSWORD");
			ruta_excel = propiedad.getProperty("RUTA_EXCEL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ejecucion de la restauracion");
		FileWriter fil = null;
		try {

			File excel = new File(ruta_excel);
			fil = new FileWriter(ruta_excel.replace(".xlsx", ".txt"));
			if (excel.exists()) {
				FileInputStream fis = new FileInputStream(excel);
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next();

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					try {
						DataFormatter formatter = new DataFormatter();
						String order = formatter.formatCellValue(row.getCell(0));

						if (!order.isEmpty()) {

							String uuid = formatter.formatCellValue(row.getCell(0));

							
							AlfrescoNode node = restaurar(uuid);
							if (node != null) {
								//System.out.println("CODIGO: " + rptaBean.getCode());
							//	System.out.println("MENSAJE: " + rptaBean.getMessage());
								// if (node.)) {

								System.out.println("Documento restaurado -->" + uuid + "  Nombre Documento: " + node.getName());

								fil.write(uuid + "  |  UUID:  " + node.getName() + "\n");
								System.out.println("\n");
								gc();

								/*
								 * } else { System.out.println("ERROR" + rptaBean.getException()); }
								 */

							} else {

								System.out.println("No hay archivo");
							}
							fil.flush();
						}

					} catch (Exception e) {
						System.err.println("Error al obtener el documento");
						e.printStackTrace();
					}
				}
				wb.close();
				fil.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void gc() {
		Runtime garbage = Runtime.getRuntime();
		garbage.gc();
	}

	static AlfrescoNode restaurar(String uuid) {

		AlfrescoService service = new AlfrescoService();
		Map<String,String> credenciales=new HashMap<String,String>();

		credenciales.put("HOST", host);
		credenciales.put("USER", user);
		credenciales.put("PASSWORD", password);
		try {

			service.setConexion(credenciales);
			return service.restuararNode(uuid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
