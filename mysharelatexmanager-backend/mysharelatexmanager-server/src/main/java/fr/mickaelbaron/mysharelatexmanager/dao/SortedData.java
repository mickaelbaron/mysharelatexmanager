package fr.mickaelbaron.mysharelatexmanager.dao;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class SortedData {

	private String name;

	private boolean isAscendant;

	public SortedData() {
	}

	public SortedData(String pName, boolean pIsAscendant) {
		this.name = pName;
		this.isAscendant = pIsAscendant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAscendant() {
		return isAscendant;
	}

	public void setAscendant(boolean isAscendant) {
		this.isAscendant = isAscendant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAscendant ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SortedData other = (SortedData) obj;
		if (isAscendant != other.isAscendant) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
