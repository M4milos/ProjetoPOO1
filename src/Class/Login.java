package Class;

public class Login {
	private int id;
    private String username;
    private String passHash;
    private int permission;

    public Login(int id, String username, String passHash, int permission) {
        this.id = id;
        this.username = username;
        this.passHash = passHash;
        this.permission = permission;
    }

    public Login(String username, String passHash, int permission) {
        this.username = username;
        this.passHash = passHash;
        this.permission = permission;
    }
    
    public int getId() { 
    	return id; 
    }
    public void setId(int id) { 
    	this.id = id; 
    }
    
    public String getUsername() { 
    	return username;
    }
    public void setUsername(String username) { 
    	this.username = username;
    }
    
    public String getPassHash() { 
    	return passHash; 
    }
    public void setPassHash(String passHash) { 
    	this.passHash = passHash; 
    }
    
    public int getPermission() { 
    	return permission; 
    }
    public void setPermission(int permission) { 
    	this.permission = permission; 
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Login [getId()=");
		builder.append(getId());
		builder.append(", getUsername()=");
		builder.append(getUsername());
		builder.append(", getPermission()=");
		builder.append(getPermission());
		builder.append("]");
		return builder.toString();
	}
}
