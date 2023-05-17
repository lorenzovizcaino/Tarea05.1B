package modelo.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import modelo.Departamento;
import modelo.exceptions.InstanceNotFoundException;
import modelo.servicio.departamento.IServicioDepartamento;
import modelo.servicio.departamento.ServicioDepartamento;

public class SearchDepartmentWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextArea mensajes_text_Area;
	private JList<Departamento> JListAllDepts;

	private IServicioDepartamento departamentoServicio;

	private JTextField textField_deptNo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchDepartmentWindow frame = new SearchDepartmentWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SearchDepartmentWindow() {

		departamentoServicio = new ServicioDepartamento();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 772);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(8, 8, 821, 500);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 264, 669, 228);
		panel.add(scrollPane);

		mensajes_text_Area = new JTextArea();
		scrollPane.setViewportView(mensajes_text_Area);
		mensajes_text_Area.setEditable(false);
		mensajes_text_Area.setText("Panel de mensajes");
		mensajes_text_Area.setForeground(new Color(255, 0, 0));
		mensajes_text_Area.setFont(new Font("Monospaced", Font.PLAIN, 13));

		JListAllDepts = new JList<Departamento>();

		JListAllDepts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JListAllDepts.setBounds(403, 37, 377, 200);

		JScrollPane scrollPanel_in_JlistAllDepts = new JScrollPane(JListAllDepts);
		scrollPanel_in_JlistAllDepts.setLocation(300, 0);
		scrollPanel_in_JlistAllDepts.setSize(500, 250);

		panel.add(scrollPanel_in_JlistAllDepts);

		textField_deptNo = new JTextField();
		textField_deptNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMensaje(true, "Procesando...");
				String deptName = getDeptNameFromTextField();
				int contador=0;


					try {
						List<Departamento> dept = departamentoServicio.readName(deptName);
						if (dept != null) {
							DefaultListModel<Departamento> defModel = new DefaultListModel<>();
							for (Departamento d:dept) {
								contador++;
								defModel.addElement(d);
							}
							addMensaje(true, "Se ha encontrado " + contador + " resultados");
							JListAllDepts.setModel(defModel);
						} else {
							addMensaje(true, "El departamento con nombre: " + deptName + " es null");
							clearJListModel();
						}
					} catch (InstanceNotFoundException e1) {
						addMensaje(true, "No se encontraron resultados");
						clearJListModel();
					}

			}
		});
		textField_deptNo.setBounds(10, 56, 263, 34);
		panel.add(textField_deptNo);
		textField_deptNo.setColumns(10);

		JLabel lbal_info_deptno = new JLabel("Introduzca el nombre del departamento");
		lbal_info_deptno.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbal_info_deptno.setBounds(10, 11, 280, 34);
		panel.add(lbal_info_deptno);

	}
	
	private void clearJListModel() {
		ListModel<Departamento> model = JListAllDepts.getModel();
		if (model != null) {
			if (model instanceof DefaultListModel) {
				((DefaultListModel<Departamento>) model).removeAllElements();
			}
		}
	}

	private void addMensaje(boolean keepText, String msg) {
		String oldText = "";
		if (keepText) {
			oldText = mensajes_text_Area.getText();

		}
		oldText = oldText + "\n" + msg;
		mensajes_text_Area.setText(oldText);

	}

	private String getDeptNameFromTextField() {

		String textIntroducido = textField_deptNo.getText().trim();

		return textIntroducido;
	}

}
