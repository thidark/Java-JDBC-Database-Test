
public class Author {
private int id;
private String firstName;
private String lastName;
private int age;

public Author(int ID, String FirstName,String LastName,int Age) {
	this.id=ID;
	this.firstName=FirstName;
	this.lastName=LastName;
	this.age=Age;
	
	
}

public int getId() {
	return this.id;
	
}
public String getFirstName() {
	return this.firstName;
}

public String getLastName() {
	return this.lastName;
}

public int getAge() {
	return this.age;
}


}
