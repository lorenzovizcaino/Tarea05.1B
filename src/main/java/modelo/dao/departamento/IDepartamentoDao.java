/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo.dao.departamento;


import modelo.Departamento;
import modelo.dao.IGenericDao;
import modelo.exceptions.InstanceNotFoundException;

import java.util.List;

/**
 *
 * @author mfernandez
 */
public interface IDepartamentoDao extends IGenericDao<Departamento>{
    

	
	public boolean exists(Integer dept);
	public List<Departamento> readName(String deptName) throws InstanceNotFoundException;
	
	
	
}
