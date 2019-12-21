package os.server.users;

public abstract class User {
	private String name;
	private String id;
	private String email;
	
	public User() { }
	
	public User(String name, String id, String email) {
		this.name = name;
		this.id = id;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	public User setName(String name) {
		this.name = name;
		
		return this;
	}
	
	public String getId() {
		return id;
	}
	public User setId(String id) {
		this.id = id;
		
		return this;
	}
	
	public String getEmail() {
		return email;
	}
	public User setEmail(String email) {
		this.email = email;
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
