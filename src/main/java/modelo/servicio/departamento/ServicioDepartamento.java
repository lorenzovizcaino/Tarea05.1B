package modelo.servicio.departamento;

import modelo.Departamento;
import modelo.dao.departamento.DepartamentoEXistDao;
import modelo.dao.departamento.IDepartamentoDao;
import modelo.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ServicioDepartamento implements IServicioDepartamento {

	private IDepartamentoDao departamentoDao;

	public ServicioDepartamento() {
		departamentoDao = new DepartamentoEXistDao();
	}



	@Override
	public Departamento read(long deptno) throws InstanceNotFoundException {
		return departamentoDao.read(deptno);
	}

	@Override
	public List<Departamento> readName(String deptName) throws InstanceNotFoundException {
		List <Departamento> departamentos;
		Departamento departamento = null;
		if(deptName.toLowerCase().contains("top secret") || deptName.toLowerCase().contains("confidencial")){
			departamentos=new ArrayList<>();
		}else{
			departamentos=departamentoDao.readName(deptName);
			if(departamentos.size()==0){
				throw new InstanceNotFoundException(departamento.getDeptno(),Departamento.class.getName());
			}
		}
		return departamentos;
	}

}
