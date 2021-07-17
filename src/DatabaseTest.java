

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;

import java.awt.GridLayout;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.Font;

public class DatabaseTest extends JFrame {
	JTable authorTable = new JTable();
	JPanel p1;
	JPanel p2;
	final JTextField id, fname, lname, age;

	public DatabaseTest() {
		super("Database Testing");
		
		//--------------GUI Layout---------------------
		setFont(new Font("Times New Roman", Font.PLAIN, 14));

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

		JPanel leftGrid = new JPanel(new GridLayout(5, 2));
		JPanel rightGrid = new JPanel(new GridLayout(1, 1));
		
		leftGrid.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

		// left Grid

		leftGrid.add(new JLabel("ID :"));
		id = new JTextField();
		leftGrid.add(id);

		leftGrid.add(new JLabel("First Name :"));
		fname = new JTextField();
		leftGrid.add(fname);
		
		leftGrid.add(new JLabel("Last Name :"));
		lname = new JTextField();
		leftGrid.add(lname);
		
		leftGrid.add(new JLabel("Age :"));
		age = new JTextField();
		leftGrid.add(age);
		
		
		JButton insertBtn = new JButton("Insert");
		JButton updateBtn = new JButton("Update");
		JButton deleteBtn = new JButton("Delete");
		
		p2.add(insertBtn);
		p2.add(updateBtn);
		p2.add(deleteBtn);
		
		leftGrid.add(p2);
	
		// Right Grid

		authorTable.setModel(
				new DefaultTableModel(new Object[][] {}, 
						              new String[] { "ID", "First Name", "Last Name", "Age" }));
		authorTable.setBackground(SystemColor.PINK);
		authorTable.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		authorTable.setFillsViewportHeight(true);
		
		rightGrid.add(authorTable);

		p1.add(leftGrid);
		p1.add(rightGrid);
		getContentPane().add(p1, new BorderLayout().WEST);
		
		//---------------------------------------------------------------------------

		Show_Users_In_Table();


		insertBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "Insert into Authors values('"+
			                     id.getText()+"','" + fname.getText() + "','" + 
						         lname.getText() + "','"+ age.getText() + "')";
				executeSQLQuery(query, "Inserted");
				id.setText("");
				fname.setText("");
				lname.setText("");
				age.setText("");
				
			}
		});

		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = "UPDATE `authors` SET FirstName='" + fname.getText() + "',"+ 
			                                        "LastName ='" + lname.getText()+ "', "
			                                        + "Age='" + age.getText() + 
			                                        "' WHERE AuthorID=" + id.getText();
				executeSQLQuery(query, "Updated");
				id.setText("");
				fname.setText("");
				lname.setText("");
				age.setText("");
				
			}
		});

		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = "Delete from Authors where AuthorID=" + id.getText();
				executeSQLQuery(query, "Deleted");
				id.setText("");
				fname.setText("");
				lname.setText("");
				age.setText("");
			}
		});

		authorTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i = authorTable.getSelectedRow();
				TableModel model = authorTable.getModel();
				id.setText(model.getValueAt(i, 0).toString());
				fname.setText(model.getValueAt(i, 1).toString());
				lname.setText(model.getValueAt(i, 2).toString());
				age.setText(model.getValueAt(i, 3).toString());

			}

		});
	
		
	}

	

	public void Show_Users_In_Table() {
		ArrayList<Author> list = getUserList();
		DefaultTableModel model = (DefaultTableModel) authorTable.getModel();
		Object[] row = new Object[4];
		row[0] = "Id";
		row[1] = "First Name";
		row[2] = "Last Name";
		row[3] = "Age";
		model.addRow(row);

		for (int i = 0; i < list.size(); i++) {
			row[0] = list.get(i).getId();
			row[1] = list.get(i).getFirstName();
			row[2] = list.get(i).getLastName();
			row[3] = list.get(i).getAge();

			model.addRow(row);
		}
	}
	
	public ArrayList<Author> getUserList() {
		ArrayList<Author> authorList = new ArrayList<Author>();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/books", "root", "");
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		String query = "Select * from Authors";
		Statement st;
		ResultSet rs;

		try {

			st = connection.createStatement();
			rs = st.executeQuery(query);

			Author author;
			while (rs.next()) {
				author = new Author(rs.getInt("AuthorID"), rs.getString("FirstName"), 
						            rs.getString("LastName"),rs.getInt("Age"));
				authorList.add(author);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorList;
	}

	public void executeSQLQuery(String query, String message) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/books", "root", "");
			Statement st;
			st = connection.createStatement();
			if (st.executeUpdate(query) == 1) {
				DefaultTableModel model = (DefaultTableModel) authorTable.getModel();
				model.setRowCount(0);
				Show_Users_In_Table();
				JOptionPane.showMessageDialog(null, "Data " + message + " Successfully");

			} else {
				JOptionPane.showMessageDialog(null, "Data not " + message);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DatabaseTest window = new DatabaseTest();

		window.setVisible(true);
		window.setSize(800, 300);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
