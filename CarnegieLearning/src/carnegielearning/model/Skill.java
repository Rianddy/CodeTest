package carnegielearning.model;

/**
 * Create a skill model and make the memeber variable name unique.
 * 
 * @author rianddy
 *
 */
public class Skill {
	private String name;

	public Skill(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Override equals and hashCode methods so that when add a skill to a
	 * student's skills map, map can detect the duplicates.
	 * 
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Skill rhs = (Skill) obj;
		return this.name.equals(rhs.getName());
	}

}
