package modelo.servicio.departamento;


import modelo.Departamento;
import modelo.exceptions.InstanceNotFoundException;

import java.util.List;

public interface IServicioDepartamento {


	public Departamento read(long deptno) throws InstanceNotFoundException;

	public List<Departamento> readName(String deptName) throws  InstanceNotFoundException;
	
}


