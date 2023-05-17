/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao.departamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import modelo.Departamento;
import modelo.dao.AbstractGenericDao;
import modelo.exceptions.InstanceNotFoundException;
import util.ConnectionManager;
import util.MyDataSource;

/**
 *
 * @author mfernandez
 */
public class DepartamentoEXistDao extends AbstractGenericDao<Departamento> implements IDepartamentoDao {

	private static final String DEPT_ROW_TAG = "DEP_ROW";
	private static final String DEPT_NO_TAG = "DEPT_NO";
	private static final String DNOMBRE_TAG = "DNOMBRE";
	private static final String LOC_TAG = "LOC";
	private MyDataSource dataSource;

	public DepartamentoEXistDao() {
		this.dataSource = ConnectionManager.getDataSource();
		Class cl;
		try {
			cl = Class.forName(dataSource.getDriver());

			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");

			DatabaseManager.registerDatabase(database);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@Override
	public Departamento read(long id) throws InstanceNotFoundException {
		Departamento departamento = null;

		try (Collection col = DatabaseManager.getCollection(dataSource.getUrl() + dataSource.getColeccionDepartamentos(),
				dataSource.getUser(), dataSource.getPwd())) {

			XQueryService xqs = (XQueryService) col.getService("XQueryService", "1.0");
			xqs.setProperty("indent", "yes");

			CompiledExpression compiled = xqs.compile("//DEP_ROW[DEPT_NO=" + id + "]");
			ResourceSet result = xqs.execute(compiled);

			if (result.getSize() == 0)
				throw new InstanceNotFoundException(id, Departamento.class.getName());

			ResourceIterator i = result.getIterator();
			Resource res = null;
			while (i.hasMoreResources()) {
				try {
					res = i.nextResource();
					departamento = stringNodeToDepartamento(res.getContent().toString());

				} finally {
					// dont forget to cleanup resources
					try {
						((EXistResource) res).freeResources();
					} catch (XMLDBException xe) {
						departamento = null;
						xe.printStackTrace();
					}
				}
			}

		} catch (XMLDBException e) {
			departamento = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return departamento;
	}

	@Override
	public List<Departamento> readName(String deptName) throws InstanceNotFoundException {
		List<Departamento> departamentos = new ArrayList<>();
		Departamento departamento;

		try (Collection col = DatabaseManager.getCollection(dataSource.getUrl() + dataSource.getColeccionDepartamentos(),
				dataSource.getUser(), dataSource.getPwd())) {

			XQueryService xqs = (XQueryService) col.getService("XQueryService", "1.0");
			xqs.setProperty("indent", "yes");

			String depMin=deptName.toLowerCase();

			String query="//DEP_ROW[lower-case(DNOMBRE)= '"+depMin+"']";
			CompiledExpression compiled = xqs.compile(query);
			ResourceSet result = xqs.execute(compiled);

			if (result.getSize() == 0)
				throw new InstanceNotFoundException(deptName, Departamento.class.getName());

			ResourceIterator i = result.getIterator();
			Resource res = null;
			while (i.hasMoreResources()) {
				try {
					res = i.nextResource();

					//System.out.println(res.getContent().toString());

					departamento = stringNodeToDepartamento(res.getContent().toString());

					departamentos.add(departamento);

				} finally {
					// dont forget to cleanup resources
					try {
						((EXistResource) res).freeResources();
					} catch (XMLDBException xe) {
						departamento = null;
						xe.printStackTrace();
					}
				}
			}

		} catch (XMLDBException e) {
			departamento = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return departamentos;

	}


	private Departamento stringNodeToDepartamento(String nodeString) {
		Element node = null;
		Departamento departamento = null;
		try {
			node = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new ByteArrayInputStream(nodeString.getBytes())).getDocumentElement();

			String nombre = getElementText(node, DNOMBRE_TAG);
			String location = getElementText(node, LOC_TAG);
			Integer id = Integer.parseInt(getElementText(node, DEPT_NO_TAG));

			departamento = new Departamento(nombre, location);
			departamento.setDeptno(id);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return departamento;
	}

	private String getElementText(Element parent, String tag) {
		String texto = "";
		NodeList lista = parent.getElementsByTagName(tag);

		if (lista.getLength() > 0) {
			texto = lista.item(0).getTextContent();
		}

		return texto;
	}



	@Override
	public boolean create(Departamento entity) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean update(Departamento entity) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean delete(Departamento entity) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public List<Departamento> findAll() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean exists(Integer dept) {
		// TODO Auto-generated method stub
		return false;
	}

	

	




}
