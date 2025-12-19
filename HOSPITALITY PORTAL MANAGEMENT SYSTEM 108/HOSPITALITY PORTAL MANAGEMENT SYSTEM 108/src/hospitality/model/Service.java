package hospitality.model;
import java.sql.Timestamp;
public class Service {
	private int serviceID;
	private String name;
	private String description;
	private String category;
	private double priceOrValue;
	private String status;
	private Timestamp createdAt;
	private Integer guestID;

	// Constructors, Getters and Setters
	public Service() {}

	public Service(String name, String description, String category, double priceOrValue, String status) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.priceOrValue = priceOrValue;
		this.status = status;
	}

	// Getters and Setters
	public int getServiceID() { return serviceID; }
	public void setServiceID(int serviceID) { this.serviceID = serviceID; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public double getPriceOrValue() { return priceOrValue; }
	public void setPriceOrValue(double priceOrValue) { this.priceOrValue = priceOrValue; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public Timestamp getCreatedAt() { return createdAt; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

	public Integer getGuestID() { return guestID; }
	public void setGuestID(Integer guestID) { this.guestID = guestID; }
}




